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
	
}

