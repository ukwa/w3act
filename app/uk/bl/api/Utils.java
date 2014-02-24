package uk.bl.api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import play.Logger;
import uk.bl.Const;

/**
 * Helper class.
 */
public class Utils {

	/**
	 * This method generates random Long ID.
	 * @return new ID as Long value
	 */
	public static Long createId() {
        UUID id = UUID.randomUUID();
        Logger.info("id: " + id.toString());
        Long res = id.getMostSignificantBits();
        if (res < 0) {
        	res = res*(-1);
        }
        return res;
	}
	
	/**
	 * This method normalizes boolean values expressed in different string values.
	 * @param value
	 * @return normalized boolean value (true or false)
	 */
	public static boolean getNormalizeBooleanString(String value) {
		boolean res = false;	
		if (value != null && value.length() > 0) {
			if (value.equals("Yes") 
					|| value.equals("yes") 
					|| value.equals("True") 
					|| value.equals("true") 
					|| value.equals("On") 
					|| value.equals("on") 
					|| value.equals("Y") 
					|| value.equals("y")) {
				res = true;
			}
		}
		return res;
	}
	
    /**
     * This method converts Boolean value to string
     * @param value
     * @return
     */
    public static String getStringFromBoolean(Boolean value) {
    	String res = "";
    	if (value != null && value.toString().length() > 0) {
    		res = value.toString();
    	}
    	return res;
    }
    	
    /**
     * This method creates CSV file for passed data.
     * @param sFileName
     */
    public static void generateCsvFile(String sFileName, String data)
    {
	 	try
	 	{
	 	    FileWriter writer = new FileWriter(sFileName);

	  	    String decodedData = URLDecoder.decode(data, Const.STR_FORMAT);
//	  	    Logger.info("generateCsvFile: " + decodedData);
	 	    writer.append(decodedData);
	 	    writer.flush();
	 	    writer.close();
	 	}
	 	catch(IOException e)
	 	{
	 	     e.printStackTrace();
	 	} 
    }    
    
    /**
     * This method generates current date for e.g. licence form.
     * @return
     */
    public static String getCurrentDate() {
    	return new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
    }
    
    /**
     * Retrieve formatted timestamp
     * @param timestamp
     * @return formatted timestamp
     */
    public static String showTimestamp(String timestamp) {
    	String res = "";
    	if (timestamp.length() > 0) {
			try {
				Date resDate = new SimpleDateFormat("yyyyMMddHHMMss").parse(timestamp);
				if (resDate != null) {
					Calendar mydate = new GregorianCalendar();
					mydate.setTime(resDate);
					res = Integer.toString(mydate.get(Calendar.DAY_OF_MONTH)) + "/" +
							Integer.toString(mydate.get(Calendar.MONTH)) + "/" +
							Integer.toString(mydate.get(Calendar.YEAR));
				}
			} catch (ParseException e) {
				Logger.info("QA timestamp conversion error: " + e);
			}
    	}
    	return res;
    }              
    
    /**
     * This method evaluates if element is in a list separated by list delimiter e.g. ', '.
     * @param elem The given element for searching
     * @param list The list that contains elements
     * @return true if in list
     */
    public static boolean hasElementInList(String elem, String list) {
    	boolean res = false;
    	if (list != null) {
    		if (list.contains(Const.LIST_DELIMITER)) {   	
		    	String[] parts = list.split(Const.LIST_DELIMITER);
		    	for (String part: parts)
		        {
		    		if (part.equals(elem)) {
		    			res = true;
		    			break;
		    		}
		        }
    		} else {
    			if (list.equals(elem)) {
    				res = true;
    			}
    		}
    	}
    	return res;
    }
    
    /**
     * This method evaluates if element is in a list separated by list delimiter e.g. ', '.
     * @param elem The given element for searching
     * @param list The list that contains elements
     * @return true if in list
     */
    public static String[] getMailArray(String list) {
    	String[] mailArray = {"None"};
    	if (list != null) {
    		if (list.contains(Const.LIST_DELIMITER)) {
    			mailArray = list.split(Const.LIST_DELIMITER);
    		} else {
    			mailArray[0] = list;
    		}
    	}
    	return mailArray;
    }
    
    /**
     * This method converts string array to a string.
     * @param arr
     * @return string value
     */
    public static String convertStringArrayToString(String[] arr) {
    	StringBuilder builder = new StringBuilder();
    	for(String s : arr) {
    	    builder.append(s);
    	}
    	return builder.toString();
    }
    
    /**
     * This class reads text from a given text file.
     * @param fileName
     * @return string value
     */
    public static String readTextFile(String fileName) {
    	String res = "";
    	try {
    		fileName = "conf\\templates\\default.txt";
    		System.out.println("template path: " + fileName);
	    	BufferedReader br = new BufferedReader(new FileReader(fileName));
	        try {
	            StringBuilder sb = new StringBuilder();
	            String line = br.readLine();
	
	            while (line != null) {
	                sb.append(line);
	                sb.append(System.getProperty("line.separator"));
	                line = br.readLine();
	            }
	            res = sb.toString();
	        } finally {
	            br.close();
	        }
    	} catch (Exception e) {
    		System.out.println("read text file error: " + e);
    	}
        return res;
    }
}

