package uk.bl.api;

import models.Target;
import play.Logger;
import uk.bl.exception.ActException;
import uk.bl.exception.WhoisException;
import uk.bl.scope.Scope;

/**
 * This class executes WhoIs value calculation for targets
 * in a thread.
 */
public class WhoIsThread implements Runnable  {

	Target target;
	int number;
	
	public WhoIsThread(Target target, int number) {
        this.target = target;
        this.number = number;
    }
	
    @Override
	public void run() {
    	synchronized (target) {
	        try {
				boolean res = Scope.INSTANCE.checkWhoisThread(number);
//				target.isInScopeUkRegistration = res;
			} catch (ActException e) {
				Logger.error("WhoIsThread error: " + e.getStackTrace());
			}
	        target.notify();
	        Logger.debug("WhoIs thread DONE!");
    	}
    }
}