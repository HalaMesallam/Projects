package GUI;

import javax.swing.JPanel;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SupervisionStation;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.SystemColor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StationsOverview extends JPanel {
	private SupervisionStation ss;
	private AttendantGUIController controller;
	private JLabel station1;
	private JLabel stationSignal1;
	private JButton stationButton1;
	
	private JLabel station2;
	private JLabel stationSignal2;
	private JButton stationButton2;
	private JLabel message;
	

	/**
	 * Create the panel.
	 */
	public StationsOverview(AttendantGUIController controller, SupervisionStation ss) {
		this.ss = ss;
		this.controller = controller;
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(6, 6, 217, 173);
		add(panel);
		panel.setLayout(null);
		
		JLabel station1 = new JLabel("Station 1");
		station1.setBounds(56, 6, 99, 38);
		station1.setFont(new Font("Times New Roman", Font.BOLD, 26));
		panel.add(station1);
		
		stationSignal1 = new JLabel("");
		stationSignal1.setOpaque(true);
		stationSignal1.setBackground(Color.BLACK);
		stationSignal1.setBounds(76, 44, 61, 45);
		panel.add(stationSignal1);
		
		JButton stationButton1 = new JButton("Details");
		stationButton1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				controller.proceedToStationOneControl();
			}
			
		});
		stationButton1.setBounds(49, 101, 105, 29);
		panel.add(stationButton1);
		
		JButton btnStartUp = new JButton("Start up");
		btnStartUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.startUp(1);
			}
		});
		btnStartUp.setBounds(6, 138, 105, 29);
		panel.add(btnStartUp);
		
		JButton stationButton1_1_1 = new JButton("Shut down");
		stationButton1_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.shutDown(1);
			}
		});
		stationButton1_1_1.setBounds(106, 138, 105, 29);
		panel.add(stationButton1_1_1);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBounds(256, 6, 217, 173);
		add(panel_1);
		
		JLabel station2 = new JLabel("Station 2");
		station2.setFont(new Font("Times New Roman", Font.BOLD, 26));
		station2.setBounds(56, 6, 99, 38);
		panel_1.add(station2);
		
		stationSignal2 = new JLabel("");
		stationSignal2.setOpaque(true);
		stationSignal2.setBackground(Color.BLACK);
		stationSignal2.setBounds(77, 44, 61, 45);
		panel_1.add(stationSignal2);
		
		JButton stationButton2 = new JButton("Details");
		stationButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.proceedToStationTwoControl();
			}
		});
		stationButton2.setBounds(50, 101, 117, 29);
		panel_1.add(stationButton2);
		
		JButton btnStartUp_1 = new JButton("Start up");
		btnStartUp_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.startUp(2);
			}
		});
		btnStartUp_1.setBounds(6, 138, 105, 29);
		panel_1.add(btnStartUp_1);
		
		JButton stationButton1_1_1_1 = new JButton("Shut down");
		stationButton1_1_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.shutDown(2);
			}
		});
		stationButton1_1_1_1.setBounds(106, 138, 105, 29);
		panel_1.add(stationButton1_1_1_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBounds(499, 6, 217, 173);
		add(panel_2);
		
		JLabel lblStation_1 = new JLabel("Station 3");
		lblStation_1.setFont(new Font("Times New Roman", Font.BOLD, 26));
		lblStation_1.setBounds(56, 6, 99, 38);
		panel_2.add(lblStation_1);
		
		JLabel lblNewLabel_1_2 = new JLabel("");
		lblNewLabel_1_2.setOpaque(true);
		lblNewLabel_1_2.setBackground(Color.BLACK);
		lblNewLabel_1_2.setBounds(77, 44, 61, 45);
		panel_2.add(lblNewLabel_1_2);
		
		JButton btnNewButton_2 = new JButton("Details");
		btnNewButton_2.setBounds(49, 101, 117, 29);
		panel_2.add(btnNewButton_2);
		
		JButton stationButton1_1_1_2 = new JButton("Shut down");
		stationButton1_1_1_2.setBounds(106, 138, 105, 29);
		panel_2.add(stationButton1_1_1_2);
		
		JButton btnStartUp_1_1 = new JButton("Start up");
		btnStartUp_1_1.setBounds(6, 138, 105, 29);
		panel_2.add(btnStartUp_1_1);
		
		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBounds(6, 211, 217, 173);
		add(panel_3);
		
		JLabel lblStation_2 = new JLabel("Station 4");
		lblStation_2.setFont(new Font("Times New Roman", Font.BOLD, 26));
		lblStation_2.setBounds(56, 6, 99, 38);
		panel_3.add(lblStation_2);
		
		JLabel lblNewLabel_1_3 = new JLabel("");
		lblNewLabel_1_3.setOpaque(true);
		lblNewLabel_1_3.setBackground(Color.BLACK);
		lblNewLabel_1_3.setBounds(77, 44, 61, 45);
		panel_3.add(lblNewLabel_1_3);
		
		JButton btnNewButton_3 = new JButton("Details");
		btnNewButton_3.setBounds(49, 101, 117, 29);
		panel_3.add(btnNewButton_3);
		
		JButton btnStartUp_1_2 = new JButton("Start up");
		btnStartUp_1_2.setBounds(0, 138, 105, 29);
		panel_3.add(btnStartUp_1_2);
		
		JButton stationButton1_1_1_3 = new JButton("Shut down");
		stationButton1_1_1_3.setBounds(106, 138, 105, 29);
		panel_3.add(stationButton1_1_1_3);
		
		JPanel panel_4 = new JPanel();
		panel_4.setLayout(null);
		panel_4.setBounds(256, 211, 217, 173);
		add(panel_4);
		
		JLabel lblStation_3 = new JLabel("Station 5");
		lblStation_3.setFont(new Font("Times New Roman", Font.BOLD, 26));
		lblStation_3.setBounds(56, 6, 99, 38);
		panel_4.add(lblStation_3);
		
		JLabel lblNewLabel_1_4 = new JLabel("");
		lblNewLabel_1_4.setOpaque(true);
		lblNewLabel_1_4.setBackground(Color.BLACK);
		lblNewLabel_1_4.setBounds(77, 44, 61, 45);
		panel_4.add(lblNewLabel_1_4);
		
		JButton btnNewButton_4 = new JButton("Details");
		btnNewButton_4.setBounds(49, 101, 117, 29);
		panel_4.add(btnNewButton_4);
		
		JButton btnStartUp_1_3 = new JButton("Start up");
		btnStartUp_1_3.setBounds(0, 142, 105, 29);
		panel_4.add(btnStartUp_1_3);
		
		JButton stationButton1_1_1_4 = new JButton("Shut down");
		stationButton1_1_1_4.setBounds(106, 142, 105, 29);
		panel_4.add(stationButton1_1_1_4);
		
		JPanel panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBounds(499, 211, 217, 173);
		add(panel_5);
		
		JLabel lblStation_4 = new JLabel("Station 6");
		lblStation_4.setFont(new Font("Times New Roman", Font.BOLD, 26));
		lblStation_4.setBounds(56, 6, 99, 38);
		panel_5.add(lblStation_4);
		
		JLabel lblNewLabel_1_5 = new JLabel("");
		lblNewLabel_1_5.setOpaque(true);
		lblNewLabel_1_5.setBackground(Color.BLACK);
		lblNewLabel_1_5.setBounds(77, 44, 61, 45);
		panel_5.add(lblNewLabel_1_5);
		
		JButton btnNewButton_5 = new JButton("Details");
		btnNewButton_5.setBounds(49, 101, 117, 29);
		panel_5.add(btnNewButton_5);
		
		JButton btnStartUp_1_4 = new JButton("Start up");
		btnStartUp_1_4.setBounds(0, 138, 105, 29);
		panel_5.add(btnStartUp_1_4);
		
		JButton stationButton1_1_1_5 = new JButton("Shut down");
		stationButton1_1_1_5.setBounds(106, 138, 105, 29);
		panel_5.add(stationButton1_1_1_5);
		
		JButton LogOutButton = new JButton("Log out");
		LogOutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.returnToLoginFromOverview();
			}
		});
		LogOutButton.setBounds(6, 396, 92, 29);
		add(LogOutButton);
		
		message = new JLabel("");
		message.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		message.setBounds(110, 393, 609, 29);
		add(message);

	}
	
	public void changeSignal(int stationIndex, Color color) {
		switch(stationIndex){
		case 1: 
			stationSignal1.setBackground(color);
			break;
		case 2:
			stationSignal2.setBackground(color);
			break;
		}
		repaint();
		revalidate();
	}
	
	public void changeMsg(String msg) {
		message.setText(msg);
	}
}
