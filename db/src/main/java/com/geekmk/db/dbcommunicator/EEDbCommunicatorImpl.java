package com.geekmk.db.dbcommunicator;

import android.net.Uri;
import android.util.Log;

import com.easyexpense.commons.utils.CollectionUtils;
import com.easyexpense.commons.utils.StringUtils;
import com.geekmk.db.constants.DefaultConstants;
import com.geekmk.db.constants.TransactionType;
import com.geekmk.db.dto.Board;
import com.geekmk.db.dto.CardTag;
import com.geekmk.db.dto.CardTransaction;
import com.geekmk.db.dto.Ledger;
import com.geekmk.db.dto.LedgerCard;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Mani on 31/03/17.
 */

public class EEDbCommunicatorImpl implements EEDbCommunicatorInf {


    private static final String TAG = "EEDbCommunicatorImpl";

    @Override
    public Board getDefaultBoard() {
        Board defaultBoard =  Board.getDefaultBoard();
        if(defaultBoard == null){
            long defaultId = createInternalBoard(true,null);
            createLedgers(defaultId);
            return Board.load(Board.class,defaultId);
        }
        return defaultBoard;
    }


    private long createInternalBoard(boolean isDefault,String name) {
        Board board = new Board();
        if(StringUtils.isNotEmpty(name)){
            board.setName(name);
        }else{
            board.setName(DefaultConstants.DefaultBoardConstants.BOARD_NAME);
        }
        board.setAmount(0);
        board.setCreatedTS(Calendar.getInstance().getTimeInMillis());
        board.setUpdatedTS(Calendar.getInstance().getTimeInMillis());
        board.setDefault(isDefault);
        board.setRemoteId(0);
        return board.save();
    }

    private void createLedgers(long boardId) {

        Ledger expenseLedger = new Ledger();
        expenseLedger.setName(DefaultConstants.DefaultBoardConstants.EXPENSE_LIST);
        expenseLedger.setDefault(true);
        expenseLedger.setCreatedTS(Calendar.getInstance().getTimeInMillis());
        expenseLedger.setUpdatedTS(Calendar.getInstance().getTimeInMillis());
        expenseLedger.setAmount(0);
        expenseLedger.setBoardId(boardId);
        expenseLedger.setLedgerType(DefaultConstants.LedgerType.TYPE_EXPENSE_LIST);
        expenseLedger.setPositionOrder(1000);
        expenseLedger.save();



        Ledger accountLedger = new Ledger();
        accountLedger.setName(DefaultConstants.DefaultBoardConstants.ACCOUNT_LIST);
        accountLedger.setDefault(true);
        accountLedger.setCreatedTS(Calendar.getInstance().getTimeInMillis());
        accountLedger.setUpdatedTS(Calendar.getInstance().getTimeInMillis());
        accountLedger.setAmount(0);
        accountLedger.setBoardId(boardId);
        accountLedger.setLedgerType(DefaultConstants.LedgerType.TYPE_ACCOUNT_LIST);
        accountLedger.setPositionOrder(2000);
        accountLedger.save();


        Ledger incomeLedger = new Ledger();
        incomeLedger.setName(DefaultConstants.DefaultBoardConstants.INCOME_LIST);
        incomeLedger.setDefault(true);
        incomeLedger.setCreatedTS(Calendar.getInstance().getTimeInMillis());
        incomeLedger.setUpdatedTS(Calendar.getInstance().getTimeInMillis());
        incomeLedger.setAmount(0);
        incomeLedger.setBoardId(boardId);
        incomeLedger.setLedgerType(DefaultConstants.LedgerType.TYPE_INCOME_LIST);
        incomeLedger.setPositionOrder(3000);
        incomeLedger.save();

    }

    @Override
    public Board getBoard(long id) {
        return Board.getBoard(id);
    }

    @Override
    public List<Ledger> getLedgerList(long boardId) {
        return  Ledger.getLedgerListForBoard(boardId);
    }

    @Override
    public LedgerCard addLedgerCard(long boardId, long ledgerId, String name,int cardType, String amount) {
        LedgerCard ledgerCard = new LedgerCard();
        ledgerCard.setBoardId(boardId);

        ledgerCard.setName(name.toLowerCase());

        if (StringUtils.isEmpty(amount)) {
            ledgerCard.setAmount(0);
        } else {
            ledgerCard.setAmount(Long.parseLong(amount));
        }
        ledgerCard.setCardType(cardType);
        ledgerCard.setLedgerId(ledgerId);
        ledgerCard.setUpdatedTS(Calendar.getInstance().getTimeInMillis());
        ledgerCard.setCreatedTS(Calendar.getInstance().getTimeInMillis());
        ledgerCard.save();

        return ledgerCard;
    }

