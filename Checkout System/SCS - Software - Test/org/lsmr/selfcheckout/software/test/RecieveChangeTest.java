package org.lsmr.selfcheckout.software.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.SimulationException;
import org.lsmr.selfcheckout.software.BankDatabase;
import org.lsmr.selfcheckout.software.DeliverChangeSoftware;

@RunWith(JUnit4.class)
public class RecieveChangeTest {
/*
	Black-Box test cases:
	- Deliver one coin
	- Deliver multiple coins
	- Deliver one banknote
	- Deliver multiple banknotes
	- Not enough coins for full change
	- Not enought banknotes for full change
	
 */
	int[] bills = new int[] {5,10,20,50,100};
	BigDecimal[] coins = new BigDecimal[] {new BigDecimal(0.05), new BigDecimal(0.10), new BigDecimal(0.25), new BigDecimal(1.00), new BigDecimal(2.00)};
	
	BankDatabase db = new BankDatabase();
	BigDecimal[] amountPaid = new BigDecimal[1];
	
	SelfCheckoutStation scs; 
	DeliverChangeSoftware dc;
	
	
	@Before
	public void setup() {
		scs = new SelfCheckoutStation(Currency.getInstance("CAD"), bills, coins, 1, 1);
		amountPaid[0] = new BigDecimal(0);
		dc = new DeliverChangeSoftware(scs);
		Coin.DEFAULT_CURRENCY = Currency.getInstance("CAD");
	}
	
	/*
	 * Simulating the machine delivering one coin
	 * */
	@Test 
	public void deliverChangeCoin() throws SimulationException, OverloadException{
		scs.coinDispensers.get(new BigDecimal(2)).load(new Coin(new BigDecimal(2)));
		dc.deliverChange(new BigDecimal(0), new BigDecimal(2));
		
		List<Coin> coins = scs.coinTray.collectCoins();
		Assert.assertEquals(coins.get(0).getValue().intValue(), 2);
	}
	
	/*
	 * Simulating the machine delivering multiple $2 coins 
	 * when it needs to return $4 in change
	 * */
	@Test 
	public void deliverChangeCoins() throws SimulationException, OverloadException{
		scs.coinDispensers.get(new BigDecimal(2)).load(new Coin(coins[4]),new Coin(coins[4]),new Coin(coins[4]));
		dc.deliverChange(new BigDecimal(0), new BigDecimal(4));
		
		List<Coin> coins = scs.coinTray.collectCoins();
		BigDecimal currAmount = new BigDecimal(0);
		for(Coin c: coins) {
			if(c!=null) {
				currAmount = currAmount.add(c.getValue());
			}
		}
		
		Assert.assertEquals(new BigDecimal(4).byteValue(), currAmount.byteValue());
	}
	
	/*
	 * Simulating the machine when it's deliverying change
	 * and no change is available
	 * */
	@Test
	public void deliverChangeEmptyMachine() {
		dc.deliverChange(new BigDecimal(20), new BigDecimal(26));
		Assert.assertTrue(dc.deliverSuccess == false);
	}
	
	/*
	 * Simulating the machine delivering change for one banknote
	 * */
	@Test
	public void deliverChangeBanknote() throws SimulationException, OverloadException {
		scs.banknoteDispensers.get(5).load(new Banknote(Coin.DEFAULT_CURRENCY,5));
		dc.deliverChange(new BigDecimal(0), new BigDecimal(5));
		List<Banknote> bn = new ArrayList<>();
		List<Banknote> bankNotes = new ArrayList<Banknote>();
		while(!scs.banknoteOutput.hasSpace()) {
			Banknote[] arr = scs.banknoteOutput.removeDanglingBanknotes();
			for(Banknote b: arr) {
				bankNotes.add(b);
			}
		}
		
		if(bankNotes.size()>1)
			Assert.fail();
		
		Assert.assertEquals(bankNotes.get(0).getValue(),5);
	}
	
