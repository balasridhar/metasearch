package org.meta.search.IR.Bo;

import java.util.ArrayList;
import java.util.Collections;

import org.meta.search.IR.pageFormatter.AltavistaAggregateParser;
import org.meta.search.IR.pageFormatter.AskAggregateParser;
import org.meta.search.IR.pageFormatter.BaseAggregateParser;
import org.meta.search.IR.pageFormatter.LycosAggregateParser;
import org.meta.search.IR.pageFormatter.MicrosoftAggregateParser;
import org.meta.search.IR.pageFormatter.YahooAggregateParser;

/**
 * Generic Aggregation class that takes three arraylists of results from three separate
 * engines and does the aggregation on them and returns a single aggregated array list. 
 * @author Bala Sridhar
 */
public class GenericAggregation {
	/**
	 * Holds the aggregated result to be displayed to the user.
	 */
	private ArrayList<String> aggregatedResult = null;
	/**
	 * Three ArrayLists that hold the results from the three 
	 * separate search engines.
	 */
	private ArrayList<String> list1 = null;
	private ArrayList<String> list2 = null;
	private ArrayList<String> list3 = null;

	/**
	 * Holds the list of links that are unique across different search engines.
	 */
	private ArrayList localList = null;
	/**
	 * Holds a list of strings that tells us where each unique link in the 
	 * localList is got from which search engine.
	 */
	private ArrayList locationList = null;
	/**
	 * holds the overall weight for each unique link in the localList.
	 */
	private ArrayList weightList = null;
	/**
	 * These set of array lists holds all the links of the corresponding 
	 * search engines after removing the unwnated tags. 
	 */
	private ArrayList<String> modifiedList1 = null;
	private ArrayList<String> modifiedList2 = null;
	private ArrayList<String> modifiedList3 = null;
	/**
	 * holds the static weights for the three search engines selected.
	 */
	private int weight1 = 0;
	private int weight2 = 0;
	private int weight3 = 0;

	/**
	 * holds the name of the three search engines from which the results were
	 * retrieved.
	 */
	private String searchEngine1 = null;
	private String searchEngine2 = null;
	private String searchEngine3 = null;
	
	/**
	 * BaseAggregateparser objects that are used for parsing the aggregated result
	 * and converting them into a unified format that could be displayed on the meta
	 * search engine result page.
	 */
	private BaseAggregateParser bap1 = null;
	private BaseAggregateParser bap2 = null;
	private BaseAggregateParser bap3 = null;
	/**
	 * Holds the string that tells us the search engines involved in this aggregation.
	 */
	private String aggSearch = null;

	/**
	 * Constructor.
	 * @param list1 - search engine 1 results
	 * @param list2 - search engine 2 results
	 * @param list3 - search engine 3 results
	 * @param aggSearch - string holding the search engines involved in the process. 
	 */
	public GenericAggregation(ArrayList<String> list1, ArrayList<String> list2,
			ArrayList<String> list3, String aggSearch) {
		aggregatedResult = new ArrayList<String>();
		this.list1 = list1;
		this.list2 = list2;
		this.list3 = list3;
		localList = new ArrayList();
		locationList = new ArrayList();
		weightList = new ArrayList();
		this.aggSearch = aggSearch;
		// gather the name, weight and create the aggregate parser object for the first search engine.
		String searchEngineString = aggSearch;
		searchEngine1 = initializeSearchEngines(searchEngine1,searchEngineString);
		bap1 = initializeAggregateParsers(bap1,searchEngineString,UtilityConstants.searchColor1);
		weight1 = initializeWeights(weight1,searchEngineString);
		
		// gather the name, weight and create the aggregate parser object for the second search engine.
		searchEngineString = searchEngineString.replaceFirst(searchEngine1, "");
		searchEngine2 = initializeSearchEngines(searchEngine2,searchEngineString);
		bap2 = initializeAggregateParsers(bap2,searchEngineString,UtilityConstants.searchColor2);
		weight2 = initializeWeights(weight2,searchEngineString);
		
		// gather the name, weight and create the aggregate parser object for the third search engine.
		searchEngineString = searchEngineString.replaceFirst(searchEngine2, "");
		searchEngine3 = initializeSearchEngines(searchEngine3,searchEngineString);
		bap3 = initializeAggregateParsers(bap3,searchEngineString,UtilityConstants.searchColor3);
		weight3 = initializeWeights(weight3,searchEngineString);
	}

	/**
	 * Gets the name of the search engine present in the concatenated string.
	 * @param engine - search engine object to be returned
	 * @param aggSearch - concatenated string that holds the all the three search engine names.
	 * @return
	 */
	private String initializeSearchEngines(String engine, String aggSearch) {
		if (aggSearch.contains(UtilityConstants.yahooString)) {
			engine = UtilityConstants.yahooString;
			return engine;
		} else if (aggSearch.contains(UtilityConstants.altavistaString)) {
			engine = UtilityConstants.altavistaString;
			return engine;
		} else if (aggSearch.contains(UtilityConstants.microsoftString)) {
			engine =UtilityConstants.microsoftString;
			return engine;
		} else if (aggSearch.contains(UtilityConstants.lycosString)) {
			engine = UtilityConstants.lycosString;
			return engine;
		} else if (aggSearch.contains(UtilityConstants.askString)) {
			engine = UtilityConstants.askString;
			return engine;
		}
		return engine;
	}
	
