package org.lsmr.selfcheckout.software;

import java.math.BigDecimal;
import java.util.Currency;

import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.BanknoteStorageUnitObserver;
import org.lsmr.selfcheckout.devices.observers.BanknoteValidatorObserver;

public class BanknoteSlotSoftware implements BanknoteStorageUnitObserver, BanknoteValidatorObserver{

	BigDecimal[] amountPaid;
	SelfCheckoutStation scs;

	public BanknoteSlotSoftware(SelfCheckoutStation scs, BigDecimal[] amountPaid) {
		this.amountPaid = amountPaid;
		this.scs = scs;
		scs.banknoteValidator.attach(this);
		scs.banknoteStorage.attach(this);
	}

	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void banknotesFull(BanknoteStorageUnit unit) {
		System.err.println("Storage is full, please do not use station.");
	}

	@Override
	public void banknoteAdded(BanknoteStorageUnit unit) {
		//If the admin wants to check how many banknotes are in the storage.

	}

	@Override
	public void banknotesLoaded(BanknoteStorageUnit unit) {
		// Admin loads storage with banknotes

	}

	@Override
	public void banknotesUnloaded(BanknoteStorageUnit unit) {
		// Admin unloads all banknotes
	}

	@Override
	public void validBanknoteDetected(BanknoteValidator validator, Currency currency, int value) {
		BigDecimal inserted = (amountPaid[0].add(BigDecimal.valueOf(value)));
		this.amountPaid[0] = inserted;
	}

	@Override
	public void invalidBanknoteDetected(BanknoteValidator validator) {
		System.err.println("InvalidBanknoteDetected! Banknote is returning.");
	}
}
