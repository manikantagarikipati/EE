package com.geekmk.db.dbcommunicator;

import android.net.Uri;

import com.geekmk.db.dto.Board;
import com.geekmk.db.dto.Ledger;
import com.geekmk.db.dto.LedgerCard;

import java.util.List;

/**
 * Created by Mani on 31/03/17.
 */

public interface EEDbCommunicatorInf {

    Board getDefaultBoard();

    Board getBoard(long id);


    List<Ledger> getLedgerList(long boardId);

    LedgerCard addLedgerCard(long boardId,long ledgerId,String name,int type,String amount);

    void performIncomeCardAdditionTransaction(long sourceCardId,double amount);

    void renameBoard(String resultObject,long boardId);

    List<Board> getBoardList();

    Board createBoard(String boardName);

    void deleteBoard(long id);

    Ledger createChildLedger(Ledger parentLedger,String name);

    void deleteLedger(long id);

    void renameLedger(String name, long id);

    void performIncomeTransaction(long sourceCardId, long destinationCardId, double amount,String comment,Uri imageUrl,List<Long> tags);

    void performExpenseTransaction(long sourceCardId, long destinationCardId, double amount,String comment,Uri imageUrl,List<Long> tags);

    void performAccountTransaction(long sourceCardId, long destinationCardId, double amount,String comment,Uri imageUrl,List<Long> tags);
}
