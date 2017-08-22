package com.easyexpense.android.ledgerdetail.ledgerlist;

import com.geekmk.db.dto.LedgerCard;

/**
 * Created by Mani on 03/04/17.
 */

public interface LedgerListView {

    void userPressedAddCardToLedgerSuccess(LedgerCard ledgerCard);

    void userPressedAddCardToLedgerFailure(String reason);

    void userPressedDeleteLedgerSuccess();

    void updateLedgerNameSuccess();
}
