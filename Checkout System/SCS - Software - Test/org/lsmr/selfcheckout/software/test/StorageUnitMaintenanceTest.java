package org.lsmr.selfcheckout.software.test;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.*;
import static org.junit.Assert.*;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.devices.BanknoteDispenser;
import org.lsmr.selfcheckout.devices.BanknoteStorageUnit;
import org.lsmr.selfcheckout.devices.CoinDispenser;
import org.lsmr.selfcheckout.devices.CoinStorageUnit;
import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.software.StorageUnitFacade;
import org.lsmr.selfcheckout.software.StorageUnitFacadeListener;
import org.lsmr.selfcheckout.software.DispenserFacade.DenominationNotExistException;

public class StorageUnitMaintenanceTest {
	
    private Currency currency = Currency.getInstance("CAD");

    int[] banknoteDenominations = { 5, 10, 20, 50, 100 };
    BigDecimal[] coinDenominations = { new BigDecimal("0.05"), new BigDecimal("0.1"), new BigDecimal("0.25"),
            new BigDecimal("1.00"), new BigDecimal("2.00") }; // not a correct coin denomination, but accounts for errors with change collecting
    int scaleMaximumWeight = 100;
    int scaleSensitivity = 1;

    private SelfCheckoutStation scs; //The self-checkout station hardware
    private StorageUnitFacade suf;
    
    @Before
    public void setup() {
    	scs = new SelfCheckoutStation(currency, banknoteDenominations, coinDenominations,scaleMaximumWeight,scaleSensitivity);
		suf = new StorageUnitFacade(scs);
    }
    
    @Test
    public void testEmptyBanknoteStorageUnit() {
    	BanknoteStorageUnit banknoteStorage = scs.banknoteStorage;
    	try {
    		while (scs.banknoteStorage.hasSpace())
    			banknoteStorage.accept(new Banknote(currency, 20));
		} catch (DisabledException e) {
			fail();
		} catch (OverloadException e) {
			fail();
		}
    	Assert.assertTrue(banknoteStorage.getCapacity() == banknoteStorage.getBanknoteCount());
    	suf.notifyBanknoteFull();
    	suf.emptyBanknoteStorageUnit();
    	Assert.assertTrue(banknoteStorage.getBanknoteCount() == 0);
    	
    }
    
    @Test
    public void testEmptyCoinStorageUnit() {
    	CoinStorageUnit coinStorage = scs.coinStorage;
    	try {
    		while (scs.coinStorage.hasSpace())
    			coinStorage.accept(new Coin(currency, new BigDecimal("0.05")));
		} catch (DisabledException e) {
			fail();
		} catch (OverloadException e) {
			fail();
		}
    	Assert.assertTrue(coinStorage.getCapacity() == coinStorage.getCoinCount());
    	suf.notifyCoinFull();
    	suf.emptyCoinStorageUnit();
    	Assert.assertTrue(coinStorage.getCoinCount() == 0);
    	
    }
    
}