	/**
	 * Create the appropriate aggregate parser based on the search engines involved.
	 * @param bap - BaseAggregateParser object.
	 * @param aggSearch - concatenated string that holds the all the three search engine names.
	 * @param color - color that needs to be assigned to that result from that search engine.
	 * @return
	 */
	private BaseAggregateParser initializeAggregateParsers(
			BaseAggregateParser bap, String aggSearch, String color) {
		if (aggSearch.contains(UtilityConstants.yahooString)) {
			bap = new YahooAggregateParser(color);
			return bap;
		} else if (aggSearch.contains(UtilityConstants.altavistaString)) {
			bap = new AltavistaAggregateParser(color);
			return bap;
		} else if (aggSearch.contains(UtilityConstants.microsoftString)) {
			bap = new MicrosoftAggregateParser(color);
			return bap;
		} else if (aggSearch.contains(UtilityConstants.lycosString)) {
			bap = new LycosAggregateParser(color);
			return bap;
		} else if (aggSearch.contains(UtilityConstants.askString)) {
			bap = new AskAggregateParser(color);
			return bap;
		}
		return bap;
	}
	
	/**
	 * Get the corresponding weight for the search engine involved. 
	 * @param weight - static weight for that search engine.
	 * @param aggSearch - concatenated string that holds the all the three search engine names.
	 * @return
	 */
	private int initializeWeights(int weight, String aggSearch) {
		if (aggSearch.contains(UtilityConstants.yahooString)) {
			weight = Integer.parseInt(UtilityConstants.yahooWeight);
			return weight;
		} else if (aggSearch.contains(UtilityConstants.altavistaString)) {
			weight = Integer.parseInt(UtilityConstants.altavistaWeight);
			return weight;
		} else if (aggSearch.contains(UtilityConstants.microsoftString)) {
			weight = Integer.parseInt(UtilityConstants.microsoftWeight);
			return weight;
		} else if (aggSearch.contains(UtilityConstants.lycosString)) {
			weight = Integer.parseInt(UtilityConstants.lycosWeight);
			return weight;
		} else if (aggSearch.contains(UtilityConstants.askString)) {
			weight = Integer.parseInt(UtilityConstants.askWeight);
			return weight;
		}
		return weight;
	}
	
	/**
	 * Start the aggregation Process and construct the unified formatted list of results
	 * to be displayed. 
	 */
	public void startAggregating() {
		/*System.out.println();
		UtilityFunctions.listContents(list2);
		System.out.println();*/
		ArrayList<String> links1 = getSearchLinks(searchEngine1, list1);
		ArrayList<String> links2 = getSearchLinks(searchEngine2, list2);
		ArrayList<String> links3 = getSearchLinks(searchEngine3, list3);
		
		Aggregation(links1, links2, links3);
		compiledAggregatedResult();
	}
	
	/**
	 * gets the list of links from the corresponding search engine result list.
	 * @param searchEngine - search engine involved in the process.
	 * @param list - list of results from each search engine.
	 * @return
	 */
	private ArrayList<String> getSearchLinks(String searchEngine,
			ArrayList<String> list) {
		ArrayList<String> links = new ArrayList<String>();
		String link = null;
		for (int i = 0; i < list.size(); i++) {
			String text = (String) list.get(i);
			link = getLink(searchEngine, text);
			//System.out.println(link);
			links.add(link);
		}
		return links;
	}
	
	/**
	 * Return just the link after removing all the unwanted search engine specific information.
	 * @param searchEngine - search engine involved in the process.
	 * @param text - result text.
	 * @return
	 */
	private String getLink(String searchEngine, String text) {
		String link = null;
		if (UtilityConstants.yahooString.equals(searchEngine)) {
			link = UtilityFunctions.removeUnwantedTagsYahoo(text.substring(
					text.indexOf("**") + 2, text.indexOf(">", text
							.indexOf("**") + 3)));
		} else if (UtilityConstants.altavistaString.equals(searchEngine)) {
			link = UtilityFunctions.removeUnwantedTagsAltavista(text.substring(text
					.indexOf("**") + 2, text.indexOf(">",
					text.indexOf("**") + 3)));
		} else if (UtilityConstants.microsoftString.equals(searchEngine)) {
			link = UtilityFunctions.removeUnwantedTagsMicrosoft("http://"
					+ text.substring(text.indexOf("<cite>") + 6, text
							.indexOf("</cite>")));
		} else if (UtilityConstants.lycosString.equals(searchEngine)) {
			link = UtilityFunctions.removeUnwantedTagsLycos(text.substring(text
					.indexOf("href=\""), text.indexOf("\" onmouseover")));
		} else if (UtilityConstants.askString.equals(searchEngine)) {
			link = UtilityFunctions.removeUnwantedTagsAsk(text.substring(text
					.indexOf("href=\""), text.indexOf("\" onmousedown")));
		}
		return link;
	}
	
