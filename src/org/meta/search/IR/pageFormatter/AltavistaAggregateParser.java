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
 * Class that takes care of converting the result text from using altavista formatting
 * to use the unified format.
 * @author Anusha
 *
 */
public class AltavistaAggregateParser extends BaseAggregateParser {
	/** 
	 * holds the color value in which the result title has to be displayed.
	 */
	private String color = null;
	/**
	 * Constructor.
	 * @param color
	 */
	public AltavistaAggregateParser(String color){
		this.color = color;
	}
	
	/**
	 * Get the formatted result text.
	 */
	public String getParsedOutput(String altavistaText) {
		String text = null;
		text = altavistaText;
		try {
			text = parsesa(text,false)+parsesspan(text)+parsesa(text,true);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}
	
	/**
	 * get the link text formatted. if the value is false then get the title
	 * if the value is true get the cached information displayed as part of the
	 * altavista search engine.
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
			if(!value){
				if(UtilityConstants.altavistaLabelClass3.equals(tn.getAttribute(UtilityConstants.classAttribute))){
					text = "<urlTitle><"+n.getText()+">"+"<font color=\""+color+"\"><b>"+n.toPlainTextString()+"</b></font>"+"</a></urlTitle>";
				}
			}
			if(value){
				if(UtilityConstants.altavistaLabelClass4.equals(tn.getAttribute(UtilityConstants.classAttribute))){
					text = "<"+n.getText()+">"+n.toPlainTextString()+"</a></p>";
				}
			}
		}
		return text;
	}
	/**
	 * get the abstract text from the result string.
	 * @param text
	 * @return
	 * @throws ParserException
	 */
	private String parsesspan(String text) throws ParserException{
		Parser parser = new Parser(text);
		NodeList nl = parser.parse(null);
		HasAttributeFilter haf = new HasAttributeFilter(UtilityConstants.classAttribute);
		TagNameFilter tg = new TagNameFilter(UtilityConstants.spanTag);
		AndFilter af = new AndFilter(tg,haf);
		NodeList nla = nl.extractAllNodesThatMatch(af,true);
		NodeList nlaclass = nla.extractAllNodesThatMatch(haf, true);
		
		for(int i=0; i<nlaclass.size();i++){
			Node n = nlaclass.elementAt(i);
			TagNode tn = (TagNode)n;
			if((UtilityConstants.altavistaLabelClass5.equals(tn.getAttribute(UtilityConstants.classAttribute)))){
				text = "<br><p align=\"left\"><abstr>"+n.toPlainTextString()+"</abstr>";
			}else if((UtilityConstants.altavistaLabelClass6.equals(tn.getAttribute(UtilityConstants.classAttribute)))){
				text = text+"<br><font color=\"green\">"+n.toPlainTextString()+"</font><br>";
			}
		}
		return text;
	}
}
