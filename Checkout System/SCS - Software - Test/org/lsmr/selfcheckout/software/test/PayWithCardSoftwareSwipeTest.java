package org.lsmr.selfcheckout.software.test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Card;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.software.BankDatabase;
import org.lsmr.selfcheckout.software.PayWithCardSoftware;
import org.lsmr.selfcheckout.software.PaymentController;

/**
 * In using the testing software, there is a random chance for the cardreader to fail reading.
 * 
 * A 'while' loop would likely have proved more effective in getting the test to keep attempting to pay,however since it is pre-determined to fail, 
 * a 'while' loop will result in an infinite loop and eventually cause memory to run out. 
 * @author jacky.tran1
 */
public class PayWithCardSoftwareSwipeTest {
	/*
	 * TEST CASES
	 * User elects to pay with Card
	 * Swipes debit or credit to pay
	 * 
	 * 1. Valid debit card, sufficient funds -> Successful payment
	 * 2. Valid credit card, sufficient funds -> Successful payment
	 * 3. Valid debit card, insufficient funds -> Failed payment
	 * 4. Valid credit card, insufficient funds -> Failed payment
	 * 5. Invalid debit card (Invalid CVV) -> Successful payment and verification
	 * 			a. Only the debit is tested for this because the methods for both debit and credit are mirrored.
	 */

	// Demonimations of accepted banknotes and coins, used to initiate the SelfCheckoutStation
	int[] bills = new int[] {5,10,20,50,100};
	BigDecimal[] coins = new BigDecimal[] {new BigDecimal(0.05), new BigDecimal(0.10), new BigDecimal(0.25), new BigDecimal(1.00), new BigDecimal(2.00)};

	// Initiate a BigDecimal array to stub the total amount to pay going into the tests
	BigDecimal[] amountPaid = new BigDecimal[1];

	// Instantiate classes to use and test
	SelfCheckoutStation scs; 
	PayWithCardSoftware pwcs;
	PaymentController pc;
	BankDatabase db = new BankDatabase();

	// Sample cards to be used to test the payment method
	Card newDebitCard = new Card("Debit", "1000000000000000", "Sipeng He", "100", "1000", true, true);
	Card newCreditCard = new Card("Credit", "1000000000000000","Sipeng He", "100", "1000", true, true);
	Card newDCBadCVV = new Card("Debit", "1000000000000000", "Sipeng He", "200", "1000", true, true);

	/**
	 * Pre-test method which initiates certain variables prior to each test.
	 */
	@Before
	public void setup() {
		scs = new SelfCheckoutStation(Currency.getInstance("CAD"), bills, coins, 1, 1);
		amountPaid[0] = new BigDecimal(0);
		pwcs = new PayWithCardSoftware(scs, amountPaid);
	}

	/**
	 * Post-test method which resets certain variables to 0 and clears the card reader device.
	 */
	@After
	public void cleanup() {
		pwcs.setAmountPaid(new BigDecimal(0));
		scs.cardReader.remove();
	}

	/**
	 * 1. Tests to ensure that when swiping a debit card, if card is valid and funds are sufficient, payment occurs.
	 * @throws IOException
	 */
	@Test
	public void payDebit_validCard_swipe_sufficientFunds() throws IOException {
		// Set the total amount of money to be paid
		BigDecimal newTotal = new BigDecimal(5);
		// Initiate the card paying software. Simulates user picking to pay with Debit
		pwcs.initiatePayment(newTotal, "Debit");

		// Reduces the chance for failure only for testing
		try {
			// Simulates swiping the card through the reader
			scs.cardReader.swipe(newDebitCard);
		} catch (Exception MagneticStripeFailureException) {
			scs.cardReader.swipe(newDebitCard);
		}

		// Boolean containing the difference between the amount paid and the amount to be paid.
		boolean result = pwcs.getAmountPaid().compareTo(newTotal) == 0;

		// Compare the amount that was paid to the amount to be paid.
		Assert.assertEquals(true, result);
	}

