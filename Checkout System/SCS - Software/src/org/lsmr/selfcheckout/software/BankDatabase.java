package org.lsmr.selfcheckout.software;

import org.lsmr.selfcheckout.Card;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This is a stub database class for card data stored in the bank.
 * Each credit card number and debit card number is matched to one entry in the database.
 */
public class BankDatabase {
    /**
     * This class is the stub for an entry in the credit card database.
     */
    private class CreditCardEntry{
        private String cardholder;
        private String cvv;
        private String pin;
        private BigDecimal creditAvailable;

        public CreditCardEntry(String cardholder, String cvv, String pin, BigDecimal creditAvailable){
            this.cardholder = cardholder;
            this.cvv = cvv;
            this.pin = pin;
            this.creditAvailable = creditAvailable;
        }
    }

    /**
     * This class is the stub for an entry in the debit card database.
     */
    private class DebitCardEntry{
        private String cardholder;
        private String cvv;
        private String pin;
        private BigDecimal balance;

        public DebitCardEntry(String cardholder, String cvv, String pin, BigDecimal balance){
            this.cardholder = cardholder;
            this.cvv = cvv;
            this.pin = pin;
            this.balance =balance;
        }
    }
    
    private class GiftCardEntry{
    	private String number;
    	private BigDecimal amount;
    	public GiftCardEntry(String number, BigDecimal amount) {
    		this.number = number;
    		this.amount = amount;
    	}
    }

    private Map<String, DebitCardEntry> debitCardDatabase = new HashMap<>();
    private Map<String, CreditCardEntry> creditCardDatabase = new HashMap<>();
    private Map<String, GiftCardEntry> giftCardDatabase = new HashMap<>();

    /**
     * Constructor for the database. Some dummy entries are added when the database is created.
     */
    public BankDatabase(){
        Map<String, DebitCardEntry> debitCardDatabase = new HashMap<>();
        Map<String, CreditCardEntry> creditCardDatabase = new HashMap<>();
        Map<String, CreditCardEntry> giftCardDatabase = new HashMap<>();
        addCreditCardEntry("1000000000000000","Sipeng He", "100", "1000", new BigDecimal(50));
        addCreditCardEntry("1000000000000001", "Jacky Tran", "101","1001", new BigDecimal(500));
        addCreditCardEntry("1000000000000002", "Manbir Sandhu", "102","1002", new BigDecimal(1000));
        addDebitCardEntry("1000000000000000","Sipeng He", "100", "1000", new BigDecimal(50));
        addDebitCardEntry("1000000000000001", "Jacky Tran", "101","1001", new BigDecimal(500));
        addDebitCardEntry("1000000000000002", "Manbir Sandhu", "102","1002", new BigDecimal(1000));
        addGiftCardEntry("1000000000000000", new BigDecimal(50));
        addGiftCardEntry("1000000000000001", new BigDecimal(500));
        addGiftCardEntry("1000000000000002", new BigDecimal(1000));
    }

    /**
     * Add an entry to the credit card database.
     * @param number Card number.
     * @param cardholder Card holder.
     * @param cvv Card CVV.
     * @param pin Card pin.
     * @param creditAvailable Available credit left in the card.
     */
    public void addCreditCardEntry(String number, String cardholder, String cvv, String pin, BigDecimal creditAvailable){
        CreditCardEntry newCreditCardEntry = new CreditCardEntry(cardholder, cvv, pin, creditAvailable);
        creditCardDatabase.put(number, newCreditCardEntry);
    }

    /**
     * Add an entry to the debit card database.
     * @param number Card number.
     * @param cardholder Card holder.
     * @param cvv Card CVV.
     * @param pin Card pin.
     * @param balance Card balance.
     */
    public void addDebitCardEntry(String number, String cardholder, String cvv, String pin, BigDecimal balance){
        DebitCardEntry newDebitCardEntry = new DebitCardEntry(cardholder, cvv, pin, balance);
        debitCardDatabase.put(number, newDebitCardEntry);
    }
    
    public void addGiftCardEntry(String number, BigDecimal amount) {
    	GiftCardEntry gc = new GiftCardEntry(number,amount);
    	giftCardDatabase.put(number,gc);
    }

    /**
     * Verify if there is an entry with the given card number in the database.
     * @param data Card data obtained from card reader.
     * @return If the entry exists.
     */
    public boolean verifyCard(Card.CardData data, boolean swipeStatus) throws UnsupportedOperationException {
        if(data.getType().equals("Credit")){
            CreditCardEntry entry = creditCardDatabase.get(data.getNumber());
            if(entry==null) {
            	//System.out.println("This card does not exist in the bank");
                return false;
            }
            if(entry.cardholder.compareTo(data.getCardholder())!=0) {
            	//System.out.println("The cardholder does not match the card");
                return false;
            }
            if (!swipeStatus) {
            	if(entry.cvv.compareTo(data.getCVV())!=0) {
            		//System.out.println("The Credit CVV does not match the database");
            		return false;
            }
            	}
        } else if(data.getType().equals("Debit")) {
            DebitCardEntry entry = debitCardDatabase.get(data.getNumber());
            if(entry==null){
            	//System.out.println("This card does not exist in the bank");
                return false;
            }
            if(entry.cardholder.compareTo(data.getCardholder())!=0) {
            	//System.out.println("The cardholder does not match the card");
            	return false;
            }
            if (!swipeStatus) {
            	if(entry.cvv.compareTo(data.getCVV())!=0) {
            		//System.out.println("The CVV does not match the database");
            		return false;
            	}
            }
        }
        return true;
    }
    
    public boolean giftCardPayment(String gcNumber, BigDecimal total) {
    	GiftCardEntry gc = giftCardDatabase.get(gcNumber);
    	if(gc.amount.compareTo(total)<0) {
    		return false;
    	}else {
    		gc.amount = gc.amount.subtract(total);
    		return true;
    	}
    }

    /**
     * Complete a credit card payment.
     * @param cardNumber The card number.
     * @param total Total amount to pay.
     * @return If the payment is successful or not.
     */
    public boolean creditCardPayment(String cardNumber, BigDecimal total){
        CreditCardEntry entry = creditCardDatabase.get(cardNumber);
        if(entry.creditAvailable.compareTo(total)<0){
            return false;
        }
        else{
            entry.creditAvailable = entry.creditAvailable.subtract(total);
            return true;
        }
    }

    /**
     * Complete a debit card payment.
     * @param cardNumber The card number.
     * @param total Total amount to pay.
     * @return If the payment is successful or not.
     */
    public boolean debitCardPayment(String cardNumber, BigDecimal total){
        DebitCardEntry entry = debitCardDatabase.get(cardNumber);
        if(entry.balance.compareTo(total)<0){
            return false;
        }
        else{
            entry.balance = entry.balance.subtract(total);
            return true;
        }
    }
}
