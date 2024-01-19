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
import org.lsmr.selfcheckout.SimulationException;
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
public class PayWithCardSoftwareTapTest {
	/*
	 * TEST CASES
	 * Elects to pay with Card
	 * Taps debit or credit to pay
	 * 
	 * 1. Valid debit card, sufficient funds -> Successful payment
	 * 2. Valid credit card, sufficient funds -> Successful payment
	 * 3. Valid debit card, insufficient funds -> Failed payment
	 * 4. Valid credit card, insufficient funds -> Failed payment
	 * 5. Valid debit card, expected type credit -> Failed payment
	 * 6. Valid credit card, expected type debit -> Failed payment
	 * 
	 * 			***These are tested only with tap because the card verification method is shared between all other card reader methods.***
	 * 7. Debit card number not in bank debit card database -> Failed payment
	 * 8. Credit card number not in bank credit card database -> Failed payment
	 * 9. Debit card cardholder not found in debit card database -> Failed payment
	 * 10. Credit card cardholder not found in credit card database -> Failed payment
	 * 11. Debit card CVV invalid -> Failed payment
	 * 12. Credit card CVV invalid -> Failed payment
	 * 13. Tap not available -> Failed payment
	 * 			a. Tap is only tested via debit because it is a feature shared between both card types
	 */

	// Demonimations of accepted banknotes and coins, used to initiate the SelfCheckoutStation
	private int[] bills = new int[] {5,10,20,50,100};
	private BigDecimal[] coins = new BigDecimal[] {new BigDecimal(0.05), new BigDecimal(0.10), new BigDecimal(0.25), new BigDecimal(1.00), new BigDecimal(2.00)};

	// Initiate a BigDecimal array to stub the total amount to pay going into the tests
	private BigDecimal[] amountPaid = new BigDecimal[1];

	// Instantiate classes to use and test
	private SelfCheckoutStation scs; 
	private PayWithCardSoftware pwcs;
	private PaymentController pc;
	private BankDatabase db = new BankDatabase();

	// Sample cards to be used to test the payment method
	private Card newDebitCard = new Card("Debit", "1000000000000000", "Sipeng He", "100", "1000", true, true);
	private Card newCreditCard = new Card("Credit", "1000000000000000","Sipeng He", "100", "1000", true, true);
	private Card newDCBadNum = new Card("Debit", "2000000000000000","Sipeng He", "100", "1000", true, true);
	private Card newCCBadNum = new Card("Credit", "1000000000000009","Sipeng He", "100", "1000", true, true);
	private Card newDCBadHolder = new Card("Debit", "1000000000000000", "Jacky Tran", "100", "1000", true, true);
	private Card newCCBadHolder = new Card("Credit", "1000000000000000","Manbir Sandhu", "100", "1000", true, true);
	private Card newDCBadCVV = new Card("Debit", "1000000000000000", "Sipeng He", "200", "1000", true, true);
	private Card newCCBadCVV = new Card("Credit", "1000000000000000","Sipeng He", "200", "1000", true, true);
	private Card newDCNoTap = new Card("Debit", "1000000000000000", "Sipeng He", "100", "1000", false, true);

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
	 * 1. Tests to ensure that when tapping a debit card, if card is valid and funds are sufficient, payment occurs.
	 * @throws IOException
	 */
	@Test
	public void payDebit_validCard_tap_sufficientFunds() throws IOException {
		// Set the total amount of money to be paid
		BigDecimal newTotal = new BigDecimal(5);
		// Initiate the card paying software. Simulates user picking to pay with Debit
		pwcs.initiatePayment(newTotal, "Debit");

		// Reduces the chance for failure only for testing
		try {
			// Simulates the user tapping the card on the card reader.
			scs.cardReader.tap(newDebitCard);
		} catch (Exception TapFailureException) {
			scs.cardReader.tap(newDebitCard);
		}

		// Boolean containing the difference between the amount paid and the amount to be paid.
		boolean result = pwcs.getAmountPaid().compareTo(newTotal) == 0;

		// Compare the amount that was paid to the amount to be paid.
		Assert.assertEquals(true, result);
	}

