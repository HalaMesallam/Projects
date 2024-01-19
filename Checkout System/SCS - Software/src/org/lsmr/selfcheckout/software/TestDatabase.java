package org.lsmr.selfcheckout.software;

import java.math.BigDecimal;
import java.util.HashMap;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Numeral;

public class TestDatabase {
	
	HashMap<Barcode, ItemInfo> db = new HashMap<Barcode, ItemInfo>();
	
	public TestDatabase(){
		this.db.put(new Barcode(new Numeral[] {Numeral.one}), new ItemInfo(new BigDecimal("1.58"), 1200, "Dairy Land 2% Milk", null, null));
		this.db.put(new Barcode(new Numeral[] {Numeral.two}), new ItemInfo(new BigDecimal("26.96"), 210, "50Pc Disposable Face Mask", null, null));
		this.db.put(new Barcode(new Numeral[] {Numeral.three}), new ItemInfo(new BigDecimal("1900.98"), 1800, "EVGA GeForce RTX 3080 Ti FTW3", null, null));
		this.db.put(new Barcode(new Numeral[] {Numeral.four}), new ItemInfo(new BigDecimal("1.00"), -1, "Negative Weight", null, null));				//Negative weight
		this.db.put(new Barcode(new Numeral[] {Numeral.five}), new ItemInfo(new BigDecimal("-23.00"), 123, "Negative Price", null, null));				//Negative money
		this.db.put(new Barcode(new Numeral[] {Numeral.six}), new ItemInfo(new BigDecimal("0.00000"), 0, "Nil", null, null));							//Nil
		this.db.put(new Barcode(new Numeral[] {Numeral.seven}), new ItemInfo(new BigDecimal("3.21"), 12, "", null, null));								//Empty String
		this.db.put(new Barcode(new Numeral[] {Numeral.eight}), new ItemInfo(new BigDecimal("19.98"), 1800, "This is a super long line. This is a super long line. This is a super long line", null, null));
		System.out.println("Database Created!");
	}
	
	public HashMap<Barcode, ItemInfo> getDatabase(){
		return db;
	}
	
	public ItemInfo lookupBarcode(Barcode barcode) {
		return db.get(barcode);
	}
}
