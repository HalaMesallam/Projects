package org.lsmr.selfcheckout.software.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.*;
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.BarcodeScannerObserver;
import org.lsmr.selfcheckout.software.AttendantBlockException;
import org.lsmr.selfcheckout.software.SelfCheckoutStationSoftware;

import GUI.ProductDatabasesSample;

import java.math.BigDecimal;
import java.util.*;

/**
 * Test cases to test the software as a whole.
 * Two cases included:
 * -The customer scanned items, make partial payment and then decide to add more items.
 * -The customer scanned items and make the payment without adding new items.
 */
public class WholeCheckoutFlowTest {
    private SelfCheckoutStation scs;
    private SelfCheckoutStationSoftware scss;

    //Data needed for create the self-checkout station
    private Currency currency = Currency.getInstance("CAD");
    int[] banknoteDenominations = { 5, 10, 20, 50, 100 };
    BigDecimal[] coinDenominations = { new BigDecimal("0.05"), new BigDecimal("0.1"), new BigDecimal("0.25"),
            new BigDecimal("1"), new BigDecimal("2") };
    int scaleMaximumWeight = 100;
    int scaleSensitivity = 1;
    
    //database
    private ProductDatabasesSample pds = new ProductDatabasesSample(); 
    
    //Stub items
    Item milk = new BarcodedItem(new Barcode(new Numeral[] {Numeral.one}), 1200);
    Item faceMask = new BarcodedItem(new Barcode(new Numeral[] {Numeral.two}), 210);

    //Stub cash
    Coin oneDollarCoin = new Coin(currency, new BigDecimal(1));
    Banknote fiftyDollarBill = new Banknote(currency, 50);

    //List to catch change
    private List<Coin> coinsReturned; //The list that catches coins returned as change
    private Banknote[] banknotesReturned;  //The list that catches banknotes returned as change

    private boolean ifBarcodeScanned = false; //Indicate whether the item is scanned successfully or not

    /**
     * Barcode Scanner observer used to ensure that an item is scanned when the customer moved to the next one.
     */
    private class BarcodeScannerListener implements BarcodeScannerObserver{

        @Override
        public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {

        }

        @Override
        public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {

        }

        @Override
        public void barcodeScanned(BarcodeScanner barcodeScanner, Barcode barcode) {
            ifBarcodeScanned=true;
        }
    }

    private BarcodeScannerListener bsl;


    /**
     * Helper method to calculate the value of changes collected by the customer after the checkout process.
     * @return The value of changes collected by the customer
     */
    private BigDecimal calculateReceivedChange(){
        coinsReturned = scs.coinTray.collectCoins();
//        banknotesReturned = null;
        if(banknotesReturned != null) {
        	while(!scs.banknoteOutput.hasSpace()) {
            	banknotesReturned = scs.banknoteOutput.removeDanglingBanknotes();
        	}
        }
        BigDecimal amountOfChange = BigDecimal.ZERO;
        for(Banknote b: banknotesReturned) {
            amountOfChange = amountOfChange.add(new BigDecimal(b.getValue()));
        }
        for(Coin c: coinsReturned) {
            if(c != null)
                amountOfChange = amountOfChange.add(c.getValue());
        }
        return amountOfChange;
    }

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

    @Before
    /**
     * Setup for the test.
     */
    public void setUp() throws OverloadException {
        scs = new SelfCheckoutStation(currency, banknoteDenominations, coinDenominations,scaleMaximumWeight,scaleSensitivity);
        scss = new SelfCheckoutStationSoftware(scs);

        //Add banknotes and coins to the dispenser
        for(Integer denomination: banknoteDenominations){
            scs.banknoteDispensers.get(denomination).load(newBanknote(denomination), newBanknote(denomination), newBanknote(denomination));
        }
        for(BigDecimal denomination: coinDenominations){
            scs.coinDispensers.get(denomination).load(newCoin(denomination),newCoin(denomination),newCoin(denomination));
        }

        //Attach the listener to the scanner
        bsl = new BarcodeScannerListener();
        scs.mainScanner.attach(bsl);

        //Add ink and paper to the printer
        scs.printer.addInk(1000);
        scs.printer.addPaper(100);
    }

    @Test
    /**
     * Test the checkout process involving adding new items when doing the payment.
     * @throws EmptyException 
     */
    public void testPartialPaymentAndAddItems() throws DisabledException, OverloadException, InterruptedException, EmptyException, SimulationException, AttendantBlockException {
        while(ifBarcodeScanned == false){   //While loop is used to ensure that the item is scanned
            scs.mainScanner.scan(milk);
        }
        ifBarcodeScanned = false;
        scs.baggingArea.add(milk);

        //Check if the total due calculated is as expected
        Assert.assertTrue(scss.total().compareTo(new BigDecimal("1.58"))==0);

        scss.checkout();
        scs.coinSlot.accept(oneDollarCoin);
        scss.suspendCheckout(); //The customer decides to suspend checkout process and scan more items

        while(ifBarcodeScanned == false){   //While loop is used to ensure that the item is scanned
            scs.mainScanner.scan(faceMask);
        }
        ifBarcodeScanned = false;
        scs.baggingArea.add(faceMask);

        //Check if the updated new total due is as expected
        Assert.assertTrue(scss.total().compareTo(new BigDecimal("28.54"))==0);
        scss.checkout();
        scs.banknoteInput.accept(fiftyDollarBill);

        //Check if the amount paid recorded is as expected
        Assert.assertTrue(scss.getAmountPaid().compareTo(new BigDecimal(51))==0);
        scss.finishCheckout();;
        BigDecimal changeAmount = calculateReceivedChange();
        BigDecimal expectedChangeAmount = new BigDecimal("22.45");
        //Check if the change returned is as expected
        Assert.assertTrue(changeAmount.compareTo(expectedChangeAmount)==0);
        scss.print(scss.total());
    }

    /**
     * Test the normal process of purchasing items, without adding items when doing the payment.
     * @throws EmptyException 
     * @throws AttendantBlockException 
     */
    @Test
    public void testNormalPurchase() throws OverloadException, DisabledException, InterruptedException, EmptyException, AttendantBlockException {
//        Barcode tempBarcode = new Barcode(new Numeral[] {Numeral.one});
//        Barcode tempBarcode2 = new Barcode(new Numeral[] {Numeral.two});
    	while(ifBarcodeScanned == false){ //While loop is used to ensure that the item is scanned
            scs.mainScanner.scan(milk);
        }
        ifBarcodeScanned =false;
        scs.baggingArea.add(milk);

        while(ifBarcodeScanned == false){   //While loop is used to ensure that the item is scanned
            scs.mainScanner.scan(faceMask);
        }
        ifBarcodeScanned =false;
        scs.baggingArea.add(faceMask);

        //Check if the total due calculated is as expected
        BigDecimal expectedDue = new BigDecimal("28.54");
        System.out.println(expectedDue + "and" + scss.total());
        Assert.assertTrue(scss.total().compareTo(expectedDue)==0);
        scss.checkout();
        scs.banknoteInput.accept(fiftyDollarBill);
        BigDecimal expectedAmountPaid = new BigDecimal(fiftyDollarBill.getValue());

        //Check if the amount paid recorded is as expected
        Assert.assertTrue(scss.getAmountPaid().compareTo(expectedAmountPaid)==0);
        scss.finishCheckout();

        BigDecimal changeAmount = calculateReceivedChange();
        BigDecimal expectedChangeAmount = new BigDecimal("21.45");

        //Check if the change returned is as expected
        Assert.assertTrue(changeAmount.compareTo(expectedChangeAmount)==0);
        scss.print(scss.total());
    }
}
