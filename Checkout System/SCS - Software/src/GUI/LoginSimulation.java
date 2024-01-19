package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.lsmr.selfcheckout.devices.Keyboard;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginSimulation extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private Login login;
	private AttendantStationSoftware ass;
	private Keyboard kb;
	
	/**
	 * Create the dialog.
	 */
	public LoginSimulation(Login login, AttendantStationSoftware ass, Keyboard kb) {
		setTitle("Login Simulation");
		this.login = login;
		this.ass= ass;
		this.kb = kb;
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JButton btnNewButton = new JButton("Type in valid credential");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					validCredential();
				}
			});
			btnNewButton.setBounds(101, 65, 226, 29);
			contentPanel.add(btnNewButton);
		}
		{
			JButton btnNewButton_1 = new JButton("Type in invalid credential");
			btnNewButton_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					invalidCredential();
				}
			});
			btnNewButton_1.setBounds(101, 137, 226, 29);
			contentPanel.add(btnNewButton_1);
		}
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	public void validCredential() {
		kb.type("admin");
		String username = ass.getKeyboardFacade().getInput();
		ass.getKeyboardFacade().clearBuffer();
		
		kb.type("admin");
		String password = ass.getKeyboardFacade().getInput();
		ass.getKeyboardFacade().clearBuffer();
		
		login.setUsername(username);
		login.setPassword(password);
	}

	public void invalidCredential() {
		kb.type("nonAdmin");
		String username = ass.getKeyboardFacade().getInput();
		ass.getKeyboardFacade().clearBuffer();
		
		kb.type("nonAdmin");
		String password = ass.getKeyboardFacade().getInput();
		ass.getKeyboardFacade().clearBuffer();
		
		login.setUsername(username);
		login.setPassword(password);
	}
}
