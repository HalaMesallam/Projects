package org.lsmr.selfcheckout.software;

import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.BanknoteStorageUnit;
import org.lsmr.selfcheckout.devices.CoinStorageUnit;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.BanknoteStorageUnitObserver;
import org.lsmr.selfcheckout.devices.observers.CoinStorageUnitObserver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StorageUnitFacade {
    private SelfCheckoutStation scs;
    private BanknoteStorageUnit bsu;
    private CoinStorageUnit csu;
    private BanknoteStorageUnitListener bsul;
    private CoinStorageUnitListener csul;

    //The set to store registered listeners to this class
    private final Set<StorageUnitFacadeListener> listeners = new HashSet<>();

    class BanknoteStorageUnitListener implements BanknoteStorageUnitObserver{

        @Override
        public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {

        }

        @Override
        public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {

        }

        @Override
        public void banknotesFull(BanknoteStorageUnit unit) {
        	notifyBanknoteFull();
        }

        @Override
        public void banknoteAdded(BanknoteStorageUnit unit) {

        }

        @Override
        public void banknotesLoaded(BanknoteStorageUnit unit) {
        	
        }

        @Override
        public void banknotesUnloaded(BanknoteStorageUnit unit) {
        	notifyBanknoteUnloaded();
        }
    }

    class CoinStorageUnitListener implements CoinStorageUnitObserver{

        @Override
        public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {

        }

        @Override
        public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {

        }

        @Override
        public void coinsFull(CoinStorageUnit unit) {
        	notifyCoinFull();
        }

        @Override
        public void coinAdded(CoinStorageUnit unit) {

        }

        @Override
        public void coinsLoaded(CoinStorageUnit unit) {

        }

        @Override
        public void coinsUnloaded(CoinStorageUnit unit) {
        	notifyCoinUnloaded();
        }
    }

    public StorageUnitFacade(SelfCheckoutStation scs){
        this.scs = scs;
        this.bsu = scs.banknoteStorage;
        this.csu = scs.coinStorage;
        this.bsul = new BanknoteStorageUnitListener();
        this.csul = new CoinStorageUnitListener();
        bsu.attach(bsul);
        csu.attach(csul);
    }

    public void attach(StorageUnitFacadeListener SUFL){
        this.listeners.add(SUFL);
    }

    public List<Banknote> emptyBanknoteStorageUnit(){
        return bsu.unload();
    }

    public List<Coin> emptyCoinStorageUnit(){
        return csu.unload();
    }

    public void notifyCoinFull(){
        for(StorageUnitFacadeListener listener: listeners){
            listener.notifyCoinFull();
        }
    }

    public void notifyBanknoteFull(){
        for(StorageUnitFacadeListener listener: listeners){
            listener.notifyBanknoteFull();
        }
    }
    
    private void notifyCoinUnloaded() {
    	for(StorageUnitFacadeListener listener: listeners){
            listener.notifyCoinUnloaded();
        }
    }
    private void notifyBanknoteUnloaded() {
    	for(StorageUnitFacadeListener listener: listeners){
            listener.notifyBanknoteUnloaded();
        }
    }
}
