package org.meta.search.IR.Bo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.htmlparser.util.ParserException;
import org.meta.search.IR.pagesParser.AltaVistaParser;
import org.meta.search.IR.pagesParser.AskSearchParser;
import org.meta.search.IR.pagesParser.LycosSearchParser;
import org.meta.search.IR.pagesParser.MicrosoftLiveSearchParser;
import org.meta.search.IR.pagesParser.YahooSearchParser;

/**
 * This class is used for retrieving results from each search engine based 
 * on the selection made by the user.
 * 
 * @author Bala Sridhar
 */
public class TestingHtmlParser {
	/**
	 * Object of yahoo search parser that parses through the yahoo html 
	 * document to get the results.
	 */
	private YahooSearchParser ysp = null;
	/**
	 * Object of altavista search parser that parses through the altavista html 
	 * document to get the results.
	 */
	private AltaVistaParser avp = null;
	/**
	 * Object of microsoft search parser that parses through the microsoft html 
	 * document to get the results.
	 */
	private MicrosoftLiveSearchParser mlsp = null;
	/**
	 * Object of lycos search parser that parses through the lycos html 
	 * document to get the results.
	 */
	private LycosSearchParser lsp = null;
	/**
	 * Object of ask search parser that parses through the ask html 
	 * document to get the results.
	 */
	private AskSearchParser asp = null;
	/**
	 * Object of URLConnection which is used for retrieving Html
	 * document.
	 */
	private URLConnection connection= null;
	/**
	 * URL class object type.
	 */
	private URL con = null;
	/**
	 * Boolean flag that is set if there is an error occured
	 * in either of the subsystem.
	 */
	private boolean errorRetrieval = false;
	/**
	 *  String that holds the search engine causing a problem.
	 */
	private String errorSearchEngine = null;
	
	/** 
	 * Constructor.
	 */
	public TestingHtmlParser(){
		ysp = new YahooSearchParser(false);
		avp = new AltaVistaParser(false);
		mlsp = new MicrosoftLiveSearchParser(false);
		lsp = new LycosSearchParser(false);
		asp = new AskSearchParser(false);
	}
	
	/**
	 * Getting search results from the different search engines. 
	 * @param query - Search Query
	 * @param engine - Engine to retrieve
	 * @param page - Page to be retrieved.
	 * @return
	 */
	public ArrayList<String> getSearchResults(String query, String engine, int page){
		ArrayList<String> links = null;
		links = getParsedContents(query,engine,page);
		return links;
	}
	
	/**
	 * Get back parsed results from the corresponding search parsers.
	 * @param query - Search Query
	 * @param engine - Engine to retrieve.
	 * @param page - page to be retrieved.
	 * @return
	 * @throws ParserException
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<String> getParsedContents(String query, String engine, int page){
		ArrayList<String> links = null;
		
		if (errorRetrieval){
			return links;
		}
		
		if (UtilityConstants.yahooString.equals(engine)){
			ysp.setText(getSearchResultsFromSearchEngines(UtilityConstants.yahooSearchUrl,query,((page-1)*10)+1));
			try {
				links = ysp.parsingYahoo();
				errorRetrieval = false;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				errorRetrieval = true;
				errorSearchEngine = UtilityConstants.yahooString; 
			}
		} else if (UtilityConstants.altavistaString.equals(engine)){
			avp.setText(getSearchResultsFromSearchEngines(UtilityConstants.altavistaSearchUrl,query,((page-1)*10)));
			try {
				links = avp.parsingAltaVista();
				errorRetrieval = false;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				errorRetrieval = true;
				errorSearchEngine = UtilityConstants.altavistaString;
			}
		} else if (UtilityConstants.microsoftString.equals(engine)){
			mlsp.setText(getSearchResultsFromSearchEngines(UtilityConstants.microsoftSearchUrl,query,((page-1)*10)+1));
			try {
				links = mlsp.parsingMicrosoftLiveSearch();
				errorRetrieval = false;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				errorRetrieval = true;
				errorSearchEngine = UtilityConstants.microsoftString;
			}
		} else if (UtilityConstants.lycosString.equals(engine)){
			lsp.setText(getSearchResultsFromSearchEngines(UtilityConstants.lycosSearchUrl,query, (page-1)));
			try {
				links = lsp.parsingLycosSearch();
				errorRetrieval = false;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				errorRetrieval = true;
				errorSearchEngine = UtilityConstants.lycosString;
			}
		} else if (UtilityConstants.askString.equals(engine)){
			asp.setText(getSearchResultsFromSearchEngines(UtilityConstants.askSearchUrl,query, page));
			try {
				links = asp.parsingAskSearch();
				errorRetrieval = false;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				errorRetrieval = true;
				errorSearchEngine = UtilityConstants.askString;
			}
		}
		return links;
	}
	
	/**
	 * Get the HTML document by substituting the search string and the page number
	 * in the URL for the corresponding search engines.  
	 * @param queryUrl
	 * @param query
	 * @param pageNum
	 * @return
	 */
	private String getSearchResultsFromSearchEngines(String queryUrl,String query, int pageNum){

		String modifiedQuery = query.replace(" ", "+");
		queryUrl = queryUrl.replace("ryque", modifiedQuery);
		String page = Integer.toString(pageNum);
		queryUrl = queryUrl.replace("agep", page);
		return searchEngines(queryUrl);
	}
	
	/**
	 * Do the connection to the search engine and get the HTML document.
	 * @param constructedURL
	 * @return
	 */
	private String searchEngines(String constructedURL){
		StringBuffer sb = null;
		try{
			con = new URL(constructedURL);
			connection = con.openConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		BufferedReader in = null;
		try {
		in = new BufferedReader(
		new InputStreamReader(
		connection.getInputStream()));

		sb = new StringBuffer();
		String inputLine;
		while (
		(inputLine = in.readLine()) != null) {
		sb.append(inputLine);
		}
		} catch (IOException e) {
		e.printStackTrace();
		} 
		return sb.toString();
	}
	
	/**
	 * Returns the Error Retrieval Value. if set then there is an error occured in
	 * one of the subsystems.
	 * @return
	 */
	public boolean getErrorRetrievalValue(){
		return errorRetrieval;
	}
	
	/**
	 * returns the search engine causing the problem.
	 * @return
	 */
	public String getErrorSearchEngine() {
		return errorSearchEngine;
	}
}
