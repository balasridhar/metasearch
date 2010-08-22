package org.meta.search.IR.Bo;

import java.util.ArrayList;
import org.meta.search.IR.Form.SearchForm;

/**
 * Aggregation Wrapper that takes care of passing the right set of parameters 
 * to the Generic Aggregation class. This class gets the result from the different 
 * search engines based on the aggregation type selected by the user 
 * and passes them to the Generic Aggregation class to be aggregated.
 *  
 * @author Bala Sridhar
 */
public class AggregationWrapper {
	/**
	 * Object of the Java Bean class Search Form.
	 */
	private SearchForm sForm = null;
	/**
	 * Object of the TestingHtmlParser Class.
	 */
	private TestingHtmlParser thp = null;
	/**
	 * boolean value that identifies whether yahoo search engine was selected or not.
	 */
	private boolean yahooList = false;
	/**
	 * boolean value that identifies whether microsoft search engine was selected or not.
	 */
	private boolean microsoftList = false;
	/**
	 * boolean value that identifies whether altavista search engine was selected or not.
	 */
	private boolean altavistaList = false;
	/**
	 * boolean value that identifies whether ask search engine was selected or not.
	 */
	private boolean askList = false;
	/**
	 * boolean value that identifies whether lycos search engine was selected or not.
	 */
	private boolean lycosList = false;
	
	/** 
	 * Constructor.
	 * @param sForm
	 * @param thp
	 */
	public AggregationWrapper(SearchForm sForm, TestingHtmlParser thp){
		this.sForm = sForm;
		this.thp = thp;
	}
	
	/**
	 * Get aggregated result depending on the aggregation type.
	 * @param aResult - aggType
	 * @param query - search query
	 * @param page - page number
	 * @return
	 */
	public ArrayList<String> getAggregationResult(String aResult, String query, int page){
		ArrayList<String> result = null;
		if(UtilityConstants.singleAggregationType.equals(aResult)){
			result = getSingleAggregatedResult(query,page);
		}else if(UtilityConstants.tripleAggregationType.equals(aResult)){
			result = getThreePageAggregatedResult(query, page);
		}
		return result;
	}
	
	/**
	 * Get single aggregated type of result from the search engines selected.
	 * @param query - search query
	 * @param page - page number
	 * @return
	 */
	private ArrayList<String> getSingleAggregatedResult(String query, int page){
		ArrayList<String> list1 = null;
		ArrayList<String> list2 = null;
		ArrayList<String> list3 = null;
		ArrayList<String> result = null;
		setBooleanValues(false);
		list1 = getIndividualSearchResult(query,page);
		list2 = getIndividualSearchResult(query,page);
		list3 = getIndividualSearchResult(query,page);
		
		if(list1 == null){
			System.out.println("SingleAggregation : List1 is null");
		}else if(list2 == null){
			System.out.println("SingleAggregation : List2 is null");
		}else if(list3 == null){
			System.out.println("SingleAggregation : List3 is null");
		}
		
		if(!thp.getErrorRetrievalValue()){
			String aggSearch = getSearchEngines();
			GenericAggregation spa = new GenericAggregation(list1,list2,list3,aggSearch);
			spa.startAggregating();
			result = spa.getAggregatedResult();
		}else{
			result = constructErrorString();
		}
		return result;
	}
	
	/**
	 * Construct the Error String that needs to be displayed ....
	 * @return
	 */
	private ArrayList<String> constructErrorString() {
		// TODO Auto-generated method stub
		ArrayList<String> result = new ArrayList<String>();
		String errorString = UtilityConstants.errorString;
		errorString = errorString.replaceAll(UtilityConstants.errorReplace, thp.getErrorSearchEngine());
		result.add(errorString);
		return result;
	}

	/**
	 * Get triple aggregated type of result from the search engines selected.
	 * @param query - search query
	 * @param page -  page number
	 * @return
	 */
	private ArrayList<String> getThreePageAggregatedResult(String query, int page){
		ArrayList<String> list1 = null;
		ArrayList<String> list2 = null;
		ArrayList<String> list3 = null;
		ArrayList<String> result = null;
		
		setBooleanValues(false);
		list1 = getThreeAggreSearchResult(query,page);
		list2 = getThreeAggreSearchResult(query,page);
		list3 = getThreeAggreSearchResult(query,page);
		
		if(list1 == null){
			System.out.println("ThreePageAggregation : List1 is null");
		}else if(list2 == null){
			System.out.println("ThreePageAggregation : List2 is null");
		}else if(list3 == null){
			System.out.println("ThreePageAggregation : List3 is null");
		}
		if(!thp.getErrorRetrievalValue()){
			String aggSearch = getSearchEngines();
			GenericAggregation spa = new GenericAggregation(list1,list2,list3,aggSearch);
			spa.startAggregating();
			result = spa.getAggregatedResult();
		}else{
			
		}
		return result;
	}
	
	/**
	 * Get the list of search results from the individual search engines based on
	 * whether the search engine is selected or not for a single page aggregation. 
	 * @param query - search query
	 * @param page - page number
	 * @return
	 */
	private ArrayList<String> getIndividualSearchResult(String query, int page){
		ArrayList<String> list = null;
		if (("on".equals(sForm.getYahooCheck()))&&(!yahooList)){
			list = thp.getSearchResults(query,UtilityConstants.yahooString,page);
			yahooList =true;
			return list;
		}else if(("on".equals(sForm.getAltavistaCheck()))&&(!altavistaList)){
			list = thp.getSearchResults(query,UtilityConstants.altavistaString,page);
			altavistaList = true;
			return list;
		}else if(("on".equals(sForm.getMicrosoftCheck()))&&(!microsoftList)){
			list = thp.getSearchResults(query,UtilityConstants.microsoftString,page);
			microsoftList = true;
			return list;
		}else if(("on".equals(sForm.getLycosCheck()))&&(!lycosList)){
			list = thp.getSearchResults(query,UtilityConstants.lycosString,page);
			lycosList = true;
			return list;
		}else if(("on".equals(sForm.getAskCheck()))&&(!askList)){
			list = thp.getSearchResults(query,UtilityConstants.askString,page);
			askList = true;
			return list;
		}
		return list;
	}
	
