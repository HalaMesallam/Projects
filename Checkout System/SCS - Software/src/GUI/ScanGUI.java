package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.PLUCodedItem;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.devices.BarcodeScanner;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.software.AttendantBlockException;
import org.lsmr.selfcheckout.software.ItemInfo;
import org.lsmr.selfcheckout.software.SelfCheckoutStationSoftware;

public class ScanGUI {
	JFrame scanFrame;
	JPanel scanPanel, buttonPanel, cartPanel;
	JPanel pluPanel = new JPanel();
	JButton checkoutButton;
	JList itemList, priceList;
	DefaultListModel itemModel, priceModel;
	JScrollPane listScrollPane;
	JLabel totalPrice, subTotalPrice, taxPrice;

	SelfCheckoutStationSoftware scss;
	BarcodeScanner bss;
	int index;
	private static final DecimalFormat df = new DecimalFormat("0.00");

	BarcodedItem newItem = new BarcodedItem(new Barcode(new Numeral[] {Numeral.zero}), 1);

	private StationControl stationControl;

	class MyListDataListener implements ListDataListener {

		@Override
		public void intervalAdded(ListDataEvent e) {
			addedItem(null, null);
		}

		@Override
		public void intervalRemoved(ListDataEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void contentsChanged(ListDataEvent e) {
			// TODO Auto-generated method stub
		}
	}

	public ScanGUI(JFrame frame, SelfCheckoutStation station, StationControl stationControl) {

		scss = new SelfCheckoutStationSoftware(station);

		this.itemModel = scss.getItemModel();
		this.priceModel = scss.getPriceModel();

		this.stationControl = stationControl;


		bss = scss.scs.mainScanner;
		// Takes the passed in frame for reuse
		scanFrame = frame;
		// Creates a new panel for this step
		scanPanel = new JPanel();
		// Sets the layout to the BorderLayout
		scanPanel.setLayout(new BorderLayout());

		cartPanel = new JPanel();
		cartPanel.setLayout(new BorderLayout());
		scanPanel.add(cartPanel, BorderLayout.CENTER);

		// Setup the buttons to the GUI
		addCheckoutButton();

		// Creates the shopping cart display
		createItemCart();
		createTotalPriceDisplay();
		createSampleBarcodedProducts();

		// Startup the shopping cart gui
		changeDisplay(pluPanel, scanPanel);
	}

	private void proceedToPayment() {
		// Remove the current contents displayed on the touch screen
		scanFrame.remove(scanPanel);
		scanFrame.setVisible(false);
		// Create the next step
		PaymentGUI nextStep = new PaymentGUI(scanFrame, scss, bss, scanFrame, stationControl);
	}

	private void gotoCatalog() {
		// Remove current contents displayed on the touch  screen
		scanFrame.remove(scanPanel);
		scanFrame.setVisible(false);
		// Create catalog GUI
		PLUCatalogGUI catalogGui = new PLUCatalogGUI(scanFrame, scss, pluPanel, this);
	}

