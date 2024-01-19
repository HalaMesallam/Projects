package org.lsmr.selfcheckout.software;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.BarcodeScannerObserver;
import org.lsmr.selfcheckout.devices.observers.ElectronicScaleObserver;
import org.lsmr.selfcheckout.products.BarcodedProduct;

import GUI.ProductDatabasesSample;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import static java.util.concurrent.TimeUnit.SECONDS;

public class DiscrepancyChecker {

    private double discrepancyThreshold;
    private SelfCheckoutStation scs;
    private ProductDatabasesSample db;
    private double expectedWeight;
    private double lastTimeWeight;
    private Timer timer;
    private Boolean ifFinishedCheckout;
    private State state; 
    private final Set<DiscrepancyCheckerListener> listeners = new HashSet<>();

    private ESO eso;
    private BSO bso;

    public enum State{
        NORMAL,
        DISCREPANCY,
        WAITINGFORBAGGING,
    }
    
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	int countdownStarter = 5;
	boolean timeout ;
	
	
    public class ESO implements ElectronicScaleObserver{

        @Override
        public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) { }

        @Override
        public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {

        }

        @Override
        public void weightChanged(ElectronicScale scale, double weightInGrams) {
        	 System.out.println(weightInGrams);
     
           if(lastTimeWeight > weightInGrams){ //weight decreased from last time
               if(ifFinishedCheckout==false && Math.abs(expectedWeight-weightInGrams)>discrepancyThreshold){ //an item is removed from the scale without permission
                   disableScanners();
                   state=State.DISCREPANCY;
                   notifyDiscrepancy();
               }else{ //an item is removed from the scale with permission
            	   //SINCE CHECKOUT HAS FINISHED, CUSTOMER IS REMOVEING THE PURCHASED ITEM:
                   lastTimeWeight = weightInGrams;
               }
           }else if(lastTimeWeight <weightInGrams){ //weight increased from to last time
        	   if(state == State.WAITINGFORBAGGING) {
        		   if(Math.abs(expectedWeight-weightInGrams)<=discrepancyThreshold) { //a scanned item is added as expected
                       enableScanners();
                       timer.cancel();
                       lastTimeWeight = weightInGrams;
                       state = State.NORMAL;
                   } else{ //An item that is not scanned is added
                	   System.out.println("ITEM ADDED IS DIFFERENT FROM THE SCANNED ITEM!!");
                       disableScanners();
                       state=State.DISCREPANCY;
                       notifyDiscrepancy();
                   }
        	   }else {
        		   //attendent attention requred
        		   disableScanners();
                   state=State.DISCREPANCY;
                   notifyDiscrepancy();
        		   System.out.println("ITEM ADDED WITHOUT SCANNING!!");
        	   }
               
           }
        }

        @Override
        public void overload(ElectronicScale scale) {
            //Display: bagging area is overload
        }

        @Override
        public void outOfOverload(ElectronicScale scale) {
            //Display: bagging area is out of overload
        }
    }

    public class BSO implements BarcodeScannerObserver{

        @Override
        public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {

        }

        @Override
        public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {

        }

        @Override
        public void barcodeScanned(BarcodeScanner barcodeScanner, Barcode barcode) {
        	if(state == State.NORMAL) {
        		ifFinishedCheckout = false;
                disableScanners(); //disable the scanner immediately when an item is scanned
                BarcodedProduct product = db.getItem(barcode);
                if (product == null) {
                    return;
                } else{
                	state = State.WAITINGFORBAGGING;
                    expectedWeight += product.getExpectedWeight();
                    setUpTimer();
                }
        	}
        }
    }

    public DiscrepancyChecker(SelfCheckoutStation scs,Double discrepancyThreshold){
    	this.db = new ProductDatabasesSample();
        this.scs= scs;
        this.discrepancyThreshold = discrepancyThreshold;
        expectedWeight = 0;
        lastTimeWeight = 0;
        ifFinishedCheckout = false;
        
        state = State.NORMAL;

        //db = new TestDatabase();
        eso = new ESO();
        bso = new BSO();

        scs.handheldScanner.attach(bso);
        scs.mainScanner.attach(bso);
        scs.baggingArea.attach(eso);
    }
    
    public boolean checkStateDiscrepancy() {
    	if(state == State.DISCREPANCY) {
    		return true;
    	}else {
    		return false;
    	}
    }

    
    	
   public void setUpTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
            	state = state.DISCREPANCY;
                notifyDiscrepancy();
                System.out.println("Please put the scanned item into the bagging area.");
                //display message: Please put the scanned item into the bagging area.
            }
        }, 5*1000);
    }
    

    private void disableScanners(){
        scs.mainScanner.disable();
        scs.handheldScanner.disable();
    }

    private void enableScanners(){
        scs.mainScanner.enable();
        scs.handheldScanner.enable();
    }

    public void approveDiscrepancy(){ //used for attendant approve a discrepancy
        try {
            expectedWeight = scs.baggingArea.getCurrentWeight();
            lastTimeWeight = expectedWeight;
            enableScanners();
            state = State.NORMAL;
        } catch (OverloadException e) {
            e.printStackTrace();
            //Display: Can't approve discrepancy in overload.
        }
    }
    
    /**
     * for testing purposes only 
     * @param weight
     */
    public double getlastTimeWeight() {
    	return this.lastTimeWeight;
    }
    public double getExpectedWeight() {
    	return this.expectedWeight;
    }
    /**
     * for testing purposes ONLY
     */
    public void forceDiscrepancyState() {
    	this.state = State.DISCREPANCY;
    }

    public void removeScannedItem(ItemInfo info){ //used when removed a scanned item
        expectedWeight -=info.weight;
    }

    public void finishedAndRemove(){ //used when the customer finished the payment and remove the items from the bagging area
        expectedWeight = 0;
        lastTimeWeight = 0;
        ifFinishedCheckout = true;
    }

    public void attach(DiscrepancyCheckerListener dcl){
        listeners.add(dcl);
    }

    private void notifyDiscrepancy(){ //notify the attendant about the discrepancy
        for(DiscrepancyCheckerListener listener: listeners){
            listener.notifyDiscrepancy();
        }
    }
    
    public State getState() {
    	return this.state;
    }
    
    public void setLastTimeWeight(double value) {
    	this.lastTimeWeight = value;
    }
    
}
