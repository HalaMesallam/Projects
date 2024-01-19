package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.*;

import org.lsmr.selfcheckout.devices.BarcodeScanner;
import org.lsmr.selfcheckout.software.AttendantBlockException;
import org.lsmr.selfcheckout.software.MembershipCardDatabase;
import org.lsmr.selfcheckout.software.SelfCheckoutStationSoftware;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;

public class PaymentGUI {
	JFrame paymentFrame, buttonFrame, scanFrame;
	JPanel paymentPanel, buttonPanel;
	JButton payAllButton, payHalfButton, payCustomButton, addMembership, payCash, payCard, addMoreItems;
	SelfCheckoutStationSoftware scss;
	BarcodeScanner bss;
	private MembershipCardDatabase mcd;
	private WelcomeGUI startScreen;
	private StationControl stationControl;
	
	
	public PaymentGUI(JFrame frame, SelfCheckoutStationSoftware scss1, BarcodeScanner bss, JFrame scanFrameArg, StationControl stationControl) {
		scss = scss1;
		this.stationControl = stationControl;
		paymentFrame = new JFrame();
		paymentFrame.setPreferredSize(new Dimension(1280,720));
		paymentPanel = new JPanel();
		paymentPanel.setLayout(new GridLayout(4,1));
		
		scanFrame = scanFrameArg;
		
		buttonFrame = new JFrame();
		buttonFrame.setPreferredSize(new Dimension(1280,720));
		buttonFrame.setVisible(false);
		addPaymentTypes();
		
		
		paymentFrame.getContentPane().add(paymentPanel, BorderLayout.CENTER);
		paymentFrame.pack();
		paymentFrame.setVisible(true);
		
	}
	private void addPaymentTypes() {
		// Creates a new button to add membership
		addMembership = new JButton("Add Membership");

		addMembership.setPreferredSize(new Dimension(100, 100));
		// Adds a listener that will move the GUI to the checkout screen
		addMembership.addActionListener(e -> {
			enterMembership();
		});
		addMembership.setBackground(Color.WHITE);
		// Adds the button to the payment panel
		paymentPanel.add(addMembership);
		
		// Creates a new button to pay with cash
		payCash = new JButton("Pay with Cash");
		payCash.setPreferredSize(new Dimension(100, 100));
		// Adds a listener that will move the GUI of the checkout screen
		payCash.addActionListener(e -> {
			addPaymentButtons();
			changeDisplay(paymentFrame, buttonFrame);
			
		});		
		payCash.setBackground(Color.BLUE);
		// Adds the button to the payment panel
		paymentPanel.add(payCash);
		
		// Creates a new button to pay with card
		payCard = new JButton("Pay with Card");
		payCard.setPreferredSize(new Dimension(100, 100));
		// Adds a listener that will move the GUI to the checkout screen
		payCard.addActionListener(e -> {
			addPaymentButtons();
			changeDisplay(paymentFrame, buttonFrame);
		});
		payCard.setBackground(Color.BLUE);
		// Adds the button to the payment panel
		paymentPanel.add(payCard);
				
		// Creates a new button to add more items
		addMoreItems = new JButton("Add More Items (return to scan)");
		addMoreItems.setPreferredSize(new Dimension(100, 100));
		// Adds a listener that will move the GUI to the checkout screen
		addMoreItems.addActionListener(e -> {
			changeDisplay(paymentFrame, scanFrame);
		});	
		addMoreItems.setBackground(Color.CYAN);
		// Adds the button to the payment panel
		paymentPanel.add(addMoreItems);
	} 
	private void addPaymentButtons() {
		//buttonFrame.setVisible(true);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(3,1));
		// Creates a new button to pay all
		payAllButton = new JButton("Pay all (Display Total)");
		payAllButton.setPreferredSize(new Dimension(100, 100));
		// Adds a listener that will move the GUI to the checkout screen
		payAllButton.addActionListener(e -> {
			paymentcomplete();
			buttonFrame.dispose();
		});
		payAllButton.setBackground(Color.RED);
		// Adds the button to the payment panel
		buttonPanel.add(payAllButton);
		
		// Creates a new button to pay all
		payHalfButton = new JButton("Pay half (Display Half)");
		payHalfButton.setPreferredSize(new Dimension(100, 100));
		// Adds a listener that will move the GUI to the checkout screen
		payHalfButton.addActionListener(e -> {
			try {
				payHalf();
			} catch (AttendantBlockException e1) {
				
			}
		});		
		payHalfButton.setBackground(Color.RED);
		// Adds the button to the payment panel
		buttonPanel.add(payHalfButton);
		
		// Creates a new button to pay all
		payCustomButton = new JButton("Custom amount)");
		payCustomButton.setPreferredSize(new Dimension(100, 100));
		// Adds a listener that will move the GUI to the checkout screen
		payCustomButton.addActionListener(e -> {
		});		
		payCustomButton.setBackground(Color.RED);
		// Adds the button to the payment panel
		buttonPanel.add(payCustomButton);
		
		buttonFrame.getContentPane().add(buttonPanel, BorderLayout.CENTER);		
		buttonFrame.pack();
		buttonFrame.setVisible(true);
	}
	

	// switches the frame that is visible on the screen
	private void changeDisplay(JFrame oldFrame, JFrame newFrame) {
		// hides the payment frame
		oldFrame.dispose();
		
		paymentPanel.setVisible(false);
		// sets visible the scan frame
		newFrame.setVisible(true);
	}
	
