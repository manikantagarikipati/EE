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

@Table(name = DBConstants.TableNames.CARD_TRANSACTION)
public class CardTransaction extends Model{

    @Column(name = DBConstants.CardTransactionColumn.REMOTE_ID)
    private long remoteId;

    @Column(name = DBConstants.CardTransactionColumn.SOURCE_CARD_ID)
    private long sourceCard;

    @Column(name = DBConstants.CardTransactionColumn.DESTINATION_CARD_ID)
    private long destinationCard;

    @Column(name = DBConstants.CardTransactionColumn.SOURCE_LIST_ID)
    private long sourceListId;

    @Column(name = DBConstants.CardTransactionColumn.DESTINATION_LIST_ID)
    private long destinationListId;

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    @Column(name = DBConstants.CardTransactionColumn.TRANSACTION_TYPE)
    private int transactionType;

    public long getSourceListId() {
        return sourceListId;
    }

    public void setSourceListId(long sourceListId) {
        this.sourceListId = sourceListId;
    }

    public long getDestinationListId() {
        return destinationListId;
    }

    public void setDestinationListId(long destinationListId) {
        this.destinationListId = destinationListId;
    }

    @Column(name = DBConstants.CardTransactionColumn.BOARD_ID)
    private long enclosingBoard;

    @Column(name = DBConstants.CardTransactionColumn.TRANSACTION_DATE)
    private long transactionDate;

    @Column(name = DBConstants.CardTransactionColumn.IMAGE_PATH)
    private String imagePath;

    @Column(name = DBConstants.CardTransactionColumn.COMMENT)
    private String comment;

    @Column(name = DBConstants.CardTransactionColumn.AMOUNT)
    private double amount;

    @Column(name = DBConstants.CardTransactionColumn.ATTACHED_TAGS)
    private long attachedTags;

    @Column(name = DBConstants.CardTransactionColumn.CREATED_TIME_STAMP)
    private long createdTS;

    @Column(name = DBConstants.CardTransactionColumn.UPDATED_TIME_STAMP)
    private long updatedTS;

    public long getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(long remoteId) {
        this.remoteId = remoteId;
    }

    public long getSourceCard() {
        return sourceCard;
    }

    public void setSourceCard(long sourceCard) {
        this.sourceCard = sourceCard;
    }

    public long getDestinationCard() {
        return destinationCard;
    }

    public void setDestinationCard(long destinationCard) {
        this.destinationCard = destinationCard;
    }

    public long getEnclosingBoard() {
        return enclosingBoard;
    }

    public void setEnclosingBoard(long enclosingBoard) {
        this.enclosingBoard = enclosingBoard;
    }

    public long getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(long transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getAttachedTags() {
        return attachedTags;
    }

    public void setAttachedTags(long attachedTags) {
        this.attachedTags = attachedTags;
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

    public static CardTransaction createTransaction(){
        return null;
    }

    public static int deleteTransaction(int id){
        return 0;
    }

    public static void deleteTransactionsOfBoard(long boardId){
        new Delete().from(CardTransaction.class).where(DBConstants.CardTransactionColumn.BOARD_ID+" = ?",boardId).execute();
    }

    public static List<CardTransaction> getTransactionForLedgerId(long ledgerId){
        return new Select()
                .from(CardTransaction.class)
                .where(DBConstants.CardTransactionColumn.SOURCE_LIST_ID+" = ? OR "
                        +DBConstants.CardTransactionColumn.DESTINATION_LIST_ID+" = ?",ledgerId,ledgerId)
                .orderBy(DBConstants.CardTransactionColumn.CREATED_TIME_STAMP+" DESC")
                .execute();
    }


    public static List<CardTransaction> getTransactionForCardId(long cardId){
        return new Select()
                .from(CardTransaction.class)
                .where(DBConstants.CardTransactionColumn.SOURCE_CARD_ID+" = ? OR "
                        +DBConstants.CardTransactionColumn.DESTINATION_CARD_ID+" = ?",cardId,cardId)
                .orderBy(DBConstants.CardTransactionColumn.CREATED_TIME_STAMP+" DESC")
                .execute();
    }

    public static List<CardTransaction> getTransactionForBoardId(long boardId){
        return new Select()
                .from(CardTransaction.class)
                .where(DBConstants.CardTransactionColumn.BOARD_ID+" = ?",boardId)
                .orderBy(DBConstants.CardTransactionColumn.CREATED_TIME_STAMP+" DESC")
                .execute();
    }

}
