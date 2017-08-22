package com.easyexpense.android.boardlist;

/**
 * Created by Mani on 19/03/17.
 */

public interface BoardListPresenter {

    void systemFetchBoardList();


    void addBoard(String boardName);

    void deleteBoard(long id);
}
