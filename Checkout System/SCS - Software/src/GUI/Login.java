package GUI;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.Keyboard;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.KeyboardObserver;
import org.lsmr.selfcheckout.software.AttendantOverride;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.JPasswordField;

public class Login extends JPanel {
	private JTextField textField;
	private JPasswordField passwordField;
	private JLabel failedMessage;
	private AttendantOverride ao;
	private AttendantGUIController controller;

	/**
	 * Create the panel.
	 * @param attendantGUIController 
	 */
	public Login(AttendantGUIController controller) {
		this.controller = controller;
		
		setBackground(SystemColor.textHighlight);
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setBounds(67, 102, 88, 40);
		add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(155, 109, 153, 26);
		add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Self-checkout Station");
		lblNewLabel_1.setFont(new Font("Times", Font.BOLD, 22));
		lblNewLabel_1.setBounds(111, 23, 210, 26);
		add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Attendant Login");
		lblNewLabel_2.setBounds(155, 65, 110, 16);
		add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Password:");
		lblNewLabel_3.setBounds(67, 165, 88, 16);
		add(lblNewLabel_3);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loginAttempt();
			}	
		});
		btnNewButton.setBounds(177, 235, 117, 29);
		add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.exit();
			}
		});
		btnNewButton_1.setBounds(306, 235, 117, 29);
		add(btnNewButton_1);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(155, 160, 153, 26);
		add(passwordField);
		
		failedMessage = new JLabel("");
		failedMessage.setBounds(155, 198, 153, 16);
		add(failedMessage);
		
	}
	

	public void loginAttempt() {
		this.ao = new AttendantOverride(controller.getStation());
		String user = textField.getText();
		String pass = new String(passwordField.getPassword());
		if (ao.login(user,pass)) {
			controller.loginSuccess();
		} else {
			failedMessage.setText("Login failed.");
			this.repaint();
			this.revalidate();
		}
	}


	public void setUsername(String username) {
		// TODO Auto-generated method stub
		textField.setText(username);
		this.repaint();
		this.revalidate();
	}


	public void setPassword(String password) {
		// TODO Auto-generated method stub
		passwordField.setText(password);
		this.repaint();
		this.revalidate();
	}
	
	public void clear() {
		textField.setText("");
		passwordField.setText("");
	}
}
