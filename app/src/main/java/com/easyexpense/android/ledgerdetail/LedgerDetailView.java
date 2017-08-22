package com.easyexpense.android.ledgerdetail;

import com.geekmk.db.dto.Ledger;

/**
 * Created by Mani on 20/02/17.
 */

public interface LedgerDetailView {

    void systemFetchHomeInfoSuccess(LedgerDetailBean ledgerDetailBean);

    void updateToolbarDetails(String title,String subTitle);

    void userPressedCreateChildLedgerSuccess(Ledger ledger);
}
