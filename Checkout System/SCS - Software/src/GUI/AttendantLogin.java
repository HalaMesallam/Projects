package GUI;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import org.lsmr.selfcheckout.devices.SupervisionStation;
import org.lsmr.selfcheckout.devices.TouchScreen;
import org.lsmr.selfcheckout.software.AttendantOverride;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.SystemColor;

public class AttendantLogin {
	private SupervisionStation ss;
	private AttendantOverride mAttendant;
	//GUI stuff
	private JFrame mainFrame;
	private JPanel mainPanel;
	private JLabel userID, userPassword, failMsg;
	private JTextField userText;
	private JPasswordField passText;
	private JButton loginButton;
	private JButton shutdownButton;
	
	/**
	 *  Creates the main login GUI
	 */
	public AttendantLogin() {
		ss = new SupervisionStation();
		mAttendant = new AttendantOverride(ss); // Main attendant station instance (required for login in)
		TouchScreen screen = ss.screen;
		mainFrame = screen.getFrame();
		mainPanel = new JPanel();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(new Dimension(1280, 720));
		mainFrame.getContentPane().add(mainPanel);
		
		placeComponents(mainPanel);
		
		mainFrame.setVisible(true);
	}
	
	/**
	 *  Place require login components on a panel
	 * @param panel: The panel to place to components on
	 */
	private void placeComponents(JPanel panel) {
		panel.setLayout(null);
		
		userID = new JLabel("User ID");
		userID.setBounds(50, 50, 80, 25);
		panel.add(userID);
		
		userText = new JTextField();
		userText.setBounds(125, 50, 165, 25);
		panel.add(userText);
		userText.setColumns(10);
		
		userPassword = new JLabel("Password");
		userPassword.setBounds(50, 85, 80, 25);
		panel.add(userPassword);
		
		passText = new JPasswordField();
		passText.setBounds(125, 85, 165, 25);
		panel.add(passText);
		passText.setColumns(10);
		
		loginButton = new JButton("Login");
		loginButton.setBackground(SystemColor.textHighlight);
		loginButton.setBounds(50, 120, 85, 25);
		panel.add(loginButton);
		loginButton.addActionListener(e -> {
			loginAttempt();
		});
		
		failMsg = new JLabel("");
		failMsg.setBounds(150, 120, 165, 25);
		panel.add(failMsg);
		
		shutdownButton = new JButton("Shutdown");
		shutdownButton.setBackground(SystemColor.textHighlight);
		shutdownButton.setBounds(10, 255, 165, 20);
		panel.add(shutdownButton);
		shutdownButton.addActionListener(e -> {
			shutdown();
		});
	}
	
	/**
	 *  Shutdown program
	 */
	private void shutdown() {
		System.exit(0);
	}

	/**
	 *  Attempt to login with provided credentials
	 */
	public void loginAttempt() {
		String user = userText.getText();
		String pass = new String(passText.getPassword());
		System.out.println(user + ":" + pass);
		if (mAttendant.login(user,pass)) {
			System.out.println("Attendant Station Login Successful");
			userText.setText("");
			passText.setText("");
			procceedToAttendantStationGui();
		} else {
			failMsg.setText("Login failed, please try again");
		}
	}
	
	/**
	 *  Move on to the next GUI
	 */
	private void procceedToAttendantStationGui() {
		mainFrame.remove(mainPanel);
		mainFrame.revalidate();
		mainFrame.repaint();
		
		AttendantStationGUI attendantStationGUI = new AttendantStationGUI(mainFrame, mainPanel, ss);
	}

	public static void main(String[] args) {
		AttendantLogin attendantLogin = new AttendantLogin();
	}
}
