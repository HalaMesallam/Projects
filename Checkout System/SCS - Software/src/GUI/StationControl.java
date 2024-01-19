package GUI;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.software.AttendantBlockException;
import org.lsmr.selfcheckout.software.AttendantMaintenance;
import org.lsmr.selfcheckout.software.ReceiptPrinterFacadeListener;
import org.lsmr.selfcheckout.software.SelfCheckoutStationSoftware;

import GUI.StationControl.State;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.awt.event.ActionEvent;

public class StationControl extends JPanel {
	private SelfCheckoutStation scs;
	private AttendantMaintenance am;
	private AttendantGUIController controller;
	private StationsOverview so;
	private JLabel discrepancyLabel;
	private boolean discrepancyDetected;
	private JLabel banknoteStorageState;
	private boolean bsState = true;
	private JLabel coinStorageState;
	private boolean csState = true;
	private JLabel banknoteDispenserState;
	private boolean bdState = true;
	private JLabel coinDispenserState;
	private boolean cdState = true;
	private JLabel paperState;
	private boolean ps = true;
	private JLabel inkState;
	private boolean is = true;
	private JLabel blockState;
	private State state;
	
	public enum State{
		ON,
		OFF,
	}

	private boolean ifBlocked = false;

	public MaintenanceSimulation ms;

	private String normal = "Normal";
	private String empty = "Empty";
	private String full = "Full";
	private String no = "No";
	private String yes = "Yes";
	private String on = "On";
	private String off = "Off";
	
	private DefaultListModel itemModel;
	private JList itemList;
	private DefaultListModel priceModel;
	private JList priceList;
	private JScrollPane listScrollPane;
	private Container cartPanel;
	private int index;
	private JLabel totalPrice;
	private JLabel subTotalPrice;
	private JLabel taxPrice;
	private JPanel scanPanel;
	private static final DecimalFormat df = new DecimalFormat("0.00");
	private SelfCheckoutStationSoftware scss;
	private JLabel message;
	private int scsNo;
	private BlockScreen blockScreen;
	private JLabel onOffLabel;
	private JFrame sampleBarcodes;
	private StationControl stationctrl;
	
