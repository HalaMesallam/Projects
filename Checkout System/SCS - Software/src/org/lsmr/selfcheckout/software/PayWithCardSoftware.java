package org.lsmr.selfcheckout.software;

import org.lsmr.selfcheckout.Card;
import org.lsmr.selfcheckout.Card.CardData;
import org.lsmr.selfcheckout.TapFailureException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.SimulationException;

import java.awt.desktop.SystemEventListener;
import java.math.BigDecimal;

/**
 * This class handles the credit card payment.
 */
public class PayWithCardSoftware {
    private SelfCheckoutStation scs;
    private CardReaderSoftware crs;
    private BankDatabase bankDatabase;
    private BigDecimal[] amountPaid;  //amountPaid[0] is the amount paid value shared by all related classes.

    private String expectedCardType;
    private BigDecimal amountToPayWithThisCard;
    private boolean ifPinProvided;

    private BigDecimal AMOUNT_LIMIT_WITHOUT_PIN = new BigDecimal("100");

    /**
     * The concrete CardReaderFacadeListener class.
     */
    private class CRFL implements CardReaderFacadeListener{
        /**
         * Called when the data from a credit card is read.
         * @param data The data read from the credit card.
         * @param pinProvided Whether pin is provided or not.
         */
        @Override
        public void notifyCreditCardRead(Card.CardData data, boolean pinProvided) {
            ifPinProvided = pinProvided;
            payWithCredit(data);
        }

        /**
         * Called when the data from a debit card is read.
         * @param data The data read from the debit card.
         * @param pinProvided Whether pin is provided or not.
         */
        @Override
        public void notifyDebitCardRead(Card.CardData data, boolean pinProvided) {
            ifPinProvided = pinProvided;
            payWithDebit(data);
        }

		@Override
		public void notifyGiftCardRead(CardData data, boolean pinProvided) {
			payWithGift(data);
		}
    }
    private CRFL crfl;

    /**
     * Constructor to initialize the software.
     * @param scs The selfCheckoutStation created in the selfCheckoutStationSoftware.
     */
    public PayWithCardSoftware(SelfCheckoutStation scs, BigDecimal[] amountPaid){
        this.scs = scs;
        this.crs = new CardReaderSoftware(scs);
        bankDatabase = new BankDatabase();
        this.amountPaid = amountPaid;
        crfl = new CRFL();
        crs.attach(crfl);
    }

    /**
     * The logic of paying with a debit card.
     * @param data The data read from a debit card.
     */
    @SuppressWarnings("serial")
	public void payWithDebit(Card.CardData data) {
        if(ifPinProvided==false&&amountToPayWithThisCard.compareTo(AMOUNT_LIMIT_WITHOUT_PIN)>=0){
            //display message: please insert the card and provide pin
            return;
        }
        if(!expectedCardType.equalsIgnoreCase("Debit")){
            //display message: wrong card type, need to change what happens here
        	//System.out.println("This is not a Debit Card");
    		throw new SimulationException("Wrong card type.") {
    		};
        }
        if (!bankDatabase.verifyCard(data, crs.cardWasSwiped())) {
            //Display message: verification failed
        	//System.out.println("Verification failed");
        } else {
            boolean ifPaymentSuccess = bankDatabase.debitCardPayment(data.getNumber(), getAmountToPayWithThisCard());
            if (ifPaymentSuccess == true) {
                setAmountPaid(getAmountToPayWithThisCard());
                //Display message: payment success
                //System.out.println("Payment was a success");
            } else {
            	//System.out.println("payment failed");
                //Display message: not enough balance available
            }
        }
    }

    /**
     * The logic of paying with a credit card.
     * @param data The data read from a credit card.
     */
    @SuppressWarnings("serial")
	public void payWithCredit(Card.CardData data) {
        if(ifPinProvided==false&&amountToPayWithThisCard.compareTo(AMOUNT_LIMIT_WITHOUT_PIN)>=0){
            //display message: please insert the card and provide pin
            return;
        }
        if(!expectedCardType.equalsIgnoreCase("Credit")){
            //display message: wrong card type
    		throw new SimulationException("Wrong card type.") {
    		};
        }
        if (!bankDatabase.verifyCard(data, crs.cardWasSwiped())) {
            //Display message: verification failed
        } else {
            boolean ifPaymentSuccess = bankDatabase.creditCardPayment(data.getNumber(), getAmountToPayWithThisCard());
            if (ifPaymentSuccess == true) {
                setAmountPaid(getAmountToPayWithThisCard());
                //Display message: payment success
            } else {
                //Display message: not enough credit available
            }
        }
    }
    
    public void payWithGift(Card.CardData data) {
    	if(!expectedCardType.equalsIgnoreCase("Gift")) {
    		throw new SimulationException("Wrong card type.") {};
    	}else {
	    	boolean paymentSuccess = bankDatabase.giftCardPayment(data.getNumber(),getAmountToPayWithThisCard());
	    	if(paymentSuccess == true) {
	    		setAmountPaid(getAmountToPayWithThisCard());
	    	}else {
	    		// not enought funds
	    	}
    	}
    }

    /**
     * The public method called to initiate a card payment by setting the amount to pay with the card and the card type.
     * @param amount The amount to pay with this card.
     * @param cardType  The type of the card expected.
     */
    public void initiatePayment(BigDecimal amount, String cardType){
        this.setAmountToPayWithThisCard(amount);
        this.expectedCardType = cardType;
    }

    /**
     * Getter method of amountToPayWithThisCard.
     * @return amountToPayWithThisCard
     */
	public BigDecimal getAmountToPayWithThisCard() {
		return amountToPayWithThisCard;
	}

    /**
     * Setter method of amountToPayWithThisCard.
     * @param amountToPayWithThisCard The value assigned to amountToPayWithThisCard.
     */
	public void setAmountToPayWithThisCard(BigDecimal amountToPayWithThisCard) {
		this.amountToPayWithThisCard = amountToPayWithThisCard;
	}

    /**
     * Getter method of the amountPaid.
     * @return The amountPaid.
     */
	public BigDecimal getAmountPaid() {
		return amountPaid[0];
	}

    /**
     * Setter method of the amountPaid.
     * @param amount The value assigned to amountPaid.
     */
	public void setAmountPaid(BigDecimal amount) {
		this.amountPaid[0] = amountPaid[0].add(amount);
	}
}
