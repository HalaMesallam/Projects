package org.lsmr.selfcheckout.software;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.external.ProductDatabases;
import org.lsmr.selfcheckout.products.PLUCodedProduct;

import GUI.ProductDatabasesSample;

public class PriceLookupSoftware {
	private Iterator<?> dbIterator;

	private ArrayList<ItemInfo> list;
	private ArrayList<PLUCodedProduct> searchList = new ArrayList<PLUCodedProduct>();
	private ArrayList<PLUCodedProduct> itemsSelected = new ArrayList<PLUCodedProduct>();;
	private ElectronicScaleSoftware essScan, essBagging;	
	private double weightThreshold;	
	private PLUCodedProduct itemSelected;
	private ItemInfo currentProduct; 



	public PriceLookupSoftware(ArrayList<ItemInfo> itemsAdded, ElectronicScaleSoftware baggingScale, ElectronicScaleSoftware scanningScale, double weight) {
		this.list = itemsAdded;
		this.essScan = scanningScale;
		this.essBagging = baggingScale;		
		this.weightThreshold = weight;
	}
	/** Method to lookup items in the database
	 * 
	 * @param letter: String to lookup and entry in the database
	 * @return ArrayList<PLUCodedProduct> of all items containing letter (Ends at index 0 in the database)
	 */

	public PLUCodedProduct lookupProduct(PriceLookupCode letter) {
		ProductDatabasesSample db = new ProductDatabasesSample();
		PLUCodedProduct info = db.getItem(letter);
		if (info != null) {
			BigDecimal newPrice = determinePLUprice(info, essScan.getCurrentWeight());
			ItemInfo newItem = new ItemInfo(newPrice, essScan.getCurrentWeight(), info.getDescription(), null, info.getPLUCode());
			this.currentProduct = newItem;
			list.add(newItem);
		}
		return info;
	}
	/*

	public ArrayList<PLUCodedProduct> lookupProduct(String letter) {

		dbIterator = ProductDatabases.PLU_PRODUCT_DATABASE.entrySet().iterator();
		while (dbIterator.hasNext()) {

            Map.Entry mapElement = (Map.Entry)dbIterator.next();
            PLUCodedProduct product = (PLUCodedProduct) mapElement.getValue();
            if(product.getDescription().contains(letter)) {
            	list.add(product);
            }
		}
		return list;

	}

	*/
	
	/**
	 *  Search the database for everything starting with a certain letter
	 */
	public ArrayList<PLUCodedProduct> searchDatabase(String letter){
		ProductDatabasesSample db = new ProductDatabasesSample();
		dbIterator = ProductDatabases.PLU_PRODUCT_DATABASE.entrySet().iterator();
		while (dbIterator.hasNext()) {
			 
            Map.Entry mapElement = (Map.Entry)dbIterator.next();
            PLUCodedProduct product = (PLUCodedProduct) mapElement.getValue();
            if(product.getDescription().contains(letter)) {
            	searchList.add(product);
            }
		}
		return searchList;
	}
	
	/** Lookup a product using the PLU (PriceLookupCode) in the database
	 * 
	 * @param code: PriceLookupCode
	 * @return PLUCodedProduct from the database
	 */
	public PLUCodedProduct getProduct(PriceLookupCode code){
		PLUCodedProduct entry = ProductDatabases.PLU_PRODUCT_DATABASE.get(code);
		if (entry != null) {
			return entry;
		} else {
			throw new NullPointerException("PLU code not in database");
		}
	}

	/**
	 *  Confirm selected item to add to the shopping cart
	 */
	public void finishSelecting() {
		double weightDiff = essScan.getCurrentWeight() - essScan.getWeightAtLastEvent();
		System.out.println(essScan.getCurrentWeight() + "how " + essScan.getWeightAtLastEvent());
		if(weightDiff <= 0) {
			System.out.println("Item not detected");
			throw new NullPointerException("No item found");
		} else {
			this.list.add(new ItemInfo(determinePLUprice(itemSelected, weightDiff), weightDiff, itemSelected.getDescription(), null, null));
		}
		//HERE I remove these since the method already handles this
//		double weight = ess.getCurrentWeight();
//		this.itemsScanned.add(new ItemInfo(determinePLUprice(itemSelected, weight), weight, itemSelected.getDescription()));
//		
	}

	/**
	 *  Returns the ItemInfo of the selected item
	 * @return ItemInfo
	 */
	public ArrayList<ItemInfo> getItemScanned(){
		return list;
	}

	/**
	 *  Get a list of all items in the database
	 * @return ArrayList<PLUCodedProducts contain all products in database
	 */
	public ArrayList<PLUCodedProduct> getAllProducts(){
		ProductDatabasesSample db = new ProductDatabasesSample();
		ArrayList<PLUCodedProduct> allProducts = new ArrayList<PLUCodedProduct>();
		dbIterator = ProductDatabases.PLU_PRODUCT_DATABASE.entrySet().iterator();
		while (dbIterator.hasNext()) { 
            Map.Entry mapElement = (Map.Entry)dbIterator.next();
            PLUCodedProduct product = (PLUCodedProduct) mapElement.getValue();
            allProducts.add(product);
		}
		return allProducts;
	}
	
	/**
	 *  Determine the price of the PLU product
	 * @param product: PLUCodedProduct
	 * @param weight: double weight detected in grams
	 */
	public BigDecimal determinePLUprice(PLUCodedProduct product, double weight) {
		BigDecimal pricePerKilo = product.getPrice();
		BigDecimal price = new BigDecimal(0);
		price = pricePerKilo.multiply(new BigDecimal(weight/1000));
		return price;
	}



	/**
	 *  Add an item from the items selected
	 *  note: can replace <PriceLookupCode code> and <getProduct(code)> with a PLUCodedItem if needed
	 * @param code The code of the item to remove
	 */
	public void selectProduct(PriceLookupCode code) {
		//itemsSelected.add(getProduct(code));
		itemSelected = getProduct(code);
	}

	/**
	 *  Get the item selected
	 * @return PLUCodedProduct
	 */
	public PLUCodedProduct getProductSelected() {
		return itemSelected;
	}
	
	public ItemInfo getCurrentProduct() {
		return this.currentProduct;
	}

	/**
	 *  Remove the selected item from the items selected
	 */
	public void removeProduct() {
		itemSelected = null;
	}



	/**
	 *  Clear the Array list
	 */
	public void clearList() {
		list.clear();
	}
	
	/**
	 * Clear selected list
	 */
	
	public void clearSearch() {
		searchList.clear();
	}
}
