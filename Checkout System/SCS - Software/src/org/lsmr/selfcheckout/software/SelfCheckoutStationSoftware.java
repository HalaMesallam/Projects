package org.lsmr.selfcheckout.software;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.swing.DefaultListModel;

import org.lsmr.selfcheckout.devices.BanknoteDispenser;
import org.lsmr.selfcheckout.devices.CoinDispenser;
import org.lsmr.selfcheckout.devices.EmptyException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;

import GUI.ProductDatabasesSample;

import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.PLUCodedItem;
import org.lsmr.selfcheckout.SimulationException;

//This is the class that boots up and then has a gui.

public class SelfCheckoutStationSoftware {

	public SelfCheckoutStation scs;
	private ProductDatabasesSample db;			//in an actual system this would connect to a db or something
	private ElectronicScaleSoftware essBagging, essScan;
	private BarcodeScannerSoftware bss;
	public PriceLookupSoftware plus;
	public PaymentController pc;
	private MembershipCardSoftware ms;
	private MembershipCardDatabase mdb; // currently a test database

	
	private DefaultListModel itemModel = new DefaultListModel();
	private DefaultListModel priceModel = new DefaultListModel();
	private static final DecimalFormat df = new DecimalFormat("0.00");
	//self checkout station software
	//NOTE: Any objects that are not primitive types are passed to other classes by reference. 
	//		Thus, passing the following vars to the other classes will give us an updated vars.
	private ArrayList<ItemInfo> itemsScanned;
	public ArrayList<Item> itemsInBagging;
	private String receipt;
	private final double weightThreshold = 10000;
	private BigDecimal[] amountPaid;
	private int bagsUsed;
	private int maximumBags;
	private BigDecimal priceOfBags;
	private boolean broughtBags;
	private boolean membership = false;
	private String membershipNumber;

	// Attendant Stuff
	private boolean blockedByAttendant;

	public SelfCheckoutStationSoftware(SelfCheckoutStation scs) {
		this.itemsScanned = new ArrayList<ItemInfo>();
		this.itemsInBagging = new ArrayList<Item>();
		//		this.setItemsAdded(new ArrayList<PLUCodedProduct>());
		this.amountPaid = new BigDecimal[1];
		this.amountPaid[0] = BigDecimal.ZERO;
		this.bagsUsed = 0;
		this.maximumBags = 10;
		this.priceOfBags = new BigDecimal("0.05");

		this.scs = scs;
		this.db = new ProductDatabasesSample();
		this.mdb = new MembershipCardDatabase();
		this.essBagging = new ElectronicScaleSoftware();
		this.essScan = new ElectronicScaleSoftware();
		this.bss = new BarcodeScannerSoftware(db,this,essBagging, itemsScanned, weightThreshold);
		this.plus = new PriceLookupSoftware(getItemsScanned(), essBagging, essScan, weightThreshold);

		//attach the ess and bss to the selfcheckout hardware
		this.scs.mainScanner.attach(bss);
		this.scs.handheldScanner.attach(bss);
		this.scs.baggingArea.attach(essBagging);	
		this.scs.scanningArea.attach(essScan);

		//Create the payment controller instance
		this.pc = new PaymentController(scs, amountPaid);

		this.ms = new MembershipCardSoftware(scs, this);

		// default not blocked or else bad stuff will happen
		this.blockedByAttendant = false;
	}

	public BigDecimal total() throws AttendantBlockException {
		if(!blockedByAttendant) {
			BigDecimal total = BigDecimal.ZERO;
			for (ItemInfo i : itemsScanned) {
				if (i != null)
					total = total.add(i.price);
			}

//			total = total.add(priceOfBags.multiply(new BigDecimal(Integer.toString(bagsUsed))));
			total = total.multiply(new BigDecimal(1.05));
			return total;
		} else {
			throw new AttendantBlockException("This method cannot be used, station is blocked by Attendant");
		}
	}


	@SuppressWarnings("serial")
	public void promptForBags() throws SimulationException, AttendantBlockException{
		if(!blockedByAttendant) {
			if (bagsUsed > maximumBags) {
				System.err.println("Please enter a valid number");	
			}
			if (bagsUsed == 0) {
				broughtBags = true;
			}
			else if(bagsUsed < 0) {
				throw new SimulationException("Please enter a valid number") {
				};
			}
		} else {
			throw new AttendantBlockException("This method cannot be used, station is blocked by Attendant");
		}
	}

