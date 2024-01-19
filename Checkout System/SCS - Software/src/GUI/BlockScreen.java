package GUI;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;

public class BlockScreen extends JPanel {

	/**
	 * Create the panel.
	 */
	public BlockScreen() {
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("This station is blocked");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 32));
		lblNewLabel.setBounds(56, 40, 324, 101);
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Please wait for the attendant");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 28));
		lblNewLabel_1.setBounds(45, 134, 350, 63);
		add(lblNewLabel_1);
		
	}

}
