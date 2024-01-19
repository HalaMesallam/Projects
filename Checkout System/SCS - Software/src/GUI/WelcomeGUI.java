package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.util.Currency;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.TouchScreen;

public class WelcomeGUI extends JFrame {
	JFrame newFrame;
	JPanel welcomePanel;
	JButton startButton;
	SelfCheckoutStation scs;
	StationControl stationControl;
	// Demonimations of accepted banknotes and coins, used to initiate the SelfCheckoutStation
	int[] bills = new int[] {5,10,20,50,100};
	BigDecimal[] coins = new BigDecimal[] {new BigDecimal(0.05), new BigDecimal(0.10), new BigDecimal(0.25), new BigDecimal(1.00), new BigDecimal(2.00)};

	public WelcomeGUI(StationControl stationControl, SelfCheckoutStation scs) {
		
		this.scs = scs;
		this.stationControl = stationControl;

		// Initiates new frame on touchscreen
		TouchScreen screen = scs.screen;
		
		// Import the frame for use from Touchscreen
		newFrame = screen.getFrame();
		
		// Creates a new panel to add items to
		welcomePanel = new JPanel();
		
		// Sets the layout of the panel
		welcomePanel.setLayout(new GridLayout(1,1));
		
		// Initiates the things  to be added to the panel
		addStart();
		
		// Adds the panel to the frame
		newFrame.getContentPane().add(welcomePanel, BorderLayout.CENTER);
		
		// Sets the default size of the frame
		newFrame.setPreferredSize(new Dimension(1280, 720));

		// Packs it all together
		newFrame.pack();
		// Ensures the frame is visible to the user
		
		// Resets frame for adding things
		newFrame.setVisible(false);
		// Finally makes it visible
		newFrame.setVisible(true);
	}
	
	private void addStart() {
		// Initiates new button with the following text
		startButton = new JButton("PRESS ANYWHERE TO START");
		
		// Styling for the button
		startButton.setFont(new Font("Freestyle Script", Font.BOLD, 100));
		startButton.setBackground(Color.GREEN);
		
		// Adds the button to the panel
		welcomePanel.add(startButton);
		
		// Adds an action listener to the button
		startButton.addActionListener(e -> {
			proceedToCheckout();
		});
	}
	
	private void proceedToCheckout() {
		// Remove the current contents displayed on the touchscreen
		newFrame.remove(welcomePanel);
		
		// Create the next step
		ScanGUI nextStep = new ScanGUI(newFrame, scs, stationControl);
	}
}
