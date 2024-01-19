package org.lsmr.selfcheckout.software;

import org.lsmr.selfcheckout.Card;

/**
 * This is the interface of the listener that listens the activity of the card reader software.
 */
public interface CardReaderFacadeListener {
    /**
     * Notify the listener when the data from a credit card is read.
     * @param data The data read from the credit card.
     * @param pinProvided Whether pin is provided or not.
     */
    void notifyCreditCardRead(Card.CardData data, boolean pinProvided);

    /**
     * Notify the listener when the data from a debit card is read.
     * @param data The data read from the debit card.
     * @param pinProvided Whether pin is provided or not.
     */
    void notifyDebitCardRead(Card.CardData data, boolean pinProvided);
    
    /**
     * Notify the listener when the data from the gift card is read
     * @param data The data read from the debit card.
     * @param pinProvided Whether pin is provided or not.
     */
    void notifyGiftCardRead(Card.CardData data, boolean pinProvided);
}
