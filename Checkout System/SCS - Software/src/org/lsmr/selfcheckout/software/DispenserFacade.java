package org.lsmr.selfcheckout.software;

import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.SimulationException;
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.BanknoteDispenserObserver;
import org.lsmr.selfcheckout.devices.observers.CoinDispenserObserver;
import org.lsmr.selfcheckout.software.DispenserFacade.DenominationNotExistException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class handles the following use-cases related to the coin and banknote storage units & dispensers:
 * -Attendant empties the coin storage unit
 * -Attendant empties the banknote storage unit
 * -Attendant refills the coin dispenser
 * -Attendant refills the banknote dispenser
 */
public class DispenserFacade {
    private SelfCheckoutStation scs;
    private Map<BigDecimal, CoinDispenser> coinDispensers;
    private Map<Integer,BanknoteDispenser> banknoteDispensers;

    //Canadian dollar is the valid currency
    private Currency currency = Currency.getInstance("CAD");

    //The set to store registered listeners to this class
    private final Set<DispenserFacadeListener> listeners = new HashSet<>();
    
    //The set of coins to be loaded
    private Set<Coin> coins = new HashSet<>();
    
    public class DenominationNotExistException extends Exception{
    	public DenominationNotExistException(String s)
        {
            // Call constructor of parent Exception
            super(s);
        }
    }



    class BanknoteDispenserListener implements BanknoteDispenserObserver{
        private int denomination;

        public BanknoteDispenserListener(int denomination){
            this.denomination = denomination;
        }

        @Override
        public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {

        }

        @Override
        public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {

        }

        @Override
        public void moneyFull(BanknoteDispenser dispenser) {

        }

        @Override
        public void banknotesEmpty(BanknoteDispenser dispenser) {
            notifyBanknoteDispenserEmpty(denomination);
        }

        @Override
        public void billAdded(BanknoteDispenser dispenser, Banknote banknote) {

        }

        @Override
        public void banknoteRemoved(BanknoteDispenser dispenser, Banknote banknote) {

        }

        @Override
        public void banknotesLoaded(BanknoteDispenser dispenser, Banknote... banknotes) {

        }

        @Override
        public void banknotesUnloaded(BanknoteDispenser dispenser, Banknote... banknotes) {
        	notifyBanknoteDispenserEmpty(denomination);
        }
    }
    class CoinDispenserListener implements CoinDispenserObserver {
        private BigDecimal denomination;

        public CoinDispenserListener(BigDecimal denomination){
            this.denomination = denomination;
        }

        @Override
        public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {

        }

        @Override
        public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {

        }

        @Override
        public void coinsFull(CoinDispenser dispenser) {

        }

        @Override
        public void coinsEmpty(CoinDispenser dispenser) {
            notifyCoinDispenserEmpty(denomination);
        }

        @Override
        public void coinAdded(CoinDispenser dispenser, Coin coin) {

        }

        @Override
        public void coinRemoved(CoinDispenser dispenser, Coin coin) {

        }

        @Override
        public void coinsLoaded(CoinDispenser dispenser, Coin... coins) {

        }

        @Override
        public void coinsUnloaded(CoinDispenser dispenser, Coin... coins) {
        	notifyCoinDispenserEmpty(denomination);
        }
    }

    public DispenserFacade(SelfCheckoutStation scs){
        this.scs= scs;
        this.coinDispensers = scs.coinDispensers;
        this.banknoteDispensers = scs.banknoteDispensers;
        banknoteDispensers.forEach((denomination, dispenser)->{
            dispenser.attach(new BanknoteDispenserListener(denomination));
        });
        coinDispensers.forEach((denomination, dispenser)->{
            dispenser.attach(new CoinDispenserListener(denomination));
        });
    }

    public void attach(DispenserFacadeListener dfl) {
        listeners.add(dfl);
    }

    public void refillCoin(BigDecimal denomination) throws DenominationNotExistException{
    	List<BigDecimal> coinDenominations = scs.coinDenominations;
    	if(!coinDenominations.contains(denomination)) {
    		//PrintErrorMessage
    		throw new DenominationNotExistException("Denomination not exist.");
    	}
        CoinDispenser dispenser = coinDispensers.get(denomination);
        int currentQuantity = dispenser.size();
        int capacity = dispenser.getCapacity();
        int quantityToAdd = capacity-currentQuantity;
        Coin[] coinsToAdd = new Coin[quantityToAdd];
        for(int i =0; i<quantityToAdd;i++) {
        	Coin newCoin = new Coin(currency,denomination);
        	coinsToAdd[i] = newCoin;
        }
        try {
			dispenser.load(coinsToAdd);
		} catch (SimulationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OverloadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//Should not happen
		}
    };

    public void refillBanknote(int denomination) throws DenominationNotExistException{
    	int[] banknoteDenominations = scs.banknoteDenominations;
    	boolean denominationExistFlag = false;
    	for(int i=0; i< banknoteDenominations.length;i++) {
    		if(banknoteDenominations[i] == denomination) {
    			denominationExistFlag = true;
    			continue;
    		}
    	}
    	if(!denominationExistFlag) {
    		//PrintErrorMessage
    		throw new DenominationNotExistException("Denomination not exist.");
    	}
        BanknoteDispenser dispenser = banknoteDispensers.get(denomination);
        int currentQuantity = dispenser.size();
        int capacity = dispenser.getCapacity();
        int quantityToAdd = capacity-currentQuantity;
        Banknote[] banknotesToAdd = new Banknote[quantityToAdd];
        for(int i =0; i<quantityToAdd;i++) {
        	Banknote newBanknote = new Banknote(currency, denomination);
        	banknotesToAdd[i] = newBanknote;
        }
        try {
			dispenser.load(banknotesToAdd);
		} catch (OverloadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//should not happen
		}
    }

    private void notifyBanknoteDispenserEmpty(int denomination){
        for(DispenserFacadeListener listener: listeners){
            listener.notifyBanknoteDispenserEmpty(denomination);
        }
    }

    private void notifyCoinDispenserEmpty(BigDecimal denomination){
        for(DispenserFacadeListener listener: listeners){
            listener.notifyCoinDispenserEmpty(denomination);
        }
    }
}