	/*
	 * getter and setter for bagsUsed
	 */
	public int getBagsUsed() {
		return this.bagsUsed;
	}

	public void setBagsUsed(int noOfBags) {
		this.bagsUsed = noOfBags;
	}

	/*
	 * brought bags getter
	 */
	public boolean getBroughtBags() {
		return this.broughtBags;
	}

	public void print(BigDecimal total) throws EmptyException, OverloadException, AttendantBlockException{
		if(!blockedByAttendant) {
			int widthOfReceipt = 60;
			int spaceBetweenPriceAndDesc = 3;

			String header = String.format("%32s\n%s\n%-4s%56s\n%-4s%56s", "START OF THE RECEIPT",
					"------------------------------------------------------------",
					"Item", "Price", "----", "----\n");
			for(char c : header.toCharArray()) {scs.printer.print(c);}
			System.out.println(header);
			for (ItemInfo i : itemsScanned) {
				//creates 60 white spaced string
				String receiptLine = "";

				int descSpaceLength = widthOfReceipt - (i.price.toString().length() + spaceBetweenPriceAndDesc);

				String description = "";
				if (i.description.length() > descSpaceLength) {
					description = i.description.substring(0, descSpaceLength);
					String whitespace = new String(new char[spaceBetweenPriceAndDesc]).replace("\0", " ");
					receiptLine = description.substring(0, description.length()) + whitespace + i.price.toString() + "\n";
					for(char c : receiptLine.toCharArray()) {scs.printer.print(c);}
				}else {
					int whitespaceLength = widthOfReceipt - i.description.length() - i.price.toString().length() - ("\n".length());
					String whitespace = new String(new char[whitespaceLength]).replace("\0", " ");
					receiptLine = i.description + whitespace + i.price.toString() + '\n';
					for(char c : receiptLine.toCharArray()) {scs.printer.print(c);}
					System.out.print(receiptLine);
				}
			}

			String totalLine = "Total: " + total;
			for(char c : totalLine.toCharArray()) {scs.printer.print(c);}
			System.out.println(totalLine);

			if(membership == true) {
				String membershipLine = "Membership Number: " + membershipNumber;
				for(char c : membershipLine.toCharArray()) {scs.printer.print(c);}
				System.out.println(membershipLine);
				receipt = membershipLine;
				System.out.println(receipt);
			}
		} else {
			throw new AttendantBlockException("This method cannot be used, station is blocked by Attendant");
		}
	}

	/**
	 * The Customer presses this button to start the checkout process.
	 * @throws AttendantBlockException 
	 * @throws SimulationException 
	 */
	public void checkout() throws SimulationException, AttendantBlockException {
		if(!blockedByAttendant) {
			promptForBags();
			pc.enterPayment(total());
		} else {
			throw new AttendantBlockException("This method cannot be used, station is blocked by Attendant");
		}
	}

	/**
	 * The Customer presses this button to finish the checkout process.
	 */
	public void finishCheckout() throws AttendantBlockException {
		if(!blockedByAttendant) {
			pc.checkout();
		} else {
			throw new AttendantBlockException("This method cannot be used, station is blocked by Attendant");
		}
	}

	/**
	 * The customer presses this button to suspend the checkout process.
	 * He or she can scan and add new items to the bagging area afterwards.
	 */
	public void suspendCheckout() throws AttendantBlockException {
		if(!blockedByAttendant) {
			pc.suspendPayment();
		} else {
			throw new AttendantBlockException("This method cannot be used, station is blocked by Attendant");
		}
	}

	/**
	 * The customer presses this button to continue the checkout process after suspending it.
	 */
	public void backToCheckout() throws AttendantBlockException {
		if(!blockedByAttendant) {
			pc.enterPayment(total());
		} else {
			throw new AttendantBlockException("This method cannot be used, station is blocked by Attendant");
		}
	}

	/**
	 * The customer presses this button to make a payment with cash.
	 */
	public void payWithCash() throws AttendantBlockException{		
		if(!blockedByAttendant) {
			pc.payWithCash();
		} else {
			throw new AttendantBlockException("This method cannot be used, station is blocked by Attendant");
		}
	}

