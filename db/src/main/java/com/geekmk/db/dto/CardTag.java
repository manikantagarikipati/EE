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

@Table(name = DBConstants.TableNames.CARD_TAG)
public class CardTag extends Model {

    @Column(name = DBConstants.CardTagColumn.REMOTE_ID)
    private long remoteId;

    @Column(name = DBConstants.CardTagColumn.NAME)
    private String name;

    @Column(name = DBConstants.CardTagColumn.BOARD_ID)
    private long boardId;

    @Column(name = DBConstants.CardTagColumn.CARD_ID)
    private long cardId;

    @Column(name = DBConstants.CardTagColumn.IS_DEFAULT)
    private boolean isDefault;


    @Column(name = DBConstants.BoardColumn.CREATED_TIME_STAMP)
    private long createdTS;

    @Column(name = DBConstants.BoardColumn.UPDATED_TIME_STAMP)
    private long updatedTS;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBoardId() {
        return boardId;
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }


    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public void setCreatedTS(long createdTS) {
        this.createdTS = createdTS;
    }

    public void setUpdatedTS(long updatedTS) {
        this.updatedTS = updatedTS;
    }

    public static List<CardTag> getTagsForCard(long cardId){
        return  new Select()
                .from(CardTag.class)
                .where(DBConstants.CardTagColumn.CARD_ID+" = ?",cardId)
                .execute();
    }

    public static CardTag updateTagName(int id,String tagName){
        return null;
    }

    public static void deleteTagsForBoard(long boardId){
        new Delete().from(CardTag.class).where(DBConstants.CardTagColumn.BOARD_ID+" = ?",boardId).execute();
    }

    public static List<CardTag> getTagsForBoard(long boardId){

        return  new Select()
                .from(CardTag.class)
                .where(DBConstants.CardTagColumn.BOARD_ID+" = ?",boardId)
                .execute();
    }

    public static boolean isDuplicate(String name,long ledgerCardId){
        CardTag cardTagList =   new Select()
                .from(CardTag.class)
                .where(DBConstants.CardTagColumn.CARD_ID+" = ?",ledgerCardId)
                .where(DBConstants.CardTagColumn.NAME+" = ?",name)
                .executeSingle();
        return  cardTagList!=null;

    }
    public static List<CardTag> getTagsForCardOrderedByDefault(long cardId) {
        return  new Select()
                .from(CardTag.class)
                .where(DBConstants.CardTagColumn.CARD_ID+" = ?",cardId)
                .orderBy(DBConstants.CardTagColumn.IS_DEFAULT+" DESC")
                .execute();
    }

    @Override
    public String toString() {
        return name;
    }
}