	/*
	 * Simulating the machine delivering coins when insufficient banknotes are available
	 * ex ($6 change can return either (one $5 banknote and one $1 coin) 
	 * */
	// SCS return error
	@Test 
	public void deliverChangeNotEnoughBanknotes() throws SimulationException, OverloadException {
		BigDecimal twoDol = new BigDecimal(2);
		scs.coinDispensers.get(twoDol).load(new Coin(twoDol),new Coin(twoDol),new Coin(twoDol));
		dc.deliverChange(new BigDecimal(6), new BigDecimal(12));
		
		if(!scs.banknoteOutput.hasSpace())
			Assert.fail();
		
		Assert.assertTrue(dc.deliverSuccess == false);
		
	}
	
	/*
	 * Simulating the case when there are not enough coins to finish
	 * the transaction ( $6 change needs to be delivered but the checkout system
	 * has only $5 bill)
	 * */
	@Test 
	public void deliverChangeNotEnoughCoins() throws SimulationException, OverloadException {
		scs.banknoteDispensers.get(5).load(new Banknote(Coin.DEFAULT_CURRENCY,5));
		dc.deliverChange(new BigDecimal(0), new BigDecimal(6));
		List<Banknote> bankNotes = new ArrayList<>();
		while(!scs.banknoteOutput.hasSpace()) {
			Banknote[] arr = scs.banknoteOutput.removeDanglingBanknotes();
			for(Banknote b: arr) {
				bankNotes.add(b);
			}
		}
		
		Assert.assertTrue(dc.deliverSuccess == false);
	}
	
	/*
	 * Simulating the case when the machine delivers multiple banknotes as change
	 * */
	@Test
	public void deliverChangeMultipleBanknotes() throws SimulationException, OverloadException {
		scs.banknoteDispensers.get(5).load(new Banknote(Coin.DEFAULT_CURRENCY,5));
		scs.banknoteDispensers.get(10).load(new Banknote(Coin.DEFAULT_CURRENCY,10));
		scs.banknoteDispensers.get(20).load(new Banknote(Coin.DEFAULT_CURRENCY,20));
		
		dc.deliverChange(new BigDecimal(0), new BigDecimal(35));
		List<Banknote> bankNotes = new ArrayList<>();
		while(!scs.banknoteOutput.hasSpace()) {
			Banknote[] arr = scs.banknoteOutput.removeDanglingBanknotes();
			for(Banknote b: arr) {
				bankNotes.add(b);
			}
		}
		BigDecimal amountChange = new BigDecimal(0);
		for(Banknote b: bankNotes) {
			amountChange = amountChange.add(new BigDecimal(b.getValue()));
		}
		//Same issue that's in Payment controller I don't know how to solve it -@Famington
		assertEquals(new BigDecimal(35).byteValue(),amountChange.byteValue());
	}
	
	/*
	 * Simulating the case of delivering change when there is a abundance of
	 * banknotes and coins available
	 * */
	@Test
	public void deliverChangeRegular() throws SimulationException, OverloadException {
		scs.banknoteDispensers.get(5).load(new Banknote(Coin.DEFAULT_CURRENCY,5));
		scs.coinDispensers.get(coins[3]).load(new Coin(coins[3]));
		
		dc.deliverChange(new BigDecimal(20), new BigDecimal(26));
		
		List<Coin> coins = scs.coinTray.collectCoins();
		List<Banknote> bankNotes = new ArrayList<>();
		
		while(!scs.banknoteOutput.hasSpace()) {
			Banknote[] arr = scs.banknoteOutput.removeDanglingBanknotes();
			for(Banknote b: arr) {
				bankNotes.add(b);
			}
		}
		
		BigDecimal amountChange = new BigDecimal(0);
		
		for(Coin c: coins) {
			if(c!=null)
				amountChange = amountChange.add(c.getValue());
		}
		
		for(Banknote b: bankNotes) {
			amountChange = amountChange.add(new BigDecimal(b.getValue()));
		}
		
		assertEquals(new BigDecimal(6).byteValue(),amountChange.byteValue());
	}
	
	
	
}