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
import org.meta.search.IR.pageFormatter.AltavistaAggregateParser;
/**
 * Class that is used for getting the list of search results present in the altavista html document
 * passed to this class.
 * @author Bala Sridhar
 */
public class AltaVistaParser {
	/**
	 * holds the html document as a string object.
	 */
	private String webPageText = null;
	/**
	 * Used for debugging purposes.
	 */
	private AltavistaAggregateParser aap = null;
	/**
	 * Constructor.
	 * @param pageText
	 */
	public AltaVistaParser(String pageText){
		webPageText = pageText;
	}
	/**
	 * Constructor. when the value is true the default search color is used
	 * for setting the font color for the link in the corresponding aggregate parser.
	 * @param value
	 */
	public AltaVistaParser(boolean value) {
		// TODO Auto-generated constructor stub
		if(value){
			aap = new AltavistaAggregateParser(UtilityConstants.defaultSearchColor);
		}
	}
	/**
	 * Extract the results using the html parser library by looking out for a tags with specific
	 * class attribute values in the html document for altavista. 
	 * @return
	 * @throws ParserException
	 */
	public ArrayList<String> parsingAltaVista() throws Exception{
		Parser parser = new Parser(webPageText);
		NodeList nl = parser.parse(null);
		HasAttributeFilter haf = new HasAttributeFilter(UtilityConstants.classAttribute);
		TagNameFilter tg = new TagNameFilter(UtilityConstants.aTag);
		AndFilter af = new AndFilter(tg,haf);
		NodeList nla = nl.extractAllNodesThatMatch(af,true);
		NodeList nlaclass = nla.extractAllNodesThatMatch(haf, true);
		return parseAltaVistaPage(nlaclass);
	}
	/**
	 * Now go through each of the result blocks to get each result by doing substring operations
	 * based on the class attribute names.
	 * @param nlaclass
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unchecked" })
	private ArrayList<String> parseAltaVistaPage(NodeList nlaclass)throws Exception{
		String completeText = webPageText;
		//System.out.println(completeText);
		ArrayList<String>  parsedAltaVistaContent= new ArrayList<String>();
		boolean found = false;
		int startPoint = -1;
		int endPoint = -1;
		String individualText = null;
		for(int i=0; i<nlaclass.size();i++){
			Node n = nlaclass.elementAt(i);
			TagNode tn = (TagNode)n;
			String labelString = n.toPlainTextString();
			if((UtilityConstants.altavistaLabelClass1).equals(tn.getAttribute(UtilityConstants.classAttribute))){
					if (labelString.startsWith(UtilityConstants.altavistaTextFound)){
						found =true;
					}else if (labelString.startsWith(UtilityConstants.altavistaTextMatches)){
						if(startPoint == -1){
							found = false;
						} else{
							startPoint = endPoint;
							endPoint = completeText.indexOf(n.getText());
							individualText = getTextFromWebPage(completeText,startPoint,endPoint);
							individualText = "<"+individualText.substring(0, individualText.length()-1);
							if(aap != null){
								individualText = aap.getParsedOutput(individualText);
							}
							parsedAltaVistaContent.add(individualText);
							startPoint = -1;
							found = false;
						}
					} 
			}else if ((UtilityConstants.altavistaLabelClass2).equals(tn.getAttribute(UtilityConstants.classAttribute))){
				if (startPoint != -1){
					startPoint = endPoint;
					endPoint = completeText.indexOf(n.getText());
					individualText = getTextFromWebPage(completeText,startPoint,endPoint);
					individualText = "<"+individualText.substring(0, individualText.length()-1);
					individualText = individualText.substring(0, individualText.indexOf(UtilityConstants.altavistaFinalsubStringTag));
					if(aap != null){
						individualText = aap.getParsedOutput(individualText);
					}
					parsedAltaVistaContent.add(individualText);
					startPoint = -1;
					found = false;
				}
			}
			if (found){
				if ((UtilityConstants.altavistaLabelClass3).equals(tn.getAttribute(UtilityConstants.classAttribute))){
					String aTagText = n.getText();
					if (startPoint == -1){
						startPoint = completeText.indexOf(aTagText);
					} else if (endPoint == -1){
						endPoint = completeText.indexOf(aTagText);
						individualText = getTextFromWebPage(completeText,startPoint,endPoint);
						individualText = "<"+individualText.substring(0, individualText.length()-1);
						if(aap != null){
							individualText = aap.getParsedOutput(individualText);
						}
						parsedAltaVistaContent.add(individualText);
					} else {
						startPoint = endPoint;
						endPoint = completeText.indexOf(aTagText);
						individualText = getTextFromWebPage(completeText,startPoint,endPoint);
						individualText = "<"+individualText.substring(0, individualText.length()-1);
						if(aap != null){
							individualText = aap.getParsedOutput(individualText);
						}
						parsedAltaVistaContent.add(individualText);
					}
				}
			}
		}
		return parsedAltaVistaContent;
	}
	/**
	 * Strip off all the unwanted tags.
	 * @param webPageText
	 * @param start
	 * @param end
	 * @return
	 */
	private String getTextFromWebPage(String webPageText, int start, int end)throws Exception{
		String subString;
		subString = webPageText.substring(start, end);
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
