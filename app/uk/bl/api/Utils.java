package uk.bl.api;

import java.util.UUID;

import play.Logger;

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
    	
}

