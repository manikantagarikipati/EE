package com.easyexpense.android.helper.utils;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.easyexpense.android.helper.AppConstants;
import com.easyexpense.commons.android.constants.GlobalConstants;
import com.geekmk.db.dto.Board;

import java.io.File;

/**
 * Created by Mani on 13/04/17.
 */

public class ExcelExporter extends IntentService {

    private static final String TAG = "EXCEL_EXPORTER";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ExcelExporter(String name) {
        super(name);
    }

    public ExcelExporter(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        try{

            switch (intent.getType()){
                case  GlobalConstants.ExcelConstants.EXPORT_TYPE_BOARD:
                    //todo display notification that it started
                    long boardId = intent.getExtras().getLong(AppConstants.IntentConstants.EXPORT_EXCEL_SUBJECT_ID);
                    Board board = Board.load(Board.class,boardId);
                    AppUtils.displayExportNotification(this,"Creating Excel Sheet","Creating Excel for "+board.getName(),1);
                    File createdFile = ExcelUtils.createCardsExcelSheetForBoard(boardId);
                    AppUtils.removeNotification(1,this);
                    AppUtils.displayExcelCreatedNotification(this,createdFile);
                    break;
                case  GlobalConstants.ExcelConstants.EXPORT_TYPE_CARD:
                    break;
                case  GlobalConstants.ExcelConstants.EXPORT_TYPE_LEDGER:
                    break;
            }

        }catch (Exception e){
            Log.e(TAG,e.getLocalizedMessage());
        }
    }
}
