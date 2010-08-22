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
import org.meta.search.IR.pageFormatter.YahooAggregateParser;
/**
 * Class that is used for getting the list of search results present in the yahoo html document
 * passed to this class.
 * @author Bala Sridhar
 */
public class YahooSearchParser {
	/**
	 * holds the html document as a string object.
	 */
	private String webPageText = null;
	/**
	 * Used for debugging purposes.
	 */
	private YahooAggregateParser yap = null;
	/**
	 * Constructor.
	 * @param pageText
	 */
	public YahooSearchParser(String pageText){
		webPageText = pageText;
	}
	
	/**
	 * Constructor. when the value is true the default search color is used
	 * for setting the font color for the link in the corresponding aggregate parser.
	 * @param value
	 */
	public YahooSearchParser(boolean value) {
		// TODO Auto-generated constructor stub
		if(value){
			yap = new YahooAggregateParser(UtilityConstants.defaultSearchColor);
		}
	}
	
	/**
	 * Extract the results using the html parser library by looking out for div tags
	 * in the html document. Yahoo has separate div tags that encapsulate each result.
	 * @return
	 * @throws ParserException
	 */
	public ArrayList<String> parsingYahoo() throws Exception{
		Parser parser = new Parser(webPageText);
		NodeList nl = parser.parse(null);
		HasAttributeFilter haf = new HasAttributeFilter(UtilityConstants.classAttribute);
		TagNameFilter tg = new TagNameFilter(UtilityConstants.divTag);
		AndFilter af = new AndFilter(tg,haf);
		NodeList nla = nl.extractAllNodesThatMatch(af,true);
		NodeList nlaclass = nla.extractAllNodesThatMatch(haf, true);
		return parseYahooPage(nlaclass);
	}
	
	/**
	 * Now go through each of the div blocks to get each result by doing substring operations
	 * based on the class attribute names.
	 * @param nlaclass
	 * @return
	 */
	private ArrayList<String> parseYahooPage(NodeList nlaclass) throws Exception{
		String completeText = webPageText;
		//System.out.println(nlaclass.toHtml(true));
		ArrayList<String>  parsedYahooContent= new ArrayList<String>();
		int startPoint = -1;
		int endPoint = -1;
		String individualText = null;
		for(int i=0; i<nlaclass.size();i++){
			Node n = nlaclass.elementAt(i);
			TagNode tn = (TagNode)n;
			if(!((UtilityConstants.yahooSponsoredMatch).equals(tn.getAttribute(UtilityConstants.classAttribute)))){
				if((tn.getAttribute(UtilityConstants.classAttribute).indexOf(UtilityConstants.yahooResultText))>-1){
					String divTagText = n.getText();
					//System.out.println(n.getText());
					if (startPoint == -1){
						startPoint = completeText.indexOf(divTagText,0);
					} else if (endPoint == -1){
						endPoint = completeText.indexOf(divTagText,startPoint+10);
						individualText = getTextFromWebPage(completeText,startPoint,endPoint);
						individualText = "<"+individualText.substring(0, individualText.length()-1);
						individualText = getStrippedTextFromWebPage(individualText);
						//System.out.println("Start Point : "+individualText);
						if(yap != null){
							individualText = yap.getParsedOutput(individualText);
						}
						parsedYahooContent.add(individualText);
					} else {
						startPoint = endPoint;
						endPoint = completeText.indexOf(divTagText,startPoint+10);
						individualText = getTextFromWebPage(completeText,startPoint,endPoint);
						individualText = "<"+individualText.substring(0, individualText.length()-1);
						individualText = getStrippedTextFromWebPage(individualText);
						//System.out.println("EndPoint : "+individualText);
						if(yap != null){
							individualText = yap.getParsedOutput(individualText);
						}
						parsedYahooContent.add(individualText);
					}
				}else if((tn.getAttribute(UtilityConstants.classAttribute).indexOf(UtilityConstants.yahooBottomText))>0){
					if (startPoint != -1){
						startPoint = endPoint;
						endPoint = completeText.indexOf(n.getText());
						individualText = getTextFromWebPage(completeText,startPoint,endPoint);
						individualText = "<"+individualText.substring(0, individualText.length()-1);
						individualText = getStrippedTextFromWebPage(individualText);
						//System.out.println("bot : "+individualText);
						
						if(yap != null){
							individualText = yap.getParsedOutput(individualText);
						}
						parsedYahooContent.add(individualText);
						startPoint = -1;
					}
				} else if ((UtilityConstants.yahooPageBox).equals(tn.getAttribute(UtilityConstants.classAttribute))){
					if (startPoint != -1){
						startPoint = endPoint;
						endPoint = completeText.indexOf(n.getText());
						individualText = getTextFromWebPage(completeText,startPoint,endPoint);
						individualText = "<"+individualText.substring(0, individualText.length()-1);
						individualText = getStrippedTextFromWebPage(individualText);
						//System.out.println("bbox : "+individualText);
						if(yap != null){
							individualText = yap.getParsedOutput(individualText);
						}
						parsedYahooContent.add(individualText);
						startPoint = -1;
					}
				}
			}
		}
		
		return parsedYahooContent;
	}
	
	/** 
	 * Get the text between these points.
	 * @param webPageText
	 * @param start
	 * @param end
	 * @return
	 */
	private String getTextFromWebPage(String webPageText, int start, int end) throws Exception{
		String subString;
		subString = webPageText.substring(start, end);
		return subString;
	}
	
	/**
	 * strip off the li tags 
	 * @param webPageText
	 * @return
	 */
	private String getStrippedTextFromWebPage(String webPageText)throws Exception{
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
