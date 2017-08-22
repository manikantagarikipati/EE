package com.easyexpense.android.boardlist;

import com.geekmk.db.dto.Board;

import java.util.List;

/**
 * Created by Mani on 19/03/17.
 */

public interface BoardListView {

    void systemFetchBoardListSuccess(List<Board> boardList);

    void addBoardSuccess(Board board);
}
