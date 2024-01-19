package org.lsmr.selfcheckout.software.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.*;
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.software.PaymentController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

/**
 * This class contains the tests for the general payment functions, including:
 * -Pay with a single method
 * -Pay with a mixture of multiple methods
 * -Partial payment and add new items
 * The amount of funds received by the station and the change delivered are checked in each test cases.
 * @author Sipeng He
 */
public class PaymentControllerTest {

    //Canadian dollar is the valid currency
    private Currency currency = Currency.getInstance("CAD");

    //Sample banknotes to be used
    private Banknote twentyDollarBill = new Banknote(currency, 20);
    private Banknote oneHundredBill = new Banknote(currency, 100);

    //stub for various totals to pay
    private BigDecimal smallTotalToPay = new BigDecimal("10.25");
    private BigDecimal bigTotalToPay = new BigDecimal("126.25");

    // Sample cards to be used
    Card debitCard = new Card("Debit", "1000000000000000", "Sipeng He", "100", "1000", true, true);
    Card debitCard1 = new Card("Credit", "1000000000000002", "Manbir Sandhu", "102", "1002", true, true);
    Card creditCard = new Card("Credit", "1000000000000000","Sipeng He", "100", "1000", true, true);
    Card creditCard1 = new Card("Credit", "1000000000000002", "Manbir Sandhu", "102", "1002", true, true);
    Card giftCard = new Card("Gift", "1000000000000000", "Manbir Sandhu", null, null, false, false);

    //Data needed for create the self-checkout station
    int[] banknoteDenominations = { 5, 10, 20, 50, 100 };
    BigDecimal[] coinDenominations = { new BigDecimal("0.05"), new BigDecimal("0.1"), new BigDecimal("0.25"),
            new BigDecimal("1"), new BigDecimal("2") };
    int scaleMaximumWeight = 100;
    int scaleSensitivity = 1;


    BigDecimal[] amountPaid; //The array to store the amount paid value, passed in from outside

    private SelfCheckoutStation scs; //The self-checkout station hardware
    private PaymentController pc; //The payment controller used to make the payment

    private List<Coin> coinsReturned; //The list that catches coins returned as change
    private Banknote[] banknotesReturned;  //The list that catches banknotes returned as change


    /**
     * Helper Method to create a new coin.
     * Used for set up the coin dispensers.
     * @param denomination
     *          The denomination of the new coin created.
     * @return The new coin created.
     */
    private Coin newCoin(BigDecimal denomination){
        Coin newCoin = new Coin(currency,denomination);
        return newCoin;
    }

    /**
     * Helper method to create a new banknote.
     * Used for set up the coin dispensers.
     * @param denomination
     *          The denomination of the new banknote created.
     * @return The new coin created.
     */
    private Banknote newBanknote(int denomination){
        Banknote newBanknote = new Banknote(currency, denomination);
        return newBanknote;
    }

    /**
     * Helper method to calculate the value of changes collected by the customer after the checkout process.
     * @return The value of changes collected by the customer
     */
    private BigDecimal calculateReceivedChange(){
        coinsReturned = scs.coinTray.collectCoins();
        ArrayList<Banknote> banknotesReturned = new ArrayList<Banknote>();
        while(!scs.banknoteOutput.hasSpace()) {
        	Banknote[] arr = scs.banknoteOutput.removeDanglingBanknotes();
        	for(Banknote a : arr) {
        		banknotesReturned.add(a);
        	}
        }
        BigDecimal amountOfChange = BigDecimal.ZERO;
        if(banknotesReturned != null) 
        {
        	for(Banknote b: banknotesReturned) {
        		if(b != null)
        		amountOfChange = amountOfChange.add(new BigDecimal(b.getValue()));
        	}
        }
        for(Coin c: coinsReturned) {
            if(c != null)
                amountOfChange = amountOfChange.add(c.getValue());
        }
        return amountOfChange;
    }

