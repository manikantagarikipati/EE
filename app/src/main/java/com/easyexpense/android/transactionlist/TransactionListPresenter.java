package com.easyexpense.android.transactionlist;

import android.os.Bundle;

import com.geekmk.db.dto.CardTransaction;

import java.util.List;

/**
 * Created by Mani on 17/04/17.
 */

public interface TransactionListPresenter {

    void onSaveInstanceState(Bundle outState);


    void onCreate(Bundle savedInstanceState);


    void systemFetchTransactionListForLedger(long id,String scenario);


    List<CardTransaction> getEnclosingTransactions();

}
