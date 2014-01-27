package uk.bl.api;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
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
//    public static void generateCsvFile(String sFileName, List<List<String>> data)
    public static void generateCsvFile(String sFileName, String data)
    {
	 	try
	 	{
	 	    FileWriter writer = new FileWriter(sFileName);

//	 	    Iterator<List<String>> itr = data.iterator();
//	 		while (itr.hasNext()) {
//	 			List<String> elements = itr.next();
//	 			Iterator<String> itrElem = elements.iterator();
//	 			while (itrElem.hasNext()) {
//	 				writer.append(itrElem.next());
//	 			}
//	 		}
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
    
    public static String getCurrentDate() {
    	return new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
    }
}