	/**
	 * 2. Tests to ensure that when tapping a credit card, if card is valid and funds are sufficient, payment occurs.
	 * @throws IOException
	 */
	@Test
	public void payCredit_validCard_tap_sufficientFunds() throws IOException {
		// Set the total amount of money to be paid
		BigDecimal newTotal = new BigDecimal(5);
		// Initiate the card paying software. Simulates user picking to pay with Debit
		pwcs.initiatePayment(newTotal, "Credit");

		// Reduces the chance for failure only for testing
		try {
			// Simulates the user tapping the card on the card reader.
			scs.cardReader.tap(newCreditCard);
		} catch (Exception TapFailureException) {
			scs.cardReader.tap(newCreditCard);
		}

		// Boolean containing the difference between the amount paid and the amount to be paid.
		boolean result = pwcs.getAmountPaid().compareTo(newTotal) == 0;

		// Compare the amount that was paid to the amount to be paid.
		Assert.assertEquals(true, result);
	}

	/**
	 * 3. Test to ensure that when tapping a debit card, if card is valid and funds are insufficient, payment does not occur.
	 * @throws IOException
	 */
	@Test
	public void payDebit_validCard_tap_insufficientFunds() throws IOException {
		// Set the total amount of money to be paid
		BigDecimal newTotal = new BigDecimal(100);
		// Initiate the card paying software. Simulates user picking to pay with Debit
		pwcs.initiatePayment(newTotal, "Debit");

		// Reduces the chance for failure only for testing
		try {
			// Simulates the user tapping the card on the card reader.
			scs.cardReader.tap(newDebitCard);
		} catch (Exception TapFailureException) {
			scs.cardReader.tap(newDebitCard);
		}

		// Boolean containing the difference between the amount paid and the amount to be paid.
		boolean result = pwcs.getAmountPaid().compareTo(newTotal) == 0;

		// Compare the amount that was paid to the amount to be paid.
		Assert.assertEquals(false, result);
	}

	/**
	 * 4. Test to ensure that when tapping a credit card, if card is valid and funds are insufficient, payment does not occur.
	 * @throws IOException
	 */
	@Test
	public void payCredit_validCard_tap_insufficientFunds() throws IOException {
		// Set the total amount of money to be paid
		BigDecimal newTotal = new BigDecimal(100);
		// Initiate the card paying software. Simulates user picking to pay with Debit
		pwcs.initiatePayment(newTotal, "Credit");

		// Reduces the chance for failure only for testing
		try {
			// Simulates the user tapping the card on the card reader.
			scs.cardReader.tap(newCreditCard);
		} catch (Exception TapFailureException) {
			scs.cardReader.tap(newCreditCard);
		}

		// Boolean containing the difference between the amount paid and the amount to be paid.
		boolean result = pwcs.getAmountPaid().compareTo(newTotal) == 0;

		// Compare the amount that was paid to the amount to be paid.
		Assert.assertEquals(false, result);
	}


	/**
	 * 5. Tests the use case in which the user elects to pay with credit card but instead uses a card of wrong type debit.
	 * @throws IOException
	 */
	@Test(expected = SimulationException.class)
	public void payCredit_isTypeDebit() throws IOException {
		//Set the total amount of money to be paid
		BigDecimal newTotal = new BigDecimal(5);
		// Initiate the card paying software. Simulates user picking to pay with Debit
		pwcs.initiatePayment(newTotal, "Credit");

		// Reduces the chance for failure only for testing
		try {
			// Simulates the user tapping the card on the card reader.
			scs.cardReader.tap(newDebitCard);
		} catch (Exception TapFailureException) {
			scs.cardReader.tap(newDebitCard);
		}
	}

	/**
	 * 6. Tests the use case in which the user elects to pay with debit card but instead uses a card of wrong type credit.
	 * @throws IOException
	 */
	@Test(expected = SimulationException.class)
	public void payDebit_isTypeCredit() throws IOException {
		//Set the total amount of money to be paid
		BigDecimal newTotal = new BigDecimal(5);
		// Initiate the card paying software. Simulates user picking to pay with Debit
		pwcs.initiatePayment(newTotal, "Debit");

		// Reduces the chance for failure only for testing
		try {
			// Simulates the user tapping the card on the card reader.
			scs.cardReader.tap(newCreditCard);
		} catch (Exception TapFailureException) {
			scs.cardReader.tap(newCreditCard);
		}
	}

