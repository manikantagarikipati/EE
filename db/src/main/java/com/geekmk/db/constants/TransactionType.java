package com.geekmk.db.constants;

/**
 * Created by Mani on 03/04/17.
 */

public enum TransactionType {

    CREDIT("credit"),
    DEBIT("debit");

    private String value;

    TransactionType(String value){
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static TransactionType fromString(String value) {
        for (TransactionType fname : values()) {
            if (fname.toString().equals(value)) {
                return fname;
            }
        }
        return null;
    }
}