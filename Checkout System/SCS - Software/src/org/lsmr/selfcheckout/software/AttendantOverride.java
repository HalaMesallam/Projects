package org.lsmr.selfcheckout.software;

import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SupervisionStation;

import java.util.ArrayList;

public class AttendantOverride {
    
    private ElectronicScaleSoftware ess; // i dont think we instantiate it here probably just change out of overload in ess
    private SelfCheckoutStationSoftware scss;
    private SupervisionStation ss;
    private DiscrepancyChecker sdc;
    
    private Status status;
    private boolean loggedIn;
    
    public enum Status {
        stationStarted,
        stationShutdown
    }
    
    public AttendantOverride(SupervisionStation attendantStation) {
    	this.ss = attendantStation;
    	
    	this.loggedIn = false;
    }
    
    public AttendantOverride(SupervisionStation attendantStation,DiscrepancyChecker DChecker) {
    	this.ss = attendantStation;
    	this.sdc = DChecker;
    	
    	this.loggedIn = false;

    }
    
    public AttendantOverride(SupervisionStation attendantStation,DiscrepancyChecker DChecker,SelfCheckoutStationSoftware selfCheckoutSoftware) {
    	this.ss = attendantStation;
    	this.sdc = DChecker;
    	this.scss = selfCheckoutSoftware;
    	
    	this.loggedIn = false;
    }
        
    //Attendant approves a weight discrepancy
    public void bypass() throws OverloadException{
    	if(loggedIn && status == Status.stationStarted) {
    		if(sdc.checkStateDiscrepancy()) {
    			sdc.approveDiscrepancy();
    			setBlockSupervisedStation(false);
    		}
    	}
    }
    
    //Attendant removes product from purchases
    public void forceRemove(ItemInfo item) {
    	if(loggedIn && status == Status.stationStarted) {
    		ArrayList<ItemInfo> a = scss.getItemsScanned();
            a.remove(item);
            scss.setItemsScanned(a);
    	}
    }

    //Attendant blocks a station
    public void setBlockSupervisedStation(boolean blockStatus) {
    	if(loggedIn && status == Status.stationStarted) {
            scss.setBlockStatus(blockStatus);
    	}
    }
    
    //Login
    
    public boolean login(String username, String password) {
        if(username.equals("admin") && password.equals("admin")) {
        	return true;
        }
        else{
        	return false;
        }
    }
        
    public void logout() {
    	// maybe display a "goodbye, shutting down"
        loggedIn = false;
    }
    
    //Station Start/Finish
    
    public void startSupervisionStation(SupervisionStation attendantStation, String username, String password) {
        // login
    	status = Status.stationStarted;
    	if(login(username,password)) {
    		loggedIn = true;
    	}
    	else {
    		status = Status.stationShutdown;
    		// maybe display message saying invalid user/pass
    	}
    }
    
    public boolean isStarted() {
        if (status == Status.stationStarted) {
            return true;
        }else {
            return false;
        }
    }
    
    public void shutdownSupervisionStation(SupervisionStation attendantStation) {
    	status = Status.stationShutdown;
    	logout();
    }
    
    public boolean isShutdown() {
        if (status == Status.stationShutdown) {
            return true;
        }else {
            return false;
        }
    }
    
    public Status getStatus()
    {
    	return status;
    }
    
    public boolean getLoggedIn()
    {
    	if (loggedIn)
    		return true;
    	else
    		return false;
    }
    /**
     * for test ONLY
     */
    public void forceShutdown() {
    	status = Status.stationShutdown;

    }
    
}
