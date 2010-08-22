package org.meta.search.IR.Relevance;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.meta.search.IR.Bo.UtilityConstants;

/**
 * Class for parsing through the uni formatted meta search engine results.
 * @author Anusha
 */
public class MetaResultParser {
	/**
	 * Constructor.
	 */
	public MetaResultParser(){
	}
	
	/**
	 * Get the title text from the result text submitted.
	 * @param text
	 * @return
	 */
	public String getTitle(String text){
		String title= null;
		title = text.substring(text.indexOf(UtilityConstants.boldStartTag), text.indexOf(UtilityConstants.boldEndTag));
		title = title.replaceAll(UtilityConstants.boldStartTag, "");
		return title;
	}
	
	/**
	 * Get the abstract text from the result text submitted.
	 * @param text
	 * @return
	 */
	public String getAbstract(String text){
		String abstractString = null;
		abstractString = text.substring(text.indexOf(UtilityConstants.abstractStartTag), text.indexOf(UtilityConstants.abstractEndTag));
		abstractString = abstractString.replaceAll(UtilityConstants.abstractStartTag, "");
		return abstractString;
	}
	
	/**
	 * Add the checkbox to each of the meta search engine result. 
	 * @param text
	 * @param i
	 * @return
	 */
	public String getModifiedResult(String text, int i){
		String modifiedText = null;
		String subString = text.substring(text.indexOf("<urlTitle>"), text.indexOf("</urlTitle>"));
		String startingPortion = subString.substring(subString.indexOf("<urlTitle>"), subString.indexOf("</a>"));
		String endingPortion = text.substring(text.indexOf("</urlTitle>"), text.length());
		String checkboxTag = "</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"checkbox\" id=\"check"+i+"\" value=\""+i+"\">Relevancy";
		modifiedText = startingPortion+checkboxTag+endingPortion;
		return modifiedText;
	}
	
	/**
	 * Get the List of checkbox ids that are used for getting the relevant search results.
	 * @param relevantString
	 * @return
	 */
	public ArrayList<String> getRelevantIds(String relevantString){
		ArrayList<String> arr= new ArrayList<String>();
		String delims = ",";
		StringTokenizer st = new StringTokenizer(relevantString,delims);
		while(st.hasMoreTokens()){
			arr.add(st.nextToken());
		}
		return arr;
	}
	
	/**
	 * Get a list of search results from the string.
	 * @param searchResults
	 * @return
	 */
	public ArrayList<String> getSearchResultsList(String searchResults){
		ArrayList<String> searchRes = new ArrayList<String>();
		Parser parser = null;
		try {
			parser = new Parser(searchResults);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NodeList nl = null;
		try {
			nl = parser.parse(null);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TagNameFilter tg = new TagNameFilter(UtilityConstants.liTag);
		NodeList nla = nl.extractAllNodesThatMatch(tg,true);
		for(int i=0; i<nla.size();i++){
			Node n = nla.elementAt(i);
			String labelString = n.toHtml(true);
			labelString = labelString.replaceAll(UtilityConstants.liStartTag, "");
			labelString = labelString.replaceAll(UtilityConstants.liEndTag, "");
			//System.out.println(labelString);
			searchRes.add(labelString);
		}
		return searchRes;
	}
}
