package org.lsmr.selfcheckout.software.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;



import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Card;
import org.lsmr.selfcheckout.Card.CardData;
import org.lsmr.selfcheckout.devices.EmptyException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.SimulationException;
import org.lsmr.selfcheckout.software.AttendantBlockException;
import org.lsmr.selfcheckout.software.MembershipCardDatabase;
import org.lsmr.selfcheckout.software.MembershipCardSoftware;
import org.lsmr.selfcheckout.software.SelfCheckoutStationSoftware;

public class MembershipCardTest {
	
	private MembershipCardSoftware mcs;
	private BigDecimal[] amountPaid;
	private SelfCheckoutStation scs;
	private SelfCheckoutStationSoftware scss;
	private CardData data;
	private final Currency currency = Currency.getInstance(Locale.CANADA);
	private Card card, card2, card3;
	private MembershipCardDatabase mcd;
	private String testReceipt = "            START OF THE RECEIPT\n"
			+"------------------------------------------------------------\n"
			+"Item                                                   Price\n"
			+"----                                                   ----\n"
			+"Total: 100"
			+"Membership Number: 123456789";

	//Data needed for instantiate the self-checkout station
	int[] banknoteDenominations = { 5, 10, 20, 50, 100 };
	BigDecimal[] coinDenominations = { new BigDecimal("0.05"), new BigDecimal("0.1"), new BigDecimal("0.25"),
	new BigDecimal("1"), new BigDecimal("2") };
	int scaleMaximumWeight = 100;
	int scaleSensitivity = 1;
	
	@Before
	public void setup(){
		card = new Card("Membership", "", "", "", "", true, true);
		card2 = new Card("Membership", "123456789", "Steven Hamilton", "", "", true, true);
		card3 = new Card("Debit", "", "", "", "", true, true);
		//We give the BanknoteSlotSoftware a reference to this class' variable called amountPaid.
		amountPaid = new BigDecimal[1];
		amountPaid[0] = new BigDecimal("0");
		scs = new SelfCheckoutStation(currency, banknoteDenominations, coinDenominations,scaleMaximumWeight,scaleSensitivity);
		scss = new SelfCheckoutStationSoftware(scs);
		mcs = new MembershipCardSoftware(scs, scss);
		mcd = new MembershipCardDatabase();
		mcd.addMembershipCardEntry("", "123456789", "Person");
		
		
	}
	
	@Test
	public void testEnabled() {
		mcs.enabled(null);
	}
	@Test
	public void testDisabled() {
		mcs.disabled(null);
	}
	@Test
	public void testCardInserted() {
		mcs.cardInserted(null);
	}
	@Test
	public void testCardRemoved() {
		mcs.cardRemoved(null);
	}
	@Test
	public void testCardTapped() {
		mcs.cardTapped(null);
	}
	@Test
	public void testCardSwiped() {
		mcs.cardSwiped(null);
	}
	
	@Test
    public void testCardDataRead() throws IOException {
		mcs.cardDataRead(null, card.swipe());
        Assert.assertTrue("Card type should be Membership", card.swipe().getType() == "Membership");
        mcs.clearData();
        mcs.cardDataRead(null, card.swipe());
    }
	@Test
    public void testCardDataRead2() throws IOException {
		Card card2 = new Card("Debit", "", "", "", "", true, true);
        mcs.cardDataRead(null, card2.swipe());
        Assert.assertTrue("Card type should not be Membership", card2.swipe().getType() != "Membership");
        
    }
	@Test
	public void testClearData() {
		mcs.clearData();
		Assert.assertTrue("Card should be cleared", data == null);
	}

	@Test
	public void testGetData() {
		mcs.getData();
	}

	@Test
	public void testUseMembershipCard() throws IOException, AttendantBlockException{
		mcs.getData();
		Assert.assertTrue("Card Data should not be null", card.swipe().getType() != null);
		scss.scanMembershipCard();
		// Reduces the chance for failure only for testing
		try {
			// Simulates swiping the card through the reader
			scs.cardReader.swipe(card2);
		} catch (Exception MagneticStripeFailureException) {
			scs.cardReader.swipe(card2);
		}
		Assert.assertTrue("Membership Card should be detected", scss.checkMembership() == true);
	}
	
	@Test (expected = SimulationException.class)
	public void testEnterMembershipCardNumber(){
		mcs.enterMembershipCardNumber(null, null);
	}
	
	@Test
	public void testEnterMembershipCardNumberscsSoftware() throws AttendantBlockException{
		scss.enterMembershipCardNumber("123456789");
	}
	
	@Test (expected = SimulationException.class)
	public void testEnterMembershipCardNumber2(){
		mcs.enterMembershipCardNumber("123456789", mcd);
		mcs.enterMembershipCardNumber("12345678", mcd);
	}
	
	@Test
	public void testmembershipreceipt() throws OverloadException, EmptyException, AttendantBlockException {
		scss.changeMembership(true);
		scss.addMembershipNumber("123456789");
		scs.printer.addPaper(1000);
		scs.printer.addInk(1000);
		scss.print(new BigDecimal("100"));
		Assert.assertEquals(scss.getReceipt().compareTo("Membership Number: 123456789"), 0);
	}
	
	@Test
	public void testCheckMembership() {
		mcs.checkMembership();
	}
	
	@Test
	public void testVerifyMembership() throws IOException {
		Assert.assertTrue("Verify should be true", mcd.verifyCard(card2.swipe()) == true);
	}
	
	@Test
	public void testWrongVerifyMembership() throws IOException {
		Assert.assertTrue("Verify should be false", mcd.verifyCard(card.swipe()) == false);
	}
	
	@Test
	public void testWrongTypeVerifyMembership() throws IOException {
		Assert.assertTrue("Verify should be false", mcd.verifyCard(card3.swipe()) == false);
	}
}
