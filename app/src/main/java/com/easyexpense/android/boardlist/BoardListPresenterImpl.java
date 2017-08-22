package com.easyexpense.android.boardlist;

import com.geekmk.db.dbcommunicator.EEDbCommunicatorImpl;
import com.geekmk.db.dbcommunicator.EEDbCommunicatorInf;

/**
 * Created by Mani on 19/03/17.
 */

public class BoardListPresenterImpl implements BoardListPresenter {

    private BoardListView view;

    private EEDbCommunicatorInf eeDbCommunicatorInf;

    public BoardListPresenterImpl(BoardListView view){
        this.view = view;
        this.eeDbCommunicatorInf = new EEDbCommunicatorImpl();
    }


    @Override
    public void systemFetchBoardList() {
        view.systemFetchBoardListSuccess(eeDbCommunicatorInf.getBoardList());
    }

    @Override
    public void addBoard(String boardName) {

        view.addBoardSuccess(eeDbCommunicatorInf.createBoard(boardName));
    }

    @Override
    public void deleteBoard(long id) {
        eeDbCommunicatorInf.deleteBoard(id);
    }

}
