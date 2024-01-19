package org.lsmr.selfcheckout.software;

/**
 * This is the interface of the listener that listens the activity of the receipt printer facade.
 */
public interface ReceiptPrinterFacadeListener {

    /**
     * Notify the listener when low ink is detected.
     */
    void notifyLowInk();

    /**
     * Notify the listener when low paper is detected.
     */
    void notifyLowPaper();

    /**
     * Notify the listener when paper is successfully added.
     */
    void notifyPaperAdded();

    /**
     * Notify the listener when ink is successfully added
     */
    void notifyInkAdded();

    /**
     * Notify the listener when the action of adding more ink will cause overload.
     */
    void notifyInkOverload();

    /**
     * Notify the listener when the action of adding more paper will cause overload.
     */
    void notifyPaperOverload();
}
