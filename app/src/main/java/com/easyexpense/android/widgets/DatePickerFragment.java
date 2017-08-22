package com.easyexpense.android.widgets;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.DatePicker;

import com.easyexpense.android.tags.CallBackResultSet;

import java.util.Calendar;

/**
 * Created by Mani on 13/03/17.
 */

public class DatePickerFragment extends android.support.v4.app.DialogFragment implements DatePickerDialog.OnDateSetListener {

    private CallBackResultSet callBackResultSet;


    public class MyDatePickerDialog extends DatePickerDialog {

        // Regular constructor
        public MyDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
            super(context, callBack, year, monthOfYear, dayOfMonth);
        }

        // Short constructor
        public MyDatePickerDialog(Context context, OnDateSetListener callBack, Calendar calendar) {
            super(context, callBack, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }

        @Override
        public void onDateChanged(DatePicker view, int year, int month, int day) {
            super.onDateChanged(view, year, month, day);

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(getActivity() instanceof CallBackResultSet){
            callBackResultSet = (CallBackResultSet) getActivity();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Calendar c = Calendar.getInstance();
//
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//        MyDatePickerDialog myDatePickerDialog = new MyDatePickerDialog(getActivity(), this, year-18,month,day);
//        c.set(year-18,month,day);
//        myDatePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        return new MyDatePickerDialog(getActivity(),this,Calendar.getInstance());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        int changedMonth = month+1;

        Calendar cal  =Calendar.getInstance();
        cal.set(year,month,day);
        if(callBackResultSet !=null)
        callBackResultSet.onDialogResultSet(cal.getTimeInMillis());
    }
}