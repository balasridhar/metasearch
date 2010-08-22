package org.meta.search.IR.pageFormatter;

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
 * Class that takes care of converting the result text from using lycos formatting
 * to use the unified format.
 * @author Anusha
 */
public class LycosAggregateParser extends BaseAggregateParser{
	/** 
	 * holds the color value in which the result title has to be displayed.
	 */
	private String color = null;
	/**
	 * Constructor.
	 * @param color
	 */
	public LycosAggregateParser(String color){
		this.color = color;
	}
	
	/**
	 * Get the formatted result text.
	 * remove all the unwanted lycos text formatting from the string 
	 */
	public String getParsedOutput(String lycosText) {
		String text = null;
		text = stripofUnwantedtags(lycosText);
		try {
			text = parsesa(text)+parsesabstr(text)+parsesspan(text);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}
	
	/**
	 * get the link text formatted.
	 * @param text
	 * @return
	 * @throws ParserException
	 */
	private String parsesa(String text) throws ParserException{
		Parser parser = new Parser(text);
		NodeList nl = parser.parse(null);
		HasAttributeFilter haf = new HasAttributeFilter(UtilityConstants.hrefAttribute);
		TagNameFilter tg = new TagNameFilter(UtilityConstants.aTag);
		AndFilter af = new AndFilter(tg,haf);
		NodeList nla = nl.extractAllNodesThatMatch(af,true);
		NodeList nlaclass = nla.extractAllNodesThatMatch(haf, true);
		
		for(int i=0; i<nlaclass.size();i++){
			Node n = nlaclass.elementAt(i);
			TagNode tn = (TagNode)n;
			text = "<p align=\"left\"><urlTitle><"+n.getText()+">"+"<font color=\""+color+"\"><b>"+n.toPlainTextString()+"</b></font>"+"</a></urlTitle>";
		}
		return text;
	}
	
	/**
	 * Get the Url information that is given at the end of each result in lycos search
	 * @param text
	 * @return
	 * @throws ParserException
	 */
	private String parsesspan(String text) throws ParserException{
		Parser parser = new Parser(text);
		NodeList nl = parser.parse(null);
		TagNameFilter tg = new TagNameFilter(UtilityConstants.spanTag);
		NodeList nla = nl.extractAllNodesThatMatch(tg,true);
		
		for(int i=0; i<nla.size();i++){
			Node n = nla.elementAt(i);
			TagNode tn = (TagNode)n;
			text = "<font color=\"green\">"+n.toPlainTextString()+"</font></p>";
		}
		return text;
	}
	
	/**
	 * Get the abstract text from the result string.
	 * @param text
	 * @return
	 * @throws ParserException
	 */
	private String parsesabstr(String text) throws ParserException{
		String subString = text.substring(text.indexOf("</a>"), text.indexOf("<span class=\"green\">"));
		text = "<br><abstr>"+subString+"</abstr><br>";
		return text;
	}
	
	/** 
	 * Strip of all the unwanted tags from the lycos result string.
	 * @param text
	 * @return
	 */
	private String stripofUnwantedtags(String text){
		String subString;
		subString = text.replaceAll(UtilityConstants.boldStartTag, "");
		subString = subString.replaceAll(UtilityConstants.boldEndTag, "");
		subString = subString.replaceAll(UtilityConstants.breakEndtag, "");
		return subString;
	}
}
