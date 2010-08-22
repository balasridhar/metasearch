package org.meta.search.IR.Bo;

import java.util.ArrayList;

import org.meta.search.IR.Form.SearchForm;
import org.meta.search.IR.Relevance.MetaResultParser;
import org.meta.search.IR.Relevance.MetaSearchRelevanceFeedBack;

/**
 * This class is the controller class for the whole meta search engine setup.
 * this class has logic for retreiving the aggregated result and construct a 
 * html based bulleted list of all the results that are to be shown to the user.
 * 
 * @author Bala Sridhar
 */
public class SearchSeparateEngines {
	/**
	 * Object of the TestingHtmlParser
	 */
	private TestingHtmlParser thp = null;
	/**
	 * Holds the number of results that are returned to the user.
	 */
	private int numberOfResultsReturned = 0;
	/**
	 * Search Form containing all the variables set by the struts 
	 * framework. 
	 */
	private SearchForm sForm = null;
	/**
	 * Object of the Aggregation Wrapper class.
	 */
	private AggregationWrapper aw = null;
	/**
	 * Object of the RelevanceFeedback class.
	 */
	private MetaSearchRelevanceFeedBack msrf = null;
	/**
	 * Object of the MetaResultParser class.
	 */
	private MetaResultParser mrp = null;
	
	/**
	 * Constructor.
	 * @param sForm
	 */
	public SearchSeparateEngines(SearchForm sForm){
		thp = new TestingHtmlParser();
		this.sForm = sForm;
		aw = new AggregationWrapper(sForm,thp);
		msrf = new MetaSearchRelevanceFeedBack();
		mrp = new MetaResultParser();
	}
	
	/**
	 * The Normal Search Process. This function is called each time the user
	 * submits a new query to be searched.
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String searchProcess(int page){
		String str = null;
		String query = sForm.getSearchString();
		String aggType = sForm.getAggregationType();
		
		// Single Aggregation type
		if(UtilityConstants.singleAggregationType.equals(aggType)){
			ArrayList<String>result =aw.getAggregationResult(aggType,query,page);
			if(!aw.isErrorRetrieval()){
				str = constructHtmlFromRetrievedResults(result);
			}else{
				str = convertErrorListToString(result);
			}
		// Three page Aggregation type
		}else if(UtilityConstants.tripleAggregationType.equals(aggType)){
			ArrayList<String>result = aw.getAggregationResult(aggType,query,page);
			if(!aw.isErrorRetrieval()){
				str = constructHtmlFromRetrievedResults(result);
			} else{
				str = convertErrorListToString(result);
			}
		}
		return str;
	}
	 
	/**
	 * This Function is called whenever the user submits some set of relevant results
	 * for doing relevance feedback feature.
	 * @param page - Page number 
	 * @param relevantQuery - Relevant Result IDS
	 * @return
	 */
	public String searchRelevantProcess(int page, String relevantQuery){
		String str = null;
		String query = sForm.getSearchString();
		String aggType = sForm.getAggregationType();
		
		// Single Aggregation type
		if(UtilityConstants.singleAggregationType.equals(aggType)){
			ArrayList<String> relevantIds = mrp.getRelevantIds(relevantQuery);
			ArrayList<String> result = null;
			// request came in from the same page
			if(Integer.parseInt(sForm.getRelevantInfo())== page){
				result =mrp.getSearchResultsList(sForm.getSearchResults());
				result =msrf.reArrangeResults(result,relevantIds);
			// request came for the next page. so re arrange the next page results
			// based on the relevant results selected by the user.
			}else if(Integer.parseInt(sForm.getRelevantInfo()) != page){
				result =aw.getAggregationResult(aggType,query,page);
				if(!aw.isErrorRetrieval()){
					ArrayList<String> queryPage =mrp.getSearchResultsList(sForm.getRelevantSearchResults());
					result =msrf.getRelevantResultsPages(queryPage, relevantIds, result);
				}else{
					str = convertErrorListToString(result);
					return str;
				}
			}
			str = constructHtmlFromRetrievedResults(result);
		// Three page Aggregation type
		}else if(UtilityConstants.tripleAggregationType.equals(aggType)){
			ArrayList<String> result = null;
			ArrayList<String> relevantIds = mrp.getRelevantIds(relevantQuery);
			// request came in from the same page
			if(Integer.parseInt(sForm.getRelevantInfo())== page){
				result = mrp.getSearchResultsList(sForm.getSearchResults());
				result = msrf.reArrangeResults(result,relevantIds);
			// request came for the next page. so re arrange the next page results
			// based on the relevant results selected by the user.
			}else if(Integer.parseInt(sForm.getRelevantInfo()) != page){
				result = aw.getAggregationResult(aggType,query,page);
				if(!aw.isErrorRetrieval()){
					ArrayList<String> queryPage = mrp.getSearchResultsList(sForm.getRelevantSearchResults());
					result = msrf.getRelevantResultsPages(queryPage, relevantIds, result);
				}else{
					str = convertErrorListToString(result);
					return str;
				}
			}
			str = constructHtmlFromRetrievedResults(result);
		}
		return str;
	}
	
	/**
	 * This function constructs the bulleted list of meta search engine results
	 * that is displayed to the user.
	 * @param links
	 * @return
	 */
	private String constructHtmlFromRetrievedResults(ArrayList<String> links){
		String constructedHtmlPage = null;
		numberOfResultsReturned =links.size();
		for (int i =0; i<links.size();i++){
			String separateLink = (String) links.get(i);
			//System.out.println(separateLink);
			separateLink = mrp.getModifiedResult(separateLink, i);
			separateLink =UtilityConstants.liStartTag+separateLink+UtilityConstants.liEndTag;
			if (i == 0){
				constructedHtmlPage = UtilityConstants.ulStartTag+separateLink;
			}else if(i == (links.size()-1)){
				constructedHtmlPage = constructedHtmlPage+separateLink+UtilityConstants.ulEndTag;
			}else{
				constructedHtmlPage = constructedHtmlPage+separateLink;
			}
		}
		return constructedHtmlPage;
	}
	
	/**
	 * Convert the error List into a errorString
	 * @param result
	 * @return
	 */
	private String convertErrorListToString(ArrayList<String> result){
		String errorHtmlText = null;
		errorHtmlText = result.get(0);
		return errorHtmlText;
	}
	
	/**
	 * returns the number of results going to be displayed as part of the results page.
	 * @return
	 */
	public int getNumberOfResultsReturned(){
		return numberOfResultsReturned;
	}
	
	/** 
	 * Returns whether there was any error in any of the subsystems.
	 * @return
	 */
	public boolean isError(){
		return aw.isErrorRetrieval();
	}
}
