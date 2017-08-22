package com.easyexpense.android.ledgerdetail.ledgerlist;

/**
 * Created by Mani on 03/04/17.
 */

public interface LedgerListPresenter {

    void userPressedAddCardToLedger(String name,String amount);

    void deleteLedger();

    void updateLedgerName(String name);

}
