package org.lsmr.selfcheckout.software.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SupervisionStation;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.software.AttendantOverride;
import org.lsmr.selfcheckout.software.DiscrepancyChecker;
import org.lsmr.selfcheckout.software.SelfCheckoutStationSoftware;
import org.lsmr.selfcheckout.software.AttendantOverride.Status;
import org.lsmr.selfcheckout.software.DiscrepancyChecker.BSO;
import org.lsmr.selfcheckout.software.DiscrepancyChecker.ESO;
import org.lsmr.selfcheckout.software.DiscrepancyChecker.State;

import GUI.ProductDatabasesSample;

public class DiscrepancyCheckerTest {
	//declare testing variables and objects	
    private DiscrepancyChecker sdc;
    
    private ProductDatabasesSample db = new ProductDatabasesSample();
    
    
	public Currency currency = Currency.getInstance(Locale.CANADA);
    public int[] denominations = new int[]{5, 10, 20, 50, 100};
    public BigDecimal[] coinDenominations = new BigDecimal[]{
		new BigDecimal("0.05"),
		new BigDecimal("0.10"),
		new BigDecimal("0.25"),
		new BigDecimal("1.00"),
		new BigDecimal("2.00"),
	};;
    
	private SelfCheckoutStation scs;
    private SelfCheckoutStationSoftware scss;
    private Item item;
    private DiscrepancyChecker dc;
	
    private State state;
	
	@Before
	//runs before each test
	public void setUp() {
		//this is taken from the selfcheckout class. just setting everything up
		scs =  new SelfCheckoutStation(currency , denominations, coinDenominations, 200, 1);
    	scss = new SelfCheckoutStationSoftware(scs);
		dc = new DiscrepancyChecker(scs, 0.01);
		this.state = dc.getState();
	}

	@After
	public void tearDown() {
		
		

	}
	
	
	@Test
	public void testItemScanned() {
		Barcode barcode1 = new Barcode(new Numeral[] {Numeral.one});
		BarcodedProduct testProduct1 = db.getItem(barcode1);
		Item it1 = new BarcodedItem(testProduct1.getBarcode(), testProduct1.getExpectedWeight());
		scs.mainScanner.scan(it1);
		scs.baggingArea.add(it1);
		
		boolean expected = true;
		boolean actual = false;
		if(dc.getState() == state.NORMAL) {
			actual = true;
		}
		
		assertEquals("Item should be scanned withour discrepency error",
				expected, actual);	
	}
	
	
	@Test
	public void testItemNotPutOnScaleAfterScanning() {
		Barcode barcode1 = new Barcode(new Numeral[] {Numeral.one});
		BarcodedProduct testProduct1 = db.getItem(barcode1);
		Item it1 = new BarcodedItem(testProduct1.getBarcode(), testProduct1.getExpectedWeight());
		scs.mainScanner.scan(it1);
		
		//GIVE 6 SECONDS FOR THE TIMER 
		try{Thread.sleep(6000);}catch(InterruptedException e){System.out.println(e);}  
		//scs.baggingArea.add(it1);
		
		boolean expected = true;
		boolean actual = false;
		if(dc.getState() == state.DISCREPANCY) {
			actual = true;
		}
		
		assertEquals("Item should have discrepancy error",
				expected, actual);	
	}

	@Test
	public void testApproveDiscrepancy() {
		dc.forceDiscrepancyState();
		
		dc.approveDiscrepancy();
		
		State state = dc.getState();
		
		boolean expected = true;
		boolean actual = false;
		if(state == State.NORMAL) {
			actual = true;
		}

		assertEquals("SHOULD BE IN NORMAL STATE",
				expected, actual);	
	}
	
	@Test
	public void testItemRemoveWithoutFinishingCheckout() {
		
		Barcode barcode1 = new Barcode(new Numeral[] {Numeral.one});
		BarcodedProduct testProduct1 = db.getItem(barcode1);
		Item it1 = new BarcodedItem(testProduct1.getBarcode(), testProduct1.getExpectedWeight());
		dc.setLastTimeWeight(10000);
		scs.baggingArea.add(it1);
				
		State state = dc.getState();
		
		boolean expected = true;
		boolean actual = false;
		if(state == State.DISCREPANCY) {
			actual = true;
		}

		assertEquals("SHOULD BE IN DISCREPANCY STATE",
				expected, actual);	
	}
	
	@Test
	public void testAddingItemWithoutScanning() {
		
		Barcode barcode1 = new Barcode(new Numeral[] {Numeral.one});
		BarcodedProduct testProduct1 = db.getItem(barcode1);
		Item it1 = new BarcodedItem(testProduct1.getBarcode(), testProduct1.getExpectedWeight());
		
		scs.baggingArea.add(it1);
				
		State state = dc.getState();
		
		boolean expected = true;
		boolean actual = false;
		if(state == State.DISCREPANCY) {
			actual = true;
		}

		assertEquals("SHOULD BE IN DISCREPANCY STATE",
				expected, actual);	
	}

}
