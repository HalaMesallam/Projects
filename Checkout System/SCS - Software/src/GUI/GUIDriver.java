package GUI;

import java.math.BigDecimal;
import java.util.Currency;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SupervisionStation;
import org.lsmr.selfcheckout.software.SelfCheckoutStationSoftware;

public class GUIDriver {
	public static void main(String[] args) {
		int[] bills = new int[] {5,10,20,50,100};
		BigDecimal[] coins = new BigDecimal[] {new BigDecimal(0.05), new BigDecimal(0.10), new BigDecimal(0.25), new BigDecimal(1.00), new BigDecimal(2.00)};
		
		SelfCheckoutStation scs1;
		SelfCheckoutStation scs2;
		
		SelfCheckoutStationSoftware scss1;
		SelfCheckoutStationSoftware scss2;
		
		SupervisionStation attendantStation;
		
		scs1 = new SelfCheckoutStation(Currency.getInstance("CAD"), bills, coins, 1000000, 1);
		scs2 = new SelfCheckoutStation(Currency.getInstance("CAD"), bills, coins, 1000000, 1);
		scss1= new SelfCheckoutStationSoftware(scs1);
		scss2= new SelfCheckoutStationSoftware(scs2);
		
		
		attendantStation = new SupervisionStation();
		AttendantStationSoftware ass = new AttendantStationSoftware(attendantStation, scs1, scs2, scss1, scss2);
		
		AttendantGUIController AGUIC = new AttendantGUIController(attendantStation, ass);
	}
}
