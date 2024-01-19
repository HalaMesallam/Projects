package org.lsmr.selfcheckout.software.test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Card;
import org.lsmr.selfcheckout.ChipFailureException;
import org.lsmr.selfcheckout.InvalidPINException;
import org.lsmr.selfcheckout.SimulationException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.software.BankDatabase;
import org.lsmr.selfcheckout.software.PayWithCardSoftware;
import org.lsmr.selfcheckout.software.PaymentController;

public class PayWithGiftTest {
	int[] bills = new int[] {5,10,20,50,100};
	BigDecimal[] coins = new BigDecimal[] {new BigDecimal(0.05), new BigDecimal(0.10), new BigDecimal(0.25), new BigDecimal(1.00), new BigDecimal(2.00)};

	// Initiate a BigDecimal array to stub the total amount to pay going into the tests
	BigDecimal[] amountPaid = new BigDecimal[1];

	// Instantiate classes to use and test
	SelfCheckoutStation scs; 
	PayWithCardSoftware pwcs;
	PaymentController pc;
	BankDatabase db = new BankDatabase();
	
	Card giftCard1 = new Card("Gift", "1000000000000000", "Manbir Sandhu", null, null, false,false);
	Card debitCard = new Card("Debit", "1000000000000000", "Sipeng He", "100", "1000", true, true);

	@Before
	public void setup() {
		scs = new SelfCheckoutStation(Currency.getInstance("CAD"), bills, coins, 1, 1);
		amountPaid[0] = new BigDecimal(0);
		pwcs = new PayWithCardSoftware(scs, amountPaid);
	}
	
	@Test
	public void payWithGiftRegularSwipe() throws IOException {
		// Set the total amount of money to be paid
		BigDecimal newTotal = new BigDecimal(5);
		// Initiate the card paying software. Simulates user picking to pay with Debit
		pwcs.initiatePayment(newTotal, "Gift");

		// Reduces the chance for failure only for testing
		try {
			// Simulates swiping the card through the reader
			scs.cardReader.swipe(giftCard1);
		} catch (Exception MagneticStripeFailureException) {
			scs.cardReader.swipe(giftCard1);
		}

		// Boolean containing the difference between the amount paid and the amount to be paid.
		
		Assert.assertEquals(newTotal, pwcs.getAmountPaid());
	}
	
	@Test
	public void payWithGiftInsufficientFunds() throws IOException {
		// Set the total amount of money to be paid
		BigDecimal newTotal = new BigDecimal(10000);
		// Initiate the card paying software. Simulates user picking to pay with Debit
		pwcs.initiatePayment(newTotal, "Gift");

		// Reduces the chance for failure only for testing
		try {
			// Simulates swiping the card through the reader
			scs.cardReader.swipe(giftCard1);
		} catch (Exception MagneticStripeFailureException) {
			scs.cardReader.swipe(giftCard1);
		}
		// Boolean containing the difference between the amount paid and the amount to be paid.
		
		Assert.assertNotEquals(newTotal, pwcs.getAmountPaid());
	}
	
	@Test(expected = SimulationException.class)
	public void payWithGiftWrongType() throws IOException {

		BigDecimal newTotal = new BigDecimal(5);

		pwcs.initiatePayment(newTotal, "Gift");

		// Reduces the chance for failure only for testing
		try {
			// Simulates the user tapping the card on the card reader.
			scs.cardReader.tap(debitCard);
		} catch (Exception TapFailureException) {
			scs.cardReader.tap(debitCard);
		}
	}

}
