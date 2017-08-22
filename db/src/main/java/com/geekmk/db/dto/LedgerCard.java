package com.geekmk.db.dto;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.geekmk.db.constants.DBConstants;

import java.util.List;

/**
 * Created by Mani on 30/03/17.
 */

@Table(name = DBConstants.TableNames.LEDGER_CARD)
public class LedgerCard extends Model{


    @Column(name = DBConstants.LedgerCardColumn.BOARD_ID)
    private long boardId;

    @Column(name = DBConstants.LedgerCardColumn.NAME)
    private String name;

    @Column(name = DBConstants.LedgerCardColumn.CREATED_TIME_STAMP)
    private long createdTS;

    @Column(name = DBConstants.LedgerCardColumn.UPDATED_TIME_STAMP)
    private long updatedTS;

    @Column(name = DBConstants.LedgerCardColumn.AMOUNT)
    private double amount;

    @Column(name = DBConstants.LedgerCardColumn.LEDGER_ID)
    private long ledgerId;

    @Column(name = DBConstants.LedgerCardColumn.CARD_TYPE)
    private int cardType;

    public long getLastTransactionCardId() {
        return lastTransactionCardId;
    }

    public void setLastTransactionCardId(long lastTransactionCardId) {
        this.lastTransactionCardId = lastTransactionCardId;
    }

    @Column(name = DBConstants.LedgerCardColumn.LAST_TRANSACTION_CARD)
    private long lastTransactionCardId;

    public long getBoardId() {
        return boardId;
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreatedTS() {
        return createdTS;
    }

    public void setCreatedTS(long createdTS) {
        this.createdTS = createdTS;
    }

    public long getUpdatedTS() {
        return updatedTS;
    }

    public void setUpdatedTS(long updatedTS) {
        this.updatedTS = updatedTS;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(long ledgerId) {
        this.ledgerId = ledgerId;
    }

    public int getCardType() {
        return cardType;
    }

    //DefaultConstants.LedgerType.TYPE_INCOME_LIST, Account list, ExpenseList
    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public static List<LedgerCard> getLedgerCardListForLedger(long ledgerId){
        return new Select()
                .from(LedgerCard.class)
                .where(DBConstants.LedgerCardColumn.LEDGER_ID+" = ?",ledgerId)
                .execute();
    }


    public static List<LedgerCard> getLedgerCardForType(int cardType,long boardId){
        return new Select()
                .from(LedgerCard.class)
                .where(DBConstants.LedgerCardColumn.CARD_TYPE+" = ?",cardType)
                .where(DBConstants.LedgerCardColumn.BOARD_ID+" = ?",boardId)
                .orderBy(DBConstants.LedgerCardColumn.NAME+" ASC")
                .execute();
    }

    public static void deleteCardsOfLedger(long boardId){
        new Delete().from(LedgerCard.class).where(DBConstants.LedgerCardColumn.BOARD_ID+" = ?",boardId).execute();
    }

    public static boolean isNameUnique(String name,long boardId) {
        name = name.toLowerCase();
        LedgerCard ledgerCard =
                new Select()
                        .from(LedgerCard.class)
                        .where(DBConstants.LedgerCardColumn.NAME+" = ?",name)
                        .where(DBConstants.LedgerCardColumn.BOARD_ID+" = ?",boardId)
                        .executeSingle();
        return ledgerCard==null;
    }

    @Override
    public String toString() {
        return name;
    }
}
