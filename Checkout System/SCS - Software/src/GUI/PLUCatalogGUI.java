package GUI;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.lsmr.selfcheckout.PLUCodedItem;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.software.SelfCheckoutStationSoftware;
import java.awt.GridLayout;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;


public class PLUCatalogGUI {
	private SelfCheckoutStationSoftware scss;
	private ScanGUI scanGUI;
	
	private JFrame mainFrame;
	private JPanel mainPanel, prevPanel, menuPanel, searchPanel, catalogPanel, buttonPanel, infoPanel, textPanel;
	private JButton finishButton, returnButton, searchButton, allButton;
	private JToggleButton selectButton;
	private ArrayList<JToggleButton> toggleList = new ArrayList<JToggleButton>();
	private JScrollPane selectionPanel;
	private JLabel image, itemLabel, ppkgLabel, searchLabel;
	
	private PLUCodedProduct selectedProduct;
	private boolean productDeselected;
	
	public PLUCatalogGUI(JFrame frame, SelfCheckoutStationSoftware scss, JPanel prevPanel, ScanGUI scanGUI) {
		frame.setSize(new Dimension(1280, 720));
		mainFrame = frame; 
		this.prevPanel = prevPanel;
		this.scss = scss;
		this.scanGUI = scanGUI;
		mainPanel = new JPanel();
		mainFrame.setContentPane(mainPanel);

		//mainFrame.getContentPane().add(mainPanel);
		mainFrame.setContentPane(mainPanel);
		mainFrame.pack();
		mainPanel.setLayout(new BorderLayout(0, 0));
		
		initializeMenu();
		
		initializeCatalog();
		
		mainFrame.setVisible(true);
	}
	
	// create the top menu (return/finish buttons and search letters[A-Z])
	private void initializeMenu() {
		menuPanel = new JPanel();
		mainPanel.add(menuPanel, BorderLayout.NORTH);
		menuPanel.setLayout(new BorderLayout(0, 0));
		
		searchPanel = new JPanel();
		menuPanel.add(searchPanel, BorderLayout.SOUTH);
		FlowLayout fl_searchPanel = new FlowLayout(FlowLayout.CENTER, 5, 5);
		searchPanel.setLayout(fl_searchPanel);
		searchPanel.setPreferredSize(new Dimension(100, 75));
		
		// generate an 'All' button
		allButton = new JButton("All");
		searchPanel.add(allButton);
		allButton.addActionListener(e -> {
			searchList("All");
		});
		
		// generate search buttons
		for (char i = 'A'; i <= 'Z'; i++) {
			String letter = Character.toString(i);
			searchButton = new JButton(letter);
			searchPanel.add(searchButton);
			searchButton.addActionListener(e -> {
				searchList(letter);
			});
		}
		
		buttonPanel = new JPanel();
		menuPanel.add(buttonPanel, BorderLayout.NORTH);
		buttonPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		returnButton = new JButton("Return");
		buttonPanel.add(returnButton);
		returnButton.addActionListener(e ->{
			returnToPrev();
		});
		
		finishButton = new JButton("Finish Selection");
		buttonPanel.add(finishButton);
		finishButton.addActionListener(e -> {
			finishSelection();
		});
	}

	// create the panel used by the catalog
	private void initializeCatalog() {
		selectionPanel = new JScrollPane();
		selectionPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		mainPanel.add(selectionPanel, BorderLayout.CENTER);
		selectionPanel.setPreferredSize(new Dimension(100, 75));
		
		catalogPanel = new JPanel();
		selectionPanel.setViewportView(catalogPanel);
		FlowLayout fl_catalogPanel = new FlowLayout(FlowLayout.CENTER, 5, 5);
		catalogPanel.setLayout(fl_catalogPanel);
		catalogPanel.setPreferredSize(new Dimension(100, 75));	
	}
	
