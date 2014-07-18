package uk.bl.api;

import models.Target;
import play.Logger;
import uk.bl.exception.WhoisException;
import uk.bl.scope.Scope;

/**
 * This class executes WhoIs value calculation for targets
 * in a thread.
 */
public class WhoIsThread implements Runnable  {

	Target target;
	
	public WhoIsThread(Target target) {
        this.target = target;
    }
	
    public void run() {
    	synchronized (target) {
	        try {
				boolean res = Scope.checkWhoisThread();
				target.isInScopeUkRegistrationValue = res;
			} catch (WhoisException e) {
				Logger.error("WhoIsThread error: " + e.getStackTrace());
			}
	        target.notify();
	        Logger.debug("WhoIs thread DONE!");
    	}
    }
}