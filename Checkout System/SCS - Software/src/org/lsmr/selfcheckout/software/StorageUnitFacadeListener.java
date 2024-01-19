package org.lsmr.selfcheckout.software;

public interface StorageUnitFacadeListener {

    void notifyBanknoteFull();

    void notifyCoinFull();
    
    void notifyBanknoteUnloaded();
    
    void notifyCoinUnloaded();
}