	private void createSampleBarcodedProducts() {
		JFrame sampleBarcodes = new JFrame("Sample Barcoded Products");
		sampleBarcodes.setLayout(new GridLayout(1,2));

		JPanel barcodeProductPanel = new JPanel();
		barcodeProductPanel.setLayout(new GridLayout(2,2));

		JButton scanItemButton1 = new JButton("DairyLand 2% Milk");
		barcodeProductPanel.add(scanItemButton1);
		scanItemButton1.setBackground(Color.GREEN);

		JButton scanItemButton2 = new JButton("50 Pc Disposable Face Mask");
		barcodeProductPanel.add(scanItemButton2);
		scanItemButton2.setBackground(Color.GREEN);

		JButton scanItemButton3 = new JButton("EVGA GeForce RTX 3080 Ti FTW3");
		barcodeProductPanel.add(scanItemButton3);
		scanItemButton3.setBackground(Color.GREEN);


		JButton scanItemButton4 = new JButton("CrackerBarrel block of cheese");
		barcodeProductPanel.add(scanItemButton4);
		scanItemButton4.setBackground(Color.GREEN);

		scanItemButton1.addActionListener(e -> {
			newItem = new BarcodedItem(new Barcode(new Numeral[] {Numeral.one}), 1200);
			bss.scan(newItem);
			if (scss.getItemsScanned().size() > scss.itemsInBagging.size())
				addButton(scanItemButton1, barcodeProductPanel, 0);
				// updates attendant screen
				stationControl.addedItem(scss.getBss().db.getItem(newItem.getBarcode()), null);
		});

		scanItemButton2.addActionListener(e -> {
			newItem = new BarcodedItem(new Barcode(new Numeral[] {Numeral.two}), 210);
			bss.scan(newItem);
			if (scss.getItemsScanned().size() > scss.itemsInBagging.size())
				addButton(scanItemButton2, barcodeProductPanel, 1);
				// updates attendant screen
				stationControl.addedItem(scss.getBss().db.getItem(newItem.getBarcode()), null);
		});

		scanItemButton3.addActionListener(e -> {
			newItem = new BarcodedItem(new Barcode(new Numeral[] {Numeral.three}), 1800);
			bss.scan(newItem);
			if (scss.getItemsScanned().size() > scss.itemsInBagging.size())
				addButton(scanItemButton3, barcodeProductPanel, 2);
				// updates attendant screen
				stationControl.addedItem(scss.getBss().db.getItem(newItem.getBarcode()), null);
		});

		scanItemButton4.addActionListener(e -> {
			newItem = new BarcodedItem(new Barcode(new Numeral[] {Numeral.four}), 500);
			bss.scan(newItem);
			if (scss.getItemsScanned().size() > scss.itemsInBagging.size())
				addButton(scanItemButton4, barcodeProductPanel, 3);
				// updates attendant screen
				stationControl.addedItem(scss.getBss().db.getItem(newItem.getBarcode()), null);
		});

		//sampleBarcodes.add(barcodeProductPanel);
		sampleBarcodes.getContentPane().add(barcodeProductPanel);
		sampleBarcodes.pack();
		sampleBarcodes.setVisible(true);
	}

	private void addButton(JButton button, JPanel panel, int position) {
		button.setVisible(false);
		panel.remove(button);
		JButton newButton = new JButton("Add item to bagging");
		newButton.setBackground(Color.red);
		panel.add(newButton, position);
		newButton.setVisible(true);

		// NEED TO CHECK FOR ADDING TO BAGGING HERE BEFORE FURTHER ACTION
		newButton.addActionListener(e -> {
			if (!newItem.getBarcode().equals(new Barcode(new Numeral[] {Numeral.zero}))) {
				scss.scs.baggingArea.add(newItem);
				scss.addToBagging(newItem);
			}
			newButton.setVisible(false);
			panel.remove(newButton);
			panel.add(button, position);
			button.setVisible(true);
		});
	}

	private void addCheckoutButton() {
		// Initiates new panel to add to the existing panel for a grid layout
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(200, 720));
		buttonPanel.setLayout(new GridLayout(4,1));

		// Creates a new button for checking out
		checkoutButton = new JButton("Press here to checkout");
		checkoutButton.setPreferredSize(new Dimension(200, 150));
		// Adds the button to the button panel
		buttonPanel.add(checkoutButton);

		// Adds a listener that will move the GUI to the checkout screen
		checkoutButton.addActionListener(e -> {
			proceedToPayment();
		});

		// Creates a button allowing for an item selected from the the list to be removed
		JButton removeItemButton = new JButton("Remove item");
		buttonPanel.add(removeItemButton, 0);
		removeItemButton.addActionListener(e -> {
			if(!itemModel.isEmpty() && index > -1) {
				String option = showInputDialog();
				if(option != null) {
					index = itemList.getSelectedIndex();
					try {
						scss.removeItem(itemModel.get(index).toString(), index);
						setTotals();
					} catch (AttendantBlockException e1) {
						e1.printStackTrace();
					}	
					int index2 = index;
					itemModel.remove(index);
					priceModel.remove(index2);
				}
			}
		});

