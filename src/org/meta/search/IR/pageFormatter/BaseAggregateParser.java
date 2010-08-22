package org.meta.search.IR.pageFormatter;
/**
 * This is the super class for all the aggregate parsers defined for the different
 * search engines. the main functionality is to render all the results gathered
 * into a unified format.
 * @author Anusha
 */
public class BaseAggregateParser {

	/**
	 * Constructor.
	 */
	public BaseAggregateParser(){
	}
	
	/**
	 * Overriden by the other classes. returns the converted string to be
	 * displayed.
	 * @param text
	 * @return
	 */
	public String getParsedOutput(String text) {
		// TODO Auto-generated method stub
		//System.out.println("Hello trying base class");
		return null;
	}

}