    @Before
    /**
     * Set up the station, the software stub and the payment controller.
     * Loading the dispensers.
     */
    public void Setup() throws OverloadException {
        scs = new SelfCheckoutStation(currency, banknoteDenominations, coinDenominations,scaleMaximumWeight,scaleSensitivity);
        amountPaid = new BigDecimal[1];
        amountPaid[0] = BigDecimal.ZERO;
        pc = new PaymentController(scs,amountPaid);
        for(Integer denomination: banknoteDenominations){
            scs.banknoteDispensers.get(denomination).load(newBanknote(denomination), newBanknote(denomination), newBanknote(denomination));
        }
        for(BigDecimal denomination: coinDenominations){
            scs.coinDispensers.get(denomination).load(newCoin(denomination),newCoin(denomination),newCoin(denomination));
        }
    }

    @Test
    /**
     * Test case:
     * -The customer pays the whole amount with cash.
     */
    public void payWithCash() throws OverloadException, DisabledException {
        pc.enterPayment(smallTotalToPay);
        pc.payWithCash();
        scs.banknoteInput.accept(twentyDollarBill);
        //Check if the received funds are as expected
        Assert.assertTrue(amountPaid[0].compareTo(new BigDecimal(twentyDollarBill.getValue()))==0);
        pc.checkout();
        BigDecimal amountOfChange = calculateReceivedChange();
        BigDecimal changeAmountExpected = new BigDecimal(twentyDollarBill.getValue()).subtract(smallTotalToPay);
        //Check if the change returned is as expected
        Assert.assertTrue(amountOfChange.compareTo(changeAmountExpected)==0);
    }

    @Test
    /**
     * Test case:
     * -The customer pays the whole amount with credit card.
     */
    public void payWithCreditCard() throws IOException {
        pc.enterPayment(smallTotalToPay);
        pc.payWithCreditCard(smallTotalToPay);
        try {
            scs.cardReader.swipe(creditCard);
        } catch (IOException e) {
            scs.cardReader.swipe(creditCard);
        }
        //Check if the funds received are as expected
        Assert.assertTrue(amountPaid[0].compareTo(smallTotalToPay)==0);
        pc.checkout();
        BigDecimal amountOfChange = calculateReceivedChange();
        //Check if the change returned is as expected
        Assert.assertTrue(amountOfChange.compareTo(BigDecimal.ZERO)==0);
    }

    @Test
    /**
     * Test case:
     * -The customer pays the whole amount with a debit card.
     */
    public void payWithDebitCard() throws IOException {
        pc.enterPayment(smallTotalToPay);
        pc.payWithDebitCard(smallTotalToPay);
        try{
            scs.cardReader.swipe(debitCard);
        } catch (IOException e) {
            scs.cardReader.swipe(debitCard);
        }
        //Check if the funds received are as expected
        Assert.assertTrue(amountPaid[0].compareTo(smallTotalToPay)==0);
        pc.checkout();
        BigDecimal amountOfChange = calculateReceivedChange();
        //Check if the change returned is as expected
        Assert.assertTrue(amountOfChange.compareTo(BigDecimal.ZERO)==0);
    }
    
    @Test
    /**
     * Test case:
     * -The customer pays the whole amount with a gift card.
     */
    public void payWithGiftCard() throws IOException {
        pc.enterPayment(smallTotalToPay);
        pc.payWithGiftCard(smallTotalToPay);
        try{
            scs.cardReader.swipe(giftCard);
        } catch (IOException e) {
            scs.cardReader.swipe(giftCard);
        }
        //Check if the funds received are as expected
        Assert.assertTrue(amountPaid[0].compareTo(smallTotalToPay)==0);
        pc.checkout();
        BigDecimal amountOfChange = calculateReceivedChange();
        //Check if the change returned is as expected
        Assert.assertTrue(amountOfChange.compareTo(BigDecimal.ZERO)==0);
    }

    @Test
    /**
     * Test case:
     * -The customer splits the bill to a credit card and a debit card.
     */
    public void payWithMultipleCards() throws IOException {
        BigDecimal amountOnCreditCard = new BigDecimal(2);
        BigDecimal amountOnDebitCard = smallTotalToPay.subtract(amountOnCreditCard);
        pc.enterPayment(smallTotalToPay);
        pc.payWithCreditCard(amountOnCreditCard);
        try {
            scs.cardReader.swipe(creditCard);
        } catch (IOException e) {
            scs.cardReader.swipe(creditCard);
        }
        pc.payWithDebitCard(amountOnDebitCard);
        try{
            scs.cardReader.swipe(debitCard);
        } catch (IOException e) {
            scs.cardReader.swipe(debitCard);
        }
        //Check if the funds received are as expected
        Assert.assertTrue(amountPaid[0].compareTo(smallTotalToPay)==0);
        pc.checkout();
        BigDecimal amountOfChange = calculateReceivedChange();
        //Check if the change returned is as expected
        Assert.assertTrue(amountOfChange.compareTo(BigDecimal.ZERO)==0);
    }