	// create a visual of an item
	private void createInfo(PLUCodedProduct item) {
		infoPanel = new JPanel();
		catalogPanel.add(infoPanel);
		infoPanel.setLayout(new BorderLayout(0, 0));
		
		selectButton = new JToggleButton("Select");
		infoPanel.add(selectButton, BorderLayout.SOUTH);
		selectButton.addActionListener(e -> {
			selectProduct(item);
		});
		toggleList.add(selectButton);
		
		image = new JLabel("[image]");
		infoPanel.add(image, BorderLayout.NORTH);
		
		textPanel = new JPanel();
		infoPanel.add(textPanel, BorderLayout.WEST);
		textPanel.setLayout(new BorderLayout(0, 0));
		
		itemLabel = new JLabel(item.getDescription());
		textPanel.add(itemLabel, BorderLayout.NORTH);
		
		ppkgLabel = new JLabel(String.format("%.2f", item.getPrice()) +"$/kg");
		textPanel.add(ppkgLabel, BorderLayout.SOUTH);
		
	}
	
	// action event to make a search list for items
	private void searchList(String letter) {
		catalogPanel.removeAll();
		mainFrame.setVisible(false);
		scss.plus.clearSearch();
		toggleList.clear();
		ArrayList<PLUCodedProduct> searchList = new ArrayList<PLUCodedProduct>();
		if (letter.compareTo("All") == 0) {
			// search for all products
			searchList = scss.plus.getAllProducts();
		} else {
			// Search for individual letter
			searchList = scss.plus.searchDatabase(letter);
		}
		if (searchList.size() == 0) {
			searchLabel = new JLabel("Sorry, no items were found... :(");
			catalogPanel.add(searchLabel);
		} else {
			for(int i = 0; i < searchList.size(); i++) {
				createInfo(searchList.get(i));
			}
		}
		mainFrame.setVisible(true);
	}
	
	// action event to select a product and prevent the selection of other products
	private void selectProduct(PLUCodedProduct product) {
			if (productDeselected != true) {
				// deactivate all but the selected button
				for (int i = 0; i < toggleList.size(); i++) {
					if(!toggleList.get(i).isSelected()) {
						toggleList.get(i).setEnabled(false);
					}
					selectedProduct = product;
					productDeselected = true;
				}
			} else {
				// activate all the buttons
				for (int i = 0; i < toggleList.size(); i++) {
					toggleList.get(i).setEnabled(true);
				}
				selectedProduct = null;
				productDeselected = false;
			}
		}
	
	
	// action event to return to the previous menu
	private void returnToPrev() {
		mainFrame.remove(mainPanel);
		mainFrame.setContentPane(prevPanel);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}
	
	// action event to finish selection, and add selected item to the checkout list
	private void finishSelection() {
		if (selectedProduct != null) {
			// Code taken from scanGUI
			// If valid, will search up the info and make the necessary changes to the GUI
			PLUCodedItem newPLUItem = new PLUCodedItem(new PriceLookupCode(selectedProduct.getPLUCode().toString()), 500);
			scss.scs.scanningArea.add(newPLUItem);
			PLUCodedProduct info = scss.plus.lookupProduct(newPLUItem.getPLUCode());
			if (info != null) {				
				scss.scs.baggingArea.add(newPLUItem);
				scanGUI.addedItem(null, info);
				scanGUI.addItemToScanned(null, info);
				scss.scs.scanningArea.remove(newPLUItem);
			}
			returnToPrev();
		} else {
			JOptionPane.showMessageDialog(null, "No item selected");
		}
	}
	
	/** Method for testing
	public static void main(String[] args) {
		int[] bills = new int[] {5,10,20,50,100};
		BigDecimal[] coins = new BigDecimal[] {new BigDecimal(0.05), new BigDecimal(0.10), new BigDecimal(0.25), new BigDecimal(1.00), new BigDecimal(2.00)};
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SelfCheckoutStation scs = new SelfCheckoutStation(Currency.getInstance("CAD"), bills, coins, 1, 1);
		PLUCatalogGUI startPage = new PLUCatalogGUI(new JFrame(), new SelfCheckoutStationSoftware(new SelfCheckoutStation(Currency.getInstance("CAD"), bills, coins, 1, 1)));
	}
	*/
}
