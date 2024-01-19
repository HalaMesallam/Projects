package GUI;

import java.math.BigDecimal;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.external.ProductDatabases;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;

public class ProductDatabasesSample {
	private ProductDatabases productDatabase;
	
	public ProductDatabasesSample() {
		setupPLUDatabase();	
		setupBarcodedDatabase();
		}
	
	public void setupPLUDatabase() {
		PLUCodedProduct product1 = new PLUCodedProduct(new PriceLookupCode("12345"), "Lettuce", new BigDecimal(3.99));
		PLUCodedProduct product2 = new PLUCodedProduct(new PriceLookupCode("2468"), "Bulk All Purpose Flour", new BigDecimal(0.89));
		PLUCodedProduct product3 = new PLUCodedProduct(new PriceLookupCode("11111"), "Bulk Assorted Nuts", new BigDecimal(1.89));
		PLUCodedProduct product4 = new PLUCodedProduct(new PriceLookupCode("7849"), "Red Grapes", new BigDecimal(5.29));
		productDatabase.PLU_PRODUCT_DATABASE.put(product1.getPLUCode(), product1);
		productDatabase.PLU_PRODUCT_DATABASE.put(product2.getPLUCode(), product2);
		productDatabase.PLU_PRODUCT_DATABASE.put(product3.getPLUCode(), product3);
		productDatabase.PLU_PRODUCT_DATABASE.put(product4.getPLUCode(), product4);
	}
	
	public void setupBarcodedDatabase() {
		BarcodedProduct product1 = new BarcodedProduct(new Barcode(new Numeral[] {Numeral.one}), "DairyLand 2% Milk", new BigDecimal(5.99), 1200);
		BarcodedProduct product2 = new BarcodedProduct(new Barcode(new Numeral[] {Numeral.two}), "50Pc Disposable Face Mask", new BigDecimal(26.96), 210);
		BarcodedProduct product3 = new BarcodedProduct(new Barcode(new Numeral[] {Numeral.three}), "EVGA GeForce RTX 3080 Ti FTW3", new BigDecimal(1900.98), 1800);
		BarcodedProduct product4 = new BarcodedProduct(new Barcode(new Numeral[] {Numeral.four}), "CrackerBarrel block of cheese", new BigDecimal(9.79), 500);
		productDatabase.BARCODED_PRODUCT_DATABASE.put(product1.getBarcode(), product1);
		productDatabase.BARCODED_PRODUCT_DATABASE.put(product2.getBarcode(), product2);
		productDatabase.BARCODED_PRODUCT_DATABASE.put(product3.getBarcode(), product3);
		productDatabase.BARCODED_PRODUCT_DATABASE.put(product4.getBarcode(), product4);
	}
	
	public void setupInventory() {
		// Requires further implementation
	}
	
	public BarcodedProduct getItem(Barcode product) {
		return productDatabase.BARCODED_PRODUCT_DATABASE.get(product);
	}
	
	public PLUCodedProduct getItem(PriceLookupCode product) {
		return productDatabase.PLU_PRODUCT_DATABASE.get(product);
	}
}