	private void enterMembership() {
		MembershipCardDatabase mcd = new MembershipCardDatabase();
		//mcd.printdatabase();
		JPanel membershipPanel = new JPanel();
		membershipPanel.setLayout(null);
		
		JLabel searchLabel = new JLabel("Enter your 9 digit membership card details");
		searchLabel.setBounds(450, 200, 400, 20);
		membershipPanel.add(searchLabel);
		searchLabel.setHorizontalAlignment(JLabel.CENTER);
		
		JLabel errorLabel = new JLabel();
		errorLabel.setBounds(350, 350, 600, 20);
		membershipPanel.add(errorLabel);
		errorLabel.setHorizontalAlignment(JLabel.CENTER);
		
		JTextField searchBox = new JTextField(9);
		searchBox.setBounds(550, 250, 200, 30);
		membershipPanel.add(searchBox);
		searchBox.setHorizontalAlignment(JTextField.CENTER);
		
	
		
		// Creates a button which takes the entry from the textfield and searches the database
		JButton confirmButton = new JButton("Confirm");
		confirmButton.setBounds(500, 300, 90, 40);
		membershipPanel.add(confirmButton);
		confirmButton.addActionListener(e -> {
			String number = searchBox.getText();
			if (mcd.verifyCardNumber(number) == false) {
				errorLabel.setText("Membership Card number does not exist.");
				errorLabel.setForeground(Color.RED);
							
			}
			else {
				System.out.println(mcd.verifyCardNumber(number));
				errorLabel.setText("Membership Card confirmed.");
				errorLabel.setForeground(Color.GREEN);
			}	
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBounds(700, 300, 90, 40);
		membershipPanel.add(cancelButton);
		cancelButton.addActionListener(e -> {
			changePanel(membershipPanel, paymentPanel);
			paymentFrame.dispose();
		});
		
		
		changePanel(paymentPanel, membershipPanel);
	}
	private void paymentcomplete() {
		JPanel receiptPanel = new JPanel();
		receiptPanel.setLayout(null);
		
		JLabel receiptLabel = new JLabel("Would you like your receipt?");
		receiptLabel.setBounds(450, 100, 400, 20);
		receiptPanel.add(receiptLabel);
		receiptLabel.setHorizontalAlignment(JLabel.CENTER);
		receiptLabel.setForeground(Color.BLACK);
		
		JLabel thanksLabel = new JLabel("Thank you for shopping with us. Have a nice day");
		
		JButton yesButton = new JButton("Yes");
		yesButton.setBounds(600, 300, 90, 40);
		receiptPanel.add(yesButton);
		
		JButton noButton = new JButton("No");
		noButton.setBounds(800, 300, 90, 40);
		receiptPanel.add(noButton);
		
		yesButton.addActionListener(e -> {
			scanFrame.getContentPane().remove(yesButton);
			scanFrame.getContentPane().remove(noButton);
			receiptLabel.setText("Printing Receipt......");
			thanksLabel.setBounds(300, 150, 400, 20);
			receiptPanel.add(thanksLabel);
			thanksLabel.setHorizontalAlignment(JLabel.CENTER);
			thanksLabel.setForeground(Color.GREEN);
			returnToWelcome(5000, receiptPanel);
			
		});
		
		noButton.addActionListener(e -> {
			thanksLabel.setBounds(300, 150, 400, 20);
			receiptPanel.add(thanksLabel);
			thanksLabel.setHorizontalAlignment(JLabel.CENTER);
			thanksLabel.setForeground(Color.GREEN);
			
			returnToWelcome(5000, receiptPanel);
		});
		changePanel(buttonPanel, receiptPanel);
	}
	
	private void payHalf() throws AttendantBlockException {	
		double total = (scss.total()).doubleValue();
		double half = total/2;
		String strHalf = String.valueOf(half);
		
		JPanel halfPanel = new JPanel();
		halfPanel.setLayout(null);
		
		JLabel remainderLabel = new JLabel("You remaining balance is:   " + strHalf + " \n Do you want to change payment methods?");
		remainderLabel.setBounds(450, 100, 500, 20);
		halfPanel.add(remainderLabel);
		remainderLabel.setHorizontalAlignment(JLabel.CENTER);
		remainderLabel.setForeground(Color.BLACK);
		
		JButton yesButton = new JButton("Yes");
		yesButton.setBounds(600, 300, 90, 40);
		halfPanel.add(yesButton);
		yesButton.addActionListener(e -> {
			
			changePanel(halfPanel, paymentPanel);
		});
		
		JButton noButton = new JButton("No");
		noButton.setBounds(800, 300, 90, 40);
		halfPanel.add(noButton);
		noButton.addActionListener(e -> {
			paymentcomplete();
		});
		
		changePanel(buttonPanel, halfPanel);
	}
	
	// changes between a specified old panel and new panel
	private void changePanel(JPanel oldPanel, JPanel newPanel) {
		// Removes the current panel from the frame
		scanFrame.remove(oldPanel);
		// Sets the new panel to the frame
		scanFrame.setContentPane(newPanel);
		// Packs the contents into frame
		scanFrame.pack();
		// Set the new contents to be visible
		scanFrame.setVisible(true);
	}

	// a one tick timer
	private void returnToWelcome(int delay, JPanel oldPanel) {
		
		Timer timer = new Timer(5000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("timer3!");
				
				//paymentFrame.dispose();
				scanFrame.dispose();
				
				startScreen = new WelcomeGUI(stationControl, scss.scs);
			}
			
		});
		timer.setRepeats(false);
		timer.setInitialDelay(delay);
		timer.start();
	}
}
