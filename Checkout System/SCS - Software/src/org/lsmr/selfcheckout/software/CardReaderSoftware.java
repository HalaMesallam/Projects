package org.lsmr.selfcheckout.software;

import org.lsmr.selfcheckout.Card;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.CardReader;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.CardReaderObserver;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is used to listen to the card reader, and handles the credit card payment.
 */
public class CardReaderSoftware implements CardReaderObserver {
    private CardReader cardReader;
    private boolean ifDataRead;
    private boolean wasSwiped;
    private boolean pinProvided;
    private CardReaderFacadeListener listener;


    /**
     * Constructor
     * @param scs The selfCheckoutStation facade created in the selfCheckoutStationSoftware class
     */
    public CardReaderSoftware(SelfCheckoutStation scs){
        cardReader = scs.cardReader;
        cardReader.attach(this);
        ifDataRead = false;
        wasSwiped = false;
        cardReader.disable(); //card reader should be disabled in the first place, until the payment process starts.
    }

    /**
     * This method is called when the card reader is enabled.
     * @param device The card reader that the class is listening to.
     */
    @Override
    public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
        //ignore
    }

    /**
     * This method is called when the card reader is disabled.
     * @param device The card reader that the class is listening to.
     */
    @Override
    public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
        //ignore
    }

    /**
     * This method is called when a card is inserted to the card reader.
     * Check 3 seconds later to see if the card inserted is identified as a credit/debit card. If not, error message should be display.
     * @param reader The card reader that the class is listening to.
     */
    @Override
    public void cardInserted(CardReader reader) {
        this.setWasSwiped(false);
        this.pinProvided = true;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(ifDataRead==false){
                    //display failed message on the screen
                }
            }
        }, 3*1000);
    }

    /**
     * This method is called when a card is removed from the card reader.
     * @param reader The card reader that the class is listening to.
     */
    @Override
    public void cardRemoved(CardReader reader) {
        //ignore
    }

    /**
     * This method is called when a card is tapped on the card reader.
     * Check 3 seconds later to see if the card tapped is identified as a credit/debit card. If not, error message should be display.
     * @param reader The card reader that the class is listening to.
     */
    @Override
    public void cardTapped(CardReader reader) {
        this.setWasSwiped(false);
        this.pinProvided = false;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(ifDataRead==false){
                    //display failed message on the screen
                }
            }
        }, 3*1000);
    }

    /**
     * This method is called when a card is swiped on the card reader.
     * Check 3 seconds later to see if the card swiped is identified as a credit/debit card. If not, error message should be display.
     * @param reader The card reader that the class is listening to.
     */
    @Override
    public void cardSwiped(CardReader reader) {
        this.setWasSwiped(true);
        this.pinProvided = false;
    	Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(ifDataRead==false){
                    //display failed message on the screen
                }
            }
        }, 3*1000);
    }

    /**
     * This method is called when the data has been read from the card(possible corrupted data).
     * @param reader
     *            The reader where the event occurred.
     * @param data
     *            The data read from the card(possibly corrupted).
     */
    @Override
    public void cardDataRead(CardReader reader, Card.CardData data) {
        String cardType = data.getType();
        if(cardType.equals("Credit")) { //Check if the card can be identified as a credit/debit one.
            ifDataRead = true;
            cardReader.disable();
            listener.notifyCreditCardRead(data, pinProvided);
        } else if(cardType.equals("Debit")){
            ifDataRead = true;
            cardReader.disable();
            listener.notifyDebitCardRead(data, pinProvided);
        } else if(cardType.equals("Gift")){
        	ifDataRead = true;
            cardReader.disable();
            listener.notifyGiftCardRead(data, pinProvided);
        }
    }

    /**
     * Attach a listener to an instance of this class.
     * @param listener The listener that got notified when the card data is read.
     */
    public void attach(CardReaderFacadeListener listener){
        this.listener = listener;
    }

    /**
     * Getter method of the boolean variable wasSwiped.
     * @return THe boolean value that indicates whether the card is swiped or not.
     */
	public boolean cardWasSwiped() {
		return wasSwiped;
	}

    /**
     * Set the wasSwipe value.
     * @param wasSwiped The boolean value assign to wasSwiped.
     */
	private void setWasSwiped(boolean wasSwiped) {
		this.wasSwiped = wasSwiped;
	}
}
