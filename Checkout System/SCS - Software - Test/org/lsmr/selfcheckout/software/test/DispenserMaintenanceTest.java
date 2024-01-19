package org.lsmr.selfcheckout.software.test;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.devices.BanknoteDispenser;
import org.lsmr.selfcheckout.devices.CoinDispenser;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.software.DispenserFacade;
import org.lsmr.selfcheckout.software.DispenserFacade.DenominationNotExistException;
import org.lsmr.selfcheckout.software.SelfCheckoutStationSoftware;

public class DispenserMaintenanceTest {
	//Canadian dollar is the valid currency
    private Currency currency = Currency.getInstance("CAD");

    int[] banknoteDenominations = { 5, 10, 20, 50, 100 };
    BigDecimal[] coinDenominations = { new BigDecimal("0.05"), new BigDecimal("0.1"), new BigDecimal("0.25"),
            new BigDecimal("1.00"), new BigDecimal("2.00") }; // not a correct coin denomination, but accounts for errors with change collecting
    int scaleMaximumWeight = 100;
    int scaleSensitivity = 1;

    private SelfCheckoutStation scs; //The self-checkout station hardware
    private DispenserFacade df;
    
    @Before
    public void setup() {
    	scs = new SelfCheckoutStation(currency, banknoteDenominations, coinDenominations,scaleMaximumWeight,scaleSensitivity);
		df = new DispenserFacade(scs);
    }
    
    @Test
    public void testRefillBanknoteDispenser() throws DenominationNotExistException {
    	int denominationToTest = 5;
    	BanknoteDispenser dispenser = scs.banknoteDispensers.get(denominationToTest);
    	Assert.assertTrue(dispenser.size()==0);
    	df.refillBanknote(denominationToTest);
    	Assert.assertTrue(dispenser.size() == dispenser.getCapacity());
    }
    
    @Test
    public void testRefillCoinDispenser() throws DenominationNotExistException {
    	BigDecimal denominationToTest = new BigDecimal("0.05");
    	CoinDispenser dispenser = scs.coinDispensers.get(denominationToTest);
    	Assert.assertTrue(dispenser.size()==0);
    	df.refillCoin(denominationToTest);
    	Assert.assertTrue(dispenser.size() == dispenser.getCapacity());
    }
    
    @Test
    public void testRefillBanknoteWhenFull() throws DenominationNotExistException {
    	int denominationToTest = 10;
    	BanknoteDispenser dispenser = scs.banknoteDispensers.get(denominationToTest);
    	Assert.assertTrue(dispenser.size()==0);
    	df.refillBanknote(denominationToTest);
    	Assert.assertTrue(dispenser.size() == dispenser.getCapacity());
    	df.refillBanknote(denominationToTest);
    	Assert.assertTrue(dispenser.size() == dispenser.getCapacity());
    }
    
    @Test
    public void testRefillCoinWhenFull() throws DenominationNotExistException {
    	BigDecimal denominationToTest = new BigDecimal("0.1");
    	CoinDispenser dispenser = scs.coinDispensers.get(denominationToTest);
    	Assert.assertTrue(dispenser.size()==0);
    	df.refillCoin(denominationToTest);
    	Assert.assertTrue(dispenser.size() == dispenser.getCapacity());
    	df.refillCoin(denominationToTest);
    	Assert.assertTrue(dispenser.size() == dispenser.getCapacity());
    }
    
    @Test(expected = DenominationNotExistException.class)
    public void testRefillBanknoteWithInvalidDenomination() throws DenominationNotExistException {
    	int denominationToTest = 20;
    	int invalidDenomination = 19;
    	BanknoteDispenser dispenser = scs.banknoteDispensers.get(denominationToTest);
    	Assert.assertTrue(dispenser.size()==0);
    	df.refillBanknote(invalidDenomination);
    }
    
    @Test(expected = DenominationNotExistException.class)
    public void testRefillCoinWithInvalidDenomination() throws DenominationNotExistException {
    	BigDecimal denominationToTest = new BigDecimal("0.25");
    	BigDecimal invalidDenomination = new BigDecimal("0.24");
    	CoinDispenser dispenser = scs.coinDispensers.get(denominationToTest);
    	Assert.assertTrue(dispenser.size()==0);
    	df.refillCoin(invalidDenomination);
    }
}
