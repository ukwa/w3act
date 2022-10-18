package uk.bl.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.postgresql.util.PGInterval;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlUpdate;
import com.github.kevinsawicki.timeago.TimeAgo;

import models.Collection;
import models.FieldUrl;
import models.Subject;
import models.Target;
import play.Logger;
import uk.bl.Const;
import uk.bl.exception.ActException;
import uk.bl.exception.UrlInvalidException;

/**
 * Helper class.
 */
public enum Utils {

	INSTANCE;

	public String dayCount(Date timestamp){
        try {
            TimeAgo time = new TimeAgo();
            return time.timeAgo(timestamp.getTime());
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String dayCount(String timestamp) {
    	if (timestamp == null || timestamp.length() < 14) return timestamp;

		try {
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Date d2 = sdf.parse(timestamp);
			TimeAgo time = new TimeAgo();

			return time.timeAgo(d2.getTime());
		}
		catch (Exception e) {
			e.printStackTrace();
			return "ERR!";
		}
    }

    /**
	 * This method generates random Long ID.
	 * @return new ID as Long value
	 */
	public Long createId() {
        UUID id = UUID.randomUUID();
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
	public boolean getNormalizeBooleanString(String value) {
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
    public String getStringFromBoolean(Boolean value) {
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
    public File generateCsvFile(String sFileName, String data) {
 		File file = new File(sFileName);
    	
	 	try {
	 	    FileWriter writer = new FileWriter(file);

	  	    String decodedData = replacer(data); //URLDecoder.decode(data, Const.STR_FORMAT);
//	  	    Logger.debug("generateCsvFile: " + decodedData);
	 	    writer.append(decodedData);
	 	    writer.flush();
	 	    writer.close();
	 	} catch(IOException e) {
	 		e.printStackTrace();
	 	}
	 	return file;
    }    
    
    /**
     * This method secures handling of percent in string.
     * @param data
     * @return
     */
    public String replacer(String data) {
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
    public String getCurrentDate() {
    	return new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
    }
    
    /**
     * This method generates current date in timestamp
     * @return
     */
    public Timestamp getCurrentTimeStamp() {
    	java.util.Date date= new java.util.Date();
   	 	return new Timestamp(date.getTime());
    }
    
    /**
     * This method adds 1 more day to the given date
     * @return
     */
    public String getaddDayToDate(String date) {
    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(date));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		c.add(Calendar.DATE, 1);  // number of days to add
		String modDate = sdf.format(c.getTime());
   	 	return modDate;
    }
    
    /**
     * This method performs a conversion of date in string format 'dd-MM-yyyy' to unix date.
     * @param curDate
     * @return long value of the unix date in string format
     */
    public String getUnixDateStringFromDate(String curDate) {
    	String res = "";
		try {
	    	Logger.debug("getUnixDateStringFromDate curDate: " + curDate);
			Date resDate = new SimpleDateFormat(Const.DATE_FORMAT).parse(curDate);
			/**
			 * 86400 seconds stand for one day. We add it because simple date format conversion
			 * creates timestamp for one day older than the current day.
			 */
			Long longTime = new Long(resDate.getTime()/1000 + 86400);
			Logger.debug("long time: " + longTime);
			res = String.valueOf(longTime);
			Logger.debug("res date: " + res);
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
    public String getUnixDateStringFromDateExt(String curDate) {
    	String res = "";
		try {
	    	Logger.debug("getUnixDateStringFromDate curDate: " + curDate);
			Date resDate = new SimpleDateFormat(Const.DATE_FORMAT).parse(curDate);
			Long longTime = new Long(resDate.getTime()/1000);
			Logger.debug("long time: " + longTime);
			res = String.valueOf(longTime);
			Logger.debug("res date: " + res);
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
    public String getDateFromUnixDate(String unixDate) {
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
    public String showTimestamp(String timestamp) {
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
				Logger.debug("QA timestamp conversion error: " + e);
			}
    	}
    	return res;
    }              
    
    /**
     * Show timestamp in HTML page
     * @param timestamp
     * @return formatted timestamp
     */
    public String showTimestampInHtml(String timestamp) {
    	String res = "";
		Logger.debug("showTimestampInHtml timestamp: " + timestamp);
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
				Logger.debug("QA timestamp conversion error: " + e);
			}
    	}
    	return res;
    }              
    
    /**
     * Convert long timestamp to a date for presentation in a table.
     * @param long timestamp
     * @return formatted timestamp "dd/MM/yyyy"
     */
    public String showTimestampInTable(String timestamp) {
    	String res = "";
    	if (timestamp.length() > 0) {
			try {
				Date resDate = new Date(Long.parseLong(timestamp)*1000L);
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // the format of date
				res = sdf.format(resDate);
//				Logger.debug("res date: " + res);
			} catch (Exception e) {
				Logger.debug("Long timestamp conversion error: " + e);
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
    public boolean hasElementInList(String elem, String list) {
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
    public boolean hasObjectInList(String object, List<String> list) {
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
    public String removeDuplicatesFromList(String list) {
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
    public String[] getMailArray(String list) {
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
    public String convertStringArrayToString(String[] arr) {
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
    public String readTextFile(String fileName) {
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
	public String buildWebRequestByUrl(String resourceUrl, String target) {
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
			Logger.debug("Reading last github hash: " + e);
		} catch (Exception e) {
			Logger.debug("Reading last github hash: " + e);
		}
		return res;
	}

    /**
     * This method presents boolean value as sting in a view.
     * @param value The boolean value
     * @return value as a string
     */
    public String showBooleanAsString(boolean value) {
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
    public boolean isNumeric(String str)  
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
    public boolean isNumericLong(String str)  
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
    public String showMissingFields(String fields) {
    	if (fields != null && fields.length() > 0) {
//    		for (Map.Entry<String, String> entry : Const.guiMap.entrySet()) {
//				if (fields.contains(entry.getKey())) {
//					fields = fields.replace(entry.getKey(), entry.getValue());
//				}
//			}
    	}
    	return fields;
    	
    }
    
    /**
     * Replace domain field name by GUI field name
     * @param fields
     * @return
     */
    public String showMissingField(String field) {
    	String res = field;
    	if (field != null && field.length() > 0) {
//    		res = Const.guiMap.get(field);
    	}
    	return res;
    	
    }
    
    /**
     * This method cuts only the first selected value in a string list separated by comma.
     * @param str The string list
     * @return first value
     */
    public String cutFirstSelection(String str) {
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
    public void removeAssociationFromDb(String tableName, String columnName, Long id) {
        SqlUpdate removeOldAssociation = Ebean.createSqlUpdate("DELETE FROM " + tableName + " WHERE " + columnName + " = " + id);
        removeOldAssociation.execute();     	
    }
    
    public String formatSqlTimestamp(Timestamp timestamp) {
    	if (timestamp != null) {
			String formatted = new SimpleDateFormat("E dd MMM yyyy HH:mm:ss ").format(timestamp);
			return formatted;
    	}
		return "";
	}
    
    public String convertPGIntervalToDate(Object object) {
    	PGInterval pgInterval = (PGInterval)object;
    	int years = pgInterval.getYears();
    	int months = pgInterval.getMonths();
    	int days = pgInterval.getDays();
    	int hours = pgInterval.getHours();
    	int minutes = pgInterval.getMinutes();
    	double seconds = pgInterval.getSeconds();
    	
    	StringBuilder builder = new StringBuilder("");
    	if (years > 0) builder.append(years + " years ");
    	if (months > 0) builder.append(months + " months ");
    	if (days > 0) builder.append(days + " days " );
    	if (hours > 0) builder.append(hours + " hours ");
    	if (minutes > 0) builder.append(minutes + " minutes ");
    	if (seconds > 0) builder.append(Math.round(seconds) + " seconds ");
    	return builder.toString();
    }
    
    public Date convertDate(String dateText) throws ParseException {
    	Date date = null;
    	if (StringUtils.isNotEmpty(dateText)) {
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			date = formatter.parse(dateText);
    	}
    	return date;
    }
    
	public Date getDateFromSeconds(Long seconds) {
		Date date = null;
		if (seconds != null) {
			date = new Date(seconds*1000L);
//			Logger.debug("converted date: " + date);
		}
		return date;
	}
	
	public Date getDateFromLongValue(String dateValue) throws ParseException {
		Date date = null;
    	if (StringUtils.isNotEmpty(dateValue)) {
//			20090617 083042
			DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			date = formatter.parse(dateValue);
		}
		return date;
	}
	
    public Set<String> getVaryingUrls(String url) {
    	Set<String> varyingUrls = new HashSet<String>();
    	// remove all slashes
    	if (url.endsWith("/")) {
    		url = removeSlash(url);
    	}
    	
		String http = convertHttpToHttps(url); // https://
		varyingUrls.add(http);
		
		String httpSlash = addSlash(http); // https://www.werwer.com/
		varyingUrls.add(httpSlash);
		
		String httpNoWww = removeWww(http); // https://werwer.com
		varyingUrls.add(httpNoWww);
		
		String httpNoWwwSlash = addSlash(httpNoWww); // https://werwer.com/
		varyingUrls.add(httpNoWwwSlash);
		
		String httpWww = addWww(http);
		varyingUrls.add(httpWww);
		
		String httpWwwSlash = addSlash(httpWww); // https://werwer.com/
		varyingUrls.add(httpWwwSlash);
		
		
		String https = convertHttpsToHttp(url);
		varyingUrls.add(https);
		
		String httpsSlash = addSlash(https);
		varyingUrls.add(httpsSlash);

		String httpsNoWww = removeWww(https);
		varyingUrls.add(httpsNoWww);

		String httpsNoWwwSlash = addSlash(httpsNoWww); // https://werwer.com/
		varyingUrls.add(httpsNoWwwSlash);
		
		String httpsWww = addWww(https);
		varyingUrls.add(httpsWww);

		String httpsWwwSlash = addSlash(httpsWww); // https://werwer.com/
		varyingUrls.add(httpsWwwSlash);

    	return varyingUrls;
    }
    
    private String removeSlash(String url) {
		return url.substring(0, url.length()-1);
    }
    
    private String convertHttpToHttps(String url) {
    	return url.replace("http://", "https://");
    }

    private String convertHttpsToHttp(String url) {
    	return url.replace("https://", "http://");
    }

    private String addSlash(String url) {
    	return url + "/";
    }
    
    private String removeWww(String url) {
    	return url.replace("www.", "");
    }
    
    private String addWww(String url) {
    	if (!url.contains("www.")) {
			StringBuilder builder = new StringBuilder(url);
			int offset = 7;
			if (url.startsWith("https://")) {
				offset = 8;
			}
			builder.insert(offset, "www.");
			return builder.toString();
    	}
    	return url;
    }
    
    public boolean validUrl(String url) {
        String urlRegex = "^https?:\\/\\/\\S+$";
    	return url.matches(urlRegex);
    }
    
    public boolean validDomain(String domain) {
        String domainRegex = "^((?!-)[a-z0-9-]{1,63}(?<!-)\\.)+[a-z]{2,6}$";
    	return domain.matches(domainRegex);
    }

    public String convertToDateString(Date date) {
    	String formatted = null;
    	if (date != null) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			dateFormat.setTimeZone(TimeZone.getDefault());
			formatted =  dateFormat.format(date);
    	}
    	return formatted;
    }

    public String convertToDateTime(Date date) {
    	String formatted = null;
    	if (date != null) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			dateFormat.setTimeZone(TimeZone.getDefault());
			formatted =  dateFormat.format(date);
    	}
    	return formatted;
    }
    
    public String convertToDateTimeISO(Date date) {
    	String formatted = null;
    	if (date != null) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//			"yyyy-MM-dd'T'HH:mm:ss.SSSZ" 	2001-07-04T12:08:56.235-0700
			dateFormat.setTimeZone(TimeZone.getDefault());
			formatted =  dateFormat.format(date);
    	}
    	return formatted;
    }

	public String getActUrl(String jsonId) {
		return this.getUrl(Const.ACT_URL, jsonId);
	}
	
	public String getWctUrl(String jsonId) {
		return this.getUrl(Const.WCT_URL, jsonId);
	}
	
	private String getUrl(String prefix, String jsonId) {
		String actUrl = null;
		if (StringUtils.isNotEmpty(jsonId)) {
			StringBuilder url = new StringBuilder(prefix).append(jsonId);
			actUrl = url.toString();
		}
		return actUrl;
	}
	
	public String validateUrl(String url) throws UrlInvalidException {
		if (url.startsWith("at ")) {
			url = url.replace("at ", "");
		}
		else if (url.startsWith("www.")) {
			url = "http://" + url;
		}
		else if (url.startsWith("ttp")) {
			url = url.replace("ttp", "");
			url = "http" + url;
		}
		else if (url.startsWith("ttps")) {
			url = url.replace("ttps", "");
			url = "https" + url;
		}
		else if (!url.startsWith("http")) {
			url = "http://" + url;
		}
		url = url.replaceAll(" ", "%20");
		
//		UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_ALL_SCHEMES);
//	    if (!urlValidator.isValid(url)) {
//	    	if (!url.endsWith(Scope.UK_DOMAIN) || !url.endsWith(Scope.SCOT_DOMAIN) || !url.endsWith(Scope.LONDON_DOMAIN)) {
//	    		throw new UrlInvalidException("Something wrong with this url: " + url);
//	    	}
//	    }
		return url;
	}
	
    public FieldUrl isExistingTarget(String url) {
    	
    	// remove protocol
    	
    	Set<String> varyingUrls = getVaryingUrls(url);
    	for (String varyingUrl : varyingUrls) {
    		Logger.debug("varyingUrl: " + varyingUrl);
    		FieldUrl fieldUrl = FieldUrl.findByUrl(varyingUrl);
    		Logger.debug("fieldUrl found : " + fieldUrl);
    		if (fieldUrl != null) {
    			return fieldUrl;
    		}
    	}
    	
    	return null;
	
    }
    
    public List<Subject> processParentsSubjects(List<Subject> subjects, Long parentId) {
		Subject parent = Subject.findById(parentId);
		if (!subjects.contains(parent)) {
			subjects.add(parent);
		}
		if (parent.parent != null) {
			processParentsSubjects(subjects, parent.parent.id);
		}
		return subjects;
    }
    
    public List<Collection> processParentsCollections(List<Collection> collections, Long parentId) {
		Collection parent = Collection.findById(parentId);
		if (!collections.contains(parent)) {
			collections.add(parent);
		}
		if (parent.parent != null) {
			processParentsCollections(collections, parent.parent.id);
		}
		return collections;
    }

	/**
	 * Problematic characters should be handled may vary with the use case.
	 * Our method calls the escapeSpecialCharacters method on some data as it's building up a String.
	 * @param field
	 * @return
	 */
	public String escapeSpecialCharacters(String field) {
		String escapedData = field.replaceAll("\\R", " ");
		if (field.contains(",") || field.contains("\"") || field.contains("'")) {
			field = field.replace("\"", "\"\"");
			escapedData = "\"" + field + "\"";
		}
		return escapedData;
	}
    
    /**
     * This method exports selected targets to CSV file.
     * @param list of Target objects
     * @return
     */
    public String export(List<Target> targetList) {
    	Logger.debug("export() targetList size: " + targetList.size());

    	StringBuilder builder = new StringBuilder();
//        StringWriter sw = new StringWriter();
        for (int i = 0; i < Const.targetExportMap.size(); i++) {
        {
            for (Map.Entry<String, Integer> entry : Const.targetExportMap.entrySet())
//        	Logger.debug("export key: " + entry.getKey());
            	if (entry.getValue() == i) {
            		builder.append(entry.getKey());
            		builder.append(Const.CSV_SEPARATOR);
            	}
            }
        }

        builder.append(Const.CSV_LINE_END);
 	    
 	    if (targetList != null && targetList.size() > 0) {
// 			"nid", "title", "field_url", "author", "field_crawl_frequency", "created"	
 	    	for (Target target : targetList) {
 	    		builder.append(String.valueOf(target.id));
 	    		builder.append(Const.CSV_SEPARATOR);
 	    		builder.append(escapeSpecialCharacters(target.title));
 	    		builder.append(Const.CSV_SEPARATOR);
 	    		builder.append(target.fieldUrl());
 	    		builder.append(Const.CSV_SEPARATOR);
		 	    String authorName = "";
		 	    if (target.authorUser != null) {
		 	    	authorName = target.authorUser.name;
		 	    }
				builder.append(authorName);
				builder.append(Const.CSV_SEPARATOR);
				builder.append(target.crawlFrequency);
				builder.append(Const.CSV_SEPARATOR);
				builder.append(convertToDateTimeISO(target.createdAt));
				builder.append(Const.CSV_LINE_END);
 	    	}
 	    }
//    	Utils.INSTANCE.generateCsvFile(Const.EXPORT_FILE, sw.toString());
 	    return builder.toString();
    }
    
    public boolean isDuplicate(String url, String dbUrl) throws ActException {
        url = Utils.INSTANCE.getPath(url);
        dbUrl = Utils.INSTANCE.getPath(dbUrl);
    	boolean match = (url.equalsIgnoreCase(dbUrl));
    	Logger.debug("matched: " + url + " " + dbUrl);
    	Logger.debug("matched: " + match);
    	return match;
    }
    
    public String getPath(String url) throws ActException {
    	String path = null;
        URI uri;
		try {
			uri = new URI(url).normalize();
	        path = uri.getHost() + uri.getPath();
	        if (uri.getQuery() != null) {
	        	path += "?" + uri.getQuery();
	        }
	        if (path.startsWith("www.")) {
	        	path = path.replace("www.", "");
	        }
		} catch (URISyntaxException e) {
			throw new ActException(e);
		}
		return path;
    }
    
}

