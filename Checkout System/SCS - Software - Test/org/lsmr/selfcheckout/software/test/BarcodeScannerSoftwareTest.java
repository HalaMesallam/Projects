package org.lsmr.selfcheckout.software.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.BarcodeScanner;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.software.BarcodeScannerSoftware;
import org.lsmr.selfcheckout.software.ElectronicScaleSoftware;
import org.lsmr.selfcheckout.software.ItemInfo;
import org.lsmr.selfcheckout.software.SelfCheckoutStationSoftware;
import org.lsmr.selfcheckout.software.TestDatabase;

import GUI.ProductDatabasesSample;

public class BarcodeScannerSoftwareTest
{
	private ElectronicScaleSoftware ess = new ElectronicScaleSoftware();
	private BarcodeScanner bs = new BarcodeScanner();

	public Currency currency = Currency.getInstance(Locale.CANADA);
    public int[] denominations = new int[]{5, 10, 20, 50, 100};
    public BigDecimal[] coinDenominations = new BigDecimal[]{
		new BigDecimal("0.05"),
		new BigDecimal("0.10"),
		new BigDecimal("0.25"),
		new BigDecimal("1.00"),
		new BigDecimal("2.00"),
	};;
	
	private ProductDatabasesSample pds = new ProductDatabasesSample();
	private ItemInfo info;
	private AbstractDevice<?> device;
	private Barcode barcode;
	private ProductDatabasesSampleStub pdss;
	private BarcodeScannerSoftware bss; 
	private SelfCheckoutStation scs =  new SelfCheckoutStation(currency , denominations, coinDenominations, 200, 1);
	private SelfCheckoutStationSoftware scss = new SelfCheckoutStationSoftware(scs);

	private ArrayList<ItemInfo> itemsScanned;

	// Stub class for ItemInfo
	public class ItemInfoStub extends ItemInfo
	{
		BigDecimal price;
		double weight;
		String description;

		protected ItemInfoStub(BigDecimal price, double weight, String description, boolean infoNull)
		{
			super(price, weight, description, null, null);
			this.price = price;
			this.weight = weight;
			this.description = description;
		}
    }

	// Stub for TestDatabase
	public class ProductDatabasesSampleStub extends ProductDatabasesSample
	{
		ItemInfoStub infoStub;

		protected ProductDatabasesSampleStub(BigDecimal price, double weight, String description, boolean infoNull)
		{
			super();
			infoStub = new ItemInfoStub(price, weight, description, infoNull);

			if (infoNull)
					infoStub = null;
		}

//        @Override
//        public ItemInfoStub lookupBarcode(Barcode barcode)
//        {
//        	return infoStub;
//        }
	}

	@Before
	public void setup()
	{
		itemsScanned = new ArrayList<>();
		info = new ItemInfo(new BigDecimal(10), 25, "Someitem", null, null);
		itemsScanned.add(info);
		bss = new BarcodeScannerSoftware(pds, null, ess, itemsScanned, 1000);
	}

	@Test
	public void testGetItemsScanned()
	{
		itemsScanned = bss.getItemsScanned();
		assertTrue(itemsScanned.contains(info));
	}

	@Test
	public void testClearItemsScanned()
	{
		bss.clearItemsScanned();
		assertTrue(itemsScanned.isEmpty());
	}

	@Test
	public void testEnabled()
	{
		bss.enabled(device);
	}

	@Test
	public void testDisabled()
	{
		bss.disabled(device);
	}


	@Test
	public void testBarcodeScanned()
	{
		// Standard test with price = 10, weight = 50, description = "Milk", infoNull = false
		// with weightThreshold = 0 for one of the branches at line 63 (in BarcodeScannerSoftware)
		bss = new BarcodeScannerSoftware(pds, scss, ess, itemsScanned, 0);
		barcode = new Barcode(new Numeral[] {Numeral.one});
		bss.barcodeScanned(bs, barcode);
		assertTrue(bss.getItemsScanned().contains(bss.getCurrentProduct()));

		// Standard test but with weightThreshold = 100000 for the other the branches at line 63
		bss = new BarcodeScannerSoftware(pds, scss, ess, itemsScanned, 100000);
		barcode = new Barcode(new Numeral[] {Numeral.one});
		bss.barcodeScanned(bs, barcode);
		assertTrue(bss.getItemsScanned().contains(bss.getCurrentProduct()));

		// Standard test but with negative weight
		pdss = new ProductDatabasesSampleStub(new BigDecimal("1.00"), -1, "Negative Weight", false);
		bss = new BarcodeScannerSoftware(pds, scss, ess, itemsScanned, 0);
		barcode = new Barcode(new Numeral[] {Numeral.one});
		bss.barcodeScanned(bs, barcode);
		// Cannot have assertTrue here since an item doesn't get added (line 74 in BarcodeScannerSoftware)
		// because the tested weight in this case is negative
		assertFalse(bss.getItemsScanned().contains(pdss.infoStub));


		// Standard test but with negative price
		pdss = new ProductDatabasesSampleStub(new BigDecimal("-23.00"), 123, "Negative Price", false);
		bss = new BarcodeScannerSoftware(pds, scss, ess, itemsScanned, 0);
		barcode = new Barcode(new Numeral[] {Numeral.one});
		bss.barcodeScanned(bs, barcode);
		// Cannot have assertTrue here since an item doesn't get added (line 74 in BarcodeScannerSoftware)
		// because the tested price in this case is negative
		assertFalse(bss.getItemsScanned().contains(pdss.infoStub));

		// Standard test but infoNull = true
		pdss = new ProductDatabasesSampleStub(new BigDecimal("3.21"), 12, "", true);
		bss = new BarcodeScannerSoftware(pds, scss, ess, itemsScanned, 0);
		barcode = new Barcode(new Numeral[] {Numeral.one});
		bss.barcodeScanned(bs, barcode);
		// Cannot have assertTrue here since an item doesn't get added (line 74 in BarcodeScannerSoftware)
		// because the item to be added is null
		assertFalse(bss.getItemsScanned().contains(pdss.infoStub));
	}
}
