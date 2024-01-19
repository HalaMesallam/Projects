package org.lsmr.selfcheckout.software.test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.software.BanknoteSlotSoftware;


/*The reason why we have these as null is because the methods aren't really implemented for this iteration.*/


public class BanknoteSlotTest {

	private SelfCheckoutStation scs;
	 private BanknoteSlotSoftware bss;
	 private BigDecimal[] amountPaid;

	 private final Currency currency = Currency.getInstance(Locale.CANADA);

	//Data needed for instantiate the self-checkout station
	int[] banknoteDenominations = { 5, 10, 20, 50, 100 };
	BigDecimal[] coinDenominations = { new BigDecimal("0.05"), new BigDecimal("0.1"), new BigDecimal("0.25"),
			new BigDecimal("1"), new BigDecimal("2") };
	int scaleMaximumWeight = 100;
	int scaleSensitivity = 1;


	@Before
	public void setup() {
		//We give the BanknoteSlotSoftware a reference to this class' variable called amountPaid.
		amountPaid = new BigDecimal[1];
		amountPaid[0] = new BigDecimal("0");
		scs = new SelfCheckoutStation(currency, banknoteDenominations, coinDenominations,scaleMaximumWeight,scaleSensitivity);
		bss = new BanknoteSlotSoftware(scs, amountPaid);

	}

	@Test
	public void testEnabled() {
		bss.enabled(null);
	}

	@Test
	public void testDisabled() {
		bss.disabled(null);
	}

	@Test
	public void testBanknotesFull() {
		bss.banknotesFull(null);
	}

	@Test
	public void testBanknotesAdded() {
		bss.banknoteAdded(null);
	}

	@Test
	public void testBanknotesLoaded() {
		bss.banknotesLoaded(null);
	}

	@Test
	public void testBanknotesUnloaded() {
		bss.banknotesUnloaded(null);
	}

	//Ends with an underscore because of same name method in BanknoteSlotTest.java
	@Test
	public void testBanknotesLoaded_() {
		bss.banknotesLoaded(null);
	}

	//Ends with an underscore because of same name method in BanknoteSlotTest.java
	@Test
	public void testBanknotesUnloaded_() {

		bss.banknotesUnloaded(null);
	}

	@Test
	public void testInvalidBanknoteDetected() {
		amountPaid[0] = new BigDecimal("0");
		bss.invalidBanknoteDetected(null);
		Assert.assertTrue("Amount paid should be zero", amountPaid[0].intValue() == 0);
		amountPaid[0] = new BigDecimal("0");
	}

	@Test
	public void testValidBanknoteDetected() {
		amountPaid[0] = new BigDecimal("0");
		int value = 5;
		bss.validBanknoteDetected(null, Currency.getInstance(Locale.CANADA), value);
		Assert.assertTrue("Amount Paid should be" + value, amountPaid[0].intValue() == value);
		amountPaid[0] = new BigDecimal("0");
	}
}
