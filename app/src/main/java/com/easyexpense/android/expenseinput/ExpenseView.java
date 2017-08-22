package com.easyexpense.android.expenseinput;

import com.geekmk.db.dto.LedgerCard;

import java.util.List;

/**
 * Created by Mani on 24/03/17.
 */

public interface ExpenseView {

    void systemSetPageInfoSuccess(List<LedgerCard> sourceList, List<LedgerCard> destinatoinList);

    void systemSetPageInfoFailure(String reason);

    void userPressedTransactionSuccess(long boardId);
}
