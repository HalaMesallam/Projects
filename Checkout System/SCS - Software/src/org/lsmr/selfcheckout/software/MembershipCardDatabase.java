package org.lsmr.selfcheckout.software;

import java.util.HashMap;
import java.util.Map;

import org.lsmr.selfcheckout.Card;
import org.lsmr.selfcheckout.Card.CardData;


public class MembershipCardDatabase {

	//stub for card entries
	private class MembershipCardEntry{
		private String type;
		private String number;
		private String cardholder;

        public MembershipCardEntry(String type, String number, String cardholder){
        	this.type = type;
        	this.number = number;
            this.cardholder = cardholder;
        }
    }
	
	
	private Map<String, MembershipCardEntry> membershipCardDatabase;
	
	
	//dummy database
	public MembershipCardDatabase() {
		  membershipCardDatabase = new HashMap<>();
		  addMembershipCardEntry("Membership", "123456789", "Steven Hamilton");
		 
	}
	
	// add a membership card to the database
	public void addMembershipCardEntry(String type, String number, String cardholder) {
		MembershipCardEntry newMembershipCardEntry = new MembershipCardEntry(type, number, cardholder);
        membershipCardDatabase.put(number, newMembershipCardEntry);
	}
	
	// verify the card against the database
	public boolean verifyCard(CardData data){
        if(data.getType().equals("Membership")){
            MembershipCardEntry entry = membershipCardDatabase.get(data.getNumber());
            if(entry==null){
                return false;
            }
            return true;
        } else {
        	return false;
        }
    }
	
	// verify the card number against the database
	public boolean verifyCardNumber(String number){
		MembershipCardEntry entry = membershipCardDatabase.get(number);
	    if(entry==null){
	    	return false;
	    } else {
	        return true;
	    }
	}
	public void printdatabase() {
		System.out.println(verifyCardNumber("123456789"));
	}
}