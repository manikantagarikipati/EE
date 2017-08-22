package com.geekmk.db.dto;

import android.database.Cursor;

import com.activeandroid.ActiveAndroid;
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

@Table( name = DBConstants.TableNames.LEDGER)
public class Ledger extends Model{

    @Column(name = DBConstants.LedgerColumn.BOARD_ID)
    private long boardId;

    @Column(name = DBConstants.LedgerColumn.NAME)
    private String name;

    @Column(name = DBConstants.LedgerColumn.CURRENT_FILTER)
    private int currentFilter;

    @Column(name = DBConstants.LedgerColumn.CREATED_TIME_STAMP)
    private long createdTS;

    @Column(name = DBConstants.LedgerColumn.UPDATED_TIME_STAMP)
    private long updatedTS;

    @Column(name = DBConstants.LedgerColumn.AMOUNT)
    private double amount;

    @Column(name = DBConstants.LedgerColumn.IS_DEFAULT)
    private boolean isDefault;

    @Column(name = DBConstants.LedgerColumn.LEDGER_TYPE)
    private int ledgerType;

    public int getPositionOrder() {
        return positionOrder;
    }

    public void setPositionOrder(int positionOrder) {
        this.positionOrder = positionOrder;
    }

    @Column(name = DBConstants.LedgerColumn.POSITION_ORDER)
    private int positionOrder;


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

    public int getCurrentFilter() {
        return currentFilter;
    }

    public void setCurrentFilter(int currentFilter) {
        this.currentFilter = currentFilter;
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

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public int getLedgerType() {
        return ledgerType;
    }

    public void setLedgerType(int ledgerType) {
        this.ledgerType = ledgerType;
    }

    public static List<Ledger> getLedgerListForBoard(long boardId){

        return new Select()
                .from(Ledger.class)
                .where(DBConstants.LedgerColumn.BOARD_ID+" = ?",boardId)
                .orderBy(DBConstants.LedgerColumn.POSITION_ORDER+" ASC")
                .execute();

    }

    public static List<Ledger> getLedgerListOfType(long boardId,int ledgerType){

        return new Select()
                .from(Ledger.class)
                .where(DBConstants.LedgerColumn.BOARD_ID+" = ?",boardId)
                .where(DBConstants.LedgerColumn.LEDGER_TYPE+" = ?",ledgerType)
                .orderBy(DBConstants.LedgerColumn.POSITION_ORDER+" ASC")
                .execute();

    }

    public static int getMaxOfLedgerType(int ledgerType){

        String query = "SELECT MAX("+DBConstants.LedgerColumn.POSITION_ORDER+") as maxValue FROM "
                +DBConstants.TableNames.LEDGER +" WHERE "+DBConstants.LedgerColumn.LEDGER_TYPE+" = "+ledgerType;

        Cursor c = ActiveAndroid.getDatabase().rawQuery(query, null);

        if(c.moveToFirst()) {
            int value = c.getInt(c.getColumnIndex("maxValue"));
            c.close();
            return (int) value;
        }
        c.close();
        return 0;
    }

    public static void deleteLedgersForTable(long boardId){
        new Delete().from(Ledger.class).where(DBConstants.LedgerColumn.BOARD_ID+" = ?",boardId).execute();
    }
}
