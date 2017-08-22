package com.easyexpense.android.ledgerdetail;

import com.geekmk.db.dto.Board;
import com.geekmk.db.dto.Ledger;

import java.util.List;

/**
 * Created by Mani on 31/03/17.
 */

public class LedgerDetailBean {

    private Board board;

    private List<Ledger> ledgerList;

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<Ledger> getLedgerList() {
        return ledgerList;
    }

    public void setLedgerList(List<Ledger> ledgerList) {
        this.ledgerList = ledgerList;
    }
}
