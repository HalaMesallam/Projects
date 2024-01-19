package org.lsmr.selfcheckout.software.test;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.devices.EmptyException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.software.ReceiptPrinterFacade;
import org.lsmr.selfcheckout.software.ReceiptPrinterFacadeListener;


public class ReceiptPrinterMaintanenceTest {

	//Canadian dollar is the valid currency
	    private Currency currency = Currency.getInstance("CAD");
	
	    int[] banknoteDenominations = { 5, 10, 20, 50, 100 };
	    BigDecimal[] coinDenominations = { new BigDecimal("0.05"), new BigDecimal("0.1"), new BigDecimal("0.25"),
	            new BigDecimal("1.00"), new BigDecimal("2.00") }; // not a correct coin denomination, but accounts for errors with change collecting
	    int scaleMaximumWeight = 100;
	    int scaleSensitivity = 1;
	    boolean lowInkNotified =false;
	    boolean lowPaperNotified=false;
	    boolean paperAddedNotified =false;
	    boolean inkAddedNotified =false;
	    boolean inkOverloadNotified = false;
	    boolean paperOverloadNotified = false;
	
	    private SelfCheckoutStation scs; //The self-checkout station hardware
	    private ReceiptPrinterFacade rp;
	    private RPL rpl;
	    
	    class RPL implements ReceiptPrinterFacadeListener{

			@Override
			public void notifyLowInk() {
				// TODO Auto-generated method stub
				lowInkNotified = true;
			}

			@Override
			public void notifyLowPaper() {
				// TODO Auto-generated method stub
				lowPaperNotified = true;
			}

			@Override
			public void notifyPaperAdded() {
				// TODO Auto-generated method stub
				paperAddedNotified = true;
			}

			@Override
			public void notifyInkAdded() {
				// TODO Auto-generated method stub
				inkAddedNotified = true;
			}

			@Override
			public void notifyInkOverload() {
				// TODO Auto-generated method stub
				inkOverloadNotified = true;
			}

			@Override
			public void notifyPaperOverload() {
				// TODO Auto-generated method stub
				paperOverloadNotified = true;
			}
	    }
	    
	    @Before
	    public void setup() {
	    	scs = new SelfCheckoutStation(currency, banknoteDenominations, coinDenominations,scaleMaximumWeight,scaleSensitivity);
			rp = new ReceiptPrinterFacade(scs);
			rpl = new RPL();
			rp.attach(rpl);
	    }
	   
	    
	    @Test
	    public void testNotifyInkLow() throws OverloadException, EmptyException {
	    	scs.printer.addInk(1);
	    	Assert.assertTrue(inkAddedNotified);
	    	scs.printer.addPaper(2);
	    	Assert.assertTrue(paperAddedNotified);
	    	scs.printer.print('a');
	    	Assert.assertTrue(lowInkNotified==true);
	    	Assert.assertTrue(lowPaperNotified ==false);
	    }
	    
	    @Test
	    public void testNotifyPaperLow() throws OverloadException, EmptyException {
	    	scs.printer.addInk(2);
	    	Assert.assertTrue(inkAddedNotified);
	    	scs.printer.addPaper(1);
	    	Assert.assertTrue(paperAddedNotified);
	    	scs.printer.print('a');
	    	scs.printer.print('\n');
	    	Assert.assertTrue(lowInkNotified==false);
	    	Assert.assertTrue(lowPaperNotified==true);
	    }
	    
	    @Test
	    public void testAddInk() throws EmptyException, OverloadException {
	    	rp.setInkQuantityToAdd(1);
	    	rp.addInk();
	    	rp.addPaper();
	    	do {
	    		scs.printer.print('a');
	    	} while(lowInkNotified == false);
	    	scs.printer.cutPaper();
	    	String receipt = scs.printer.removeReceipt();
	    	Assert.assertTrue(receipt.length()==1);
	    }
	    
	    @Test
	    public void testAddPaper() throws EmptyException, OverloadException {
	    	rp.setPaperUnitsToAdd(1);
	    	rp.addInk();
	    	rp.addPaper();
	    	do {
	    		scs.printer.print('\n');
	    	} while(lowPaperNotified == false);
	    	scs.printer.cutPaper();
	    	String receipt = scs.printer.removeReceipt();
	    	Assert.assertTrue(receipt.length()==1);
	    }
	    
	    @Test(expected = OverloadException.class)
	    public void testAddPaperCausingOverload() throws OverloadException {
	    	rp.setPaperUnitsToAdd(1<<10+1);
	    	rp.addPaper();
	    	Assert.assertTrue(paperOverloadNotified);
	    }
	    
	    @Test(expected = OverloadException.class)
	    public void testAddInkCausingOverload() throws OverloadException {
	    	rp.setInkQuantityToAdd(1<<20+1);
	    	rp.addInk();
	    	Assert.assertTrue(inkOverloadNotified);
	    }
}
