package org.lsmr.selfcheckout.software.test;

import java.math.BigDecimal;
import java.util.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.software.CoinSlotSoftware;

public class CoinSlotSoftwareTest {

    private BigDecimal[] amountPaid;
    private AbstractDevice<?> device;
    private CoinSlotSoftware css;
    private SelfCheckoutStation scs;
    private Currency cur = Currency.getInstance(Locale.CANADA);
    private CoinValidator cv;
    private CoinStorageUnit unit;

    //Data needed for instantiate the self-checkout station
    private final Currency currency = Currency.getInstance(Locale.CANADA);
    int[] banknoteDenominations = { 5, 10, 20, 50, 100 };
    BigDecimal[] coinDenominations = { new BigDecimal("0.05"), new BigDecimal("0.1"), new BigDecimal("0.25"),
            new BigDecimal("1"), new BigDecimal("2") };
    int scaleMaximumWeight = 100;
    int scaleSensitivity = 1;

    @Before
    public void setup() {
        amountPaid = new BigDecimal[1];
        amountPaid[0] = BigDecimal.ZERO;
        scs = new SelfCheckoutStation(currency, banknoteDenominations, coinDenominations,scaleMaximumWeight,scaleSensitivity);
        css = new CoinSlotSoftware(scs, amountPaid);
        cv = new CoinValidator(cur, Arrays.asList(coinDenominations));
    }

    @Test
    public void testEnabled(){
        css.enabled(device);
    }

    @Test
    public void testDisabled(){
        css.disabled(device);
    }

    @Test
    public void testValidCoinDetected() {
        amountPaid[0] = new BigDecimal("0");
        BigDecimal value = new BigDecimal("0.5");
        css.validCoinDetected(null, value);
        Assert.assertEquals("Amount Paid should be" + value, 0, amountPaid[0].compareTo(value));
        amountPaid[0] = new BigDecimal("0");
    }

    @Test
    public void testInvalidCoinDetected() {
        css.invalidCoinDetected(cv);
    }

    @Test
    public void testCoinAdded() {
        css.coinAdded(scs.coinStorage);
    }

    @Test
    public void testCoinsFull() {
        css.coinsFull(unit);
    }

    @Test
    public void testCoinAddedStorage() {
        css.coinAdded(unit);
    }

    @Test
    public void testCoinsLoaded() {
        css.coinsLoaded(unit);
    }

    @Test
    public void testCoinsUnloaded() {
        css.coinsUnloaded(unit);
    }

    @Test
    public void testCoinsFullDispenser() {
        css.coinsFull(scs.coinStorage);
    }

    @Test
    public void testCoinAddedDispenserCoin() {
        css.coinAdded(scs.coinStorage);
    }


    @Test
    public void testCoinsLoadedDispenser() {
        css.coinsLoaded(scs.coinStorage);
    }

    @Test
    public void testCoinsUnloadedDispenser() {
        css.coinsUnloaded(scs.coinStorage);
    }

}