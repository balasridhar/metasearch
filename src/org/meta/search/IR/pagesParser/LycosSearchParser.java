package org.meta.search.IR.pagesParser;

import java.util.ArrayList;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.meta.search.IR.Bo.UtilityConstants;
import org.meta.search.IR.pageFormatter.LycosAggregateParser;
/**
 * Class that is used for getting the list of search results present in the lycos html document
 * passed to this class.
 * @author Bala Sridhar
 */
public class LycosSearchParser {
	/**
	 * holds the html document as a string object.
	 */
	private String webPageText = null;
	/**
	 * Used for debugging purposes.
	 */
	private LycosAggregateParser lap = null;
	/**
	 * Constructor.
	 * @param htmlText
	 */
	public LycosSearchParser(String htmlText) {
		// TODO Auto-generated constructor stub
		webPageText = htmlText;
	}
	/**
	 * Constructor. when the value is true the default search color is used
	 * for setting the font color for the link in the corresponding aggregate parser.
	 * @param value
	 */
	public LycosSearchParser(boolean value) {
		// TODO Auto-generated constructor stub
		if(value){
			lap = new LycosAggregateParser(UtilityConstants.defaultSearchColor);
		}
	}
	
	/**
	 * Extract the results using the html parser library by looking out for li tags
	 * in the html document. Lycos encapsulates each result in an li tag.
	 * @return
	 * @throws ParserException
	 */
	public ArrayList<String> parsingLycosSearch() throws Exception {
		// TODO Auto-generated method stub
		Parser parser = new Parser(webPageText);
		NodeList nl = parser.parse(null);
		TagNameFilter tg = new TagNameFilter(UtilityConstants.liTag);
		NodeList nla = nl.extractAllNodesThatMatch(tg, true);
		return parseLycosSearchPage(nla);
	}
	
	/**
	 * Now go through each of the li block to get each result 
	 * @param nlaclass
	 * @return
	 */
	private ArrayList<String> parseLycosSearchPage(NodeList nlaclass) throws Exception{
		// TODO Auto-generated method stub
		ArrayList<String>  parsedLycosContent= new ArrayList<String>();
		for(int i=0; i<nlaclass.size();i++){
			Node n = nlaclass.elementAt(i);
			String interMediatetext = null;
			interMediatetext = n.toHtml(true);
			if ((interMediatetext.contains(UtilityConstants.lycosCompareString1))&&(!interMediatetext.contains(UtilityConstants.lycosCompareString2))){
				interMediatetext = getStrippedTextFromWebPage(interMediatetext);
				if(lap != null){
					interMediatetext = lap.getParsedOutput(interMediatetext);
				}
				parsedLycosContent.add(interMediatetext.trim());
			}
		}
		return parsedLycosContent;
	}
	/**
	 * Strip off the unwanted tags in the result string.
	 * @param webPageText
	 * @return
	 */
	private String getStrippedTextFromWebPage(String webPageText) throws Exception{
		String subString;
		subString = webPageText.replaceAll(UtilityConstants.liStartTag, "");
		subString = subString.replaceAll(UtilityConstants.liEndTag, "");
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
