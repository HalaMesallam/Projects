package org.lsmr.selfcheckout.software.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SupervisionStation;
import org.lsmr.selfcheckout.software.AttendantOverride;
import org.lsmr.selfcheckout.software.DiscrepancyChecker;
import org.lsmr.selfcheckout.software.DiscrepancyChecker.State;
import org.lsmr.selfcheckout.software.AttendantOverride.Status;
import org.lsmr.selfcheckout.software.ItemInfo;
import org.lsmr.selfcheckout.software.SelfCheckoutStationSoftware;

public class AttendantOverrideTest {
    private SupervisionStation ss;
    private DiscrepancyChecker sdc;
    private AttendantOverride ao, ao2;
    
	public Currency currency = Currency.getInstance(Locale.CANADA);
    public int[] denominations = new int[]{5, 10, 20, 50, 100};
    public BigDecimal[] coinDenominations = new BigDecimal[]{
		new BigDecimal("0.05"),
		new BigDecimal("0.10"),
		new BigDecimal("0.25"),
		new BigDecimal("1.00"),
		new BigDecimal("2.00"),
	};;
    
	private SelfCheckoutStation scs =  new SelfCheckoutStation(currency , denominations, coinDenominations, 200, 1);
    private SelfCheckoutStationSoftware scss;
    private Item item;
    private Status status;
    private State state;
    
    @Before
    public void testAttendantOverride()
    {
    	// Tests that the constructors work properly
    	ao = new AttendantOverride(ss);
    	ao2 = new AttendantOverride(ss, sdc);
    }
    
    @Test
    public void testStatus()
    {
    	// Simple tests just to cover the different statuses
    	status = Status.stationStarted;
    	status = Status.stationShutdown;
    }
    
    @Test
    public void testBypass() throws OverloadException
    {
    	sdc = new DiscrepancyChecker(scs, (double)10);
    	scss = new SelfCheckoutStationSoftware(scs);
    	AttendantOverride AttendantOverrideInstance = new AttendantOverride(ss, sdc, scss);
    	AttendantOverrideInstance.startSupervisionStation(ss, "admin", "admin");
    	AttendantOverrideInstance.setBlockSupervisedStation(true);
    	sdc.forceDiscrepancyState();
    	AttendantOverrideInstance.bypass();
    	
    	assertTrue("Discrepancy should've been resolved",sdc.checkStateDiscrepancy() == false);
    	assertTrue("station should no longer be blocked",scss.getBlockStatus() == false);
    	assertTrue("weight should've been reset to last instance",sdc.getlastTimeWeight() == sdc.getExpectedWeight());
    }
    
    @Test
    public void testBypassNotLoggedInAndShutdown() throws OverloadException
    {
    	sdc = new DiscrepancyChecker(scs, (double)10);
    	scss = new SelfCheckoutStationSoftware(scs);
    	AttendantOverride AttendantOverrideInstance = new AttendantOverride(ss, sdc, scss);
    	AttendantOverrideInstance.logout();
    	AttendantOverrideInstance.forceShutdown();
    	sdc.forceDiscrepancyState();
    	AttendantOverrideInstance.bypass();
    	
    	assertFalse(AttendantOverrideInstance.getLoggedIn());
    	assertTrue(AttendantOverrideInstance.isShutdown());


    	
    }
    
    @Test
    public void testBypassStationLoggedInButShutdown() throws OverloadException
    {
    	sdc = new DiscrepancyChecker(scs, (double)10);
    	scss = new SelfCheckoutStationSoftware(scs);
    	AttendantOverride AttendantOverrideInstance = new AttendantOverride(ss, sdc, scss);
    	AttendantOverrideInstance.startSupervisionStation(ss, "admin", "admin");
    	AttendantOverrideInstance.forceShutdown();
    	AttendantOverrideInstance.bypass();
    	
    	assertTrue(AttendantOverrideInstance.getLoggedIn());
    	assertTrue(AttendantOverrideInstance.isShutdown());

    	
    }
    
    @Test
    public void testBypassNoDiscrepancy() throws OverloadException
    {
    	sdc = new DiscrepancyChecker(scs, (double)10);
    	scss = new SelfCheckoutStationSoftware(scs);
    	AttendantOverride AttendantOverrideInstance = new AttendantOverride(ss, sdc, scss);
    	AttendantOverrideInstance.startSupervisionStation(ss, "admin", "admin");
    	AttendantOverrideInstance.setBlockSupervisedStation(true);
    	AttendantOverrideInstance.bypass();
    	
    	assertTrue("station should no longer be blocked",scss.getBlockStatus() == true);

    	
    }
    
    
    @Test
    public void testForceRemove()
    {
    	scss = new SelfCheckoutStationSoftware(scs);
    	AttendantOverride AttendantOverrideInstance = new AttendantOverride(ss, sdc, scss);
    	AttendantOverrideInstance.startSupervisionStation(ss, "admin", "admin");
    	ArrayList<ItemInfo> scannedItemList = scss.getItemsScanned();
    	ItemInfo info = new ItemInfo(new BigDecimal(10), 25, "Someitem", null, null);    	scannedItemList.add(info);
    	scss.setItemsScanned(scannedItemList);
    	AttendantOverrideInstance.forceRemove(info);
    	
    	assertTrue("list is supposed to be empty", scss.getItemsScanned().size() == 0);
    }
    