		// Creates a button that shifts the screen to the catalogue
		JButton catalogueButton = new JButton("Item Catalogue");
		buttonPanel.add(catalogueButton, 1);

		catalogueButton.addActionListener(e -> {
			scanFrame.remove(scanPanel);
			PLUCatalogGUI catalogue = new PLUCatalogGUI(scanFrame, scss, scanPanel, this);
		});

		// Creates a button that allows for the user to simply input a PLU code
		JButton PLUButton = new JButton("Enter PLU");
		buttonPanel.add(PLUButton, 1);

		PLUButton.addActionListener(e  -> {
			createPLU();
			changeDisplay(scanPanel, pluPanel);
		});

		// Adds the button panel to the main panel
		scanPanel.add(buttonPanel, BorderLayout.LINE_END);
	}

	private void createPLU() {
		// Creates a new JPanel to which the Price lookup can be built upon
		pluPanel = new JPanel();
		pluPanel.setLayout(null);

		// Creates a label which will indicate to the customer which action to take
		JLabel searchLabel = new JLabel("Please enter the 4-5 digit PLU code");
		searchLabel.setBounds(450, 150, 400, 20);
		pluPanel.add(searchLabel);
		searchLabel.setHorizontalAlignment(JLabel.CENTER);

		// Creates a blank label which will indicate an alternative action to take
		JLabel errorLabel = new JLabel();
		errorLabel.setBounds(350, 170, 600, 20);
		pluPanel.add(errorLabel);
		errorLabel.setHorizontalAlignment(JLabel.CENTER);

		// Creates a textfield where the customer may input the PLU number
		JTextField searchBox = new JTextField(5);
		searchBox.setBounds(550, 200, 200, 30);
		pluPanel.add(searchBox);
		searchBox.setHorizontalAlignment(JTextField.CENTER);

		// Creates a button which takes the entry from the textfield and searches the database
		JButton searchButton = new JButton("Search");
		searchButton.setBounds(675, 250, 75, 40);
		pluPanel.add(searchButton);

		// Creates a button which returns the user to the shopping cart scren
		JButton returnToScanButton = new JButton("Return");
		returnToScanButton.setBounds(550, 250, 75, 40);
		pluPanel.add(returnToScanButton);

		// Adds a listener to the search button
		searchButton.addActionListener(e -> {
			if(!searchBox.getText().isEmpty() && searchBox.getText().length() <= 5) {
				String text = searchBox.getText();
				// Checks if the entry was a valid PLU code (digits)
				try {
					Integer.parseInt(text);
				} catch (NumberFormatException E){
					searchLabel.setText("Invalid PLU code. The code must be between 4-5 digits.");
					searchLabel.setForeground(Color.RED);
					errorLabel.setText("Please try again, ask the attendant for help, or search through the product catalogue!");
					errorLabel.setForeground(Color.RED);
					return;
				}

				// If valid, will search up the info and make the necessary changes to the GUI
				PLUCodedItem newPLUItem = new PLUCodedItem(new PriceLookupCode(text), 500);
				scss.scs.scanningArea.add(newPLUItem);
				PLUCodedProduct info = scss.plus.lookupProduct(newPLUItem.getPLUCode());
				if (info != null) {				
					scss.scs.baggingArea.add(newPLUItem);
					scss.addToBagging(newPLUItem);
					updatePLUList();
					scss.scs.scanningArea.remove(newPLUItem);
					// updates customer scan list on attendant screen
					stationControl.addedItem(null, info);
					changeDisplay(pluPanel, scanPanel);
				}
				else {
					searchLabel.setText("Product not found!");
					searchLabel.setForeground(Color.RED);
					errorLabel.setText("Please try again, ask the attendant for help, or search through the product catalogue!");
					errorLabel.setForeground(Color.RED);
					return;
				}
			}
			else {
				searchLabel.setText("Invalid PLU code. The code must be between 4-5 digits.");
				searchLabel.setForeground(Color.RED);
				errorLabel.setText("Please try again, ask the attendant for help, or search through the product catalogue!");
				errorLabel.setForeground(Color.RED);
			}
		});

		// Adds a listener to the return button
		returnToScanButton.addActionListener(e -> {
			changeDisplay(pluPanel, scanPanel);
		});

		// PLU catalog button
		JButton gotoCatalogButton = new JButton("Search Catalog");
		gotoCatalogButton.setBounds(575, 300, 150, 40);
		pluPanel.add(gotoCatalogButton);
		gotoCatalogButton.addActionListener(e -> {
			gotoCatalog();
		});
	}
	/**
	 * Creates the display piece that displays the breakdown of the 
	 * total amount due
	 */
	private void createTotalPriceDisplay() {
		// Creates a new border panel for displaying totals due
		JPanel totalPanel = new JPanel();
		totalPanel.setLayout(new GridLayout(3,2));
		totalPanel.setBackground(Color.GREEN);
		Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		totalPanel.setBorder(padding);

		// Creates a new label for the total due text
		JLabel totalLabel = new JLabel("Total :");
		totalLabel.setFont(new Font("Comic Sans", Font.BOLD, 20));
		totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		// Creates a new label for the subtotal due text
		JLabel subTotalLabel = new JLabel("Subtotal :");
		subTotalLabel.setFont(new Font("Comic Sans", Font.BOLD, 20));
		subTotalLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		// Creates a new label for the tax due text
		JLabel taxLabel = new JLabel("GST :");
		taxLabel.setFont(new Font("Comic Sans", Font.BOLD, 20));
		taxLabel.setHorizontalAlignment(SwingConstants.RIGHT);


		// Creates a new label showing the total amount that is due
		totalPrice = new JLabel("$ 0.00");
		totalPrice.setFont(new Font("Comic Sans", Font.BOLD, 20));
		totalPrice.setHorizontalAlignment(SwingConstants.RIGHT);

		// Creates a new label showing the subtotal amount that is due before tax
		subTotalPrice = new JLabel("$ 0.00");
		subTotalPrice.setFont(new Font("Comic Sans", Font.BOLD, 20));
		subTotalPrice.setHorizontalAlignment(SwingConstants.RIGHT);

		// Creates a new label showing the tax amount
		taxPrice = new JLabel("$ 0.00");
		taxPrice.setFont(new Font("Comic Sans", Font.BOLD, 20));
		taxPrice.setHorizontalAlignment(SwingConstants.RIGHT);

		// Adds the labels to the total panel
		totalPanel.add(subTotalLabel);
		totalPanel.add(subTotalPrice);
		totalPanel.add(taxLabel);
		totalPanel.add(taxPrice);
		totalPanel.add(totalLabel);
		totalPanel.add(totalPrice);

		// Adds the total panel to the shopping cart display panel
		cartPanel.add(totalPanel, BorderLayout.PAGE_END);
	}

