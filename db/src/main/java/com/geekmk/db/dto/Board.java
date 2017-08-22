package com.geekmk.db.dto;

import android.database.Cursor;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.geekmk.db.constants.DBConstants;

import java.util.List;

/**
 * Created by Mani on 30/03/17.
 */

@Table(name = DBConstants.TableNames.BOARD)
public class Board extends Model {

    @Column(name = DBConstants.BoardColumn.REMOTE_ID)
    private long remoteId;

    @Column(name = DBConstants.BoardColumn.NAME)
    private String name;

    @Column(name = DBConstants.BoardColumn.AMOUNT)
    private double amount;

    @Column(name = DBConstants.BoardColumn.IS_DEFAULT)
    private boolean isDefault;

    @Column(name = DBConstants.BoardColumn.CREATED_TIME_STAMP)
    private long createdTS;

    @Column(name = DBConstants.BoardColumn.UPDATED_TIME_STAMP)
    private long updatedTS;



    public static Board getDefaultBoard(){
        return new Select()
                .from(Board.class)
                .where(DBConstants.BoardColumn.IS_DEFAULT+" = ?",true)
                .executeSingle();
    }

    public static Board getBoard(long id){
        return  Board.load(Board.class,id);
    }

    public static List<Board> getAllBoards(){
        return  new Select().from(Board.class).execute();

    }

    public static boolean deleteBoard(long boardId){
        Board board = Board.load(Board.class,boardId);
        board.delete();
        return true;
    }

    public static long sumOfLedgers(){


        String query = "SELECT SUM("+DBConstants.BoardColumn.AMOUNT+") as total FROM "
                +DBConstants.TableNames.BOARD;

        Cursor c = ActiveAndroid.getDatabase().rawQuery(query, null);

        if(c.moveToFirst()) {
            double value = c.getDouble(c.getColumnIndex("total"));
            c.close();
            return (long) value;
        }
        c.close();
        return 0;
    }

    public long getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(long remoteId) {
        this.remoteId = remoteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

}
