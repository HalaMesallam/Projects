package GUI;

import java.awt.SystemColor;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.lsmr.selfcheckout.devices.SupervisionStation;

public class AttendantStationGUI {
	private SupervisionStation ss;
	
	private JFrame mainFrame;
	private JPanel prevPanel, mainPanel;
	private JButton logoutButton;
	
	public AttendantStationGUI(JFrame frame, JPanel panel, SupervisionStation ss) {
		mainFrame = frame;
		this.ss = ss;
		prevPanel = panel;
		mainPanel = new JPanel();
		mainFrame.add(mainPanel);
		
		// temp location
		logoutButton = new JButton("Logout");
		logoutButton.setBackground(SystemColor.textHighlight);
		logoutButton.setBounds(10, 255, 165, 20);
		mainPanel.add(logoutButton);
		logoutButton.addActionListener(e -> {
			returnToLogin();
		});
		
		mainFrame.setVisible(true);
	}

	private void returnToLogin() {
		mainFrame.remove(mainPanel);
		mainFrame.revalidate();
		mainFrame.repaint();
		
		mainFrame.add(prevPanel);
	}
}
