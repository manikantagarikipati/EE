package com.geekmk.db.constants;

/**
 * Created by Mani on 30/03/17.
 */

public class DBConstants {

    private DBConstants(){}


    public static class TableNames{

        public static final String BOARD = "table_board";

        public static final String CARD_TAG = "table_card_tag";

        public static final String CARD_TRANSACTION = "table_card_transaction";

        public static final String LEDGER = "table_ledger";

        public static final String LEDGER_CARD = "table_ledger_card";

    }

    public static class BoardColumn{

        public static final String REMOTE_ID = "remote_id";

        public static final String NAME = "name";

        public static final String IS_DEFAULT = "is_default";

        public static final String  AMOUNT = "balance_amount";

        public static final String  CREATED_TIME_STAMP = "created_ts";

        public static final String UPDATED_TIME_STAMP = "updated_ts";

    }

    public static class CardTagColumn{

        public static final String REMOTE_ID = "remote_id";

        public static final String NAME = "name";

        public static final String CARD_ID = "card_id";

        public static final String IS_DEFAULT = "is_default";

        public static final String  CREATED_TIME_STAMP = "created_ts";

        public static final String UPDATED_TIME_STAMP = "updated_ts";

        public static final String BOARD_ID = "board_id";
    }

    public static class  CardTransactionColumn{

        public static final String REMOTE_ID = "remote_id";

        public static final String SOURCE_CARD_ID   = "source_card_id";

        public static final String DESTINATION_CARD_ID = "destination_card_id";

        public static final String SOURCE_LIST_ID = "source_list_id";

        public static final String DESTINATION_LIST_ID = "destination_list_id";

        public static final String BOARD_ID = "board_id";

        public static final String TRANSACTION_DATE = "transaction_date";

        public static final String IMAGE_PATH = "image_path";

        public static final String COMMENT = "comment";

        public static final String  AMOUNT = "amount";

        public static final String ATTACHED_TAGS = "attached_tags";

        public static final String  CREATED_TIME_STAMP = "created_ts";

        public static final String UPDATED_TIME_STAMP = "updated_ts";

        public static final String TRANSACTION_TYPE = "transaction_type";

    }

    public static class LedgerColumn {

        public static final String BOARD_ID = "board_id";

        public static final String NAME = "name";

        public static final String CURRENT_FILTER = "current_filter";

        public static final String  CREATED_TIME_STAMP = "created_ts";

        public static final String UPDATED_TIME_STAMP = "updated_ts";

        public static final String AMOUNT = "amount";

        public static final String IS_DEFAULT = "is_default";

        public static final String LEDGER_TYPE = "ledger_type";

        public static final String POSITION_ORDER = "position_order";
    }

    public static class LedgerCardColumn {

        public static final String LEDGER_ID = "ledger_id";

        public static final String BOARD_ID = "board_id";

        public static final String NAME = "name";

        public static final String AMOUNT = "amount";

        public static final String  CREATED_TIME_STAMP = "created_ts";

        public static final String UPDATED_TIME_STAMP = "updated_ts";

        public static final String CARD_TYPE = "card_type";

        public static final String LAST_TRANSACTION_CARD = "last_transaction_card";

    }
}
