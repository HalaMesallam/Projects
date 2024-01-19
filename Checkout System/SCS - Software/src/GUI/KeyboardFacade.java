package GUI;

import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.Keyboard;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SupervisionStation;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.KeyboardObserver;

public class KeyboardFacade {
	private Keyboard kb;
	private KeyboardListener kbl;
	private String buffer;
	
	class KeyboardListener implements KeyboardObserver{

		@Override
		public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(Keyboard k, char c) {
			// TODO Auto-generated method stub
			buffer+=c;
		}
		
	}
	
	public KeyboardFacade(Keyboard kb) {
		this.kb = kb;
		this.kbl = new KeyboardListener();
		this.buffer = new String();
		kb.attach(kbl);
	}
	
	public String getInput() {
		return buffer;
	}
	
	public void clearBuffer() {
		buffer = new String();
	}
}