    @Override
    public void performIncomeTransaction(long sourceCardId, long destinationCardId, double amount,
                                         String comment,Uri imageUrl,List<Long> tags) {
        LedgerCard sourceCard = LedgerCard.load(LedgerCard.class,sourceCardId);

        //credit amount to source
        performOperation(sourceCardId,amount,TransactionType.CREDIT);
        updateLedgerBalance(sourceCard.getLedgerId(),amount,TransactionType.CREDIT);
        //credit amount to account
        LedgerCard destinationCard = LedgerCard.load(LedgerCard.class,destinationCardId);

        sourceCard.setLastTransactionCardId(destinationCardId);
        sourceCard.save();

        performOperation(destinationCardId,amount,TransactionType.CREDIT);
        updateLedgerBalance(destinationCard.getLedgerId(),amount,TransactionType.CREDIT);
        updateBoardBalance(sourceCard.getBoardId(),amount,TransactionType.CREDIT);

        addTransaction(amount,sourceCardId,destinationCardId,sourceCard.getBoardId(),
                comment,imageUrl,tags,sourceCard.getLedgerId(),
                destinationCard.getLedgerId(),DefaultConstants.TransactionConstants.TRANSACTION_INCOME);

    }

    @Override
    public void performExpenseTransaction(long sourceCardId, long destinationCardId,
                                          double amount,String comment,Uri imageUrl,List<Long> tags) {
        LedgerCard sourceCard = LedgerCard.load(LedgerCard.class,sourceCardId);
        //deduct amount from source
        performOperation(sourceCardId,amount,TransactionType.DEBIT);
        //update balance
        updateLedgerBalance(sourceCard.getLedgerId(),amount,TransactionType.DEBIT);
        updateBoardBalance(sourceCard.getBoardId(),amount,TransactionType.DEBIT);
        //credit amunt to destinatio
        LedgerCard destinationCard = LedgerCard.load(LedgerCard.class,destinationCardId);

        destinationCard.setLastTransactionCardId(sourceCardId);
        destinationCard.save();

        performOperation(destinationCardId,amount,TransactionType.CREDIT);
        updateLedgerBalance(destinationCard.getLedgerId(),amount,TransactionType.CREDIT);
        addTransaction(amount,sourceCardId,destinationCardId,
                sourceCard.getBoardId(),comment,imageUrl,tags,sourceCard.getLedgerId(),
                destinationCard.getLedgerId(), DefaultConstants.TransactionConstants.TRANSACTION_EXPENSE);

    }


    @Override
    public void performAccountTransaction(long sourceCardId, long destinationCardId, double amount,
                                            String comment,Uri imageUrl,List<Long> tags) {

        LedgerCard sourceCard = LedgerCard.load(LedgerCard.class,sourceCardId);
        //remove amount from source card info
        performOperation(sourceCardId,amount,TransactionType.DEBIT);
        //and also from ledger balance
        updateLedgerBalance(sourceCard.getLedgerId(),amount,TransactionType.DEBIT);
//        //update balance
//        updateBoardBalance(sourceCard.getBoardId(),amount,TransactionType.DEBIT);

        //credit amunt to destination

        LedgerCard destinationCard = LedgerCard.load(LedgerCard.class,destinationCardId);

        sourceCard.setLastTransactionCardId(destinationCardId);
        sourceCard.save();

        performOperation(destinationCardId,amount,TransactionType.CREDIT);
        updateLedgerBalance(destinationCard.getLedgerId(),amount,TransactionType.CREDIT);
        //update balance
//        updateBoardBalance(destinationCard.getBoardId(),amount,TransactionType.CREDIT);

        addTransaction(amount,sourceCardId,destinationCardId,sourceCard.getBoardId(),
                comment,imageUrl,tags,sourceCard.getLedgerId(),destinationCard.getLedgerId(),DefaultConstants.TransactionConstants.TRANSACTION_ACCOUNT);

    }

    private void addTransaction(double amount,long sourceCardId,long destinationCardId,
                                long boardId,String comment,Uri imageUrl,List<Long> tagsList,
                                long sourceListId,long destinationListId,int transactionType){
        CardTransaction cardTransaction = new CardTransaction();
        cardTransaction.setCreatedTS(Calendar.getInstance().getTimeInMillis());
        cardTransaction.setAmount(amount);
        cardTransaction.setSourceCard(sourceCardId);
        if(CollectionUtils.isNotEmpty(tagsList))
        cardTransaction.setAttachedTags(tagsList.get(0));
        cardTransaction.setDestinationCard(destinationCardId);
        cardTransaction.setEnclosingBoard(boardId);
        cardTransaction.setTransactionType(transactionType);
        cardTransaction.setUpdatedTS(Calendar.getInstance().getTimeInMillis());

        cardTransaction.setSourceListId(sourceListId);
        cardTransaction.setDestinationListId(destinationListId);
        if(StringUtils.isNotEmpty(comment))
        cardTransaction.setComment(comment);
        if(imageUrl!=null)
        cardTransaction.setImagePath(imageUrl.toString());
        cardTransaction.setTransactionDate(Calendar.getInstance().getTimeInMillis());
        cardTransaction.save();
    }
    @Override
    public void renameBoard(String resultObject,long boardId) {
        Board board = Board.load(Board.class,boardId);
        board.setName(resultObject);
        board.save();
    }

