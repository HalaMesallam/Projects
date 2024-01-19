package org.lsmr.selfcheckout.software.test;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.external.ProductDatabases;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.software.ElectronicScaleSoftware;
import org.lsmr.selfcheckout.software.ItemInfo;
import org.lsmr.selfcheckout.software.PluTestDatabase;
import org.lsmr.selfcheckout.software.PriceLookupSoftware;

import GUI.ProductDatabasesSample;

/**
 *  COMPLETELY BROKEN
 *
 */
public class ProductLookupTest {
	private PriceLookupSoftware pls;
	private ProductDatabasesSample db;
	private PLUCodedProduct product;
	private ArrayList<PLUCodedProduct> list;
	private ArrayList<ItemInfo> addedItemsList;
	private ElectronicScaleSoftware essScan, essBagging;
	
	@Before
	public void setup() {
		addedItemsList = new ArrayList<ItemInfo>();
		essScan = new ElectronicScaleSoftware();
		essBagging = new ElectronicScaleSoftware();
		pls = new PriceLookupSoftware(addedItemsList, essBagging, essScan, 2000);
		db = new ProductDatabasesSample();
		
	}
	
	@Test
	public void lookupProductTest() {
		PriceLookupCode tempPLU = new PriceLookupCode("12345");
		pls.lookupProduct(tempPLU);
		
		Assert.assertTrue(addedItemsList.contains(pls.getCurrentProduct()));
		
		/*// Use to see the list
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).getDescription() + ": " + list.get(i).getPrice());
		}
		Assert.assertTrue("List should contain Rabbit at index 0", list.get(0).getDescription() == "Rabbit");
		Assert.assertTrue("List should contain Rice Bag at index 1", list.get(1).getDescription() == "Rice Bag");
	*/
	}
	
	@Test
	public void lookupProductNothingFoundTest() {
		//this test is bound to fail as the database includes a product with the letter m
		ArrayList<PLUCodedProduct> list = pls.searchDatabase("m");
		Assert.assertTrue("List should be empty", list.size() == 0);
	}
	
	@Test
	public void getProductTest() {
		PriceLookupCode tempPLU = new PriceLookupCode("12345");
		Assert.assertTrue("Should be Rice Bag", pls.getProduct(tempPLU) == db.getItem(tempPLU));
	}
	
	@Test(expected = NullPointerException.class)
	public void getProductNoEntryTest() {
		PriceLookupCode tempPLU = new PriceLookupCode("0200");
		pls.getProduct(tempPLU);
	}
	
	@Test
	public void selectProductTest() {
		PriceLookupCode tempPLU = new PriceLookupCode("12345");
		pls.selectProduct(tempPLU);
		

		Assert.assertTrue(pls.getProductSelected() == db.getItem(tempPLU));
	}
	
	@Test
	public void removeProductTest() {
		PriceLookupCode tempPLU = new PriceLookupCode("12345");
		pls.selectProduct(tempPLU);
		pls.removeProduct();
		Assert.assertEquals(pls.getProductSelected(), null);
	}
	
	@Test
	public void addThenRemoveTest() {
		
		// different implmentation
//		pls.selectProduct(new PriceLookupCode("0000"));
//		pls.selectProduct(new PriceLookupCode("0001"));
//		ArrayList<PLUCodedProduct> list = new ArrayList<PLUCodedProduct>();
//		list.add(ProductDatabases.PLU_PRODUCT_DATABASE.get(new PriceLookupCode("0001")));
//		pls.removeProduct(new PriceLookupCode("0000"));
		PriceLookupCode tempPLU = new PriceLookupCode("12345");
		pls.selectProduct(tempPLU);
		pls.removeProduct();
		Assert.assertEquals(pls.getProductSelected(), null);
	}
	
	@Test
	public void testClearSelected() {
		pls.removeProduct();
		Assert.assertEquals(pls.getProductSelected(), null);
		}
	
	@Test
	public void testClearList() {
		pls.clearList();
		Assert.assertEquals(list, null);
	}
}
