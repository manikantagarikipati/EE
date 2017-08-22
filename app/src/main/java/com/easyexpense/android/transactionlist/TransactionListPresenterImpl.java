package com.easyexpense.android.transactionlist;

import android.os.Bundle;

import com.easyexpense.android.helper.AppConstants;
import com.geekmk.db.dto.CardTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mani on 17/04/17.
 */

public class TransactionListPresenterImpl implements TransactionListPresenter {

    private TransactionListView view;


    private List<CardTransaction> cardTransactionList = new ArrayList<>();

    public TransactionListPresenterImpl(TransactionListView view){
        this.view = view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

    }


    @Override
    public void systemFetchTransactionListForLedger(long id,String scenario) {


        switch (scenario){

            case AppConstants.TransactionListConstants.LEDGER:
                cardTransactionList = CardTransaction.getTransactionForLedgerId(id);
                break;

            case AppConstants.TransactionListConstants.BOARD:
                cardTransactionList = CardTransaction.getTransactionForBoardId(id);
                break;

            case AppConstants.TransactionListConstants.CARD:
                cardTransactionList = CardTransaction.getTransactionForCardId(id);
                break;
        }
        view.systemFetchHomeInfoSuccess(cardTransactionList);
    }

    @Override
    public List<CardTransaction> getEnclosingTransactions() {
        return  cardTransactionList;
    }

}
