package org.meta.search.IR.Relevance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.Vector;

import org.meta.search.IR.Bo.UtilityConstants;
import org.meta.search.IR.Bo.UtilityFunctions;

/**
 * MetaSearchRelevanceFeedback feature is implemented in this class.
 * @author Bala Sridhar
 */
public class MetaSearchRelevanceFeedBack {
	/**
	 * MetaResultParser Object type.
	 */
	private MetaResultParser mrp = null;
	/**
	 * Constructor.
	 */
	public MetaSearchRelevanceFeedBack(){
		mrp = new MetaResultParser();
	}
	
	/**
	 * Rearrange the results based on the relevant results.
	 * @param result
	 * @param arr
	 * @return
	 */
	public ArrayList<String> reArrangeResults(ArrayList<String> result, ArrayList<String> arr){
		ArrayList<String> links = null;
		Vector queryDoc = getRelevantQueryTokens(result,arr);
		links = getReArrangedVector(queryDoc, result);
		return descendingOrder(links);
	}
	
	/**
	 * Rearrange the next page of results based on the relevant tokens.
	 * @param queryPage - result set where the relevant ids were selected.
	 * @param arr - list of relevant ids
	 * @param currentPage - result set for the current page
	 * @return
	 */
	public ArrayList<String> getRelevantResultsPages(ArrayList<String> queryPage, ArrayList<String> arr, ArrayList<String> currentPage ){
		ArrayList<String> links = null;
		Vector queryDoc = getRelevantQueryTokens(queryPage,arr);
		links = getReArrangedVector(queryDoc, currentPage);
		return descendingOrder(links);
	}
	
	/**
	 * Get the relevant result word tokens.
	 * @param result
	 * @param relevantIds
	 * @return
	 */
	private Vector<String> getRelevantQueryTokens(ArrayList<String> result, ArrayList<String> relevantIds){
		Vector<String> queryTokens = new Vector<String>();
		String delims = "|-,. ";
		ArrayList<String> stopWord = getStopWords();
		for(int i=0; i<relevantIds.size();i++){
			int val = Integer.parseInt(relevantIds.get(i));
			String text = getTextForRelevanceFeedBack(result.get(val));
			StringTokenizer st = new StringTokenizer(text,delims);
			if(i == 0){
				while(st.hasMoreTokens()){
					String token = st.nextToken().toLowerCase();
					if(!stopWord.contains(token)){
						if(!queryTokens.contains(token)){
							queryTokens.add(token);
						}
					}
				}
			}else{
				while(st.hasMoreTokens()){
					String token = st.nextToken();
					if(!stopWord.contains(token)){
						if(!queryTokens.contains(token.toLowerCase())){
							queryTokens.add(token.toLowerCase());
						}
					}
				}
			}
		}
		
	/*	System.out.println();
		System.out.println();
		for(int i =0; i<queryTokens.size();i++){
			System.out.println(queryTokens.get(i));
		}*/
		return queryTokens;
	}
	
	/** 
	 * based on the relevance feedback type get the appropriate text.
	 * @param text
	 * @return
	 */
	private String getTextForRelevanceFeedBack(String text){
		String tex = null;
		if(UtilityConstants.relevanceFeedBackType.equals(UtilityConstants.feedBackType1)){
			tex = mrp.getTitle(text);
		}else if(UtilityConstants.relevanceFeedBackType.equals(UtilityConstants.feedBackType2)){
			tex = mrp.getAbstract(text);
		}
		return tex;
	}
	
