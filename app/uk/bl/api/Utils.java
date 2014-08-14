package uk.bl.api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlUpdate;

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

	  	    String decodedData = replacer(data); //URLDecoder.decode(data, Const.STR_FORMAT);
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
     * This method secures handling of percent in string.
     * @param data
     * @return
     */
    public static String replacer(String data) {
        try {
           StringBuffer tempBuffer = new StringBuffer();
           int incrementor = 0;
           int dataLength = data.length();
           while (incrementor < dataLength) {
              char charecterAt = data.charAt(incrementor);
              if (charecterAt == '%') {
                 tempBuffer.append("<percentage>");
              } else if (charecterAt == '+') {
                 tempBuffer.append("<plus>");
              } else {
                 tempBuffer.append(charecterAt);
              }
              incrementor++;
           }
           data = tempBuffer.toString();
           data = URLDecoder.decode(data, Const.STR_FORMAT);
           data = data.replaceAll("<percentage>", "%");
           data = data.replaceAll("<plus>", "+");
        } catch (Exception e) {
           e.printStackTrace();
        }
        return data;
    }
    
    /**
     * This method generates current date for e.g. licence form.
     * @return
     */
    public static String getCurrentDate() {
    	return new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
    }
    
    /**
     * This method performs a conversion of date in string format 'dd-MM-yyyy' to unix date.
     * @param curDate
     * @return long value of the unix date in string format
     */
    public static String getUnixDateStringFromDate(String curDate) {
    	String res = "";
		try {
	    	Logger.debug("getUnixDateStringFromDate curDate: " + curDate);
			Date resDate = new SimpleDateFormat(Const.DATE_FORMAT).parse(curDate);
			/**
			 * 86400 seconds stand for one day. We add it because simple date format conversion
			 * creates timestamp for one day older than the current day.
			 */
			Long longTime = new Long(resDate.getTime()/1000 + 86400);
			Logger.info("long time: " + longTime);
			res = String.valueOf(longTime);
			Logger.info("res date: " + res);
			Logger.debug("check stored date - convert back to human date: " + getDateFromUnixDate(res));
		} catch (ParseException e) {
			Logger.debug("Conversion of date in string format dd-MM-yyyy to unix date: " + e);
		}
        return res;
    }
    
    /**
     * This method performs a conversion of date in string format 'dd-MM-yyyy' to unix date without 
     * modifying the date.
     * @param curDate
     * @return long value of the unix date in string format
     */
    public static String getUnixDateStringFromDateExt(String curDate) {
    	String res = "";
		try {
	    	Logger.debug("getUnixDateStringFromDate curDate: " + curDate);
			Date resDate = new SimpleDateFormat(Const.DATE_FORMAT).parse(curDate);
			Long longTime = new Long(resDate.getTime()/1000);
			Logger.info("long time: " + longTime);
			res = String.valueOf(longTime);
			Logger.info("res date: " + res);
			Logger.debug("check stored date - convert back to human date: " + getDateFromUnixDate(res));
		} catch (ParseException e) {
			Logger.debug("Conversion of date in string format dd-MM-yyyy to unix date: " + e);
		}
        return res;
    }
    
    /**
     * This method converts unix date to date.
     * @param unixDate
     * @return date as a string
     */
    public static String getDateFromUnixDate(String unixDate) {
    	String res = "";
//    	Logger.debug("getDateFromUnixDate unixDate: " + unixDate);
    	if (unixDate != null && unixDate.length() > 0) {
	    	long unixSeconds = Long.valueOf(unixDate);
	    	Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
	    	SimpleDateFormat sdf = new SimpleDateFormat(Const.DATE_FORMAT); // the format of your date
	    	sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
	    	res = sdf.format(date);
    	}
    	return res;
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
     * Show timestamp in HTML page
     * @param timestamp
     * @return formatted timestamp
     */
    public static String showTimestampInHtml(String timestamp) {
    	String res = "";
		Logger.info("showTimestampInHtml timestamp: " + timestamp);
    	if (timestamp.length() > 0) {
			try {
				Date resDate = new SimpleDateFormat("yyyyMMddHHMMss").parse(timestamp);
				if (resDate != null) {
					Calendar mydate = new GregorianCalendar();
					mydate.setTime(resDate);
					res = Integer.toString(mydate.get(Calendar.DAY_OF_MONTH)) + "-" +
							Integer.toString(mydate.get(Calendar.MONTH)) + "-" +
							Integer.toString(mydate.get(Calendar.YEAR));
				}
			} catch (ParseException e) {
				Logger.info("QA timestamp conversion error: " + e);
			}
    	}
    	return res;
    }              
    
    /**
     * Convert long timestamp to a date for presentation in a table.
     * @param long timestamp
     * @return formatted timestamp "dd/MM/yyyy"
     */
    public static String showTimestampInTable(String timestamp) {
    	String res = "";
    	if (timestamp.length() > 0) {
			try {
				Date resDate = new Date(Long.parseLong(timestamp)*1000L);
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // the format of date
				res = sdf.format(resDate);
//				Logger.info("res date: " + res);
			} catch (Exception e) {
				Logger.info("Long timestamp conversion error: " + e);
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
     * This method evaluates if object is in a list.
     * @param object The given object for searching
     * @param list The list that contains objects of the same type
     * @return true if in list
     */
    public static boolean hasObjectInList(String object, List<String> list) {
    	boolean res = false;
    	if (list != null && object != null) {
	    	Iterator<String> itr = list.iterator();
	    	while (itr.hasNext()) {
	    		String entry = itr.next();
	    		if (entry.equals(object)) {
	    			res = true;
	    			break;
	    		}
    		}
    	}
    	return res;
    }
    
    /**
     * This method removes duplicated entries from the string list with values separated by ', '.
     * @param list The list that contains elements
     * @return result list
     */
    public static String removeDuplicatesFromList(String list) {
    	String res = "";
    	boolean firstIteration = true;
    	if (list != null) {
    		if (list.contains(Const.LIST_DELIMITER)) {   	
		    	String[] parts = list.split(Const.LIST_DELIMITER);
		    	for (String part: parts)
		        {
		    		boolean isAlreadyInList = false;
			    	String[] resListParts = res.split(Const.LIST_DELIMITER);
			    	for (String resListPart: resListParts)
			        {
			    		if (resListPart.equals(part)) {
			    			isAlreadyInList = true;
			    			break;
			    		}
			        }
			    	if (!isAlreadyInList) {
			    		if (firstIteration) {
			    			res = res + part;
			    			firstIteration = false;
			    		} else {
			    			res = res + Const.LIST_DELIMITER + part;
			    		}
			    	}
		        }
    		} else {
   				res = list;
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
    		Logger.debug("template path: " + fileName);
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
    		Logger.error("read text file error: " + e);
    	}
        return res;
    }
    
	/**
	 * This method creates a web request for passed parameter, processes a call and returns
	 * a server response.
	 * @param resourceUrl The web site URL
	 * @param target The target string in site content
	 * @return target row
	 */
	public static String buildWebRequestByUrl(String resourceUrl, String target) {
        String res = "";
		try {
	        URL github = new URL(resourceUrl);
	        URLConnection yc = github.openConnection();
	        BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                yc.getInputStream()));
	        while ((res = in.readLine()) != null) {
	        	if (res.contains(target)) {
	        		break;
	        	}
	        }
	        in.close();
		} catch (MalformedURLException e) {
			Logger.info("Reading last github hash: " + e);
		} catch (Exception e) {
			Logger.info("Reading last github hash: " + e);
		}
		return res;
	}

    /**
     * This method presents boolean value as sting in a view.
     * @param value The boolean value
     * @return value as a string
     */
    public static String showBooleanAsString(boolean value) {
    	String res = Const.NO;
    	if (value) {
    		res = Const.YES;
    	}
    	return res;
    }
    
    /**
     * This method checks whether given string is
     * a numeric value.
     * @param str
     * @return true if numeric
     */
    public static boolean isNumeric(String str)  
    {  
        try {  
            Double.parseDouble(str);  
            String regex = "[0-9]+";
            if (str.matches(regex) == false) {
            	return false;
            }
        } catch (NumberFormatException nfe) {  
            return false;  
        }  
        return true;  
    }    
    
    /**
     * This method checks whether given string is
     * a numeric value of type Long.
     * @param str
     * @return true if numeric
     */
    public static boolean isNumericLong(String str)  
    {  
        try {  
            Long.parseLong(str);  
        } catch (NumberFormatException nfe) {  
            return false;  
        }  
        return true;  
    }    
    
    /**
     * Replace domain field names by GUI field names
     * @param fields
     * @return
     */
    public static String showMissingFields(String fields) {
    	if (fields != null && fields.length() > 0) {
    		for (Map.Entry<String, String> entry : Const.guiMap.entrySet()) {
				if (fields.contains(entry.getKey())) {
					fields = fields.replace(entry.getKey(), entry.getValue());
				}
			}
    	}
    	return fields;
    	
    }
    
    /**
     * Replace domain field name by GUI field name
     * @param fields
     * @return
     */
    public static String showMissingField(String field) {
    	String res = field;
    	if (field != null && field.length() > 0) {
    		res = Const.guiMap.get(field);
    	}
    	return res;
    	
    }
    
    /**
     * This method cuts only the first selected value in a string list separated by comma.
     * @param str The string list
     * @return first value
     */
    public static String cutFirstSelection(String str) {
    	String res = str;
    	if (str != null && str.contains(Const.COMMA)) {
    		int commaPos = str.indexOf(Const.COMMA);
    		res = str.substring(0, commaPos - 1);
    	}
    	return res;
    }
    
    /**
     * This method removes associations from join table in database
     * for given table name and id in the case of ManyToMany association.
     * @param tableName
     * @param id
     */
    public static void removeAssociationFromDb(String tableName, Long id) {
        SqlUpdate removeOldAssociation = Ebean.createSqlUpdate("DELETE FROM " + tableName + " WHERE id_target = " + id);
        removeOldAssociation.execute();     	
    }
}

