package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;

import org.lsmr.selfcheckout.PLUCodedItem;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.software.SelfCheckoutStationSoftware;

public class AttendantPLU extends JPanel {
	private JFrame mainFrame;
	private JPanel menuPanel, searchPanel, catalogPanel, buttonPanel, infoPanel, textPanel;
	private JButton finishButton, returnButton, searchButton, allButton;
	private JToggleButton selectButton;
	private ArrayList<JToggleButton> toggleList = new ArrayList<JToggleButton>();
	private JScrollPane selectionPanel;
	private JLabel image, itemLabel, ppkgLabel, searchLabel;
	
	private PLUCodedProduct selectedProduct;
	private boolean productDeselected;
	private AttendantGUIController controller;
	private SelfCheckoutStationSoftware scs;
	private StationControl stationCtrl;
	
	public AttendantPLU(SelfCheckoutStationSoftware scs, AttendantGUIController controller, StationControl stationCtrl) {
		this.controller = controller;
		this.scs = scs;
		this.stationCtrl = stationCtrl;
		this.mainFrame = controller.getScreen();
		setSize(new Dimension(1280, 720));
		this.setLayout(new BorderLayout(0, 0));

		
		initializeMenu();
		
		initializeCatalog();
		
	}
	
	private void initializeMenu() {
		menuPanel = new JPanel();
		this.add(menuPanel, BorderLayout.NORTH);
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
		this.add(selectionPanel, BorderLayout.CENTER);
		selectionPanel.setPreferredSize(new Dimension(100, 75));
		
		catalogPanel = new JPanel();
		selectionPanel.setViewportView(catalogPanel);
		FlowLayout fl_catalogPanel = new FlowLayout(FlowLayout.CENTER, 5, 5);
		catalogPanel.setLayout(fl_catalogPanel);
		catalogPanel.setPreferredSize(new Dimension(100, 75));	
	}
	
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
		scs.plus.clearSearch();
		toggleList.clear();
		ArrayList<PLUCodedProduct> searchList = new ArrayList<PLUCodedProduct>();
		if (letter.compareTo("All") == 0) {
			// search for all products
			searchList = scs.plus.getAllProducts();
		} else {
			// Search for individual letter
			searchList = scs.plus.searchDatabase(letter);
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
			controller.returnFromPLU(stationCtrl);
		}
		
		// action event to finish selection, and add selected item to the checkout list
		private void finishSelection() {
			if (selectedProduct != null) {
				// Code taken from scanGUI
				// If valid, will search up the info and make the necessary changes to the GUI
				PLUCodedItem newPLUItem = new PLUCodedItem(new PriceLookupCode(selectedProduct.getPLUCode().toString()), 500);
				scs.scs.scanningArea.add(newPLUItem);
				PLUCodedProduct info = scs.plus.lookupProduct(newPLUItem.getPLUCode());
				if (info != null) {				
					scs.scs.baggingArea.add(newPLUItem);
					stationCtrl.setTotals();
					stationCtrl.addedItem(null, info);
					scs.scs.scanningArea.remove(newPLUItem);
				}
				returnToPrev();
			} else {
				JOptionPane.showMessageDialog(null, "No item selected");
			}
		}
		
}