	/**
	 * The customer presses this button to end cash payment.
	 * He or she can now switch to another payment type(credit/debit).
	 */
	public void endPayWithCash() throws AttendantBlockException{		
		if(!blockedByAttendant) {
			pc.endPayWithCash();
		} else {
			throw new AttendantBlockException("This method cannot be used, station is blocked by Attendant");
		}
	}

	/**
	 * The customer presses this button to pay with credit card.
	 * The screen will prompt the customer for an amount to pay with this card.
	 */
	public void payWithCreditCard() throws AttendantBlockException{
		if(!blockedByAttendant) {
			BigDecimal amount = BigDecimal.ZERO;
			//Prompt the customer for the amount to pay with this card
			pc.payWithCreditCard(amount);
		} else {
			throw new AttendantBlockException("This method cannot be used, station is blocked by Attendant");
		}
	}

	/**
	 * The customer presses this button to pay with debit card.
	 * The screen will prompt the customer for an amount to pay with this card.
	 */
	public void payWithDebitCard() throws AttendantBlockException{	
		if(!blockedByAttendant) {
			BigDecimal amount = BigDecimal.ZERO;
			//Prompt the customer for the amount to pay with this card
			pc.payWithCreditCard(amount);
		} else {
			throw new AttendantBlockException("This method cannot be used, station is blocked by Attendant");
		}
	}

	/**
	 * The customer presses this button to cancel a card payment after choosing to pay with credit/debit card.
	 */
	public void cancelCardPayment() throws AttendantBlockException{
		if(!blockedByAttendant) {
			pc.cancelCardPayment();
		} else {
			throw new AttendantBlockException("This method cannot be used, station is blocked by Attendant");
		}
	}

	/**
	 * The customer presses this to scan a membership card
	 */
	public void scanMembershipCard() throws AttendantBlockException{		
		if(!blockedByAttendant) {
			ms.useMembershipCard();
		} else {
			throw new AttendantBlockException("This method cannot be used, station is blocked by Attendant");
		}
	}

	/**
	 * The customer uses this to enter their membership card number
	 */
	public void enterMembershipCardNumber(String membershipCardNum) throws AttendantBlockException{		
		if(!blockedByAttendant) {
			ms.enterMembershipCardNumber(membershipCardNum, mdb);
		} else {
			throw new AttendantBlockException("This method cannot be used, station is blocked by Attendant");
		}
	}

	public void resetVars() throws AttendantBlockException {
		if(!blockedByAttendant) {
			this.itemsScanned = new ArrayList<ItemInfo>();
			this.amountPaid[0] = BigDecimal.ZERO;
			this.bagsUsed = 0;
		} else {
			throw new AttendantBlockException("This method cannot be used, station is blocked by Attendant");
		}
	}

	public boolean verifyWeights() throws AttendantBlockException{	
		if(!blockedByAttendant) {
			double expected = bss.getExpectedWeight();
			double measured = essBagging.getCurrentWeight();
			//System.out.println(expected + " " + measured);
			if(expected == measured) {
				return true;
			}
			else {
				return false;
			}
		} else {
			throw new AttendantBlockException("This method cannot be used, station is blocked by Attendant");
		}
	}

	//Alternative implementation for emptying and refilling banknotes and coins
	/*
	 * //Refill bank dispenser public List<Banknote>
	 * refillBankDispenser(List<Banknote> notes) { List<Banknote> unload = new
	 * ArrayList<Banknote>();
	 * 
	 * for (Banknote note: notes) { BanknoteDispenser dispense =
	 * scs.banknoteDispensers.get(note.getValue()); if(dispense.size() <
	 * dispense.getCapacity()) { try { dispense.load(note); }
	 * catch(OverloadException e) { unload.add(note); } } else { unload.add(note); }
	 * } return unload; }
	 * 
	 * //Refill coin dispenser public List<Coin> refillCoinDispenser(List<Coin>
	 * coins) { List<Coin> unload = new ArrayList<Coin>();
	 * 
	 * for (Coin coin: coins) { CoinDispenser dispense =
	 * scs.coinDispensers.get(coin.getValue()); if(dispense.hasSpace()) { try {
	 * dispense.load(coin); } catch(OverloadException e) { unload.add(coin); } }
	 * else { unload.add(coin); } } return unload; }
	 * 
	 * //Empties Bank Note storage public void emptyBankNoteStorage() {
	 * scs.banknoteStorage.unload(); }
	 * 
	 * //Empties coin storage Unit public void emptyCoinStorageUnit() {
	 * scs.coinStorage.unload(); }
	 */


