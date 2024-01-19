package org.lsmr.selfcheckout.software;

import java.math.BigDecimal;

public interface DispenserFacadeListener {

    void notifyBanknoteDispenserEmpty(int denomination);

    void notifyCoinDispenserEmpty(BigDecimal denomination);
}
