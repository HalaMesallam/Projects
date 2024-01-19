package GUI;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SupervisionStation;
import org.lsmr.selfcheckout.software.AttendantBlockException;
import org.lsmr.selfcheckout.software.SelfCheckoutStationSoftware;

public class AttendantStationSoftware {
	public SelfCheckoutStation scs1;
	public SelfCheckoutStation scs2;
	public SelfCheckoutStationSoftware scss1;
	public SelfCheckoutStationSoftware scss2;
	public KeyboardFacade kf;
	
	public SupervisionStation ss;
	
	public AttendantStationSoftware(SupervisionStation ss, SelfCheckoutStation scs1, SelfCheckoutStation scs2, SelfCheckoutStationSoftware scss1,SelfCheckoutStationSoftware scss2) {
		this.ss = ss;
		this.scs1 = scs1;
		this.scs2 = scs2;
		this.scss1 = scss1;
		this.scss2 = scss2;
		this.kf = new KeyboardFacade(ss.keyboard);
	}
	
	public KeyboardFacade getKeyboardFacade() {
		return kf;
	}
}
