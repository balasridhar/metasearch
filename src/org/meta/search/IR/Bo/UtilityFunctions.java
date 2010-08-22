package org.meta.search.IR.Bo;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This class contains some utility functions that are used across the project.
 * @author Bala Sridhar
 */
public class UtilityFunctions {
	/**
	 * Used for debugging purposes.
	 * @param list
	 */
	public static void listContents(ArrayList<String> list) {
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
	
	/**
	 * remove unwanted yahoo tags from the URL of each result.
	 * @param text
	 * @return
	 */
	public static String removeUnwantedTagsYahoo(String text) {
		String subString;
		subString = text.replaceAll("%3a", ":");
		subString = subString.replaceAll("%3f", "?");
		subString = subString.replaceAll("\"", "");
		return subString;
	}
	
	/**
	 * remove unwanted altavista tags from the URL of each result.
	 * @param text
	 * @return
	 */
	public static String removeUnwantedTagsAltavista(String text) {
		String subString;
		subString = text.replaceAll("%3a", ":");
		subString = subString.replaceAll("%3f", "?");
		subString = subString.replaceAll("\'", "");
		subString = subString.replaceAll("%3A", ":");
		return subString;
	}
	
	/**
	 * remove unwanted microsoft tags from the URL of each result.
	 * @param text
	 * @return
	 */
	public static String removeUnwantedTagsMicrosoft(String text) {
		String subString;
		subString = text.replaceAll("<strong>", "");
		subString = subString.replaceAll("</strong>", "");
		return subString;
	}

	/**
	 * remove unwanted lycos tags from the URL of each result.
	 * @param text
	 * @return
	 */
	public static String removeUnwantedTagsLycos(String text) {
		String subString;
		subString = text.replaceAll("href=\"", "");
		return subString;
	}

	/**
	 * remove unwanted ask tags from the URL of each result.
	 * @param text
	 * @return
	 */
	public static String removeUnwantedTagsAsk(String text) {
		String subString;
		subString = text.replaceAll("href=\"", "");
		return subString;
	}

	/**
	 * remove unwanted common tags from the set of URLs.
	 * @param list
	 * @return
	 */
	public static ArrayList<String> removingCommonCharsProcess(ArrayList<String> list) {
		ArrayList<String> modifiedList = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			modifiedList.add(removeCommonChars((String) list.get(i)).trim());
		}
		return modifiedList;
	}

	/**
	 * remove unwanted common tags from the URL of each result.
	 * @param text
	 * @return
	 */
	public static String removeCommonChars(String text) {
		String subString;
		subString = text.replaceAll("http://", "");
		subString = subString.replaceAll("/", "");
		return subString;
	}
	
	/**
	 * Copy from one arraylist to another arraylist. 
	 * @param list
	 * @return
	 */
	public static ArrayList copyTo(ArrayList list) {
		ArrayList listTo = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			listTo.add(list.get(i));
		}
		return listTo;
	}

	/**
	 * get the largest length of the three arraylists.
	 * @param list1
	 * @param list2
	 * @param list3
	 * @return
	 */
	public static int getHighestListSize(ArrayList<String>list1,ArrayList<String>list2,ArrayList<String>list3){
		int size = 0;
		if ((list1.size() >= list2.size()) && (list1.size() >= list3.size())){
			size = list1.size();
		} else if((list2.size() >= list1.size()) && (list2.size() >= list3.size())){
			size = list2.size();
		} else if((list3.size() >= list1.size()) && (list3.size() >= list2.size())){
			size = list3.size();
		}
		return size;
	}
	
	/**
	 * Get the corresponding value of the property present in the properties file.
	 * @param text
	 * @return
	 */
	public static String getProperty(String text){
		Properties props = null;
		props = new Properties();
		try {
			InputStream istrm = null;
			istrm = UtilityFunctions.class.getClassLoader().getResourceAsStream("App1Messages.properties");
			props.load(istrm);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String value = props.getProperty(text);
		return value;
	}
}
