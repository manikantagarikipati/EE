package com.easyexpense.android.transactionlist;

import com.geekmk.db.dto.CardTransaction;

import java.util.List;

/**
 * Created by Mani on 17/04/17.
 */

public interface TransactionListView {

    void systemFetchHomeInfoSuccess(List<CardTransaction> transactionBeanList);
}
