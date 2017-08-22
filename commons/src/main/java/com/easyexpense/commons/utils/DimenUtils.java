package com.easyexpense.commons.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Mani on 18/11/16.
 */

public class DimenUtils {

    private DimenUtils(){}



    public static int getDimensInPxFromDp(int dimenInDp, Context context){

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimenInDp, context.getResources().getDisplayMetrics());
    }

    public static int getDimenInPxFromSp(int dimenInSp,Context context){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dimenInSp, context.getResources().getDisplayMetrics());
    }
}