	/**
	 * 7. Tests the use case in which the user taps the correct card of card type but the card number is not found in the debit card bank database.
	 * @throws IOException
	 */
	@Test
	public void payDebit_cardNumNotInBank() throws IOException {
		//Set the total amount of money to be paid
		BigDecimal newTotal = new BigDecimal(5);
		// Initiate the card paying software. Simulates user picking to pay with Debit
		pwcs.initiatePayment(newTotal, "Debit");

		// Initiates a local variable to store the data from the card
		Card.CardData data;

		// Reduces the chance for failure only for testing
		try {
			// Simulates the user tapping the card on the card reader. Additionally stores the card data for later assertion.
			data = scs.cardReader.tap(newDCBadNum);
		} catch (Exception TapFailureException) {
			data = scs.cardReader.tap(newDCBadNum);
		}

		// Boolean containing the difference between the amount paid and the amount to be paid.
		boolean result = pwcs.getAmountPaid().compareTo(newTotal) == 0;

		// Compare the amount that was paid to the amount to be paid.
		Assert.assertEquals(false, result);
		// Verifies that the card check fails
		Assert.assertFalse(db.verifyCard(data, false));
	}

	/**
	 * 8. Tests the use case in which the user taps the correct card of card type but the card number is not found in the credit card bank database.
	 * @throws IOException
	 */
	@Test
	public void payCredit_cardNumNotInBank() throws IOException {
		//Set the total amount of money to be paid
		BigDecimal newTotal = new BigDecimal(5);
		// Initiate the card paying software. Simulates user picking to pay with Debit
		pwcs.initiatePayment(newTotal, "Credit");

		// Initiates a local variable to store the data from the card
		Card.CardData data;

		// Reduces the chance for failure only for testing
		try {
			// Simulates the user tapping the card on the card reader. Additionally stores the card data for later assertion.
			data = scs.cardReader.tap(newCCBadNum);
		} catch (Exception TapFailureException) {
			data = scs.cardReader.tap(newCCBadNum);
		}

		// Boolean containing the difference between the amount paid and the amount to be paid.
		boolean result = pwcs.getAmountPaid().compareTo(newTotal) == 0;

		// Compare the amount that was paid to the amount to be paid.
		Assert.assertEquals(false, result);
		// Verifies that the card check fails
		Assert.assertFalse(db.verifyCard(data, false));
	}

	/**
	 * 9. Tests the case in which the cardholder signature of the card does not match the debit card bank database.
	 * @throws IOException
	 */
	@Test
	public void payDebit_cardHolderDoesNotMatch() throws IOException {
		//Set the total amount of money to be paid
		BigDecimal newTotal = new BigDecimal(5);
		// Initiate the card paying software. Simulates user picking to pay with Debit
		pwcs.initiatePayment(newTotal, "Debit");

		// Initiates a local variable to store the data from the card
		Card.CardData data;

		// Reduces the chance for failure only for testing
		try {
			// Simulates the user tapping the card on the card reader. Additionally stores the card data for later assertion.
			data = scs.cardReader.tap(newDCBadHolder);
		} catch (Exception TapFailureException) {
			data = scs.cardReader.tap(newDCBadHolder);
		}

		// Boolean containing the difference between the amount paid and the amount to be paid.
		boolean result = pwcs.getAmountPaid().compareTo(newTotal) == 0;

		// Compare the amount that was paid to the amount to be paid.
		Assert.assertEquals(false, result);
		// Verifies that the card check fails
		Assert.assertFalse(db.verifyCard(data, false));
	}