	/**
	 * Setup the shopping cart display area
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void createItemCart() {        
		// Creates an empty list model which displays items
		//		itemModel = new DefaultListModel();
		itemModel.addListDataListener(new MyListDataListener());
		itemList = new JList(itemModel);
		itemList.setFixedCellWidth(960);
		itemList.setFont(new Font("Comic Sans", Font.BOLD, 20));
		itemList.setBackground(Color.LIGHT_GRAY);
		itemList.setSelectionBackground(Color.WHITE);

		// Creates an empty list model which displays prices
		//		priceModel = new DefaultListModel();
		priceModel.addListDataListener(new MyListDataListener());
		priceList = new JList(priceModel);
		priceList.setFixedCellWidth(100);
		priceList.setFont(new Font("Comic Sans", Font.BOLD, 20));
		priceList.setBackground(Color.LIGHT_GRAY);
		priceList.setSelectionBackground(Color.WHITE);

		// Creates a new panel to add the 2 lists to
		JPanel scannedItemPanel = new JPanel();
		scannedItemPanel.setLayout(new BorderLayout());
		scannedItemPanel.add(itemList, BorderLayout.LINE_START);
		scannedItemPanel.add(priceList, BorderLayout.CENTER);
		scannedItemPanel.setBackground(Color.lightGray);

		// Creates a new ScrollPane for dynamic item addition
		listScrollPane = new JScrollPane(scannedItemPanel);
		Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		listScrollPane.setBorder(padding);
		listScrollPane.setBackground(Color.GREEN);
		listScrollPane.setHorizontalScrollBarPolicy(listScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		// Adds the scanned item scrollpane to the GUI
		cartPanel.add(listScrollPane, BorderLayout.CENTER);		

		// Adds a listener that when selecting an item from the list, it also selects it's corresponding price
		itemList.addListSelectionListener(e -> {
			index = itemList.getSelectedIndex();
			priceList.setSelectedIndex(index);
		});
		// Adds a listener that when selecting an price from the list, it also selects it's corresponding item
		priceList.addListSelectionListener(e -> {
			index = priceList.getSelectedIndex();
			itemList.setSelectedIndex(index);
		});
	}	

	/**
	 * Switches the current display for a new display on the touchscreen frame
	 * @param oldPanel
	 * @param newPanel
	 */
	private void changeDisplay(JPanel oldPanel, JPanel newPanel) {
		// Removes the current panel from the frame
		scanFrame.remove(oldPanel);
		// Sets the new panel to the frame
		scanFrame.setContentPane(newPanel);
		// Packs the contents into frame
		scanFrame.pack();
		// Set the new contents to be visible
		scanFrame.setVisible(true);
	}

