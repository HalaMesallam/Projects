package org.lsmr.selfcheckout.software.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.*;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.software.AttendantBlockException;
import org.lsmr.selfcheckout.software.SelfCheckoutStationSoftware;

public class CheckoutTest {

	//Canadian dollar is the valid currency
    private Currency currency = Currency.getInstance("CAD");

    int[] banknoteDenominations = { 5, 10, 20, 50, 100 };
    BigDecimal[] coinDenominations = { new BigDecimal("0.05"), new BigDecimal("0.1"), new BigDecimal("0.25"),
            new BigDecimal("1.00"), new BigDecimal("2.00") }; // not a correct coin denomination, but accounts for errors with change collecting
    int scaleMaximumWeight = 100;
    int scaleSensitivity = 1;

    private SelfCheckoutStation scs; //The self-checkout station hardware
    private SelfCheckoutStationSoftware scss;
 	
    // 1.58$
    private BarcodedItem item = new BarcodedItem(new Barcode(new Numeral[] {Numeral.one}), 20);
    // 1.00$
    private Coin coin = new Coin(currency, new BigDecimal("2.00")); // incorrect coin denomination, accounts for errors with change collecting
    
	@Before
	public void setup() {
		scs = new SelfCheckoutStation(currency, banknoteDenominations, coinDenominations,scaleMaximumWeight,scaleSensitivity);
		scss = new SelfCheckoutStationSoftware(scs);
	}
	
	@Test
	public void multiplePaymentsTest() throws DisabledException, OverloadException, AttendantBlockException {
		scs.mainScanner.scan(item);
		scss.setBagsUsed(1);
		scss.checkout();
		scss.payWithCash();
		scs.coinSlot.accept(coin);
		scss.suspendCheckout();
		scs.mainScanner.scan(item);
		scss.backToCheckout();
		scss.payWithCash();
		scs.coinSlot.accept(coin);
		assertEquals(scss.getAmountPaid().compareTo(new BigDecimal(4.00)), 0);
	}
}