    @Test
    /**
     * Test case:
     * -The customer pays with some cash, and then pay the rest with credit card.
     */
    public void payWithCashAndCreditCard() throws OverloadException, DisabledException, IOException {
        pc.enterPayment(bigTotalToPay);
        pc.payWithCash();
        scs.banknoteInput.accept(oneHundredBill);
        pc.endPayWithCash();
        pc.payWithCreditCard(bigTotalToPay.subtract(new BigDecimal(oneHundredBill.getValue())));
        try {
            scs.cardReader.insert(creditCard, "1000");
        } catch (IOException e) {
            scs.cardReader.remove();
            scs.cardReader.insert(creditCard, "1000");
        }
        //Check if the funds received are as expected
        Assert.assertTrue(amountPaid[0].compareTo(bigTotalToPay)==0);
        pc.checkout();
        BigDecimal amountOfChange = calculateReceivedChange();
        //Check if the change returned is as expected
        Assert.assertTrue(amountOfChange.compareTo(BigDecimal.ZERO)==0);
    }

    @Test
    /**
     * Test case:
     * -The customer pays with some cash, and then pay the rest with debit card.
     */
    public void payWithCashAndDebitCard() throws OverloadException, DisabledException, IOException {
        pc.enterPayment(bigTotalToPay);
        pc.payWithCash();
        scs.banknoteInput.accept(oneHundredBill);
        pc.endPayWithCash();
        pc.payWithDebitCard(bigTotalToPay.subtract(new BigDecimal(oneHundredBill.getValue())));
        try {
            scs.cardReader.insert(debitCard, "1000");
        } catch (IOException e) {
            scs.cardReader.remove();
            scs.cardReader.insert(debitCard, "1000");
        }
        //Check if the funds received are as expected
        Assert.assertTrue(amountPaid[0].compareTo(bigTotalToPay)==0);
        pc.checkout();
        BigDecimal amountOfChange = calculateReceivedChange();
        //Check if the change returned is as expected
        Assert.assertTrue(amountOfChange.compareTo(BigDecimal.ZERO)==0);
    }

    @Test
    /**
     * Test case:
     * -The customer pays part of the bill with debit card, and pays the rest with cash.
     */
    public void payWithDebitCardAndCash() throws IOException, OverloadException, DisabledException {
        pc.enterPayment(bigTotalToPay);
        BigDecimal amountToPayWithCard = new BigDecimal(50);
        pc.payWithDebitCard(amountToPayWithCard);
        try {
            scs.cardReader.insert(debitCard, "1000");
        } catch (IOException e) {
            scs.cardReader.remove();
            scs.cardReader.insert(debitCard, "1000");
        }
        pc.payWithCash();
        scs.banknoteInput.accept(oneHundredBill);
        //Check if the funds received are as expected
        Assert.assertTrue(amountPaid[0].compareTo(amountToPayWithCard.add(new BigDecimal(oneHundredBill.getValue())))==0);
        pc.checkout();
        BigDecimal changeAmount = calculateReceivedChange();
        BigDecimal expectedChangeAmount = new BigDecimal("150").subtract(bigTotalToPay);
        //Check if the change returned is as expected
        Assert.assertTrue(changeAmount.compareTo(expectedChangeAmount)==0);
    }