	/**
	 * Retrieves the total amount due from the selfCheckoutStationSoftware
	 * and sets and computes the values before tax, the tax, and after tax
	 */
	private void setTotals() {
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

	/**
	 * Shows input dialog which requires the attendant to clear
	 * @return
	 */

	private String showInputDialog() {
		JOptionPane checkRemove = new JOptionPane();
		String s = (String)checkRemove.showInputDialog(scanFrame, "Please input attendant code!", "Confirm",checkRemove.OK_CANCEL_OPTION,null,null,null);
		// If the ok button was pressed
		if(s != null)  {
			// If there was no input or it was not the correct code
			if(s.isEmpty() || !s.equals("admin")) 
				// Show the dialog again
				s = showInputDialog();
			else return s;
		}
		return null;
	}


	/**
	 * Method which when a new item is scanned, if it was valid and was added to the itemsScanned
	 * arraylist, then it is also updated on the shopping cart view
	 */

	public void addedItem(BarcodedProduct bProduct, PLUCodedProduct pProduct) {
		// Updates the total amount due
		setTotals();
		index = scss.getItemsScanned().size() - 1;
		// Sets the newest added item as the highlighted and visible item on the display
		itemList.setSelectedIndex(index);
		itemList.ensureIndexIsVisible(index);
		// Sets the newest added price as the highlighted and visible price on the display
		priceList.setSelectedIndex(index);
	}

	public void updatePLUList() {		
		// Checks if the itemlist has a new item compared to the current display list
		if (scss.getItemsScanned().size() > itemModel.size()) {
			// Sets the index to the bottom of the list	
			index = scss.getItemsScanned().size() - 1;
			ItemInfo info = scss.getItemsScanned().get(index);
			// Gets the name and price from the newly added item and adds it to the display list
			itemModel.addElement(info.description);
			System.out.println(scss.getItemsScanned().size());
			priceModel.addElement(df.format(info.price.doubleValue()));
		}
	}
	
	/**
	 *  Add item to the shopping cart list (used by PLUCatalog)
	 * @param bProduct
	 * @param pProduct
	 */
	public void addItemToScanned(BarcodedProduct bProduct, PLUCodedProduct pProduct) {
		setTotals();
		addedItem(bProduct, pProduct);
	}
}
