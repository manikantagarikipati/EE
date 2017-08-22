package com.easyexpense.android.ledgerdetail.ledgerlist;

import com.easyexpense.android.helper.utils.AppUtils;
import com.easyexpense.android.transactionlist.TransactionListActivity;
import com.easyexpense.commons.utils.StringUtils;
import com.geekmk.db.constants.DefaultConstants;
import com.geekmk.db.dbcommunicator.EEDbCommunicatorImpl;
import com.geekmk.db.dbcommunicator.EEDbCommunicatorInf;
import com.geekmk.db.dto.Ledger;
import com.geekmk.db.dto.LedgerCard;

/**
 * Created by Mani on 03/04/17.
 */

public class LedgerListPresenterImpl implements  LedgerListPresenter{

    private Ledger bean;

    private LedgerListView view;

    private EEDbCommunicatorInf eeDbCommunicatorInf;

    public LedgerListPresenterImpl(Ledger bean,LedgerListView view){
        this.bean =bean;
        this.view = view;
        eeDbCommunicatorInf = new EEDbCommunicatorImpl();
    }

    @Override
    public void userPressedAddCardToLedger(String name,String amount) {
        if(StringUtils.isEmpty(amount)){
            amount = "0";
        }

        LedgerCard ledgerCard = eeDbCommunicatorInf.addLedgerCard(bean.getBoardId(),bean.getId(),name,bean.getLedgerType(),amount);
        boolean isIncome = bean.getLedgerType() == DefaultConstants.LedgerType.TYPE_INCOME_LIST;
        if(isIncome){
            eeDbCommunicatorInf.performIncomeCardAdditionTransaction(ledgerCard.getId(),ledgerCard.getAmount());
        }
        view.userPressedAddCardToLedgerSuccess(ledgerCard);
    }

    @Override
    public void deleteLedger() {
        eeDbCommunicatorInf.deleteLedger(bean.getId());
        view.userPressedDeleteLedgerSuccess();
    }

    @Override
    public void updateLedgerName(String name) {
        eeDbCommunicatorInf.renameLedger(name,bean.getId());
        view.updateLedgerNameSuccess();
    }

}