	/**
	 * 2. Tests to ensure that when swiping a credit card, if card is valid and funds are sufficient, payment occurs.
	 * @throws IOException
	 */
	@Test
	public void payCredit_validCard_swipe_sufficientFunds() throws IOException {
		// Set the total amount of money to be paid
		BigDecimal newTotal = new BigDecimal(5);
		// Initiate the card paying software. Simulates user picking to pay with Debit
		pwcs.initiatePayment(newTotal, "Credit");

		// Reduces the chance for failure only for testing
		try {
			// Simulates swiping the card through the reader
			scs.cardReader.swipe(newCreditCard);
		} catch (Exception MagneticStripeFailureException) {
			scs.cardReader.swipe(newCreditCard);
		}

		// Boolean containing the difference between the amount paid and the amount to be paid.
		boolean result = pwcs.getAmountPaid().compareTo(newTotal) == 0;

		// Compare the amount that was paid to the amount to be paid.
		Assert.assertEquals(true, result);
	}

	/**
	 * 3. Tests to ensure that when swiping a debit card, if card is valid and funds are insufficient, payment does not occur.
	 * @throws IOException
	 */
	@Test
	public void payDebit_validCard_swipe_insufficientFunds() throws IOException {
		// Set the total amount of money to be paid
		BigDecimal newTotal = new BigDecimal(100);
		// Initiate the card paying software. Simulates user picking to pay with Debit
		pwcs.initiatePayment(newTotal, "Debit");

		// Reduces the chance for failure only for testing
		try {
			// Simulates swiping the card through the reader
			scs.cardReader.swipe(newDebitCard);
		} catch (Exception MagneticStripeFailureException) {
			scs.cardReader.swipe(newDebitCard);
		}

		// Boolean containing the difference between the amount paid and the amount to be paid.
		boolean result = pwcs.getAmountPaid().compareTo(newTotal) == 0;

		// Compare the amount that was paid to the amount to be paid.
		Assert.assertEquals(false, result);
	}

	/**
	 * 4. Tests to ensure that when swiping a credit card, if card is valid and funds are insufficient, payment does not occur.
	 * @throws IOException
	 */
	@Test
	public void payCredit_validCard_swipe_insufficientFunds() throws IOException {
		// Set the total amount of money to be paid
		BigDecimal newTotal = new BigDecimal(100);
		// Initiate the card paying software. Simulates user picking to pay with Credit
		pwcs.initiatePayment(newTotal, "Credit");

		// Reduces the chance for failure only for testing
		try {
			// Simulates swiping the card through the reader
			scs.cardReader.swipe(newCreditCard);
		} catch (Exception MagneticStripeFailureException) {
			scs.cardReader.swipe(newCreditCard);
		}

		// Boolean containing the difference between the amount paid and the amount to be paid.
		boolean result = pwcs.getAmountPaid().compareTo(newTotal) == 0;

		// Compare the amount that was paid to the amount to be paid.
		Assert.assertEquals(false, result);
	}

	/**
	 * 5. Tests to ensure that the observer Boolean flag is working properly.
	 * When a card is swiped, the CVV is not checked as it is not supported via swiping.
	 * Full payment occurs.
	 * @throws IOException
	 */
	@Test
	public void payDebit_swipe_badCVV() throws IOException {
		// Set the total amount of money to be paid
		BigDecimal newTotal = new BigDecimal(5);
		// Initiate the card paying software. Simulates user picking to pay with Debit.
		pwcs.initiatePayment(newTotal, "Debit");
		// Initiates a local variable to store the data from the card
		Card.CardData debitData;

		// Reduces the chance for failure only for testing
		try {
			// Simulates swiping the card through the reader. Additionally stores card data for asserting a boolean.
			debitData = scs.cardReader.swipe(newDCBadCVV);
		} catch (Exception MagneticStripeFailureException) {
			debitData = scs.cardReader.swipe(newDCBadCVV);
		}

		// Boolean containing the difference between the amount paid and the amount to be paid.
		boolean result = pwcs.getAmountPaid().compareTo(newTotal) == 0;

		// Checks if payment occurred successfully
		Assert.assertEquals(true, result);
		// Checks if verification of the card was successful (sees if CVV was not checked)
		Assert.assertTrue(db.verifyCard(debitData, true));
	}
}
