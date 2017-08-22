package com.easyexpense.android.expenseinput;

import com.easyexpense.commons.utils.CollectionUtils;
import com.geekmk.db.constants.DefaultConstants;
import com.geekmk.db.dbcommunicator.EEDbCommunicatorImpl;
import com.geekmk.db.dbcommunicator.EEDbCommunicatorInf;
import com.geekmk.db.dto.Ledger;
import com.geekmk.db.dto.LedgerCard;

import java.util.ArrayList;
import java.util.List;

import static com.geekmk.db.dto.LedgerCard.getLedgerCardForType;

/**
 * Created by Mani on 24/03/17.
 */

public class ExpensePresenterImpl implements ExpensePresenter {


    private ExpenseInputBean bean;

    private ExpenseView view;

    private EEDbCommunicatorInf eeDbCommunicatorInf;

    public  ExpensePresenterImpl(ExpenseView expenseView){
        this.view = expenseView;
        bean = new ExpenseInputBean();
        eeDbCommunicatorInf = new EEDbCommunicatorImpl();
    }



    @Override
    public ExpenseInputBean getBean() {
        return bean;
    }

    @Override
    public void systemSetPageInfo(long sourceCardId,boolean isSwap) {

        bean.setSourceCardId(sourceCardId);

        setBeanInfoBasedOnTheCardType();

        if(CollectionUtils.isNotEmpty(bean.getSourceCardList())){
            if(CollectionUtils.isNotEmpty(bean.getDestinationCardList())){

                if(isSwap){
                    bean.setDestinationCardId(bean.getLastSwapId());
                }else{
                    if(bean.getCurrentScenario() == DefaultConstants.TransactionConstants.TRANSACTION_EXPENSE){
                        LedgerCard destinationCardList = bean.getDestinationCardList().get(0);
                        bean.setDestinationCardId(destinationCardList.getId());
                        long lastTransactionCard = destinationCardList.getLastTransactionCardId();
                        LedgerCard sourceLedgerCard;
                        if(lastTransactionCard!=0){
                            sourceLedgerCard = LedgerCard.load(LedgerCard.class,lastTransactionCard);

                        }else{
                            sourceLedgerCard = bean.getSourceCardList().get(0);
                        }
                        bean.setSourceCardId(sourceLedgerCard.getId());

                    }else{
                        LedgerCard sourceLedgerCard = bean.getSourceCardList().get(0);
                        long lastTransactionCard = sourceLedgerCard.getLastTransactionCardId();
                        LedgerCard destinationLedgerCard;
                        if(lastTransactionCard!=0){
                            destinationLedgerCard = LedgerCard.load(LedgerCard.class,lastTransactionCard);

                        }else{
                            destinationLedgerCard = bean.getDestinationCardList().get(0);
                        }
                        bean.setDestinationCardId(destinationLedgerCard.getId());
                    }

                }

                view.systemSetPageInfoSuccess(bean.getSourceCardList(),bean.getDestinationCardList());
            }else{
                view.systemSetPageInfoFailure("Cannot Fetch Destination Cards please add destination cards.");
            }
        }else{
            view.systemSetPageInfoFailure("Cannot Fetch Source Cards please add source cards.");
        }

    }


    @Override
    public void performTransaction(String providedAmount,long destinationCardId,List<Long> selectedIdList) {
        LedgerCard ledgerCard = LedgerCard.load(LedgerCard.class,bean.getSourceCardId());


        double providedAmountInLong = Double.valueOf(providedAmount);
        switch (bean.getCurrentScenario()){
            case DefaultConstants.TransactionConstants.TRANSACTION_ACCOUNT:
                eeDbCommunicatorInf.performAccountTransaction(bean.getSourceCardId(),destinationCardId,
                        providedAmountInLong,bean.getComment(),bean.getSelectedImage(),selectedIdList);
                view.userPressedTransactionSuccess(ledgerCard.getBoardId());
                break;
            case DefaultConstants.TransactionConstants.TRANSACTION_INCOME:
                eeDbCommunicatorInf.performIncomeTransaction(bean.getSourceCardId(),
                        destinationCardId,providedAmountInLong,bean.getComment(),bean.getSelectedImage(),selectedIdList);
                view.userPressedTransactionSuccess(ledgerCard.getBoardId());
                break;
            case DefaultConstants.TransactionConstants.TRANSACTION_EXPENSE:
                eeDbCommunicatorInf.performExpenseTransaction(bean.getSourceCardId(),
                        destinationCardId,providedAmountInLong,bean.getComment(),bean.getSelectedImage(),selectedIdList);
                view.userPressedTransactionSuccess(ledgerCard.getBoardId());
                break;
        }
    }

    @Override
    public void swapAccounts() {
        try{
            LedgerCard sourceCard = LedgerCard.load(LedgerCard.class,bean.getSourceCardId());
            LedgerCard destinationCard = LedgerCard.load(LedgerCard.class,bean.getDestinationCardId());

            if(sourceCard.getCardType() == DefaultConstants.LedgerType.TYPE_ACCOUNT_LIST
                    && destinationCard.getCardType() == DefaultConstants.LedgerType.TYPE_ACCOUNT_LIST){
                bean.setLastSwapId(bean.getSourceCardId());
                systemSetPageInfo(bean.getDestinationCardId(),true);
            }

        }catch (Exception e){}
    }

    private void setBeanInfoBasedOnTheCardType() {
        LedgerCard ledgerCard = LedgerCard.load(LedgerCard.class,bean.getSourceCardId());

        Ledger ledger = Ledger.load(Ledger.class,ledgerCard.getLedgerId());


        List<LedgerCard> sourceList = new ArrayList<>();

        switch (ledger.getLedgerType()){
            case DefaultConstants.LedgerType.TYPE_INCOME_LIST:
                bean.setCurrentScenario(DefaultConstants.TransactionConstants.TRANSACTION_INCOME);
                sourceList.add(ledgerCard);
                bean.setSourceCardList(sourceList);
                bean.setDestinationCardList(getLedgerCardForType(
                        DefaultConstants.LedgerType.TYPE_ACCOUNT_LIST,ledger.getBoardId()));
                break;
            case DefaultConstants.LedgerType.TYPE_ACCOUNT_LIST:
                bean.setCurrentScenario(DefaultConstants.TransactionConstants.TRANSACTION_ACCOUNT);
                sourceList.add(ledgerCard);
                bean.setSourceCardList(sourceList);

                List<LedgerCard> destinationList1 = LedgerCard.getLedgerCardForType(
                        DefaultConstants.LedgerType.TYPE_ACCOUNT_LIST,ledger.getBoardId());

                int pos1 = destinationList1.indexOf(sourceList.get(0));
                if(pos1!=-1){
                    destinationList1.remove(pos1);
                }
                bean.setDestinationCardList(destinationList1);
                break;
            case DefaultConstants.LedgerType.TYPE_EXPENSE_LIST:
                bean.setCurrentScenario(DefaultConstants.TransactionConstants.TRANSACTION_EXPENSE);
                List<LedgerCard> destCards = new ArrayList<>();
                destCards.add(ledgerCard);
                bean.setSourceCardList(getLedgerCardForType(
                        DefaultConstants.LedgerType.TYPE_ACCOUNT_LIST,ledger.getBoardId()));
                bean.setDestinationCardList(destCards);
                break;
        }
    }
}
