package com.easyexpense.android.helper;

/**
 * Created by Mani on 12/03/17.
 */

public class AppConstants {

    private AppConstants(){}


    public static class IntentConstants{

        public static final String INTENT_DATA = "intentData";

        public static final String TAG_POSITION = "tagPosition";

        public static final String IS_DATA_CHANGED = "dataChanged";

        public static final String BOARD_ID = "boardId";

        public static final int LEDGER_LIST_REQUEST_CODE = 12;

        public static final int TRANSACTION_LIST_REQUEST_CODE = 13;

        public static final String REQUEST_CODE = "requestCode";

        public static final String GENERIC_NAME = "genericName";
        public static final String LEDGER_ID = "ledgerId";
        public static final String CARD_ID = "cardId";
        public static final String TAG_ID = "tagId";

        public static final String CURRENT_SCENARIO = "currentScenario";

        public static final String EXPORT_EXCEL_SUBJECT_ID ="subjectId";

    }

    public static class TransactionListConstants{


        public static final String LEDGER = "ledger";

        public static final String BOARD = "board";

        public static final String CARD = "CARD";
    }

    public static class  MediaConstants{
        public static final int ADD_IMAGE_VIA_GALLERY = 61;
    }


}
