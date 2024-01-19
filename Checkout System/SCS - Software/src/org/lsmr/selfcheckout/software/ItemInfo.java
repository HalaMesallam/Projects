package org.lsmr.selfcheckout.software;
import java.math.BigDecimal;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.PriceLookupCode;

public class ItemInfo {
	public BigDecimal price;
	public double weight;
	public String description;
	public Barcode barcode;
	public PriceLookupCode pluCode;
	
	public ItemInfo(BigDecimal price, double weight, String description, Barcode bcode, PriceLookupCode pcode) {
		this.price = price;
		this.weight = weight;
		this.description = description;
		if (bcode != null) {
			barcode = bcode;
		}
		else if (pcode != null) {
			pluCode = pcode;
		}
	}
}
