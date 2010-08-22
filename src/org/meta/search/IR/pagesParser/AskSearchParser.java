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
import org.meta.search.IR.pageFormatter.AskAggregateParser;
/**
 * Class that is used for getting the list of search results present in the ask html document
 * passed to this class.
 * @author Bala Sridhar
 */
public class AskSearchParser {
	/**
	 * holds the html document as a string object.
	 */
	private String webPageText = null;
	/**
	 * Used for debugging purposes.
	 */
	private AskAggregateParser aap = null;
	/**
	 * Constructor.
	 * @param htmlText
	 */
	public AskSearchParser(String htmlText) {
		// TODO Auto-generated constructor stub
		webPageText = htmlText;
	}
	/**
	 * Constructor. when the value is true the default search color is used
	 * for setting the font color for the link in the corresponding aggregate parser.
	 * @param value
	 */
	public AskSearchParser(boolean value) {
		// TODO Auto-generated constructor stub
		if(value){
			aap = new AskAggregateParser(UtilityConstants.defaultSearchColor);
		}
	}
	
	/**
	 * Return the list of results.
	 * @return
	 */
	public ArrayList<String> parsingAskSearch() throws Exception {
		// TODO Auto-generated method stub
		ArrayList<String> resu =null;
		resu = constructTheResultList();
		return resu;
	}
	
	/**
	 * Extract the results using the html parser library by looking out for a tags for the title,
	 * div tags for the abstract and span tags in the html document. 
	 * @return
	 */
	private ArrayList<String> constructTheResultList() throws ParserException{
		NodeList aTag = parsingTags(UtilityConstants.aTag,UtilityConstants.idAttribute);
		ArrayList<String> aTags = parseAskPage(aTag,UtilityConstants.askLinkId);
		NodeList divTag = parsingTags(UtilityConstants.divTag,UtilityConstants.idAttribute);
		ArrayList<String> divTags = parseAskPage(divTag,UtilityConstants.askAbstrId);
		NodeList spanTag = parsingTags(UtilityConstants.spanTag,UtilityConstants.idAttribute);
		ArrayList<String> spanTags = parseAskPage(spanTag,UtilityConstants.askSpanId);
		ArrayList<String> resu = concatenateLists(aTags,divTags,spanTags);
		return resu;
	}
	/**
	 * Concatenate the different components retrieved for each result and assemble it here.
	 * @param tags
	 * @param divTags
	 * @param spanTags
	 * @return
	 */
	private ArrayList<String> concatenateLists(ArrayList<String> tags,
			ArrayList<String> divTags, ArrayList<String> spanTags) {
		// TODO Auto-generated method stub
		ArrayList<String> res = new ArrayList<String>();
		if (!(tags.size() == divTags.size()) && !(tags.size() == spanTags.size())){
			//System.out.println("Cannot concatenate everything only doing the first "+tags.size());
		}
		for(int i =0;i<tags.size();i++){
			String totalText = null;
			totalText = tags.get(i)+divTags.get(i)+spanTags.get(i);
			if(aap != null){
				totalText = aap.getParsedOutput(totalText);
			}
			res.add(totalText);
		}
		return res;
	}
	/**
	 * parse the Html document to get the corresponding set of html information.
	 * @param tag
	 * @param attribute
	 * @return
	 * @throws ParserException
	 */
	private NodeList parsingTags(String tag, String attribute) throws ParserException{
		Parser parser = new Parser(webPageText);
		NodeList nl = parser.parse(null);
		HasAttributeFilter haf = new HasAttributeFilter(attribute);
		TagNameFilter tg = new TagNameFilter(tag);
		AndFilter af = new AndFilter(tg, haf);
		NodeList nla = nl.extractAllNodesThatMatch(af, true);
		NodeList nlaclass = nla.extractAllNodesThatMatch(haf, true);
		return nlaclass;
	}
	/**
	 * parse through each result getting the corresponding information, such as title,
	 * abstract anad any cached information.
	 * @param nlaclass
	 * @param toBeAppended
	 * @return
	 */
	private ArrayList<String> parseAskPage(NodeList nlaclass, String toBeAppended){
		ArrayList<String> resu = new ArrayList<String>();
		for(int i=0; i<nlaclass.size();i++){
			Node n = nlaclass.elementAt(i);
			TagNode tn = (TagNode)n;
			String interMediatetext = null;
			interMediatetext = n.toHtml(true);
			if ((tn.getAttribute(UtilityConstants.idAttribute).endsWith("_"+toBeAppended))&&(tn.getAttribute(UtilityConstants.classAttribute)!= null)){
				interMediatetext = getStrippedTextFromWebPage(interMediatetext);
				if (!resu.contains(interMediatetext.trim())){
					resu.add(interMediatetext.trim());
				}
			}
		}
		return resu;
	}
	/**
	 * Strip off the unwanted tags.
	 * @param webPageText
	 * @return
	 */
	private String getStrippedTextFromWebPage(String webPageText) {
		String subString;
		subString = webPageText.replaceAll(UtilityConstants.boldStartTag, "");
		subString = subString.replaceAll(UtilityConstants.boldEndTag, "");
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
