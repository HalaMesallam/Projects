package org.lsmr.selfcheckout.software;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;

import java.math.BigDecimal;

/**
 * This class deals with customer's choice on the touchscreen related to payment.
 * Each public method in this class corresponds to a button on the touch screen.
 *
 * Buttons before customer wish to check out:
 * -Start Checkout
 *
 * After the "Start Checkout" is pressed:
 * -Pay With Cash
 * -Pay With Credit Card
 * -Pay With Debit Card
 * -Suspend Checkout(suspend the payment and allows the customer to add items)
 * -Finish Checkout(check if the amount paid is larger than the expected total due. If yes, deliver the change)
 *
 * After "Pay with Cash" is pressed:
 * -End pay with cash(return to the "Start Checkout Menu")
 *
 * After "Pay with Debit card" or "Pay With Credit Card" is pressed:
 * -Cancel Card Payment(return to the "Start Checkout Menu")
 */
public class PaymentController {

    private SelfCheckoutStation scs;

    private PayWithCardSoftware pwcs;
    private BanknoteSlotSoftware bss;
    private CoinSlotSoftware css;
    private DeliverChangeSoftware dcs;
    private BigDecimal totalDue;
    private BigDecimal[] amountPaid;
    private Status status;
    public enum Status{
        PAYING,
        SCANNING,
        FINISHED
    }

    /**
     * Constructor class to initialize the software.
     * @param scs The Self-Checkout station created in the SelfCheckoutStation class.
     * @param amountPaid The array that stores the amount paid at index 0.
     */
    public PaymentController(SelfCheckoutStation scs, BigDecimal[] amountPaid){

        this.scs = scs;
        //initialize sub-software pieces
        pwcs = new PayWithCardSoftware(scs, amountPaid);
        bss = new BanknoteSlotSoftware(scs, amountPaid);
        css = new CoinSlotSoftware(scs, amountPaid);
        dcs = new DeliverChangeSoftware(scs);

        this.amountPaid = amountPaid;
        status = Status.SCANNING;  //initially when the controller is created,

        disableSlots();
        scs.cardReader.disable();
    }


    /**
     * Customer press the "Start Checkout" button on the screen.
     * Payment type buttons and suspend payment button shows up on the screen subsequently.
     */
    public void enterPayment(BigDecimal totalDue){
        this.totalDue = totalDue; // Total due is updated by calling the total() method in the stationSoftware
        status = Status.PAYING;

        //Disable scanners

        scs.mainScanner.disable();
        scs.handheldScanner.disable();

        //display the method selection menu
        enableSlots();
        scs.cardReader.enable();
    }

    /**
     * Customer press the "Suspend Checkout" button on the screen.
     * Scanners are enabled so the customer can add more items.
     * They can re-enter the checkout state by pressing the "Start Checkout" button and call the enterPayment() method.
     */
    public void suspendPayment(){
        status = Status.SCANNING;

        //disable slots and cardReader
        scs.cardReader.disable();
        disableSlots();

        //enable scanners so that the customer can add items
        scs.mainScanner.enable();
        scs.handheldScanner.enable();
    }

    /**
     * Customer press the "Finish Checkout" button to finish the checkout process.
     */
    public void checkout(){
        if(amountPaid[0].compareTo(totalDue)>=0){ //Check if enough funds are received
            scs.cardReader.disable();
            disableSlots();
            dcs.deliverChange(totalDue, amountPaid[0]);
            status = Status.FINISHED;
        } else{
            //Display message: funds insufficient
        }
    }

    /**
     * Customer press the "Pay With Cash" button.
     */
    public void payWithCash(){
        scs.cardReader.disable();
        enableSlots();
    }

    /**
     * Customer press the "End pay with cash" button.
     * Allow the customer to switch to another payment type.
     */
    public void endPayWithCash(){
        disableSlots();
        //display the method selection menu
    }

    /**
     * Customer press the "Pay with credit card" button
     * @param amount Customer needs to specify how much they would like to pay with this credit card.
     */
    public void payWithCreditCard(BigDecimal amount){
        pwcs.initiatePayment(amount, "Credit");
        scs.cardReader.enable();
    }

    /**
     * Customer press the "Pay with debit card" button
     * @param amount Customer needs to specify how much they would like to pay with this debit card.
     */
    public void payWithDebitCard(BigDecimal amount){
        pwcs.initiatePayment(amount, "Debit");
        scs.cardReader.enable();
    }
    
    /**
     * Customer press the "Pay with gift card" button
     * @param amount Customer needs to specify how much they would like to pay with this gift card.
     */
    public void payWithGiftCard(BigDecimal amount){
        pwcs.initiatePayment(amount, "Gift");
        scs.cardReader.enable();
    }

    /**
     * Customer press the "Cancel" button when already in a credit/debit payment process.
     */
    public void cancelCardPayment(){
        scs.cardReader.disable();
        //display the method selection menu
    }

    /**
     * This method is used for enable the slots of both banknotes and coins when needed.
     */
    private void enableSlots(){
        scs.coinSlot.enable();
        scs.banknoteInput.enable();
    }

    /**
     * This method is used for disable the slots of both banknotes and coins when needed.
     */
    private void disableSlots(){
        scs.coinSlot.disable();
        scs.banknoteInput.disable();
    }

    /**
     * Getter method for the status that the payment controller is in.
     * @return The status that the payment controller is currently in.
     */
    public Status getStatus(){
        return status;
    }
}