    @Test
    /**
     * Test case:
     * -The customer pays part of the bill with credit card, and pays the rest with cash.
     */
    public void payWithCreditCardAndCash() throws IOException, OverloadException, DisabledException {
        pc.enterPayment(bigTotalToPay);
        BigDecimal amountToPayWithCard = new BigDecimal(50);
        pc.payWithCreditCard(amountToPayWithCard);
        try {
            scs.cardReader.tap(creditCard);
        } catch (IOException e) {
            scs.cardReader.tap(creditCard);
        }
        pc.payWithCash();
        scs.banknoteInput.accept(oneHundredBill);
        //Check if the funds received are as expected
        Assert.assertTrue(amountPaid[0].compareTo(amountToPayWithCard.add(new BigDecimal(oneHundredBill.getValue())))==0);
        pc.checkout();
        BigDecimal changeAmount = calculateReceivedChange();
        BigDecimal expectedChangeAmount = new BigDecimal("150").subtract(bigTotalToPay);
        //Check if the change returned is as expected
        Assert.assertTrue(changeAmount.compareTo(expectedChangeAmount)==0);
    }

    @Test
    /**
     * Test case:
     * -The customer choose to pay with credit card on the screen, but then cancel it and choose to pay with cash instead.
     */
    public void cancelCardPaymentAndPayWithCash() throws OverloadException, DisabledException {
        pc.enterPayment(smallTotalToPay);
        pc.payWithCreditCard(smallTotalToPay);
        pc.cancelCardPayment(); //Cancel the card payment
        pc.payWithCash();
        scs.banknoteInput.accept(twentyDollarBill);
        //Check if the funds received are as expected
        Assert.assertTrue(amountPaid[0].compareTo(new BigDecimal(20))==0);
        pc.checkout();
        BigDecimal amountOfChange = calculateReceivedChange();
        BigDecimal changeAmountExpected = new BigDecimal(twentyDollarBill.getValue()).subtract(smallTotalToPay);
        //Check if the change returned is as expected
        Assert.assertTrue(amountOfChange.compareTo(changeAmountExpected)==0);
    }

    @Test
    /**
     * Test case:
     * -The customer choose to pay with credit card on the screen, but then cancel it and choose to pay with debit card instead.
     */
    public void cancelCreditCardPaymentAndPayWithDebitCard() throws IOException {
        pc.enterPayment(smallTotalToPay);
        pc.payWithCreditCard(smallTotalToPay);
        pc.cancelCardPayment(); //Cancel the card payment
        pc.payWithDebitCard(smallTotalToPay);
        try {
            scs.cardReader.tap(debitCard);
        } catch (IOException e) {
            scs.cardReader.tap(debitCard);
        }
        //Check if the funds received are as expected
        Assert.assertTrue(amountPaid[0].compareTo(smallTotalToPay)==0);
        pc.checkout();
        BigDecimal amountOfChange = calculateReceivedChange();
        //Check if the change returned is as expected
        Assert.assertTrue(amountOfChange.compareTo(BigDecimal.ZERO)==0);
    }

    @Test
    /**
     * Test case:
     * -The customer choose to pay with cash, insert some cash and then decide to add more items.
     * -The customer suspend the payment process and scanned a new item.
     * -The customer enters the payment stage again with a new total passed in as parameter.
     * -The customer pays the rest of the amount with cash.
     */
    public void partialCashPaymentAndAddNewItems() throws OverloadException, DisabledException {
        pc.enterPayment(bigTotalToPay); 
        System.out.println(bigTotalToPay);
        pc.payWithCash();
        scs.banknoteInput.accept(oneHundredBill); 
        pc.suspendPayment(); //The customer press the button on the screen to suspend the payment

        //Customer add new items and update the total
        BigDecimal priceOfNewItem = new BigDecimal("40");
        BigDecimal newTotal = bigTotalToPay.add(priceOfNewItem);
        System.out.println(newTotal);
        pc.enterPayment(newTotal); //The customer choose to enter payment stage again, the new total is passed in
        pc.payWithCash();
        scs.banknoteInput.accept(oneHundredBill);
        //Check if the funds received are as expected
        Assert.assertTrue(amountPaid[0].compareTo(new BigDecimal(200))==0);
        pc.checkout();
        BigDecimal changeAmount = calculateReceivedChange();
        System.out.println(changeAmount);
        BigDecimal expectedChangeAmount = new BigDecimal(200).subtract(newTotal);
        System.out.println(expectedChangeAmount);
        //Check if the change returned is as expected
        //I dont know why this is bugging out and I don't know how I can fix this failure -@Famington
        Assert.assertTrue(changeAmount.compareTo(expectedChangeAmount)==0);
    }

