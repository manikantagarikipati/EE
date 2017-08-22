package com.easyexpense.commons.android.constants;

/**
 * Created by Manikanta on 6/5/2016.
 * @author Manikanta
 * Holds all constants related to the andriod commons
 */
public class GlobalConstants {


    public static String BASE_DIRECTORY = "/EasyExpense";

    public static final String EXCEL_EXTENSION = ".xls";


    //hiding the default constructor
    private GlobalConstants(){}


    public static class ViewFonts {
        public static final String SECONDARY_FONT = "fonts/Poppins-Regular.ttf";
        public static final String PRIMARY_FONT = "fonts/Montserrat-Bold.ttf";
    }

    public static class ExcelConstants{


        public static final String LEDGER_CARD_NAME_COLUMN = "Name";

        public static final String LEDGER_CARD_AMOUNT_COLUMN = "Amount";

        public static final String LEDGER_CARD_CREATED_TIME_COLUMN = "Created On";

        public static final String LEDGER_CARD_TYPE_COLUMN = "Type";


        public static final String EXPORT_TYPE_BOARD = "exportBoard";

        public static final String EXPORT_TYPE_LEDGER= "exportLedger";

        public static final String EXPORT_TYPE_CARD = "exportCard";
    }
}
