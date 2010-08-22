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
 * Class that takes care of converting the result text from using microsoft formatting
 * to use the unified format.
 * @author Anusha
 */
public class MicrosoftAggregateParser extends BaseAggregateParser{

	/** 
	 * holds the color value in which the result title has to be displayed.
	 */
	private String color = null;
	
	/**
	 * Constructor.
	 * @param color
	 */
	public MicrosoftAggregateParser(String color){
		this.color = color;
	}
	
	/**
	 * Get the formatted result text.
	 * remove all the unwanted microsoft text formatting from the string 
	 */
	public String getParsedOutput(String microsoftText) {
		String text = null;
		text = stripofUnwantedtags(microsoftText);
		try {
			text = parsesa(text,false)+parsesp(text)+parsescite(text)+parsesa(text,true);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}
	
	/** 
	 * get the link text formatted. if the value is false then get the title
	 * if the value is true get the cached information displayed as part of the
	 * microsoft search engine.
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
				if(!(UtilityConstants.microsoftCachedString.equals(n.toPlainTextString()))){
					//System.out.println("dasdasdasdasdas : "+tn.toHtml(true));
					//System.out.println("asdjashdashdas : "+n.toPlainTextString());
					//System.out.println("daasasasdsa : "+n.getText());
					text = "<urlTitle><"+n.getText()+">"+"<font color=\""+color+"\"><b>"+n.toPlainTextString()+"</b></font>"+"</a></urlTitle>";
			//		System.out.println("");
				}
			}
			if(value){
				if((UtilityConstants.microsoftCachedString.equals(n.toPlainTextString()))){
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
	private String parsesp(String text) throws ParserException{
		Parser parser = new Parser(text);
		NodeList nl = parser.parse(null);
		TagNameFilter tg = new TagNameFilter(UtilityConstants.pTag);
		NodeList nla = nl.extractAllNodesThatMatch(tg,true);
		
		for(int i=0; i<nla.size();i++){
			Node n = nla.elementAt(i);
			TagNode tn = (TagNode)n;
			text = "<br><p align=\"left\"><abstr>"+n.toPlainTextString()+"</abstr>";
		}
		return text;
	}
	
	/**
	 * Get the link citation that normally accompanies a microsoft search result.
	 * @param text
	 * @return
	 * @throws ParserException
	 */
	private String parsescite(String text) throws ParserException{
		String subString = text.substring(text.indexOf("<cite>"), text.indexOf("</cite>"));
		text = "<br><font color=\"red\">"+subString+"</font><br>";
		return text;
	}
	
	/**
	 * remove all the unwanted tags from the result string 
	 * @param text
	 * @return
	 */
	private String stripofUnwantedtags(String text){
		String subString;
		subString = text.replaceAll(UtilityConstants.h3StartTag, "");
		subString = subString.replaceAll(UtilityConstants.h3EndTag, "");
		subString = subString.replaceAll(UtilityConstants.strongStartTag, "");
		subString = subString.replaceAll(UtilityConstants.strongEndTag, "");
		return subString;
	}
}