    @Test
    /**
     * Test case:
     * -The customer choose to pay with cash, insert some cash and then decide to add more items.
     * -The customer suspend the payment process and scanned a new item.
     * -The customer enters the payment stage again with a new total passed in as parameter.
     * -The customer pays the rest of the amount with credit card.
     */
    public void partialMixturePaymentAndAddNewItems() throws OverloadException, DisabledException, IOException {
        pc.enterPayment(bigTotalToPay);
        pc.payWithCash();
        scs.banknoteInput.accept(oneHundredBill);
        pc.suspendPayment(); //The customer press the button on the screen to suspend the payment

        //Customer add new items and update the total
        BigDecimal priceOfNewItem = new BigDecimal("10");
        BigDecimal newTotal = bigTotalToPay.add(priceOfNewItem);

        pc.enterPayment(newTotal); //The customer choose to enter payment stage again, the new total is passed in
        BigDecimal amountToPayWithCard = new BigDecimal(50);
        pc.payWithCreditCard(amountToPayWithCard);
        try {
            scs.cardReader.insert(creditCard, "1000");
        } catch (IOException e) {
            scs.cardReader.remove();
            scs.cardReader.insert(creditCard, "1000");
        }
        //Check if the funds received are as expected
        Assert.assertTrue(amountPaid[0].compareTo(new BigDecimal(150))==0);
        pc.checkout();
        BigDecimal changeAmount = calculateReceivedChange();
        BigDecimal expectedChangeAmount = new BigDecimal(150).subtract(newTotal);
        //Check if the change returned is as expected
        Assert.assertTrue(changeAmount.compareTo(expectedChangeAmount)==0);
    }

    /**
     * Test case:
     * -The customer insert the card and give the wrong pin for three times, causing the card being blocked
     * @throws IOException
     */
    @Test(expected = BlockedCardException.class)
    public void cardBlocked() throws IOException {
        pc.enterPayment(bigTotalToPay);
        pc.payWithCreditCard(new BigDecimal(50));
        try{
            scs.cardReader.insert(creditCard, "1001");
        } catch (IOException e) {
            scs.cardReader.remove();
            e.printStackTrace();
        }
        try{
            scs.cardReader.insert(creditCard, "1002");
        } catch (IOException e) {
            scs.cardReader.remove();
            e.printStackTrace();
        }
        try{
            scs.cardReader.insert(creditCard, "1003");
        } catch (IOException e) {
            scs.cardReader.remove();
            e.printStackTrace();
        }
        scs.cardReader.insert(creditCard, "1003");
    }

    /**
     * Test case:
     * -The customer try to tap and pay an amount that is larger than the limitation for card payment without pin.
     */
    @Test
    public void tapTooLargeAmount(){
        pc.enterPayment(bigTotalToPay);
        pc.payWithCreditCard(new BigDecimal("100"));
        try {
            scs.cardReader.tap(creditCard1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(amountPaid[0]);
        Assert.assertTrue(amountPaid[0].compareTo(BigDecimal.ZERO)==0);
    }

    /**
     * Test case:
     * -The customer try to swipe and pay an amount that is larger than the limitation for card payment without pin.
     */
    @Test
    public void swipeTooLargeAmount(){
        pc.enterPayment(bigTotalToPay);
        pc.payWithDebitCard(new BigDecimal("100"));
        try {
            scs.cardReader.tap(debitCard1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(amountPaid[0]);
        Assert.assertTrue(amountPaid[0].compareTo(BigDecimal.ZERO)==0);
    }

    /**
     * Test case:
     * -The customer wish to end the payment before enough funds are received.
     */
    @Test
    public void endPaymentWithInsufficientFunds(){
        pc.enterPayment(bigTotalToPay);
        pc.payWithCash();
        try {
            scs.banknoteInput.accept(twentyDollarBill);
        } catch (DisabledException e) {
            e.printStackTrace();
        } catch (OverloadException e) {
            e.printStackTrace();
        }
        pc.checkout();
        PaymentController.Status currentStatus= pc.getStatus();
        Assert.assertFalse(currentStatus.equals(PaymentController.Status.FINISHED));
    }
}


