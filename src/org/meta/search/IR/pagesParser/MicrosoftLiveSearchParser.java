package org.meta.search.IR.pagesParser;

import java.util.ArrayList;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.meta.search.IR.Bo.UtilityConstants;
import org.meta.search.IR.pageFormatter.MicrosoftAggregateParser;
/**
 * Class that is used for getting the list of search results present in the microsoft html document
 * passed to this class.
 * @author Bala Sridhar
 */
public class MicrosoftLiveSearchParser {
	/**
	 * holds the html document as a string object.
	 */
	private String webPageText = null;
	/**
	 * Used for debugging purposes.
	 */
	private MicrosoftAggregateParser map = null;
	/**
	 * Constructor.
	 * @param pageText
	 */
	public MicrosoftLiveSearchParser(String pageText) {
		webPageText = pageText;
	}
	/**
	 * Constructor. when the value is true the default search color is used
	 * for setting the font color for the link in the corresponding aggregate parser.
	 * @param value
	 */
	public MicrosoftLiveSearchParser(boolean value) {
		// TODO Auto-generated constructor stub
		if(value){
			map = new MicrosoftAggregateParser(UtilityConstants.defaultSearchColor);
		}
	}
	/**
	 * Extract the results using the html parser library by looking out for li tags
	 * in the html document. Microsoft encapsulates each result in an li tag.
	 * @return
	 * @throws ParserException
	 */
	public ArrayList<String> parsingMicrosoftLiveSearch() throws Exception {
		if((webPageText.indexOf(UtilityConstants.microsoftResultsStarting))> -1 && (webPageText
				.indexOf(UtilityConstants.microsoftResultsEnding))>-1){
		webPageText = webPageText.substring(webPageText
				.indexOf(UtilityConstants.microsoftResultsStarting), webPageText
				.indexOf(UtilityConstants.microsoftResultsEnding));
		}
		Parser parser = new Parser(webPageText);
		NodeList nl = parser.parse(null);
		TagNameFilter tg = new TagNameFilter(UtilityConstants.liTag);
		NodeList nla = nl.extractAllNodesThatMatch(tg, true);
		return parseMicrosoftLiveSearchPage(nla);
	}

	/**
	 * Now go through each of the li block to get each result 
	 * @param nlaclass
	 * @return
	 */
	private ArrayList<String> parseMicrosoftLiveSearchPage(NodeList nlaclass) throws Exception{
		ArrayList<String> parsedMicrosoftLiveSearchContent = new ArrayList<String>();
		for (int i = 0; i < nlaclass.size(); i++) {
			Node n = nlaclass.elementAt(i);
			String interMediatetext = null;
			interMediatetext = n.toHtml(true);
			//System.out.println(interMediatetext);
			if ((interMediatetext.contains(UtilityConstants.microsoftCompareString))){
				interMediatetext = getStrippedTextFromWebPage(interMediatetext);
		//		System.out.println(interMediatetext);
				if(map != null){
					interMediatetext = map.getParsedOutput(interMediatetext);
				}
				parsedMicrosoftLiveSearchContent.add(interMediatetext);
			}
		}
		return parsedMicrosoftLiveSearchContent;
	}
	
	/**
	 * Strip off the li tags and also some other tags if available.
	 * @param webPageText
	 * @return
	 */
	private String getStrippedTextFromWebPage(String webPageText) throws Exception{
		String subString;
		subString = webPageText.replaceAll(UtilityConstants.liStartTag, "");
		subString = subString.replaceAll(UtilityConstants.liEndTag, "");
		subString = subString.replaceAll(UtilityConstants.microsoftStringReplace, "");
		subString = subString.replaceAll(UtilityConstants.ulEndTag, "");
		return subString;
	}
	/**
	 * Set the Html document String 
	 * @param text
	 */
	public void setText(String text){
		webPageText = text;
	}
}
