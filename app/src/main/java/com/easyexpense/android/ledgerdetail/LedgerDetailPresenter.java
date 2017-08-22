package com.easyexpense.android.ledgerdetail;

import com.geekmk.db.dto.Ledger;

/**
 * Created by Mani on 20/02/17.
 */

public interface LedgerDetailPresenter {



    void systemFetchDefaultBoard();

    void systemFetchBoard(long id);

    LedgerDetailBean getEnclosingBean();

    void renameBoard(String resultObject);

    void checkIfBoardExists();

    void userPressedCreateNewLedger(Ledger parentLedger,String name);
}
