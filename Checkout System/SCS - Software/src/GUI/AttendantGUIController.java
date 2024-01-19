package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.lsmr.selfcheckout.devices.Keyboard;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SupervisionStation;
import org.lsmr.selfcheckout.devices.TouchScreen;
import org.lsmr.selfcheckout.software.SelfCheckoutStationSoftware;

import GUI.StationControl.State;

public class AttendantGUIController {
	private SupervisionStation station;
	private Login login;
	private StationsOverview overview;
	private StationControl stationOneControl;
	private StationControl stationTwoControl;
	private TouchScreen ts;
	private Keyboard kb;
	private JFrame screen;
	private LoginSimulation ls;
	private AttendantStationSoftware ass;
	private AttendantPLU plu;
	private Dimension dimension;
	private WelcomeGUI stationOne;
	private WelcomeGUI stationTwo;
	
	public AttendantGUIController(SupervisionStation station, AttendantStationSoftware ass){
		this.station = station;
		this.ass = ass;
		this.ts = station.screen;
		this.screen = ts.getFrame();
		this.login = new Login(this);
		this.overview = new StationsOverview(this,station);
		this.stationOneControl = new StationControl(this, ass.scs1, overview, ass.scss1, 1);
		this.stationTwoControl = new StationControl(this, ass.scs2, overview, ass.scss2, 2);
		this.plu = new AttendantPLU(ass.scss1, this, stationOneControl);
		this.plu.setVisible(false);
		initialization();
	}
	
	private void initialization() {
				
		// Sets the default size of the frame
		dimension = new Dimension(1280, 720);
		
		screen.setPreferredSize(dimension);
		
		screen.setLayout(null);
		
		int loginWidth = 450;
		int loginHeight = 300;
		
		login.setBounds((dimension.width - loginWidth) / 2, (dimension.height - loginHeight) / 2, loginWidth, loginHeight);
		screen.getContentPane().add(login);
		screen.getContentPane().add(plu);
		plu.setVisible(false);

		// Packs it all together
		screen.pack();
		
		// Finally makes it visible
		screen.setVisible(true);
		
		this.ls = new LoginSimulation(login,ass,station.keyboard);
		ls.setVisible(true);
	}

	public void loginSuccess() {
		// TODO Auto-generated method stub
		screen.remove(login);
		int overviewWidth = 725;
		int overviewHeight = 437;
		overview.setBounds((dimension.width - overviewWidth) / 2, (dimension.height - overviewHeight) / 2, overviewWidth, overviewHeight);
		screen.getContentPane().add(overview);
		overview.setVisible(true);
		ls.dispose();
		screen.repaint();
		screen.revalidate();
	}
	
	public JFrame getScreen() {
		return screen;
	}

	public SupervisionStation getStation() {
		// TODO Auto-generated method stub
		return station;
	}
	
	public Keyboard getKeyBoard() {
		return kb;
	}

	public void returnToLoginFromOverview() {
		// TODO Auto-generated method stub
		screen.remove(overview);
		login.clear();
		screen.add(login);
		this.ls = new LoginSimulation(login,ass,station.keyboard);
		ls.setVisible(true);
		screen.repaint();
		screen.revalidate();
	}
	
	public void proceedToStationOneControl() {
		if(stationOneControl.getState() == State.OFF) {
			overview.changeMsg("Failed: station is off.");
			return;
		}
		screen.remove(overview);
		int width = 864;
		int height = 537;
		stationOneControl.setBounds((dimension.width - width) / 2, (dimension.height - height) / 2, width, height);
		screen.getContentPane().add(stationOneControl);
		stationOneControl.setVisible(true);
		stationOneControl.setSimulationVisible();
		stationOneControl.createSampleBarcodedProducts();
		stationOneControl.ms.setVisible(true);
		screen.repaint();
		screen.revalidate();
	}
	
	public void proceedToStationTwoControl() {
		if(stationTwoControl.getState() == State.OFF) {
			overview.changeMsg("Failed: station is off.");
			return;
		}
		screen.remove(overview);
		int width = 864;
		int height = 537;
		stationTwoControl.setBounds((dimension.width - width) / 2, (dimension.height - height) / 2, width, height);
		screen.getContentPane().add(stationTwoControl);
		stationTwoControl.setVisible(true);
		stationTwoControl.setSimulationVisible();
		stationTwoControl.createSampleBarcodedProducts();
		stationTwoControl.ms.setVisible(true);
		screen.repaint();
		screen.revalidate();
	}
	
	public void returnToLoginFromStationControl(int scsNo) {
		StationControl stationControl;
		if(scsNo == 1) {
			stationControl = stationOneControl;
		} else {
			stationControl = stationTwoControl;
		}
		screen.remove(stationControl);
		login.clear();
		screen.add(login);
		this.ls = new LoginSimulation(login,ass,station.keyboard);
		ls.setVisible(true);
		screen.repaint();
		screen.revalidate();
	}
	
	public void backToOverview(int scsNo) {
		StationControl stationControl;
		if(scsNo == 1) {
			stationControl = stationOneControl;
		} else {
			stationControl = stationTwoControl;
		}
		screen.remove(stationControl);
		screen.add(overview);
		screen.repaint();
		screen.revalidate();
	}
	
	public void exit() {
		screen.dispose();
		ls.dispose();
	}
	
	public void priceLookUp(StationControl stationctrl){
		stationctrl.setVisible(false);
		this.plu.setVisible(true);
	}
	
	public void returnFromPLU(StationControl stationctrl) {
		this.plu.setVisible(false);
		stationctrl.setVisible(true);
	}
	
	public void startUp(int scsNo){
		StationControl stationControl;
		if(scsNo == 1) {
			stationControl = stationOneControl;
		} else {
			stationControl = stationTwoControl;
		}
		if(stationControl.getState()==State.ON) {
			overview.changeMsg("Station "+ scsNo+" is already on.");
			overview.repaint();
			overview.revalidate();
		}else{
			overview.changeMsg("Station "+ scsNo+" is on.");
			stationControl.setState(State.ON);
			overview.changeSignal(scsNo, Color.GREEN);
			overview.repaint();
			overview.revalidate();
			//Create the customer GUI here
			if (scsNo == 1) {
				stationOne = new WelcomeGUI(stationOneControl, ass.scs1);
			}
			else {
				stationTwo = new WelcomeGUI(stationTwoControl, ass.scs2);
			}
		}
	}
	
	public void shutDown(int scsNo) {
		StationControl stationControl;
		if(scsNo == 1) {
			stationControl = stationOneControl;
		} else {
			stationControl = stationTwoControl;
		}
		if(stationControl.getIfBlocked()==true) {
			stationControl.setMsg("Can't shut down the station when it is blocked.");
			overview.changeMsg("Can't shut down the station when it is blocked.");
			overview.repaint();
			overview.revalidate();
			return;
		}
		
		if(stationControl.getState()==State.ON) {
			stationControl.setState(State.OFF);
			overview.changeMsg("Station "+ scsNo+" is off.");
			stationControl.setMsg("Station "+ scsNo+" is off.");
			overview.changeSignal(scsNo, Color.black);
			overview.repaint();
			overview.revalidate();
			if(scsNo==1) {
				ass.scs1.screen.getFrame().dispose();
			}else if(scsNo==2) {
				ass.scs2.screen.getFrame().dispose();
			}
		}else {
			overview.changeMsg("Station "+ scsNo+" is already off.");
		}
	}
}
