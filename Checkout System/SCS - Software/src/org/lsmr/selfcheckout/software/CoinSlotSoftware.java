package org.lsmr.selfcheckout.software;

import java.math.BigDecimal;

import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.CoinStorageUnitObserver;
import org.lsmr.selfcheckout.devices.observers.CoinValidatorObserver;

public class CoinSlotSoftware implements CoinStorageUnitObserver, CoinValidatorObserver {

    BigDecimal[] amountPaid;
    SelfCheckoutStation scs;

    public CoinSlotSoftware(SelfCheckoutStation scs, BigDecimal[] amountPaid) {
        this.amountPaid = amountPaid;
        this.scs = scs;
        scs.coinValidator.attach(this);
        scs.coinStorage.attach(this);
    }

    @Override
    public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {

        // TODO Auto-generated method stub

    }

    @Override
    public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {

        // TODO Auto-generated method stub

    }

    /*
     * A valid coin is detected therefore coin gets processed and customer can
     * continue their checkout.
     */
    @Override
    public void validCoinDetected(CoinValidator validator, BigDecimal value) {
        BigDecimal inserted = (amountPaid[0].add(valueof(value)));
        this.amountPaid[0] = inserted;
    }

    private BigDecimal valueof(BigDecimal value) {
        return value;
    }

    /*
     * An invalid coin is detected therefore customer receives back the coin and has
     * to insert a valid one before continuing their checkout.
     */
    @Override
    public void invalidCoinDetected(CoinValidator validator) {

        System.err.println("Our system has detected an invalid coin, please insert a valid coin.");

    }

    /*
     * System has detected that coin storage is full therefore, administrators need
     * to empty the station.
     */
    @Override
    public void coinsFull(CoinStorageUnit unit) {

        System.err.println("Coin storage is currently full, please empty the station.");

    }

    /*
     * System has detected a new coin addition, therefore administrators can check
     * how many there are in the storage
     */
    @Override
    public void coinAdded(CoinStorageUnit unit) {

        // TODO Auto-generated method stub

    }

    /*
     * System has detected new coin loading process, which means administrators are
     * loading coins to the storage
     */
    @Override
    public void coinsLoaded(CoinStorageUnit unit) {

        // TODO Auto-generated method stub

    }

    /*
     * System has detected a new coin unloading process, which means administrators
     * are unloading coins from to the storage
     */
    @Override
    public void coinsUnloaded(CoinStorageUnit unit) {

        // TODO Auto-generated method stub

    }
}