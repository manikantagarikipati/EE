package com.easyexpense.android.expenseinput;

import android.net.Uri;

import com.geekmk.db.dto.LedgerCard;

import java.util.List;

/**
 * Created by Mani on 10/04/17.
 */

public class ExpenseInputBean {

    private int currentScenario;

    private long sourceCardId;

    private long destinationCardId;

    private long amount;

    private Uri selectedImage;

    private long lastSwapId;

    public long getLastSwapId() {
        return lastSwapId;
    }

    public void setLastSwapId(long lastSwapId) {
        this.lastSwapId = lastSwapId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Uri getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(Uri selectedImage) {
        this.selectedImage = selectedImage;
    }

    private String comment;


    public int getCurrentScenario() {
        return currentScenario;
    }

    public void setCurrentScenario(int currentScenario) {
        this.currentScenario = currentScenario;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getSourceCardId() {
        return sourceCardId;
    }

    public void setSourceCardId(long sourceCardId) {
        this.sourceCardId = sourceCardId;
    }

    public long getDestinationCardId() {
        return destinationCardId;
    }

    public List<LedgerCard> getSourceCardList() {
        return sourceCardList;
    }

    public void setSourceCardList(List<LedgerCard> sourceCardList) {
        this.sourceCardList = sourceCardList;
    }

    public List<LedgerCard> getDestinationCardList() {
        return destinationCardList;
    }

    public void setDestinationCardList(List<LedgerCard> destinationCardList) {
        this.destinationCardList = destinationCardList;
    }

    public void setDestinationCardId(long destinationCardId) {
        this.destinationCardId = destinationCardId;
    }


    private List<LedgerCard> sourceCardList;

    private List<LedgerCard> destinationCardList;

}
