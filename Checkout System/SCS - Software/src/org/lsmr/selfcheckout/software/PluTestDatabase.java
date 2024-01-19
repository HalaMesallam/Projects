package org.lsmr.selfcheckout.software;

import java.math.BigDecimal;

import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.external.ProductDatabases;
import org.lsmr.selfcheckout.products.PLUCodedProduct;


public class PluTestDatabase {

	/**
	 *  Test Database for PLU products
	 */
	public PluTestDatabase() {
		ProductDatabases.PLU_PRODUCT_DATABASE.put(new PriceLookupCode("0000"), new PLUCodedProduct(new PriceLookupCode("0000"), "Rice Bag", new BigDecimal ("6.49")));
		ProductDatabases.PLU_PRODUCT_DATABASE.put(new PriceLookupCode("0001"), new PLUCodedProduct(new PriceLookupCode("0001"), "Milk", new BigDecimal ("5.17")));
		ProductDatabases.PLU_PRODUCT_DATABASE.put(new PriceLookupCode("0005"), new PLUCodedProduct(new PriceLookupCode("0005"), "Chocolate Cookies", new BigDecimal ("7.24")));
		ProductDatabases.PLU_PRODUCT_DATABASE.put(new PriceLookupCode("0243"), new PLUCodedProduct(new PriceLookupCode("0243"), "Rabbit", new BigDecimal ("6.32")));
		ProductDatabases.PLU_PRODUCT_DATABASE.put(new PriceLookupCode("1632"), new PLUCodedProduct(new PriceLookupCode("1632"), "Ice", new BigDecimal ("3.99")));
		ProductDatabases.PLU_PRODUCT_DATABASE.put(new PriceLookupCode("5124"), new PLUCodedProduct(new PriceLookupCode("5124"), "Soy Sauce", new BigDecimal ("1.97")));
		ProductDatabases.PLU_PRODUCT_DATABASE.put(new PriceLookupCode("7332"), new PLUCodedProduct(new PriceLookupCode("7332"), "Head of Lettuce", new BigDecimal ("1.47")));
		
	}
	
	public void addEntry(PLUCodedProduct entry) {
		ProductDatabases.PLU_PRODUCT_DATABASE.put(entry.getPLUCode(), entry);
	}

}
