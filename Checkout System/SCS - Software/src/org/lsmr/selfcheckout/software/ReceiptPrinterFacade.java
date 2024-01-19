package org.lsmr.selfcheckout.software;

import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.ReceiptPrinterObserver;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * This class handles the following use-cases related to the receipt printer:
 * -Print the receipt when customer finishes the payment
 * -Notify the attendant when the paper in the receipt printer is low
 * -Notify the attendant when the ink in the receipt printer is low
 * -Attendant adds paper to the receipt printer
 * -Attendant adds ink to the receipt printer
 */
public class ReceiptPrinterFacade {

    //The quantity of ink and units of paper to be added to the printer each time the attendant choose to do so
    public final int DEFAULT_INK_QUANTITY_TO_ADD = 1000000;
    public final int DEFAULT_PAPER_UNITS_TO_ADD = 1000;

    //The selfCheckout station
    private SelfCheckoutStation scs;

    //The set to store registered listeners to this class
    private final Set<ReceiptPrinterFacadeListener> listeners = new HashSet<>();

    //Record the state that the printer is in(for the documentation)
    private State state;
    
    private int inkQuantityToAdd = DEFAULT_INK_QUANTITY_TO_ADD;
    private int paperUnitsToAdd = DEFAULT_PAPER_UNITS_TO_ADD;

    //States of the printer
    private enum State{
        NORMAL,
        LOWPAPER,
        LOWINK;
    }

    /**
     * This inner class is an implementation of the ReceiptPrinterObserver interface.
     * The observer is notified when the printer is out of paper/ink, or when paper/ink is added to the printer.
     */
    class ReceiptPrinterListener implements ReceiptPrinterObserver{

        @Override
        public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
            //ignore
        }

        @Override
        public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
            //ignore
        }

        @Override
        public void outOfPaper(ReceiptPrinter printer) {
            lowPaperNotify();
            state = State.LOWPAPER;
        }

        @Override
        public void outOfInk(ReceiptPrinter printer) {
            lowInkNotify();
            state = State.LOWINK;
        }

        @Override
        public void paperAdded(ReceiptPrinter printer) {
            paperAddedNotify();
            state = State.NORMAL;
        }

        @Override
        public void inkAdded(ReceiptPrinter printer) {
            inkAddedNotify();
            state = State.NORMAL;
        }
    }
    private ReceiptPrinterListener RPL; //receipt printer listener instance

    /**
     * Constructor.
     * @param scs The self-Checkout station hardware instance.
     */
    public ReceiptPrinterFacade(SelfCheckoutStation scs){
        this.scs=scs;
        this.RPL = new ReceiptPrinterListener();
        scs.printer.attach(RPL); //attach the receipt printer listener to the printer in the self-checkout station
        state = State.NORMAL;
    }

    /**
     * Used by the attendant class to attach a listener to this class.
     * @param RPCL The listener to be attached to this class.
     */
    public void attach(ReceiptPrinterFacadeListener RPCL){
        this.listeners.add(RPCL);
    }


    /**
     * Resume the printer when it gets out of the LOWINK or LOWPAPER state(GUI BUTTON).
     * Abandon the old receipt that is partially printed to create a new one.
     */
    public void resume(){
        scs.printer.enable();
        this.state = State.NORMAL;
    }

    /**
     * Attendant add ink to the printer(GUI Button).
     * -If adding more ink will cause the printer to overload, cancel it and notify the listener.
     * @throws OverloadException 
     */
    public void addInk() throws OverloadException{
    	scs.printer.addInk(inkQuantityToAdd);
    }

    /**
     * Attendant add paper to the printer(GUI Button).
     * -If adding more paper will cause the printer to overload, cancel it and notify the listener.
     * @throws OverloadException 
     */
    public void addPaper() throws OverloadException{
    	scs.printer.addPaper(paperUnitsToAdd);
    }

    /**
     * Notify the listener that the paper in the printer is low.
     */
    private void lowPaperNotify(){
        for(ReceiptPrinterFacadeListener listener: listeners){
            listener.notifyLowPaper();
        }
    }

    /**
     * Notify the listener that the ink in the printer is low.
     */
    private void lowInkNotify(){
        for(ReceiptPrinterFacadeListener listener: listeners){
            listener.notifyLowInk();
        }
    }

    /**
     * Notify the listener that more paper is successfully added to the printer.
     */
    private void paperAddedNotify(){
        for(ReceiptPrinterFacadeListener listener: listeners){
            listener.notifyPaperAdded();
        }
    }

    /**
     * Notify the listener that more ink is successfully added to the printer.
     */
    private void inkAddedNotify(){
        for(ReceiptPrinterFacadeListener listener: listeners){
            listener.notifyInkAdded();
        }
    }

    /**
     * Notify the listener that adding more ink will cause overload.
     */
    private void inkOverloadNotify(){
        for(ReceiptPrinterFacadeListener listener: listeners){
            listener.notifyInkOverload();
        }
    }

    /**
     * Notify the listener that adding more paper will cause overload.
     */
    private void paperOverloadNotify(){
        for(ReceiptPrinterFacadeListener listener: listeners){
            listener.notifyPaperOverload();
        }
    }
    
    public void setPaperUnitsToAdd(int units) {
    	paperUnitsToAdd = units;
    }
    
    public void setInkQuantityToAdd(int quantity) {
    	inkQuantityToAdd = quantity;
    }
}
