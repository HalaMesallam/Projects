package org.lsmr.selfcheckout.software;

import org.lsmr.selfcheckout.devices.BanknoteDispenser;
import org.lsmr.selfcheckout.devices.CoinDispenser;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.software.DispenserFacade.DenominationNotExistException;

import GUI.StationControl;
import GUI.StationsOverview;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;

public class AttendantMaintenance {
	private SelfCheckoutStation scs;
    private SUFL sufl;
    private DFL dfl;
    private RPFL rpfl;
    private DCL dcl;

    private ReceiptPrinterFacade rpf;
    private DispenserFacade df;
    private StorageUnitFacade suf;
    private DiscrepancyChecker dc;
    
    private StationControl sc;
    

    class SUFL implements StorageUnitFacadeListener{

        @Override
        public void notifyBanknoteFull() {
        	sc.setbsStateFalse();
        	sc.setMsg("Banknote storage unit is full.");
        }

        @Override
        public void notifyCoinFull() {
        	sc.setcsStateFalse();
        	sc.setMsg("Coin storage unit is full.");
        }

		@Override
		public void notifyBanknoteUnloaded() {
			// TODO Auto-generated method stub
			sc.setbsStateTrue();
			sc.setMsg("Banknote storage unit has been emptied.");
		}

		@Override
		public void notifyCoinUnloaded() {
			// TODO Auto-generated method stub
			sc.setcsStateTrue();
			sc.setMsg("Coin storage unit has been emptied.");
		}
    }

    class DFL implements  DispenserFacadeListener{

        @Override
        public void notifyBanknoteDispenserEmpty(int denomination) {
        	sc.setMsg("A banknote dispenser is empty.");
        	sc.setbdStateFalse();
        }

        @Override
        public void notifyCoinDispenserEmpty(BigDecimal denomination) {
        	sc.setMsg("A coin dispenser is empty.");
        	sc.setcdStateFalse();
        }
    }

    class RPFL implements  ReceiptPrinterFacadeListener{

        @Override
        public void notifyLowInk() {
        	sc.setMsg("Alert: low ink for receipt printer.");
        	sc.setInkStateFalse();
        }

        @Override
        public void notifyLowPaper() {
        	sc.setMsg("Alert: low paper for receipt printer.");
        	sc.setPaperStateFalse();
        }

        @Override
        public void notifyPaperAdded() {
        	sc.setMsg("Paper has been added to the printer.");
        	sc.setPaperStateTrue();
        }

        @Override
        public void notifyInkAdded() {
        	sc.setInkStateTrue();
        	sc.setMsg("Ink has been added to the printer.");
        }

        @Override
        public void notifyInkOverload() {
        	
        }

        @Override
        public void notifyPaperOverload() {
        	
        }
    }

    class DCL implements DiscrepancyCheckerListener{

        @Override
        public void notifyDiscrepancy() {
        	sc.discrepancyDetected();
        	sc.setMsg("Discrepancy detected.");
        }
    }

    public AttendantMaintenance(SelfCheckoutStation scs, StationControl stationControl){
    	this.scs= scs;
    	this.sc= stationControl;
        sufl = new SUFL();
        dfl = new DFL();
        rpfl  = new RPFL();
        dcl = new DCL();
        suf = new StorageUnitFacade(scs);
        df = new DispenserFacade(scs);
        rpf = new ReceiptPrinterFacade(scs);
        dc = new DiscrepancyChecker(scs,10.0);
        suf.attach(sufl);
        df.attach(dfl);
        rpf.attach(rpfl);
        dc.attach(dcl);
    }

    public void addPaperToPrinter(){
        try {
			this.rpf.addPaper();
		} catch (OverloadException e) {
			// TODO Auto-generated catch block
			sc.setMsg("Falied to add more paper: overload");
		}
    }

    public void addInkToPrinter(){
        try {
			this.rpf.addInk();
		} catch (OverloadException e) {
			// TODO Auto-generated catch block
			sc.setMsg("Falied to add more paper: overload");
		}
    }

    public void addCoinToDispenser(BigDecimal denomination){
        try {
			this.df.refillCoin(denomination);
		} catch (DenominationNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        boolean flag = true;
        for (Map.Entry<BigDecimal,CoinDispenser> entry : scs.coinDispensers.entrySet()) {
            if(entry.getValue().size()==0) {
            	flag = false;
            }
        }
        if(flag == true) {
        	sc.setMsg("Coin dispensers are refilled.");
        	sc.setcdStateTrue();
        }
    }

    public void addBanknoteToDispenser(int denomination){
        try {
			this.df.refillBanknote(denomination);
		} catch (DenominationNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        boolean flag = true;
        for (Entry<Integer, BanknoteDispenser> entry : scs.banknoteDispensers.entrySet()) {
            if(entry.getValue().size()==0) {
            	flag = false;
            }
        }
        if(flag == true) {
        	sc.setMsg("Banknote dispensers are refilled.");
        	sc.setbdStateTrue();
        }
    }

    public void emptyBanknoteStorage(){
        this.suf.emptyBanknoteStorageUnit();
        
    }

    public void emptyCoinStorage(){
        this.suf.emptyCoinStorageUnit();
        
    }
    
    public void approveDiscrepancy() {
    	dc.approveDiscrepancy();
    	sc.setMsg("Discrepancy has been approved.");
    }
}