    @Override
    public List<Board> getBoardList() {
        return Board.getAllBoards();
    }

    @Override
    public Board createBoard(String boardName) {
        long boardId = createInternalBoard(false,boardName);
        createLedgers(boardId);
        return Board.load(Board.class,boardId);
    }

    @Override
    public void deleteBoard(long id) {
        CardTag.deleteTagsForBoard(id);
        CardTransaction.deleteTransactionsOfBoard(id);
        LedgerCard.deleteCardsOfLedger(id);
        Ledger.deleteLedgersForTable(id);
        Board.deleteBoard(id);

    }

    @Override
    public Ledger createChildLedger(Ledger parentLedger,String name) {
        Ledger ledger = new Ledger();
        ledger.setName(name);
        ledger.setLedgerType(parentLedger.getLedgerType());
        ledger.setAmount(0);
        ledger.setBoardId(parentLedger.getBoardId());
        ledger.setDefault(false);
        ledger.setCreatedTS(Calendar.getInstance().getTimeInMillis());
        ledger.setUpdatedTS(Calendar.getInstance().getTimeInMillis());
//        int pos = Ledger.getMaxOfLedgerType(parentLedger.getLedgerType());
        int pos = parentLedger.getPositionOrder();
        int finalPosition = ++pos;

        List<Ledger> ledgerList = Ledger.getLedgerListOfType(parentLedger.getBoardId(),
                parentLedger.getLedgerType());

        for(Ledger ledger1:ledgerList){
            if(ledger1.getPositionOrder()>=finalPosition){
                ledger1.setPositionOrder(++finalPosition);
                ledger1.save();
            }
        }

        ledger.setPositionOrder(pos);
        ledger.save();
        return ledger;
    }

    @Override
    public void deleteLedger(long id) {
        Ledger ledger = Ledger.load(Ledger.class,id);
        ledger.delete();
    }

    @Override
    public void renameLedger(String name, long id) {
        Ledger ledger = Ledger.load(Ledger.class,id);
        ledger.setName(name);
        ledger.save();
    }

    @Override
    public void performIncomeCardAdditionTransaction(long sourceCardId, double amount) {
        LedgerCard ledgerCard = LedgerCard.load(LedgerCard.class,sourceCardId);


        // add ledger income
        updateLedgerBalance(ledgerCard.getLedgerId(),amount,TransactionType.CREDIT);
        // add main balance
        updateBoardBalance(ledgerCard.getBoardId(),amount,TransactionType.CREDIT);
    }


    private synchronized void updateLedgerBalance(long ledgerId,double amount,TransactionType type){
        Ledger ledger = Ledger.load(Ledger.class,ledgerId);
        if(ledger!=null){
            switch (type){
                case CREDIT:
                    double currentAmount = ledger.getAmount();
                    currentAmount+=amount;
                    ledger.setAmount(currentAmount);
                    ledger.save();
                    break;
                case DEBIT:
                    double existingAmount = ledger.getAmount();
                    existingAmount-=amount;
                    ledger.setAmount(existingAmount);
                    ledger.save();
                    break;
            }
        }else{
            Log.e(TAG,"Error while performing transaction For ledger "+ledgerId+" "+ amount+" "+type);
        }
    }


    private synchronized void performOperation(long cardId,double amount,TransactionType type){
        LedgerCard ledgerCard = LedgerCard.load(LedgerCard.class,cardId);
        if(ledgerCard!=null){
            switch (type){
                case CREDIT:
                    double currentAmount = ledgerCard.getAmount();
                    currentAmount+=amount;
                    ledgerCard.setAmount(currentAmount);
                    ledgerCard.save();
                    break;
                case DEBIT:
                    double existingAmount = ledgerCard.getAmount();
                    existingAmount-=amount;
                    ledgerCard.setAmount(existingAmount);
                    ledgerCard.save();
                    break;
            }
        }else{
            Log.e(TAG,"Error while performing transaction "+cardId+" "+ amount+" "+type);
        }
    }

    private synchronized void updateBoardBalance(long boardId,double amount,TransactionType type){
        Board board = Board.load(Board.class,boardId);
        if(board!=null){
            switch (type){
                case CREDIT:
                    double currentAmount = board.getAmount();
                    currentAmount+=amount;
                    board.setAmount(currentAmount);
                    board.save();
                    break;
                case DEBIT:
                    double existingAmount = board.getAmount();
                    existingAmount-=amount;
                    board.setAmount(existingAmount);
                    board.save();
                    break;
            }
        }else{
            Log.e(TAG,"Error while updating balance For Board"+boardId+" "+ amount+" "+type);
        }
    }
}
