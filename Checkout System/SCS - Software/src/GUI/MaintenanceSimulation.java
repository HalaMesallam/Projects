package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.devices.BanknoteDispenser;
import org.lsmr.selfcheckout.devices.CoinDispenser;
import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.EmptyException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.software.AttendantMaintenance;

import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Currency;
import java.awt.event.ActionEvent;

public class MaintenanceSimulation extends JDialog {

	private final JPanel contentPanel = new JPanel();
	
	private SelfCheckoutStation scs;
	private StationControl sc;
	private AttendantMaintenance am;
	private Currency currency = Currency.getInstance("CAD");
	
	

	/**
	 * Create the dialog.
	 * @param so 
	 * @param scs 
	 */
	public MaintenanceSimulation(SelfCheckoutStation scs, StationControl sc, AttendantMaintenance am) {
		this.am = am;
		this.scs =scs;
		this.sc = sc;
		setBounds(100, 100, 526, 426);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 521, 389);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		{
			JButton btnNewButton = new JButton("Low Ink");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					inkEmpty();
				}
			});
			btnNewButton.setBounds(38, 21, 103, 41);
			contentPanel.add(btnNewButton);
		}
		{
			JButton btnRefillInk = new JButton("Refill Ink");
			btnRefillInk.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					refillInk();
				}
			});
			btnRefillInk.setBounds(277, 21, 103, 41);
			contentPanel.add(btnRefillInk);
		}
		{
			JButton btnLowPaper = new JButton("Low Paper");
			btnLowPaper.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					paperEmpty();
				}
			});
			btnLowPaper.setBounds(38, 74, 103, 41);
			contentPanel.add(btnLowPaper);
		}
		{
			JButton btnRefillPaper = new JButton("Refill Paper");
			btnRefillPaper.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					refillPaper();
				}
			});
			btnRefillPaper.setBounds(277, 74, 103, 41);
			contentPanel.add(btnRefillPaper);
		}
		{
			JButton btnCoinDispenserEmpty = new JButton("Coin Dispenser Empty");
			btnCoinDispenserEmpty.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					coinDispenserEmpty();
				}
			});
			btnCoinDispenserEmpty.setBounds(38, 127, 159, 41);
			contentPanel.add(btnCoinDispenserEmpty);
		}
		{
			JButton btnRefillCoinDispenser = new JButton("Refill Coin Dispenser");
			btnRefillCoinDispenser.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					refillCoinDispensers();
				}
			});
			btnRefillCoinDispenser.setBounds(277, 127, 159, 41);
			contentPanel.add(btnRefillCoinDispenser);
		}
		{
			JButton btnBanknoteDispenserEmpty = new JButton("Banknote Dispenser Empty");
			btnBanknoteDispenserEmpty.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					banknoteDispenserEmpty();
				}
			});
			btnBanknoteDispenserEmpty.setBounds(38, 180, 185, 41);
			contentPanel.add(btnBanknoteDispenserEmpty);
		}
		{
			JButton btnRefill = new JButton("Refill Banknote Dispenser");
			btnRefill.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					refillBanknoteDispensers();
				}
			});
			btnRefill.setBounds(277, 180, 185, 41);
			contentPanel.add(btnRefill);
		}
		{
			JButton btnCoinStorageUnit = new JButton("Coin Storage Unit Full");
			btnCoinStorageUnit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					coinStorageFull();
				}
			});
			btnCoinStorageUnit.setBounds(38, 233, 164, 41);
			contentPanel.add(btnCoinStorageUnit);
		}
		{
			JButton btnEmptyCoinStorge = new JButton("Empty Coin Storge Unit");
			btnEmptyCoinStorge.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					emptyCoinStorage();
				}
			});
			btnEmptyCoinStorge.setBounds(277, 233, 164, 41);
			contentPanel.add(btnEmptyCoinStorge);
		}
		{
			JButton btnBanknoteStorageUnit = new JButton("Banknote Storage Unit Full");
			btnBanknoteStorageUnit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					banknoteStorageFull();
				}
			});
			btnBanknoteStorageUnit.setBounds(38, 286, 196, 41);
			contentPanel.add(btnBanknoteStorageUnit);
		}
		{
			JButton btnEmptyBanknoteStorage = new JButton("Empty Banknote Storage Unit");
			btnEmptyBanknoteStorage.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					emptyBanknoteStorage();
				}
			});
			btnEmptyBanknoteStorage.setBounds(277, 286, 196, 41);
			contentPanel.add(btnEmptyBanknoteStorage);
		}
		{
			JButton btnCreateDiscrepancy = new JButton("Create Discrepancy");
			btnCreateDiscrepancy.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					createDiscrepancy();
				}
			});
			btnCreateDiscrepancy.setBounds(38, 342, 196, 41);
			contentPanel.add(btnCreateDiscrepancy);
		}
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	public void coinDispenserEmpty() {
		BigDecimal denomination = scs.coinDenominations.get(0);
		CoinDispenser dispenser = scs.coinDispensers.get(denomination);
		dispenser.unload();
	}
	
	public void banknoteDispenserEmpty() {
		int denomination = scs.banknoteDenominations[0];
		BanknoteDispenser dispenser = scs.banknoteDispensers.get(denomination);
		dispenser.unload();
	}
	
	public void refillCoinDispensers() {
		for(BigDecimal denomination: scs.coinDenominations) {
			am.addCoinToDispenser(denomination);
		}
	}
	
	public void refillBanknoteDispensers() {
		for(int denomination: scs.banknoteDenominations) {
			am.addBanknoteToDispenser(denomination);
		}
	}
	
	public void banknoteStorageFull() {
		while(scs.banknoteStorage.hasSpace()) {
			int denomination = scs.banknoteDenominations[0];
			Banknote banknote = new Banknote(currency, denomination);
			try {
				scs.banknoteStorage.accept(banknote);
			} catch (DisabledException | OverloadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void coinStorageFull() {
		while(scs.coinStorage.getCoinCount()<scs.coinStorage.getCapacity()) {
			BigDecimal denomination = scs.coinDenominations.get(0);
			Coin coin = new Coin(currency, denomination);
			try {
				scs.coinStorage.accept(coin);
			} catch (DisabledException | OverloadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void emptyCoinStorage() {
		am.emptyCoinStorage();
	}
	
	public void emptyBanknoteStorage() {
		am.emptyBanknoteStorage();
	}
	
	public void refillInk() {
		am.addInkToPrinter();
	}
	
	public void refillPaper() {
		am.addPaperToPrinter();
	}
	
	public void paperEmpty() {
		try {
			scs.printer.addPaper(1);
			scs.printer.addInk(20);
		} catch (OverloadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			scs.printer.print('a');
			scs.printer.print('\n');
		} catch (EmptyException | OverloadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void inkEmpty() {
		try {
			scs.printer.addPaper(2);
			scs.printer.addInk(1);
		} catch (OverloadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			scs.printer.print('a');
		} catch (EmptyException | OverloadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createDiscrepancy() {
		BarcodedItem newItem = new BarcodedItem(new Barcode(new Numeral[] {Numeral.two}), 210);
		scs.baggingArea.add(newItem);
	}
}