	/**
	 * get the rearranged result.
	 * @param queryTokens
	 * @param result
	 * @return
	 */
	private ArrayList<String> getReArrangedVector(Vector<String> queryTokens,ArrayList<String> result){
		ArrayList<String> reArrResults = null;
		ArrayList scores = new ArrayList();
		ArrayList<String> stopWord = getStopWords();
		double eachScore = 0;
		String delims = "|-,. ";
		for (int i=0;i<result.size();i++){
			String text = getTextForRelevanceFeedBack(result.get(i));
			StringTokenizer st = new StringTokenizer(text,delims);
			Vector<String> eachResult = getUniqueTokens(st, stopWord);
			//System.out.println(eachResult.size());
			eachScore = getJaccardCoefficient(queryTokens, eachResult);
			//System.out.println(eachScore);
			scores.add(eachScore);
		}
		ArrayList originalScores = sortResultList(scores);
		//System.out.println("Original Scores : ");
		//listContents(originalScores);
		//System.out.println();
		//System.out.println("Scores : ");
		//listContents(scores);
		reArrResults = arrangeIt(result, originalScores, scores);
		//System.out.println();
		//listContents(reArrResults);
		return reArrResults;
	}
	
	/** 
	 * Get the unique word tokens from each of the result in the list.
	 * @param st
	 * @param stopWord
	 * @return
	 */
	private Vector<String> getUniqueTokens(StringTokenizer st, ArrayList<String> stopWord){
		Vector<String> uniqueTokens = null;
		uniqueTokens =  new Vector<String>();
		while (st.hasMoreTokens()){
			String token = st.nextToken();
			if(!stopWord.contains(token)){
				if(!uniqueTokens.contains(token.toLowerCase())){
					uniqueTokens.add(token.toLowerCase());
				}
			}
		}
		return uniqueTokens;
	}
	
	/**
	 * get the jaccard coefficient value for that result when compared
	 * with the relevant token set.
	 * @param queryTokens
	 * @param eachResult
	 * @return
	 */
	private double getJaccardCoefficient(Vector<String>queryTokens, Vector<String> eachResult){
		double score = 0;
		int count = 0;
		for (int i =0; i<queryTokens.size();i++){
			if(eachResult.contains(queryTokens.get(i))){
				count = count+1;
			}
		}
		int union = (queryTokens.size()+eachResult.size())-count;
		score = (double)((double) count/(double) union);
		return score;
	}
	
	/**
	 * Sort the list of jaccard coefficient values calculated for each of the result.
	 * @param results
	 * @return
	 */
	private ArrayList sortResultList(ArrayList results){
		ArrayList original = UtilityFunctions.copyTo(results); 
		Collections.sort(results);
		return original;
	}
	
	/**
	 * This actually does the rearrangment based on the values calculated.
	 * @param results
	 * @param originalScores
	 * @param scores
	 * @return
	 */
	private ArrayList arrangeIt(ArrayList results, ArrayList originalScores,ArrayList scores){
		ArrayList reResults = new ArrayList();
		for(int i=0;i<scores.size();i++){
			for (int j = 0;j<originalScores.size();j++){
				if(scores.get(i).equals(originalScores.get(j))){
					if(!reResults.contains(results.get(j))){
						reResults.add(results.get(j));
					}
				}
			}
		}
		return reResults;
	}
	
	/**
	 * Get the list in the descending order.
	 * @param ascendingList
	 * @return
	 */
	private ArrayList descendingOrder(ArrayList ascendingList){
		ArrayList descendingList = new ArrayList();
		for (int i=(ascendingList.size()-1);i>=0;i--){
			descendingList.add(ascendingList.get(i));
		}
		return descendingList;
	}
	
	/**
	 * Construct a list of stop words to be used in the relevance feedback feature.
	 * @return
	 */
	private ArrayList<String> getStopWords(){
		ArrayList<String> stopWords = new ArrayList<String>();
		String delims ="/ ";
		String stopWord  = UtilityConstants.stopWords;
		StringTokenizer st = new StringTokenizer(stopWord,delims);
		while (st.hasMoreTokens()){
			String token = st.nextToken();
			if(!stopWords.contains(token.toLowerCase())){
				stopWords.add(token.toLowerCase());
			}
		}
		return stopWords;
	}
}
