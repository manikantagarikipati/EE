package com.easyexpense.android.ledgerdetail;

import com.geekmk.db.dbcommunicator.EEDbCommunicatorImpl;
import com.geekmk.db.dbcommunicator.EEDbCommunicatorInf;
import com.geekmk.db.dto.Board;
import com.geekmk.db.dto.Ledger;

/**
 * Created by Mani on 20/02/17.
 */

public class LedgerDetailPresenterImpl implements LedgerDetailPresenter {

    private LedgerDetailView view;
    private EEDbCommunicatorInf eeDbCommunicatorInf;
    private LedgerDetailBean ledgerDetailBean;

    public LedgerDetailPresenterImpl(LedgerDetailView view){
        this.view = view;
        this.eeDbCommunicatorInf = new EEDbCommunicatorImpl();
        ledgerDetailBean = new LedgerDetailBean();
    }



    @Override
    public void systemFetchDefaultBoard() {
        Board board = eeDbCommunicatorInf.getDefaultBoard();
        ledgerDetailBean.setBoard(board);
        ledgerDetailBean.setLedgerList(eeDbCommunicatorInf.getLedgerList(board.getId()));
        view.systemFetchHomeInfoSuccess(ledgerDetailBean);
    }

    @Override
    public void systemFetchBoard(long id) {
        if(id==0){
         systemFetchDefaultBoard();
        }else{
            Board board = eeDbCommunicatorInf.getBoard(id);
            ledgerDetailBean.setBoard(board);
            ledgerDetailBean.setLedgerList(eeDbCommunicatorInf.getLedgerList(board.getId()));
            view.systemFetchHomeInfoSuccess(ledgerDetailBean);
        }

    }

    @Override
    public LedgerDetailBean getEnclosingBean() {
        return ledgerDetailBean;
    }

    @Override
    public void renameBoard(String resultObject) {
        eeDbCommunicatorInf.renameBoard(resultObject,ledgerDetailBean.getBoard().getId());
    }

    @Override
    public void checkIfBoardExists() {
        long id = ledgerDetailBean.getBoard().getId();
        Board board = Board.load(Board.class,id);
        if(board==null){
            systemFetchDefaultBoard();
        }
    }

    @Override
    public void userPressedCreateNewLedger(Ledger parentLedger,String name) {
        view.userPressedCreateChildLedgerSuccess(eeDbCommunicatorInf.createChildLedger(parentLedger,name));
    }

//    @Override
//    public void userPressedCreateNewItem(String name) {
////        view.userPressedCreateNewItemSuccess(createItemBean(name));
//
//    }

//    public ItemBean createItemBean(String i){
//        ItemBean creditItemBean= new ItemBean();
//        creditItemBean.setTitle("Creditasdfasdfasdfasdf "+i);
//        creditItemBean.setActionItem("Add Card "+i);
//        creditItemBean.setCardBeanList(getCardBeanList("Credit "+i));
//        return creditItemBean;
//    }
//    private List<CardBean> getCardBeanList(String item){
//        List<CardBean> cardBeanList = new ArrayList<>();
//
//        for(int i=0;i<6;i++){
//            CardBean cardBean = new CardBean();
//            cardBean.setName("Food ");
//            cardBean.setCredit(true);
//            cardBean.setItem(item);
//            cardBean.setAmount(500+i);
//            cardBean.setTransactionList(getTransactionList());
//            cardBeanList.add(cardBean);
//
//            CardBean cardBean1 = new CardBean();
//            cardBean1.setName("Cabel TV ");
//            cardBean1.setCredit(true);
//            cardBean1.setItem(item);
//            cardBean1.setAmount(100+i);
//            cardBean1.setTransactionList(getTransactionList());
//            cardBeanList.add(cardBean1);
//
//            CardBean cardBean2 = new CardBean();
//            cardBean2.setName("Medicine");
//            cardBean2.setCredit(true);
//            cardBean2.setItem(item);
//            cardBean2.setAmount(800+i);
//            cardBean2.setTransactionList(getTransactionList());
//            cardBeanList.add(cardBean2);
//        }
//
//
//        return cardBeanList;
//    }


//    private List<Transaction> getTransactionList(){
//
//        List<Transaction> transactionsList = new ArrayList<>();
//
//        for(int i=0;i<4;i++){
//            Transaction transaction = new Transaction();
//            transaction.setName("Mani "+i);
//            transaction.setAmount(12000+(i*100));
//            transaction.setType("creditType");
//            transaction.setTime(Calendar.getInstance().getTimeInMillis());
//            transactionsList.add(transaction);
//        }
//
//        return transactionsList;
//    }

}
