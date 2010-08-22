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
 * Class that takes care of converting the result text from using yahoo formatting
 * to use the unified format.
 * @author Anusha
 */
public class YahooAggregateParser extends BaseAggregateParser{
	
	/** 
	 * holds the color value in which the result title has to be displayed.
	 */
	private String color = null;
	
	/**
	 * Constructor.
	 * @param color
	 */
	public YahooAggregateParser(String color){
		this.color = color;
	}
	
	/**
	 * Get the formatted result text.
	 * remove all the unwanted yahoo text formatting from the string 
	 */
	public String getParsedOutput(String yahootext) {
		String text = null;
		if (yahootext.contains(UtilityConstants.yahooRemoveHeading)){
			text = yahootext.substring(0, yahootext.indexOf(UtilityConstants.yahooRemoveHeading));
		}else{
			text = yahootext;
		}
		try {
			text = parsesa(text,false)+parsesdiv(text)+parsesa(text,true);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}
	
	/** 
	 * get the link text formatted. if the value is false then get the title
	 * if the value is true get the cached information displayed as part of the
	 * yahoo search engine.
	 * @param text
	 * @param value
	 * @return
	 * @throws ParserException
	 */
	private String parsesa(String text, boolean value) throws ParserException{
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
			if(UtilityConstants.yahooClassAttributeName1.equals(tn.getAttribute(UtilityConstants.classAttribute))){
				text = "<urlTitle><"+n.getText()+">"+"<font color=\""+color+"\"><b>"+n.toPlainTextString()+"</b></font>"+"</a></urlTitle>";
			}
			if(value){
				if((tn.getAttribute(UtilityConstants.classAttribute)) == null){
					text = "<"+n.getText()+">"+n.toPlainTextString()+"</a></p>";
				}
			}
		}
		return text;
	}
	
	/** 
	 * Get the abstract text from the result text formatted.
	 * @param text
	 * @return
	 * @throws ParserException
	 */
	private String parsesdiv(String text) throws ParserException{
		Parser parser = new Parser(text);
		NodeList nl = parser.parse(null);
		HasAttributeFilter haf = new HasAttributeFilter(UtilityConstants.classAttribute);
		TagNameFilter tg = new TagNameFilter(UtilityConstants.divTag);
		AndFilter af = new AndFilter(tg,haf);
		NodeList nla = nl.extractAllNodesThatMatch(af,true);
		NodeList nlaclass = nla.extractAllNodesThatMatch(haf, true);
		
		for(int i=0; i<nlaclass.size();i++){
			Node n = nlaclass.elementAt(i);
			TagNode tn = (TagNode)n;
			if((UtilityConstants.yahooClassAttributeName2.equals(tn.getAttribute(UtilityConstants.classAttribute)))||(UtilityConstants.yahooClassAttributeName3.equals(tn.getAttribute(UtilityConstants.classAttribute)))){
				text = "<br><p align=\"left\"><abstr>"+n.toPlainTextString()+"</abstr>";
			}
		}
		return text;
	}
}
