package org.lsmr.selfcheckout.software;

import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.BanknoteSlotObserver;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * This class handles the change delivery.
 */
public class DeliverChangeSoftware {
    private SelfCheckoutStation scs;
    private BSO bso;
    private BigDecimal changeAmount;

    private int currentBanknoteType;
    private int NO_BANKNOTE_TO_DELIVER = -1;
    
    public boolean deliverSuccess = true;

    Map<Integer, Integer> banknoteChange;
    Map<BigDecimal, Integer> coinChange;
    Queue<Integer> banknoteTypes; //Store the denominations of all banknotes that need to be returned to the customer

    /**
     * The observer for the output banknote slot.
     * An instance of this class is notified when a banknote is removed from the output slot by customer.
     */
    private class BSO implements BanknoteSlotObserver{
        @Override
        public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
            //ignore
        }

        @Override
        public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
            //ignore
        }

        @Override
        public void banknoteInserted(BanknoteSlot slot) {
            //ignore
        }

        @Override
        public void banknotesEjected(BanknoteSlot slot) {
            //ignore
        }

        @Override
        public void banknoteRemoved(BanknoteSlot slot) {
            deliverNextBanknote(); //Deliver the next banknote(if there is any)
        }
    }

    /**
     * Constructor.
     * @param scs The selfCheckoutStation created in the selfCheckoutStationSoftware
     */
    public DeliverChangeSoftware(SelfCheckoutStation scs){
        this.scs = scs;
        this.bso = new BSO();
        scs.banknoteOutput.attach(bso);
        currentBanknoteType = NO_BANKNOTE_TO_DELIVER;
    }

    /**
     * The logic of returning change to customer.
     * @param totalDue The total amount that the customer needs to pay.
     * @param totalAmountPaid The total amount of funds inserted by the customer.
     */
    public void deliverChange(BigDecimal totalDue, BigDecimal totalAmountPaid){
        changeAmount= totalAmountPaid.subtract(totalDue);
        
        try {
			banknoteChange = calculateBanknoteChange();
		} catch (EmptyException e1) {
			e1.printStackTrace();
			deliverSuccess = false;
			return;
		}   //Calculate how many of each denomination of banknote should be delivered
        try {
			coinChange = calculateCoinChange();
		} catch (EmptyException e1) {
			e1.printStackTrace();
			deliverSuccess = false;
			return;
		}    //Calculate how many of each denomination of coin should be delivered

        //Delivering banknotes
        banknoteTypes = new LinkedList<>(); //Store the denominations of all banknotes that need to be returned to the customer
        banknoteChange.forEach((denomination, quantity)->{
            banknoteTypes.add(denomination);
        });

        if(!banknoteTypes.isEmpty()) {
            currentBanknoteType = banknoteTypes.poll(); //Set the denomination of the first banknote to be delivered
            try {
                scs.banknoteDispensers.get(currentBanknoteType).emit(); //Deliver the first banknote
            } catch (EmptyException e) {
                e.printStackTrace();
            } catch (DisabledException e) {
                e.printStackTrace();
            } catch (OverloadException e) {
                e.printStackTrace();
            }
        }
        updateRemainingBanknote();


        //Delivering coins
        coinChange.forEach((denomination, quantity)->{  //Iterate through every denomination of coins that should be returned
            for(int i = 1; i<=quantity;i++){
                try {
                    scs.coinDispensers.get(denomination).emit();    //Emit the banknote from the dispenser to the coin tray
                } catch (OverloadException e) {
                    e.printStackTrace();
                } catch (EmptyException e) {
                    e.printStackTrace();
                } catch (DisabledException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Calculate how many banknotes of each kind should be return to the customer.
     * @return banknoteChange
     *         The map of the denomination of each kind of banknote, and the quantity that should be returned.
     * @throws EmptyException 
     */
    private Map<Integer, Integer> calculateBanknoteChange() throws EmptyException{
        Map<Integer, Integer> banknoteChange = new HashMap<>();
        for(int i = scs.banknoteDenominations.length-1; i>=0;i--){
            BigDecimal banknoteValue = new BigDecimal(scs.banknoteDenominations[i]);
            if(banknoteValue.compareTo(changeAmount)<=0) {
                BigDecimal banknoteQuantity = changeAmount.divide(banknoteValue, 0, RoundingMode.FLOOR);
            	if(scs.banknoteDispensers.get(banknoteValue.intValue()).size()<banknoteQuantity.intValue()) {
            		throw new EmptyException();
            	}
                banknoteChange.put(scs.banknoteDenominations[i], banknoteQuantity.intValue());
                BigDecimal valueOfBanknotes = banknoteValue.multiply(banknoteQuantity);
                changeAmount = changeAmount.subtract(valueOfBanknotes);
            }
        }
        return banknoteChange;
    }

    /**
     * Calculate how many coins of each kind should be returned to the customer.
     * @return coinChange
     *          The map of the denomination of each kind of coin, and the quantity that should be returned.
     * @throws EmptyException 
     */
    private Map<BigDecimal, Integer> calculateCoinChange() throws EmptyException{
        Map<BigDecimal, Integer> coinChange = new HashMap<>();
        BigDecimal[] coinDenominations = new BigDecimal[scs.coinDenominations.size()];
        coinDenominations = scs.coinDenominations.toArray(coinDenominations);
        for(int i = coinDenominations.length-1;i>=0; i--){
            if(coinDenominations[i].compareTo(changeAmount)<=0){
                BigDecimal coinQuantity = changeAmount.divide(coinDenominations[i], 0, RoundingMode.FLOOR);
            	if(scs.coinDispensers.get(coinDenominations[i]).size()<coinQuantity.intValue()) {
            		throw new EmptyException();
            	}
                coinChange.put(coinDenominations[i], coinQuantity.intValue());
                BigDecimal valueOfCoins = coinDenominations[i].multiply(coinQuantity);
                changeAmount = changeAmount.subtract(valueOfCoins);
            }
        }
        return coinChange;
    }

    /**
     * Deliver the next banknote(if there is one) when the customer removes the dangling banknote at the output slot.
     */
    private void deliverNextBanknote() {
        if(currentBanknoteType == NO_BANKNOTE_TO_DELIVER)
            return; //No banknote to deliver, return immediately
        try {
            scs.banknoteDispensers.get(currentBanknoteType).emit();
        } catch (EmptyException e) {
        	
            e.printStackTrace();
        } catch (DisabledException e) {
            e.printStackTrace();
        } catch (OverloadException e) {
            e.printStackTrace();
        }
        updateRemainingBanknote();
    }

    /**
     * Update the remaining banknote for the customer to collect.
     * Called everytime when the banknote output slot emit a banknote.
     */
    private void updateRemainingBanknote(){
    	if(currentBanknoteType!=NO_BANKNOTE_TO_DELIVER) {
	        //Update the Map of banknote denomination and quantity
	        int currentBanknoteQuantity = banknoteChange.get(currentBanknoteType)-1;
	        banknoteChange.put(currentBanknoteType, currentBanknoteQuantity);
	
	        //If already delivered all banknotes of current denomination, move on to the next one
	        if(currentBanknoteQuantity==0){
	            if(!banknoteTypes.isEmpty())
	                currentBanknoteType = banknoteTypes.poll();
	            else
	                currentBanknoteType = NO_BANKNOTE_TO_DELIVER; //Finished delivering banknotes
	        }
    	}
    }
}
