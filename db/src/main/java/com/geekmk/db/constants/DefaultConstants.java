package com.geekmk.db.constants;

/**
 * Created by Mani on 31/03/17.
 */

public class DefaultConstants {

    private DefaultConstants(){}


    public class DefaultBoardConstants{

        public static final String BOARD_NAME = "Ledger";

        public static final String ACCOUNT_LIST = "Account List";

        public static final String INCOME_LIST = "Income List";

        public static final String EXPENSE_LIST = "Expense List";

    }

    public class LedgerType{

        public static final int TYPE_ACCOUNT_LIST = 1;

        public static final int TYPE_INCOME_LIST = 2;

        public static final int TYPE_EXPENSE_LIST = 3;

    }


    public class TransactionConstants{
        public static final int TRANSACTION_ACCOUNT = 1;

        public static final int TRANSACTION_INCOME = 2;

        public static final int TRANSACTION_EXPENSE = 3;

        public static final int TYPE_CREDIT = 4;

        public static final int TYPE_DEBIT = 5;

        public static final int TYPE_NEUTRAL = 6;
    }
}
