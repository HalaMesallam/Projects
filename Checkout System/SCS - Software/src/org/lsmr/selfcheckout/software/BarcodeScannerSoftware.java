package org.lsmr.selfcheckout.software;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.BarcodeScanner;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.BarcodeScannerObserver;
import org.lsmr.selfcheckout.products.BarcodedProduct;

import GUI.ProductDatabasesSample;

public class BarcodeScannerSoftware implements BarcodeScannerObserver{
	
	//settings
	private double weightThreshold;			//how much weight and item can differ by
	
	//information
	public ProductDatabasesSample db;
	private ArrayList<ItemInfo> itemsScanned;
	private ItemInfo currentProduct; 
	
	//other software
	private SelfCheckoutStationSoftware scss;
	private ElectronicScaleSoftware ess;
	
	// expected weight
	private double expectedWeight;
	
	public BarcodeScannerSoftware(ProductDatabasesSample db, SelfCheckoutStationSoftware scs, ElectronicScaleSoftware ess, 
			ArrayList<ItemInfo> itemsScanned, double weightThreshold) {
		this.scss = scs;
		this.db = db;
		this.ess = ess;
		this.itemsScanned = itemsScanned;
		this.weightThreshold = weightThreshold;
	}

	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		//We should be able to ignore this for now
		
	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		//We should be able to ignore this for now
		
	}

	@Override
	public void barcodeScanned(BarcodeScanner barcodeScanner, Barcode barcode) {
		BarcodedProduct product = db.getItem(barcode);	
		if (product == null) {
			System.out.println("Barcode not scanned properly. Method is now returning");
			return;
		}
		
		//Check if the info.price < 0 (invalid price)
		if (product.getPrice().compareTo(new BigDecimal(0)) < 0) {
			System.err.println("Negative Price Scanned!");
			return;			
		}
		
		//Check if the weight is < 0
		if (product.getExpectedWeight() > 0) {
			double weightDiff = ess.getCurrentWeight() - ess.getWeightAtLastEvent();
			if (Math.abs(weightDiff - product.getExpectedWeight()) > weightThreshold) {
				System.out.println("Please wait for an attendant");
				//Notify attendant
				//disable the touch screen
			}
		}else{
			System.err.println("Negative weight from Database!");
			return;
		}
		
		ItemInfo info = new ItemInfo(product.getPrice(), product.getExpectedWeight(), product.getDescription(), barcode, null);
		this.currentProduct = info;
		this.itemsScanned.add(info);				//Add item to the list of scanned item, price too	
		expectedWeight = product.getExpectedWeight();
		scss.addedItem();
	}
	
	public ArrayList<ItemInfo> getItemsScanned(){
		return itemsScanned;
	}
	
	public void clearItemsScanned() {
		this.itemsScanned.clear();
	}
	
	public ItemInfo getCurrentProduct() {
		return this.currentProduct;
	}
	
	public double getExpectedWeight() {
		return expectedWeight;
	}
}