	/**
	 * Aggregation Logic.
	 * @param links1 - list of links from the first search engine
	 * @param links2 - list of links from the second search engine
	 * @param links3 - list of links from the third search engine
	 */
	private void Aggregation(ArrayList<String> links1,
			ArrayList<String> links2, ArrayList<String> links3) {
		modifiedList1 = UtilityFunctions.removingCommonCharsProcess(links1);
		modifiedList2 = UtilityFunctions.removingCommonCharsProcess(links2);
		modifiedList3 = UtilityFunctions.removingCommonCharsProcess(links3);
		
		boolean link1Available = true;
		boolean link2Available = true;
		boolean link3Available = true;
		
		int size = UtilityFunctions.getHighestListSize(links1, links2, links3);
		for (int i = 0; i < size; i++) {
			int totalWeight = 0;
			String buffer = null;
			String text1 =null;
			String text2 = null;
			String text3 = null;
			int catch1 =0;
			int catch2 =0;
			int catch3 =0;
			try{
				text1 = modifiedList1.get(i);
				link1Available = true;
			}catch(Exception e){
				link1Available = false;
			}
			try{
				text2 = modifiedList2.get(i);
				link2Available = true;
			}catch(Exception e){
				link2Available = false;
			}
			try{
				text3 = modifiedList3.get(i);
				link3Available = true;
			}catch(Exception e){
				link3Available = false;
			}

			// check the link from the first search engine, whether it is available in other
			// search engine results as well.
			if(link1Available){
				catch2 = modifiedList2.indexOf(modifiedList1.get(i));
				catch3 = modifiedList3.indexOf(modifiedList1.get(i));
				totalWeight = weight1 * (size - i);
				buffer = searchEngine1;
				if (catch2 != -1) {
					totalWeight = totalWeight + weight2 * (size - catch2);
					buffer = buffer + searchEngine2;
				}
				if (catch3 != -1) {
					totalWeight = totalWeight + weight3 * (size - catch3);
					buffer = buffer + searchEngine3;
				}
				if (!localList.contains(text1)) {
					localList.add(text1);
					locationList.add(buffer);
					weightList.add(totalWeight);
				}
			}
			// check the link from the second search engine, whether it is available in other
			// search engine results as well.
			if(link2Available){
				catch1 = modifiedList1.indexOf(text2);
				catch3 = modifiedList3.indexOf(text2);
				totalWeight = weight2 * (size - i);
				buffer = searchEngine2;
				if (catch1 != -1) {
					totalWeight = totalWeight + weight1 * (size - catch1);
					buffer = buffer + searchEngine1;
				}
				if (catch3 != -1) {
					totalWeight = totalWeight + weight3 * (size - catch3);
					buffer = buffer + searchEngine3;
				}
				if (!localList.contains(text2)) {
					localList.add(text2);
					locationList.add(buffer);
					weightList.add(totalWeight);
				}
			}

			// check the link from the third search engine, whether it is available in other
			// search engine results as well.
			if(link3Available){
				catch1 = modifiedList1.indexOf(text3);
				catch2 = modifiedList2.indexOf(text3);
				totalWeight = weight3 * (size - i);
				buffer = searchEngine3;
				if (catch1 != -1) {
					totalWeight = totalWeight + weight1 * (size - catch1);
					buffer = buffer + searchEngine1;
				}
				if (catch2 != -1) {
					totalWeight = totalWeight + weight2 * (size - catch2);
					buffer = buffer + searchEngine2;
				}
				if (!localList.contains(text3)) {
					localList.add(text3);
					locationList.add(buffer);
					weightList.add(totalWeight);
				}
			}

		}

	}
	
	/**
	 * Compile the list of aggregated results into a presentable format.
	 */
	private void compiledAggregatedResult() {
		ArrayList secondWeightedList = UtilityFunctions.copyTo(weightList);
		Collections.sort(secondWeightedList);
		int position = 0;
		String location = null;
		String text = null;
		for (int i = secondWeightedList.size() - 1; i >= 0; i--) {
			position = weightList.indexOf(secondWeightedList.get(i));
			location = (String) locationList.get(position);
			if (location.startsWith(searchEngine1)) {
				text = (String) list1.get(modifiedList1.indexOf(localList
						.get(position)));
				text = bap1.getParsedOutput(text);
				if (!(aggregatedResult.contains(text))) {
					aggregatedResult.add(text);
				}
			} else if (location.startsWith(searchEngine2)) {
				text = (String) list2.get(modifiedList2.indexOf(localList
						.get(position)));
				text = bap2.getParsedOutput(text);
				if (!(aggregatedResult.contains(text))) {
					aggregatedResult.add(text);
				}
			} else if (location.startsWith(searchEngine3)) {
				text = (String) list3.get(modifiedList3.indexOf(localList
						.get(position)));
				text = bap3.getParsedOutput(text);
				if (!(aggregatedResult.contains(text))) {
					aggregatedResult.add(text);
				}
			}
		}
	}
	
	/** 
	 * return the list of results to be displayed.
	 * @return
	 */
	public ArrayList<String> getAggregatedResult() {
		return aggregatedResult;
	}
}
