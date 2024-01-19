package org.lsmr.selfcheckout.software;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.lsmr.selfcheckout.Card;
import org.lsmr.selfcheckout.Card.CardData;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.CardReader;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.SimulationException;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.CardReaderObserver;

public class MembershipCardSoftware implements CardReaderObserver{
	private SelfCheckoutStation scs;
	private SelfCheckoutStationSoftware scss;
	private CardData data;
    private boolean ifDataRead;
    private boolean wasSwiped;
	private boolean membership = false;
	
	public MembershipCardSoftware(SelfCheckoutStation selfCheckoutStation, SelfCheckoutStationSoftware selfCheckoutStationSoftware) {
		scs = selfCheckoutStation;
		scss = selfCheckoutStationSoftware;
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
	public void cardInserted(CardReader reader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cardRemoved(CardReader reader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cardTapped(CardReader reader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cardSwiped(CardReader reader) {
		this.setWasSwiped(true);
    	Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(ifDataRead==false){
                    //display failed message on the screen
                }
            }
        }, 3*1000);
		
	}

	//Get the card data if the card is a membership card
	@Override
	public void cardDataRead(CardReader reader, Card.CardData data) {
		if(data.getType().equals("Membership")) {
			this.data = data;
			try {
				scss.changeMembership(true);
	            scss.addMembershipNumber(data.getNumber());
			}
			catch (AttendantBlockException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Card not of type membership");
		}
		scs.cardReader.disable(); // stop scanning for card
	}
	
	// run to scan for membership card
	public void useMembershipCard() {
		scs.cardReader.enable(); // start scanning for card
		try {
			scss.changeMembership(true);
		}
		catch (AttendantBlockException e) {
			e.printStackTrace();
		}
	}
	
	// run to manually enter membership card with number
	@SuppressWarnings("serial")
	public void enterMembershipCardNumber(String number, MembershipCardDatabase database) {
		if(number == null) {
    		throw new SimulationException("Membership number does not exist") {
    		};
		} else if(database.verifyCardNumber(number) == true) {
			try {
				scss.changeMembership(true);
				scss.addMembershipNumber(number);
			}
			catch (AttendantBlockException e) {
				e.printStackTrace();
			}
		} else {
    		throw new SimulationException("Membership number does not exist") {
    		};
		}
	}
	
	// takes in the membership card number as user input
// 	public void enterMembershipCardInfo()
// 	{
// 		Scanner s = new Scanner(System.in);
// 		int cardNumber = s.nextInt();
// 		System.out.println("membership card info: " + cardNumber);
// 	}

	// get the data of the card
	public CardData getData() {
		return this.data;
	}
	
	// clear card data
	public void clearData() {
		this.data = null;
	}

	// check membership status
	public boolean checkMembership() {
		return membership;
	}
	
	/**
     * Set the wasSwipe value.
     * @param wasSwiped The boolean value assign to wasSwiped.
     */
	private void setWasSwiped(boolean wasSwiped) {
		this.wasSwiped = wasSwiped;
	}
}