	/**
	 * Get the list of search results from the individual search engines based on
	 * whether the search engine is selected or not for a Three page aggregation. 
	 * @param query - search query
	 * @param page - page number
	 * @return
	 */
	private ArrayList<String> getThreeAggreSearchResult(String query, int page){
		ArrayList<String> list = null;
		ArrayList<String> list1 = null;
		ArrayList<String> list2 = null;
		ArrayList<String> list3 = null;
		if (("on".equals(sForm.getYahooCheck()))&&(!yahooList)){
			list1 = thp.getSearchResults(query, UtilityConstants.yahooString,page);
			list2 = thp.getSearchResults(query, UtilityConstants.yahooString,page+1);
			list3 = thp.getSearchResults(query, UtilityConstants.yahooString,page+2);
			list = compressedVersion(list1,list2,list3);
			yahooList =true;
			return list;
		}else if(("on".equals(sForm.getAltavistaCheck()))&&(!altavistaList)){
			list1 = thp.getSearchResults(query,UtilityConstants.altavistaString,page);
			list2= thp.getSearchResults(query,UtilityConstants.altavistaString,page+1);
			list3 = thp.getSearchResults(query,UtilityConstants.altavistaString,page+2);
			list = compressedVersion(list1,list2,list3);
			altavistaList = true;
			return list;
		}else if(("on".equals(sForm.getMicrosoftCheck()))&&(!microsoftList)){
			list1 = thp.getSearchResults(query,UtilityConstants.microsoftString,page);
			list2 = thp.getSearchResults(query,UtilityConstants.microsoftString,page+1);
			list3 = thp.getSearchResults(query,UtilityConstants.microsoftString,page+2);
			list = compressedVersion(list1,list2,list3);
			microsoftList = true;
			return list;
		}else if(("on".equals(sForm.getLycosCheck()))&&(!lycosList)){
			list1 = thp.getSearchResults(query,UtilityConstants.lycosString,page);
			list2 = thp.getSearchResults(query,UtilityConstants.lycosString,page+1);
			list3 = thp.getSearchResults(query,UtilityConstants.lycosString,page+2);
			list = compressedVersion(list1,list2,list3);
			lycosList = true;
			return list;
		}else if(("on".equals(sForm.getAskCheck()))&&(!askList)){
			list1 = thp.getSearchResults(query,UtilityConstants.askString,page);
			list2 = thp.getSearchResults(query,UtilityConstants.askString,page+1);
			list3 = thp.getSearchResults(query,UtilityConstants.askString,page+2);
			list = compressedVersion(list1,list2,list3);
			askList = true;
			return list;
		}
		return list;
	}
	
	/**
	 * Compress the individual results retrieved as part of the three page aggregation. 
	 * @param list1 - first page search result
	 * @param list2 - second page search result
	 * @param list3 - three page search result
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<String> compressedVersion(ArrayList<String> list1,ArrayList<String> list2,ArrayList<String> list3){
		ArrayList<String> complete = new ArrayList<String>();
		complete = copy(list1, complete);
		complete = copy(list2, complete);
		complete = copy(list3, complete);
		return complete;
	}
	
	/**
	 * Copy from one ArrayList to another arraylist.
	 * @param list
	 * @param listTo
	 * @return
	 */
	private ArrayList<String> copy(ArrayList<String> list, ArrayList<String> listTo){
		for(int i=0; i<list.size();i++){
			listTo.add(list.get(i));
		}
		return listTo;
	}
	
	/**
	 * reset the boolean values.
	 * @param value
	 */
	private void setBooleanValues(boolean value){
		yahooList = value;
		askList= value;
		lycosList = value;
		microsoftList = value;
		altavistaList = value;
	}
	
	/**
	 * Constructs a string containing the search engines selected by the user.
	 * This is used in the Generic aggregation class to do the aggregation.
	 * like for example if yahoo, lycos and ask are selected then we get this 
	 * string 'yahoolycosask'.
	 * @return
	 */
	private String getSearchEngines(){
		String aggSearch = null;
		if(yahooList){
			aggSearch = concatenateString(UtilityConstants.yahooString, aggSearch);
		}
		if(altavistaList){
			aggSearch = concatenateString(UtilityConstants.altavistaString, aggSearch);
		}
		if(microsoftList){
			aggSearch = concatenateString(UtilityConstants.microsoftString, aggSearch);
		}
		if(lycosList){
			aggSearch = concatenateString(UtilityConstants.lycosString, aggSearch);
		}
		if(askList){
			aggSearch = concatenateString(UtilityConstants.askString, aggSearch);
		}
		return aggSearch;
	}
	
	/**
	 * Concatenate two strings. 
	 * @param text
	 * @param test
	 * @return
	 */
	private String concatenateString(String text, String test){
		if(test == null){
			test = text;
		}else{
			test = test+text;
		}
		return test;
	}
	/**
	 * returns the boolean value specifying whether there was an error or not.
	 * @return
	 */
	public boolean isErrorRetrieval(){
		return thp.getErrorRetrievalValue();
	}
}