	/**
	 * 10. Tests the case in which the cardholder signature of the card does not match the credit card bank database.
	 * @throws IOException
	 */
	@Test
	public void payCredit_cardHolderDoesNotMatch() throws IOException {
		//Set the total amount of money to be paid
		BigDecimal newTotal = new BigDecimal(5);
		// Initiate the card paying software. Simulates user picking to pay with Debit
		pwcs.initiatePayment(newTotal, "Credit");

		// Initiates a local variable to store the data from the card
		Card.CardData data;

		// Reduces the chance for failure only for testing
		try {
			// Simulates the user tapping the card on the card reader. Additionally stores the card data for later assertion.
			data = scs.cardReader.tap(newCCBadHolder);
		} catch (Exception TapFailureException) {
			data = scs.cardReader.tap(newCCBadHolder);
		}

		// Boolean containing the difference between the amount paid and the amount to be paid.
		boolean result = pwcs.getAmountPaid().compareTo(newTotal) == 0;

		// Compare the amount that was paid to the amount to be paid.
		Assert.assertEquals(false, result);
		// Verifies that the card check fails
		Assert.assertFalse(db.verifyCard(data, false));
	}

	/**
	 * 11. Tests the case in which the CVV of the card does not match the debit card bank database.
	 * @throws IOException
	 */
	@Test
	public void payDebit_badCVV() throws IOException {
		//Set the total amount of money to be paid
		BigDecimal newTotal = new BigDecimal(5);
		// Initiate the card paying software. Simulates user picking to pay with Debit
		pwcs.initiatePayment(newTotal, "Debit");

		// Initiates a local variable to store the data from the card
		Card.CardData data;

		// Reduces the chance for failure only for testing
		try {
			// Simulates the user tapping the card on the card reader. Additionally stores the card data for later assertion.
			data = scs.cardReader.tap(newDCBadCVV);
		} catch (Exception TapFailureException) {
			data = scs.cardReader.tap(newDCBadCVV);
		}

		// Boolean containing the difference between the amount paid and the amount to be paid.
		boolean result = pwcs.getAmountPaid().compareTo(newTotal) == 0;

		// Compare the amount that was paid to the amount to be paid.
		Assert.assertEquals(false, result);
		// Verifies that the card check fails
		Assert.assertFalse(db.verifyCard(data, false));
	}

	/**
	 * 12. Tests the case in which the CVV of the card does not match the credit card bank database.
	 * @throws IOException
	 */
	@Test
	public void payCredit_badCVV() throws IOException {
		//Set the total amount of money to be paid
		BigDecimal newTotal = new BigDecimal(5);
		// Initiate the card paying software. Simulates user picking to pay with Debit
		pwcs.initiatePayment(newTotal, "Credit");

		// Initiates a local variable to store the data from the card
		Card.CardData data;

		// Reduces the chance for failure only for testing
		try {
			// Simulates the user tapping the card on the card reader. Additionally stores the card data for later assertion.
			data = scs.cardReader.tap(newCCBadCVV);
		} catch (Exception TapFailureException) {
			data = scs.cardReader.tap(newCCBadCVV);
		}

		// Boolean containing the difference between the amount paid and the amount to be paid.
		boolean result = pwcs.getAmountPaid().compareTo(newTotal) == 0;

		// Compare the amount that was paid to the amount to be paid.
		Assert.assertEquals(false, result);
		// Verifies that the card check fails
		Assert.assertFalse(db.verifyCard(data, false));
	}

	/**
	 * 13. Tests the use case in which the used card does not have tap enabled, resulting in no data being read.
	 * @throws IOException
	 */
	@Test
	public void payDebit_tapNotAvailable() throws IOException {
		// Set the total amount of money to be paid
		BigDecimal newTotal = new BigDecimal(5);
		// Initiate the card paying software. Simulates user picking to pay with Debit

		// Initiates a local variable to store the data from the card
		Card.CardData data;

		// Reduces the chance for failure only for testing
		try {
			// Simulates the user tapping the card on the card reader. Additionally stores the card data for later assertion.
			data = scs.cardReader.tap(newDCNoTap);
		} catch (Exception TapFailureException) {
			data = scs.cardReader.tap(newDCNoTap);
		}

		// Boolean containing the difference between the amount paid and the amount to be paid.
		boolean result = pwcs.getAmountPaid().compareTo(newTotal) == 0;

		// Compare the amount that was paid to the amount to be paid.
		Assert.assertEquals(false, result);
		// Checks that no data was read
		Assert.assertEquals(null, data);
	}
}