	// to be strictly used for testing, will not actually be called
	// by any event listener because there is no observer
	public void enforceWeights() throws AttendantBlockException{	
		if(!blockedByAttendant) {
			if(!verifyWeights()) {
				blockedByAttendant = true;
			}
		} else {
			throw new AttendantBlockException("This method cannot be used, station is blocked by Attendant");
		}
	}

	public BarcodeScannerSoftware getBss() {
		return this.bss;
	}

	public ElectronicScaleSoftware getEss() {
		return this.essBagging;
	}

	public BigDecimal getAmountPaid() {
		return amountPaid[0];
	}

	public void changeMembership(boolean change) throws AttendantBlockException{
		if(!blockedByAttendant) {
			this.membership = change;
		} else {
			throw new AttendantBlockException("This method cannot be used, station is blocked by Attendant");
		}
	}

	public void addMembershipNumber(String number) throws AttendantBlockException {		
		if(!blockedByAttendant) {
			membershipNumber = number;
		} else {
			throw new AttendantBlockException("This method cannot be used, station is blocked by Attendant");
		}
	}

	public boolean checkMembership() throws AttendantBlockException{
		if(!blockedByAttendant) {
			return this.membership;
		} else {
			throw new AttendantBlockException("This method cannot be used, station is blocked by Attendant");
		}
	}

	public void removeMembershipNumber() throws AttendantBlockException{		
		if(!blockedByAttendant) {
			membershipNumber = null;
		} else {
			throw new AttendantBlockException("This method cannot be used, station is blocked by Attendant");
		}
	}

	public void removeItem(String itemName, int index) throws AttendantBlockException {
		Iterator<ItemInfo> a = getItemsScanned().iterator();
		int localIndex = 0;
		while (a.hasNext()) {
			ItemInfo toRemove = a.next();
			if(toRemove.description.equalsIgnoreCase(itemName) && index == localIndex) {
				if (toRemove.barcode != null) {
					BarcodedItem item = (BarcodedItem) getItemInBagging(index);
					itemsInBagging.remove(item);
					scs.baggingArea.remove(item);
				}
				else if (toRemove.pluCode != null){
					PLUCodedItem item = (PLUCodedItem) getItemInBagging(localIndex);
					itemsInBagging.remove(item);
					scs.baggingArea.remove(item);
				}
				a.remove();
				return;
			}
			localIndex++;
		}
	}

	public void addToBagging(Item newItem) {
		itemsInBagging.add(newItem);
	}

	public Item getItemInBagging(int index) {
		Iterator<Item> a = itemsInBagging.iterator();
		int localIndex = 0;
		while(a.hasNext()) {
			Item newItem = a.next();
			if (localIndex == index) 
				System.out.println(newItem);
				return newItem;
		}
		return  null;
	}
	
	public void addedItem() {
		// Checks if the itemlist has a new item compared to the current display list
		if (getItemsScanned().size() > itemModel.size()) {
			// Sets the index to the bottom of the list
			int index = getItemsScanned().size() - 1;
			ItemInfo info = getItemsScanned().get(index);
			// Gets the name and price from the newly added item and adds it to the display list
			itemModel.addElement(info.description);
			priceModel.addElement(df.format(info.price.doubleValue()));
		}
	}

	public void startUpGUI() {
		//does nothing for now
	}

	public String getReceipt() {
		return receipt;
	}

	public ArrayList<ItemInfo> getItemsScanned(){
		return this.itemsScanned;
	}

	public void setItemsScanned(ArrayList<ItemInfo> a) {
		this.itemsScanned = a;
	}

	/**
	 * Should only be accessed by AttendantOverride.java class
	 * @param args Unused.
	 * @return Nothing.
	 */
	public void setBlockStatus(boolean b) {
		blockedByAttendant = b;
	}


	public boolean getBlockStatus() {
		return blockedByAttendant;
	}

	public DefaultListModel getItemModel() {
		return itemModel;
	}

	public void setItemModel(DefaultListModel itemModel) {
		this.itemModel = itemModel;
	}

	public DefaultListModel getPriceModel() {
		return priceModel;
	}

	public void setPriceModel(DefaultListModel priceModel) {
		this.priceModel = priceModel;
	}
}