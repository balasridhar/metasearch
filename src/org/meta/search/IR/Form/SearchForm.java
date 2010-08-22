package org.meta.search.IR.Form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * This class is just used for holding all the different parameters that
 * we get from the jsp page. 
 * 
 * @author Anusha
 */
public class SearchForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	/**
	 * Holds the search query.
	 */
	private String searchString = null;
	/**
	 * Holds the Search engine used. in case we are trying to debug the
	 * results retrieved from that particular search engine.
	 */
	private String SearchEngineused = null;
	/**
	 * Holds the value 'on' if yahoo is checked on the jsp page. 
	 */
	private String yahooCheck = null;
	/**
	 * Holds the value 'on' if microsoft is checked on the jsp page. 
	 */
	private String microsoftCheck = null;
	/**
	 * Holds the value 'on' if ask is checked on the jsp page. 
	 */
	private String askCheck = null;
	/**
	 * Holds the value 'on' if lycos is checked on the jsp page. 
	 */
	private String lycosCheck = null;
	/**
	 * Holds the value 'on' if altavista is checked on the jsp page. 
	 */
	private String altavistaCheck = null;
	/**
	 * Holds the value 'Single Aggregation' or 'Three page Aggregation',
	 * depending on the type selected on the jsp page. 
	 */
	private String aggregationType = null;
	/**
	 * Holds the page number of the meta search engine results.
	 */
	private int pageId = 0;
	/**
	 * Holds the number of Results being returned as part of the meta search
	 * engine results.
	 */
	private int numberOfResults = 0;
	/**
	 * Holds the Page number where the relevant results were identified.
	 */
	private String relevantInfo = null;
	/**
	 * Holds the relevant ids of the results which were deemed as relevant 
	 * by the user.
	 */
	private String relevantIds= null;
	/** 
	 * Holds the starting rank of the result being displayed.
	 * Like for example 17-36. 
	 */
	private int startPosition = 0;
	/** 
	 * Holds the ending rank of the result being displayed.
	 * Like for example 17-36. 
	 */
	private int endPosition = 0;
	/**
	 * Holds the meta search engine search results 
	 */
	private String searchResults = null;
	/**
	 * Holds the Relevant Search results.
	 */
	private String relevantSearchResults = null;
	
	/** 
	 * Getter for Starting Rank. 
	 * @return
	 */
	public int getStartPosition() {
		return startPosition;
	}

	/** 
	 * Setter for Starting Rank. 
	 */
	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}

	/** 
	 * Getter for Ending Rank. 
	 * @return
	 */
	public int getEndPosition() {
		return endPosition;
	}

	/** 
	 * Setter for Ending Rank. 
	 */
	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}
	
	/**
	 * Constructor.
	 */
	public SearchForm(){
		searchString ="";
	}
	
	/** 
	 * Getter for Search Query. 
	 * @return
	 */
	public String getSearchString() {
		return searchString;
	}

	/** 
	 * Setter for Search Query. 
	 */
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	
	/**
	 * Validation Logic for search string. 
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		
		ActionErrors errors = new ActionErrors();
		if(("").equals(searchString)){
			errors.add("Search String", new ActionError("error.search.null"));
		}
		return errors;
	}

	/** 
	 * Getter for Search Engine being used. 
	 * @return
	 */
	public String getSearchEngineused() {
		return SearchEngineused;
	}
	
	/** 
	 * Setter for Search Engine being used. 
	 */
	public void setSearchEngineused(String searchEngineused) {
		SearchEngineused = searchEngineused;
	}
	
	/** 
	 * Getter for yahoo search engine selection. 
	 * @return
	 */
	public String getYahooCheck() {
		return yahooCheck;
	}
	
	/** 
	 * Setter for yahoo search engine selection. 
	 */
	public void setYahooCheck(String yahooCheck) {
		this.yahooCheck = yahooCheck;
	}

	/** 
	 * Getter for microsoft search engine selection. 
	 * @return
	 */
	public String getMicrosoftCheck() {
		return microsoftCheck;
	}
	
	/** 
	 * Setter for microsoft search engine selection. 
	 */
	public void setMicrosoftCheck(String microsoftCheck) {
		this.microsoftCheck = microsoftCheck;
	}

	/** 
	 * Getter for ask search engine selection. 
	 * @return
	 */
	public String getAskCheck() {
		return askCheck;
	}
	
	/** 
	 * Setter for ask search engine selection. 
	 */
	public void setAskCheck(String askCheck) {
		this.askCheck = askCheck;
	}

	/** 
	 * Getter for lycos search engine selection. 
	 * @return
	 */
	public String getLycosCheck() {
		return lycosCheck;
	}

	/** 
	 * Setter for lycos search engine selection. 
	 */
	public void setLycosCheck(String lycosCheck) {
		this.lycosCheck = lycosCheck;
	}

	/** 
	 * Getter for altavista search engine selection. 
	 * @return
	 */
	public String getAltavistaCheck() {
		return altavistaCheck;
	}

	/** 
	 * Setter for altavista search engine selection. 
	 */
	public void setAltavistaCheck(String altavistaCheck) {
		this.altavistaCheck = altavistaCheck;
	}

	/** 
	 * Getter for aggregation type selection. 
	 * @return
	 */
	public String getAggregationType() {
		return aggregationType;
	}

	/** 
	 * Setter for aggregation type selection. 
	 */
	public void setAggregationType(String aggregationType) {
		this.aggregationType = aggregationType;
	}
	
	/** 
	 * Getter for Page Number. 
	 * @return
	 */
	public int getPageId() {
		return pageId;
	}

	/** 
	 * Setter for Page Number. 
	 */
	public void setPageId(int pageId) {
		this.pageId = pageId;
	}

	/** 
	 * Getter for number of results being displayed in that page. 
	 * @return
	 */
	public int getNumberOfResults() {
		return numberOfResults;
	}
	
	/** 
	 * Setter for number of results being displayed in that page. 
	 */
	public void setNumberOfResults(int relevantIds) {
		this.numberOfResults = relevantIds;
	}

	/** 
	 * Getter for getting the page number where the relevant selection was made. 
	 * @return
	 */
	public String getRelevantInfo() {
		return relevantInfo;
	}

	/** 
	 * Setter for getting the page number where the relevant selection was made. 
	 */
	public void setRelevantInfo(String relevantInfo) {
		this.relevantInfo = relevantInfo;
	}

	/** 
	 * Getter for relevant ids selected. 
	 * @return
	 */
	public String getRelevantIds() {
		return relevantIds;
	}

	/** 
	 * Setter for relevant ids selected. 
	 */
	public void setRelevantIds(String relevantIds) {
		this.relevantIds = relevantIds;
	}

	/**
	 * Getter for getting the Search Results.
	 * @return
	 */
	public String getSearchResults() {
		return searchResults;
	}

	/**
	 * Setter for setting the Search Results.
	 * @param searchResults
	 */
	public void setSearchResults(String searchResults) {
		this.searchResults = searchResults;
	}
	
	/**
	 * Getter for holding the search relvant Search results.
	 * @return
	 */
	public String getRelevantSearchResults() {
		return relevantSearchResults;
	}
	
	/**
	 * Setter for setting the relevant search results.
	 * @param relevantSearchResults
	 */
	public void setRelevantSearchResults(String relevantSearchResults) {
		this.relevantSearchResults = relevantSearchResults;
	}
}
