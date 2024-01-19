package org.lsmr.selfcheckout.software.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.devices.BarcodeScanner;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.SimulationException;
import org.lsmr.selfcheckout.software.AttendantBlockException;
import org.lsmr.selfcheckout.software.BarcodeScannerSoftware;
import org.lsmr.selfcheckout.software.ElectronicScaleSoftware;
import org.lsmr.selfcheckout.software.SelfCheckoutStationSoftware;

public class BaggingAreaTest {
	
	public Currency currency = Currency.getInstance(Locale.CANADA);
    public int[] denominations = new int[]{5, 10, 20, 50, 100};
    public BigDecimal[] coinDenominations = new BigDecimal[]{
		new BigDecimal("0.05"),
		new BigDecimal("0.10"),
		new BigDecimal("0.25"),
		new BigDecimal("1.00"),
		new BigDecimal("2.00"),
	};;
		
    public int scaleMaximumWeight;
    public int scaleSensitivity;
    
	private ElectronicScale es = new ElectronicScale(200, 1);
	private ElectronicScaleSoftware ess = new ElectronicScaleSoftware();
	
	private SelfCheckoutStation scs =  new SelfCheckoutStation(currency , denominations, coinDenominations, 200, 1);
	private SelfCheckoutStationSoftware scss =  new SelfCheckoutStationSoftware(scs);
	
	private BarcodeScannerSoftware bss;
	private Barcode b;
	private BarcodeScanner bs = new BarcodeScanner();
	
	@Before
	public void setup() {
		ess.enabled(es);
		
		assertEquals(ess.getCurrentWeight(), 0, 0);
		assertEquals(ess.getWeightAtLastEvent(), 0, 0);	
	}
	
	@Test
	public void correctItemInBaggingAreaTest() throws AttendantBlockException {		
		bss = scss.getBss();
		ess = scss.getEss();
		b = new Barcode(new Numeral[] {Numeral.one});
		
		assertTrue(scss.verifyWeights() == true);
	}
	
	@Test
	public void incorrectItemInBaggingAreaTest() throws AttendantBlockException {
		bss = scss.getBss();
		ess = scss.getEss();
		b = new Barcode(new Numeral[] {Numeral.one});
		ess.weightChanged(es, 1);
		
		assertTrue(scss.verifyWeights() == false);
	}
	
	@Test
	public void bagsUsedGreaterThanZero() throws SimulationException, AttendantBlockException {
		scss.setBagsUsed(2);;
		scss.promptForBags();
		assertTrue("BroughtBags should be false", scss.getBroughtBags() == false);
		assertTrue("BagsUsed should be equals to 2", scss.getBagsUsed() == 2);
	}
	
	@Test
	public void bagsUsedEqualsToZero() throws SimulationException, AttendantBlockException {
		scss.setBagsUsed(0);;
		scss.promptForBags();
		assertTrue("BroughtBags should be false", scss.getBroughtBags() == true);
		assertTrue("BagsUsed should be equals to 2", scss.getBagsUsed() == 0);
	}
	
	@Test (expected = SimulationException.class)
	public void bagsUsedLessThanZero() throws SimulationException, AttendantBlockException {
		scss.setBagsUsed(-1);;
		scss.promptForBags();
	}
}