    @Test
    public void testForceRemoveNotLoggedInAndShutdown()
    {
    	scss = new SelfCheckoutStationSoftware(scs);
    	AttendantOverride AttendantOverrideInstance = new AttendantOverride(ss, sdc, scss);
    	AttendantOverrideInstance.shutdownSupervisionStation(ss);
    	ArrayList<ItemInfo> scannedItemList = scss.getItemsScanned();
    	ItemInfo info = new ItemInfo(new BigDecimal(10), 25, "Someitem", null, null);
    	scannedItemList.add(info);
    	scss.setItemsScanned(scannedItemList);
    	AttendantOverrideInstance.forceRemove(info);
    	
    	assertFalse(AttendantOverrideInstance.getLoggedIn());
    	assertTrue(AttendantOverrideInstance.isShutdown());
    	
    }
    
    @Test
    public void testForceRemoveLoggedInButShutdown()
    {
    	scss = new SelfCheckoutStationSoftware(scs);
    	AttendantOverride AttendantOverrideInstance = new AttendantOverride(ss, sdc, scss);
    	AttendantOverrideInstance.startSupervisionStation(ss, "admin", "admin");
    	AttendantOverrideInstance.forceShutdown();
    	ArrayList<ItemInfo> scannedItemList = scss.getItemsScanned();
    	ItemInfo info = new ItemInfo(new BigDecimal(10), 25, "Someitem", null, null);    	scannedItemList.add(info);
    	scss.setItemsScanned(scannedItemList);
    	AttendantOverrideInstance.forceRemove(info);
    	
    	assertTrue(AttendantOverrideInstance.getLoggedIn());
    	assertTrue(AttendantOverrideInstance.isShutdown());
    	
    }
    
    
    @Test
    public void testSetBlockSupervisedStation()
    {
    	scss = new SelfCheckoutStationSoftware(scs);
    	
    	AttendantOverride AttendantOverrideInstance = new AttendantOverride(ss, sdc, scss);
    	status = Status.stationStarted;
    	AttendantOverrideInstance.startSupervisionStation(ss, "admin", "admin"); // used to set loggedIn = true
    	AttendantOverrideInstance.setBlockSupervisedStation(true);
    	assertTrue(scss.getBlockStatus());
    }
    
    @Test
    public void testSetBlockSupervisedStationNotLoggedInAndShutdown()
    {
    	scss = new SelfCheckoutStationSoftware(scs);
    	AttendantOverride AttendantOverrideInstance = new AttendantOverride(ss, sdc, scss);
    	AttendantOverrideInstance.logout();
    	AttendantOverrideInstance.forceShutdown();
    	AttendantOverrideInstance.setBlockSupervisedStation(true);
    	
    	assertFalse(AttendantOverrideInstance.getLoggedIn());
    	assertTrue(AttendantOverrideInstance.isShutdown());
    }
    
    @Test
    public void testSetBlockSupervisedStationLoggedInButShutdown()
    {
    	scss = new SelfCheckoutStationSoftware(scs);
    	
    	AttendantOverride AttendantOverrideInstance = new AttendantOverride(ss, sdc, scss);
    	AttendantOverrideInstance.startSupervisionStation(ss, "admin", "admin"); // used to set loggedIn = true
    	AttendantOverrideInstance.forceShutdown();
    	AttendantOverrideInstance.setBlockSupervisedStation(true);
    	
    	assertTrue(AttendantOverrideInstance.getLoggedIn());
    	assertTrue(AttendantOverrideInstance.isShutdown());
    }
    
    @Test
    public void testShutdownSupervisionStation()
    {
    	ao2.shutdownSupervisionStation(ss);
    	assertTrue(ao2.getStatus() == Status.stationShutdown);
    	assertFalse(ao2.getLoggedIn());
    }
    
    @Test
    public void testIsStarted()
    {
    	ao2.startSupervisionStation(ss, "admin", "admin"); // used to set status = Status.stationStarted
    	assertTrue(ao2.isStarted());
    	ao2.startSupervisionStation(ss, "not admin", "not admin"); // used to set status = Status.stationShutdown
    	assertFalse(ao2.isStarted());
    }
    
    @Test
    public void testIsShutdown()
    {
    	ao2.startSupervisionStation(ss, "admin", "admin"); // used to set status = Status.stationStarted
    	assertFalse(ao2.isShutdown());
    	ao2.startSupervisionStation(ss, "not admin", "not admin"); // used to set status = Status.stationShutdown
    	assertTrue(ao2.isShutdown());
    }
    
    @Test 
    public void testLogout()
    {
    	ao2.logout();
    	assertFalse(ao2.getLoggedIn());
    }
    
    @Test
    public void testGetLoggedIn()
    {
    	ao2.logout();
    	assertFalse(ao2.getLoggedIn());
    	
    	ao2.startSupervisionStation(ss, "admin", "admin"); // used to set loggedIn = true
    	assertTrue(ao2.getLoggedIn());
    }
    
    @Test
    public void testLogin()
    {
    	assertTrue(ao2.login("admin", "admin"));
    	assertFalse(ao2.login("admin", "not admin"));
    	assertFalse(ao2.login("not admin", "not admin"));
    }
}
