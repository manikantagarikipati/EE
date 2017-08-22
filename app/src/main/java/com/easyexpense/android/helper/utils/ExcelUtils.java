package com.easyexpense.android.helper.utils;

import android.util.Log;

import com.easyexpense.commons.utils.FileUtils;
import com.easyexpense.commons.utils.StringUtils;
import com.geekmk.db.constants.DefaultConstants;
import com.geekmk.db.dto.Board;
import com.geekmk.db.dto.CardTag;
import com.geekmk.db.dto.CardTransaction;
import com.geekmk.db.dto.Ledger;
import com.geekmk.db.dto.LedgerCard;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mani on 14/04/17.
 */

public class ExcelUtils {




    private  ExcelUtils(){}


    private static final String TAG = ExcelUtils.class.getSimpleName();


    public static File createExcelSheetForLedgerList(long ledgerId){
        // check if available and not read only
        if (!FileUtils.isExternalStorageAvailable() || FileUtils.isExternalStorageReadOnly()) {
            Log.e("Excel Utils", "Storage not available or read only");
            return null;
        }


        Ledger ledger = Ledger.load(Ledger.class,ledgerId);

        List<LedgerCard> ledgerCardList = LedgerCard.getLedgerCardListForLedger(ledger.getId());

        Workbook wb = new HSSFWorkbook();



        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);



        for(LedgerCard ledgerCard: ledgerCardList){

            try{
                //New Sheet for card
                Sheet sheet1 = null;
                String safeSheetName = WorkbookUtil.createSafeSheetName(ledgerCard.getName());
                sheet1 = wb.createSheet(safeSheetName);

                int rowValue = 0;


                for(int i=0;i<7;i++){
                    sheet1.setColumnWidth(i,7500);

                }


                sheet1.setMargin(Sheet.LeftMargin, 0.25);
                sheet1.setMargin(Sheet.RightMargin, 0.25);
                sheet1.setMargin(Sheet.TopMargin, 0.75);
                sheet1.setMargin(Sheet.BottomMargin, 0.75);
                sheet1.setMargin(Sheet.HeaderMargin, 0.25);
                sheet1.setMargin(Sheet.FooterMargin, 0.25);


                Row datarow1 = sheet1.createRow(rowValue++);
                Object [] objArr1 = getLedgerColumnNames();
                int cellnum1 = 0;
                for (Object obj1 : objArr1) {
                    Cell cell = datarow1.createCell(cellnum1++);

                    cell.setCellValue(obj1+"");
                }



                List<CardTransaction> transactionList = CardTransaction.getTransactionForCardId(ledgerCard.getId());

                Map<String,Object[]> data = createTransactionsWithData(transactionList);

                Set<String> keyset = data.keySet();

                for (String key : keyset) {
                    Row datarow = sheet1.createRow(rowValue++);
                    Object [] objArr = data.get(key);
                    int cellnum = 0;
                    for (Object obj : objArr) {
                        Cell cell = datarow.createCell(cellnum++);

                        cell.setCellValue(obj+"");
                    }
                }

            }catch (Exception e){
                Log.e(ExcelUtils.class.getName(),"Error creating sheet for excel list of card");
            }
        }