	/**
	 * Create the panel.
	 */
	public StationControl(AttendantGUIController controller, SelfCheckoutStation scs, StationsOverview so,
			SelfCheckoutStationSoftware scss, int scsNo) {
		this.scsNo= scsNo;
		this.scss = scss;
		this.controller = controller;
		this.scs = scs;
		this.so = so;
		this.state= State.OFF;
		this.stationctrl = this;
		this.blockScreen = new BlockScreen();
		this.am = new AttendantMaintenance(scs, this);
		this.ms = new MaintenanceSimulation(scs, this, am);
		ms.setVisible(false);
		setLayout(null);
		
		//For the item list
		scanPanel = new JPanel();
		scanPanel.setBounds(6, 5, 575, 452);
		scanPanel.setLayout(null);

		cartPanel = new JPanel();
		cartPanel.setBounds(0, 0, 569, 308);
		scanPanel.add(cartPanel);
		createItemCart();
		createTotalPriceDisplay();

		JPanel panel = new JPanel();
		panel.setBounds(6, 6, 587, 463);
		add(panel);
		panel.setLayout(null);
		panel.add(scanPanel);
		// Creates a new border panel for displaying totals due
		JPanel totalPanel = new JPanel();
		totalPanel.setBounds(6, 309, 563, 137);
		scanPanel.add(totalPanel);
		totalPanel.setBackground(Color.GREEN);

		// Creates a new label for the total due text
		JLabel totalLabel = new JLabel("Total :");
		totalLabel.setBounds(10, 60, 97, 25);
		totalLabel.setFont(new Font("Comic Sans", Font.BOLD, 20));
		totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		// Creates a new label for the subtotal due text
		JLabel subTotalLabel = new JLabel("Subtotal :");
		subTotalLabel.setBounds(10, 10, 97, 25);
		subTotalLabel.setFont(new Font("Comic Sans", Font.BOLD, 20));
		subTotalLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		// Creates a new label for the tax due text
		JLabel taxLabel = new JLabel("GST :");
		taxLabel.setBounds(10, 35, 97, 25);
		taxLabel.setFont(new Font("Comic Sans", Font.BOLD, 20));
		taxLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		// Creates a new label showing the total amount that is due
		totalPrice = new JLabel("$ 0.00");
		totalPrice.setBounds(107, 60, 97, 25);
		totalPrice.setFont(new Font("Comic Sans", Font.BOLD, 20));
		totalPrice.setHorizontalAlignment(SwingConstants.RIGHT);

		// Creates a new label showing the subtotal amount that is due before tax
		subTotalPrice = new JLabel("$ 0.00");
		subTotalPrice.setBounds(107, 10, 97, 25);
		subTotalPrice.setFont(new Font("Comic Sans", Font.BOLD, 20));
		subTotalPrice.setHorizontalAlignment(SwingConstants.RIGHT);

		// Creates a new label showing the tax amount
		taxPrice = new JLabel("$ 0.00");
		taxPrice.setBounds(107, 35, 97, 25);
		taxPrice.setFont(new Font("Comic Sans", Font.BOLD, 20));
		taxPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		totalPanel.setLayout(null);

		// Adds the labels to the total panel
		totalPanel.add(subTotalLabel);
		totalPanel.add(subTotalPrice);
		totalPanel.add(taxLabel);
		totalPanel.add(taxPrice);
		totalPanel.add(totalLabel);
		totalPanel.add(totalPrice);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(6, 481, 587, 45);
		add(panel_1);
		panel_1.setLayout(null);

		JButton logOutButton = new JButton("Log out");
		logOutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ms.dispose();
				sampleBarcodes.dispose();
				controller.returnToLoginFromStationControl(scsNo);
			}
		});
		logOutButton.setBounds(6, 6, 117, 29);
		panel_1.add(logOutButton);

		JButton returnButton = new JButton("return");
		returnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ms.setVisible(false);
				controller.backToOverview(scsNo);
			}
		});
		returnButton.setBounds(135, 6, 117, 29);
		panel_1.add(returnButton);
		
		message = new JLabel("");
		message.setBounds(280, 11, 267, 16);
		panel_1.add(message);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(623, 6, 215, 520);
		add(panel_2);
		panel_2.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("Ink:");
		lblNewLabel_1.setBounds(6, 20, 61, 16);
		panel_2.add(lblNewLabel_1);

		JLabel lblNewLabel = new JLabel("Paper:");
		lblNewLabel.setBounds(6, 48, 61, 16);
		panel_2.add(lblNewLabel);

		JLabel lblNewLabel_2 = new JLabel("Coin Dispenser:");
		lblNewLabel_2.setBounds(6, 76, 105, 16);
		panel_2.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("BanknoteDispenser:");
		lblNewLabel_3.setBounds(6, 104, 132, 16);
		panel_2.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("Coin Storage:");
		lblNewLabel_4.setBounds(6, 132, 105, 16);
		panel_2.add(lblNewLabel_4);

		JLabel lblNewLabel_6 = new JLabel("");
		lblNewLabel_6.setBounds(50, 385, 61, 16);
		panel_2.add(lblNewLabel_6);

		inkState = new JLabel(normal);
		inkState.setBounds(136, 20, 61, 16);
		panel_2.add(inkState);

		paperState = new JLabel(normal);
		paperState.setBounds(136, 48, 61, 16);
		panel_2.add(paperState);

		coinDispenserState = new JLabel(normal);
		coinDispenserState.setBounds(136, 76, 61, 16);
		panel_2.add(coinDispenserState);

		banknoteDispenserState = new JLabel(normal);
		banknoteDispenserState.setBounds(136, 104, 61, 16);
		panel_2.add(banknoteDispenserState);

		coinStorageState = new JLabel(normal);
		coinStorageState.setBounds(136, 132, 61, 16);
		panel_2.add(coinStorageState);

		JLabel lblNewLabel_14 = new JLabel("Banknote Storage:");
		lblNewLabel_14.setBounds(6, 160, 121, 16);
		panel_2.add(lblNewLabel_14);

		banknoteStorageState = new JLabel(normal);
		banknoteStorageState.setBounds(136, 160, 61, 16);
		panel_2.add(banknoteStorageState);

		JLabel lblNewLabel_16 = new JLabel("Discrepancy:");
		lblNewLabel_16.setBounds(6, 222, 93, 16);
		panel_2.add(lblNewLabel_16);

		discrepancyLabel = new JLabel(no);
		discrepancyLabel.setBounds(136, 222, 61, 16);
		panel_2.add(discrepancyLabel);

		JButton approveButton = new JButton("Approve Discrepancy");
		approveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(state == State.OFF) {
					setMsg("Failed: station is off.");
					return;
				}
				approveDiscrepancy();
			}
		});
		approveButton.setBounds(23, 307, 174, 41);
		panel_2.add(approveButton);

		JButton lookUpButton = new JButton("Look up");
		lookUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(state == State.OFF) {
					setMsg("Failed: station is off.");
					return;
				}
				controller.priceLookUp(stationctrl);
			}
		});
		lookUpButton.setBounds(23, 360, 174, 41);
		panel_2.add(lookUpButton);

		JButton blockButton = new JButton("Block/Unblock");
		blockButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(state == State.OFF) {
					setMsg("Failed: station is off.");
					return;
				}
				toggleBlock();
			}
		});
		blockButton.setBounds(23, 413, 174, 41);
		panel_2.add(blockButton);

		JButton shutDownButton = new JButton("Shut Down");
		shutDownButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(state == State.OFF) {
					setMsg("Failed: station is off.");
					return;
				}
				controller.shutDown(scsNo);
			}
		});
		shutDownButton.setBounds(23, 466, 174, 41);
		panel_2.add(shutDownButton);

		JLabel lblNewLabel_5 = new JLabel("Blocked: ");
		lblNewLabel_5.setBounds(6, 194, 61, 16);
		panel_2.add(lblNewLabel_5);

		blockState = new JLabel("No");
		blockState.setBounds(136, 194, 61, 16);
		panel_2.add(blockState);
		
		JLabel lblNewLabel_7 = new JLabel("On/Off:");
		lblNewLabel_7.setBounds(6, 250, 61, 16);
		panel_2.add(lblNewLabel_7);
		
		onOffLabel = new JLabel("Off");
		onOffLabel.setBounds(136, 250, 61, 16);
		panel_2.add(onOffLabel);
	}

	private void createItemCart() {
		// Creates an empty list model which displays items
		itemModel = new DefaultListModel();

		// Creates an empty list model which displays prices
		priceModel = new DefaultListModel();
		Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		cartPanel.setLayout(null);
		itemList = new JList(itemModel);
		itemList.setFixedCellWidth(400);
		itemList.setFont(new Font("Comic Sans", Font.BOLD, 20));
		itemList.setBackground(Color.LIGHT_GRAY);
		itemList.setSelectionBackground(Color.WHITE);
		priceList = new JList(priceModel);
		priceList.setFixedCellWidth(100);
		priceList.setFont(new Font("Comic Sans", Font.BOLD, 20));
		priceList.setBackground(Color.LIGHT_GRAY);
		priceList.setSelectionBackground(Color.WHITE);

		// Creates a new panel to add the 2 lists to
		JPanel scannedItemPanel = new JPanel();
		scannedItemPanel.setMinimumSize(new Dimension(540, 720));
		scannedItemPanel.setLayout(new BorderLayout());
		scannedItemPanel.add(itemList, BorderLayout.LINE_START);
		scannedItemPanel.add(priceList, BorderLayout.LINE_END);
		scannedItemPanel.setBackground(Color.lightGray);

		// Creates a new ScrollPane for dynamic item addition
		listScrollPane = new JScrollPane(scannedItemPanel);
		listScrollPane.setBounds(6, 6, 557, 296);
		listScrollPane.setBorder(padding);
		listScrollPane.setBackground(Color.GREEN);

		// Adds the scanned item scrollpane to the GUI
		cartPanel.add(listScrollPane);

		// Adds a listener that when selecting an item from the list, it also selects
		// it's corresponding price
		itemList.addListSelectionListener(e -> {
			index = itemList.getSelectedIndex();
			priceList.setSelectedIndex(index);
		});
		// Adds a listener that when selecting an price from the list, it also selects
		// it's corresponding item
		priceList.addListSelectionListener(e -> {
			index = priceList.getSelectedIndex();
			itemList.setSelectedIndex(index);
		});
	}

	/**
	 * Retrieves the total amount due from the selfCheckoutStationSoftware and sets
	 * and computes the values before tax, the tax, and after tax
	 */
	public void setTotals() {
		try {
			// Sets the price before tax
			subTotalPrice.setText("$ " + df.format(scss.total().doubleValue() / 1.05));
			// Computes and sets the tax
			taxPrice.setText("$ " + df.format(((scss.total().doubleValue() / 1.05) * 0.05)));
			// Sets the total
			totalPrice.setText("$ " + df.format((scss.total().doubleValue())));
		} catch (AttendantBlockException e) {
			// NEEDS TO BE CHANGED
			e.printStackTrace();
		}
	}

	public void createSampleBarcodedProducts() {
		sampleBarcodes = new JFrame("Sample Barcoded Products");
		sampleBarcodes.getContentPane().setLayout(new GridLayout(1, 2));

		JPanel barcodeProductPanel = new JPanel();
		barcodeProductPanel.setLayout(new GridLayout(2, 2));

		// This is a temporary button to add products
		// Icon icon = new ImageIcon("milk.webp");
		JButton addItemButton1 = new JButton("DairyLand 2% Milk");
		barcodeProductPanel.add(addItemButton1);

		JButton addItemButton2 = new JButton("50 Pc Disposable Face Mask");
		barcodeProductPanel.add(addItemButton2);

		JButton addItemButton3 = new JButton("EVGA GeForce RTX 3080 Ti FTW3");
		barcodeProductPanel.add(addItemButton3);

		JButton addItemButton4 = new JButton("CrackerBarrel block of cheese");
		barcodeProductPanel.add(addItemButton4);

		addItemButton1.addActionListener(e -> {
			BarcodedItem newItem = new BarcodedItem(new Barcode(new Numeral[] { Numeral.one }), 1200);
			scs.mainScanner.scan(newItem);
			System.out.println(scs.mainScanner.isDisabled());
			BarcodedProduct info = scss.getBss().db.getItem(newItem.getBarcode());
			addedItem(info, null);
			setTotals();
		});

		addItemButton2.addActionListener(e -> {
			BarcodedItem newItem = new BarcodedItem(new Barcode(new Numeral[] { Numeral.two }), 210);
			scs.mainScanner.scan(newItem);
			BarcodedProduct info = scss.getBss().db.getItem(newItem.getBarcode());
			addedItem(info, null);
			setTotals();
		});

		addItemButton3.addActionListener(e -> {
			BarcodedItem newItem = new BarcodedItem(new Barcode(new Numeral[] { Numeral.three }), 1800);
			scs.mainScanner.scan(newItem);
			BarcodedProduct info = scss.getBss().db.getItem(newItem.getBarcode());
			addedItem(info, null);
			setTotals();
		});

		addItemButton4.addActionListener(e -> {
			BarcodedItem newItem = new BarcodedItem(new Barcode(new Numeral[] { Numeral.four }), 500);
			scs.mainScanner.scan(newItem);
			BarcodedProduct info = scss.getBss().db.getItem(newItem.getBarcode());
			addedItem(info, null);
			setTotals();
		});

		// sampleBarcodes.add(barcodeProductPanel);
		sampleBarcodes.getContentPane().add(barcodeProductPanel);
		sampleBarcodes.pack();
		sampleBarcodes.setVisible(true);
	}

	/**
	 * Creates the display piece that displays the breakdown of the total amount due
	 */
	private void createTotalPriceDisplay() {
		Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
	}

	/**
	 * Method which when a new item is added, if the scroll list is not focused at
	 * the bottom at the new item,
	 * 
	 */
	public void addedItem(BarcodedProduct barcode, PLUCodedProduct code) {
		if (barcode != null) {
			itemModel.addElement(barcode.getDescription());
			priceModel.addElement(barcode.getPrice().doubleValue());
		} else if (code != null) {
			itemModel.addElement(code.getDescription());
			priceModel.addElement(code.getPrice().doubleValue());
		}

		// Sets the index to the bottom of the list
		index = itemModel.getSize() - 1;
		// Sets the newest added item as the highlighted and visible item on the display
		itemList.setSelectedIndex(index);
		itemList.ensureIndexIsVisible(index);
		// Sets the newest added price as the highlighted and visible price on the
		// display
		priceList.setSelectedIndex(index);
		priceList.ensureIndexIsVisible(index);
	}

	public void setbsStateTrue() {
		bsState = true;
		banknoteStorageState.setText(normal);
		repaint();
		revalidate();
	}

	public void setbsStateFalse() {
		bsState = false;
		banknoteStorageState.setText(full);
		changeSignalToRed();
		repaint();
		revalidate();
	}

	public void setcsStateTrue() {
		csState = true;
		coinStorageState.setText(normal);
		repaint();
		revalidate();
		if (checkProblem()) {
			changeSignalToGreen();
		}
	}

	public void setcsStateFalse() {
		csState = false;
		coinStorageState.setText(full);
		changeSignalToRed();
		repaint();
		revalidate();
	}

	public void setPaperStateTrue() {
		ps = true;
		paperState.setText(normal);
		repaint();
		revalidate();
		if (checkProblem()) {
			changeSignalToGreen();
		}
	}

	public void setPaperStateFalse() {
		ps = false;
		paperState.setText(empty);
		changeSignalToRed();
		repaint();
		revalidate();
	}

	public void setInkStateTrue() {
		is = true;
		inkState.setText(normal);
		repaint();
		revalidate();
		if (checkProblem()) {
			changeSignalToGreen();
		}
	}

	public void setInkStateFalse() {
		is = false;
		inkState.setText(empty);
		changeSignalToRed();
		repaint();
		revalidate();
	}

	public void setbdStateTrue() {
		bdState = true;
		banknoteDispenserState.setText(normal);
		repaint();
		revalidate();
		if (checkProblem()) {
			changeSignalToGreen();
		}
	}

	public void setbdStateFalse() {
		bdState = false;
		banknoteDispenserState.setText(empty);
		changeSignalToRed();
		repaint();
		revalidate();
	}

	public void setcdStateTrue() {
		cdState = true;
		coinDispenserState.setText(normal);
		repaint();
		revalidate();
		if (checkProblem()) {
			changeSignalToGreen();
		}
	}

	public void setcdStateFalse() {
		cdState = false;
		coinDispenserState.setText(empty);
		changeSignalToRed();
		repaint();
		revalidate();
	}

	public void discrepancyDetected() {
		discrepancyDetected = true;
		discrepancyLabel.setText(yes);
		changeSignalToRed();
		repaint();
		revalidate();
	}

	public void approveDiscrepancy() {
		am.approveDiscrepancy();
		discrepancyDetected = false;
		discrepancyLabel.setText(no);
		repaint();
		revalidate();
		if (checkProblem()) {
			changeSignalToGreen();
		}
	}

	public void toggleBlock() {
		if (!ifBlocked) {
			ifBlocked = true;
			blockState.setText(yes);
			scs.handheldScanner.disable();
			scs.mainScanner.disable();
			so.changeSignal(scsNo, Color.yellow);
			setMsg("This station is blocked.");
			repaint();
			revalidate();
		} else {
			ifBlocked = false;
			blockState.setText(no);
			scs.handheldScanner.enable();
			scs.mainScanner.enable();
			if(checkProblem()) {
				so.changeSignal(scsNo, Color.green);
			} else {
				so.changeSignal(scsNo, Color.red);
			}
			setMsg("This station is unblocked.");
			repaint();
			revalidate();
		}
	}

	public void setSimulationVisible() {
		ms.setVisible(true);
	}

	public void changeSignalToRed() {
		so.changeSignal(scsNo, Color.red);
	}

	public void changeSignalToGreen() {
		so.changeSignal(scsNo, Color.green);
	}

	public boolean checkProblem() {
		boolean flag = true;
		if (bsState == false)
			flag = false;
		if (csState == false)
			flag = false;
		if (bdState == false)
			flag = false;
		if (cdState == false)
			flag = false;
		if (ps == false)
			flag = false;
		if (is == false)
			flag = false;
		return flag;
	}
	
	public void setMsg(String msg) {
		message.setText(msg);
		repaint();
		revalidate();
	}
	
	public void blockScreen() {
		scs.screen.getFrame().getContentPane().add(blockScreen);
		scs.screen.getFrame().repaint();
		scs.screen.getFrame().revalidate();
	}
	
	public void unBlockScreen() {
		scs.screen.getFrame().remove(blockScreen);
		scs.screen.getFrame().repaint();
		scs.screen.getFrame().revalidate();
	}
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;	
	}
	
	public boolean getIfBlocked() {
		return ifBlocked;
	}
}