        return createExcelSheet(wb,ledger.getName());

    }


    public static File createTransactionsExcelSheetForCard(long cardId){


        // check if available and not read only
        if (!FileUtils.isExternalStorageAvailable() || FileUtils.isExternalStorageReadOnly()) {
            Log.e("Excel Utils", "Storage not available or read only");
            return null;
        }

        LedgerCard ledgerCard = LedgerCard.load(LedgerCard.class,cardId);



        //New Workbook
        Workbook wb = new HSSFWorkbook();



        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        try{
            //New Sheet for card
            Sheet sheet1 = null;
            String safeSheetName = WorkbookUtil.createSafeSheetName(ledgerCard.getName());
            sheet1 = wb.createSheet(safeSheetName);

            int rowValue = 0;


            for(int i=0;i<4;i++){
                sheet1.setColumnWidth(i,6500);

            }


            sheet1.setMargin(Sheet.LeftMargin, 0.25);
            sheet1.setMargin(Sheet.RightMargin, 0.25);
            sheet1.setMargin(Sheet.TopMargin, 0.75);
            sheet1.setMargin(Sheet.BottomMargin, 0.75);
            sheet1.setMargin(Sheet.HeaderMargin, 0.25);
            sheet1.setMargin(Sheet.FooterMargin, 0.25);



            Row datarow1 = sheet1.createRow(rowValue++);
            Object [] objArr1 = getLedgerColumnNames();
            int cellnum1 = 0;
            for (Object obj1 : objArr1) {
                Cell cell = datarow1.createCell(cellnum1++);

                cell.setCellValue(obj1+"");
            }



            List<CardTransaction> transactionList = CardTransaction.getTransactionForCardId(cardId);

            Map<String,Object[]> data = createTransactionsWithData(transactionList);

            Set<String> keyset = data.keySet();

            for (String key : keyset) {
                Row datarow = sheet1.createRow(rowValue++);
                Object [] objArr = data.get(key);
                int cellnum = 0;
                for (Object obj : objArr) {
                    Cell cell = datarow.createCell(cellnum++);

                    cell.setCellValue(obj+"");
                }
            }


        }catch ( Exception e){

        }
        return  createExcelSheet(wb,ledgerCard.getName());
    }


    public static File createCardsExcelSheetForBoard(long boardId) {

        Board board = Board.load(Board.class, boardId);
        // check if available and not read only
        if (!FileUtils.isExternalStorageAvailable() || FileUtils.isExternalStorageReadOnly()) {
            Log.e("Excel Utils", "Storage not available or read only");
            return null;
        }
        List<Ledger> ledgerList = Ledger.getLedgerListForBoard(boardId);

        //New Workbook
        Workbook wb = new HSSFWorkbook();


        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        for (Ledger ledger : ledgerList) {
            try {
                //New Sheet for ledger
                Sheet sheet1 = null;
                String safeSheetName = WorkbookUtil.createSafeSheetName(ledger.getName());
                sheet1 = wb.createSheet(safeSheetName);

                int rowValue = 0;




                for (int i = 0; i < 7; i++) {
                    sheet1.setColumnWidth(i, 6500);

                }


                sheet1.setMargin(Sheet.LeftMargin, 0.25);
                sheet1.setMargin(Sheet.RightMargin, 0.25);
                sheet1.setMargin(Sheet.TopMargin, 0.75);
                sheet1.setMargin(Sheet.BottomMargin, 0.75);
                sheet1.setMargin(Sheet.HeaderMargin, 0.25);
                sheet1.setMargin(Sheet.FooterMargin, 0.25);


                Row datarow1 = sheet1.createRow(rowValue++);
                Object[] objArr1 = getBoardHeadingNames();
                int cellnum1 = 0;
                for (Object obj1 : objArr1) {
                    Cell cell = datarow1.createCell(cellnum1++);

                    cell.setCellValue(obj1 + "");
                }


                List<LedgerCard> ledgerCardList = LedgerCard.getLedgerCardListForLedger(ledger.getId());
                Map<String, Object[]> data = createCardsWithData(ledgerCardList);

                Set<String> keyset = data.keySet();

                for (String key : keyset) {
                    Row datarow = sheet1.createRow(rowValue++);
                    Object[] objArr = data.get(key);
                    int cellnum = 0;
                    for (Object obj : objArr) {
                        Cell cell = datarow.createCell(cellnum++);

                        cell.setCellValue(obj + "");
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "error creating sheet for ledger" + e.getMessage());
            }
        }

        return createExcelSheet(wb, board.getName());
    }

    private static File createExcelSheet(Workbook wb,String boardName){
        String nonConflictingFileName = FileUtils.getNonConflictingFileName(boardName);
        // Create a path where we will place our List of objects on external storage

        File file = null;
        FileOutputStream os = null;
        try {
            file = FileUtils.createExcelFile(nonConflictingFileName);
            os  = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
        } catch (IOException e) {
            Log.e("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.e("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }

        return  file;
    }
    private static Map<String,Object[]> createCardsWithData(List<LedgerCard> cardList) {
        Map<String, Object[]> data = new HashMap<>();
        for(int i=0;i<cardList.size();i++){
            LedgerCard ledgerCard = cardList.get(i);
            String date = getDate(ledgerCard.getCreatedTS());
            String type = getCardType(ledgerCard.getCardType());
            data.put(i+"", new Object[] {ledgerCard.getName(), ledgerCard.getAmount(), type,date});
        }
        return data;
    }

    private static Map<String,Object[]> createTransactionsWithData(List<CardTransaction> transactionList) {
        Map<String, Object[]> data = new HashMap<>();
        for(int i=0;i<transactionList.size();i++){
            CardTransaction transaction = transactionList.get(i);
            String date = getDate(transaction.getCreatedTS());
            String type = getTransactionType(transaction.getTransactionType());

            String tags = "No Tags added";

            try{
                long tagId = transaction.getAttachedTags();

                CardTag cardTag = CardTag.load(CardTag.class,tagId);
                if(StringUtils.isNotEmpty(cardTag.getName())){
                    tags = cardTag.getName();
                }

            }catch (Exception e){}

            String sourceCard="";
            try {
                LedgerCard ledgerCard = LedgerCard.load(LedgerCard.class, transaction.getSourceCard());
                sourceCard = sourceCard+ledgerCard.getName();
            }catch (Exception e){}

            String destCard="";
            try {
                LedgerCard destLCard = LedgerCard.load(LedgerCard.class, transaction.getDestinationCard());
                destCard = destCard+destLCard.getName();
            }catch (Exception e){}

            String transacionComment = "No Comments";
            if(StringUtils.isNotEmpty(transaction.getComment())){
                transacionComment = transaction.getComment();
            }

            data.put(i+"", new Object[] { transaction.getAmount(),
                    type, date,transacionComment, tags ,
                    sourceCard,destCard});
        }
        return data;
    }


    private static String getDate(long timInMillis){
        Date date=new Date(timInMillis);

        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        return  df2.format(date);
    }

    private static String getCardType(int type){
        switch (type){

            case 1:
                return "Account";
            case 2:
                return "Income";
            case 3:
                return "Expense";
        }

        return "Credit Nor Debit";
    }


    private static String getTransactionType(int type){
        switch (type){

            case DefaultConstants.TransactionConstants.TRANSACTION_EXPENSE:
                return "Expense Transaction";
            case DefaultConstants.TransactionConstants.TRANSACTION_ACCOUNT:
                return "Account Transaction";
            case DefaultConstants.TransactionConstants.TRANSACTION_INCOME:
                return "Income Transaction";
        }

        return "";
    }


    public static Object[] getLedgerColumnNames() {
        Object[] ledgerColumnNames = new Object[7];
        ledgerColumnNames[0] = "Amount";
        ledgerColumnNames[1] = "Type";
        ledgerColumnNames[2] = "Date";
        ledgerColumnNames[3] = "Comment";

        ledgerColumnNames[4] = "Tag";
        ledgerColumnNames[5] = "Source Card";
        ledgerColumnNames[6] = "DestinationCard";



        return ledgerColumnNames;
    }

    public static Object[] getBoardHeadingNames() {

        Object[] boardHeadingNames = new Object[4];
        boardHeadingNames[0] = "Ledger Name";
        boardHeadingNames[1] = "Amount";
        boardHeadingNames[2] = "Type";
        boardHeadingNames[3] = "Date";

        return boardHeadingNames;
    }
}
