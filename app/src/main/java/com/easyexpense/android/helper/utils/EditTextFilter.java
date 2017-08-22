package com.easyexpense.android.helper.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by Mani on 17/04/17.
 */

public class EditTextFilter implements InputFilter {

    //Avoid spaces in editText if given by replacing space with null
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        for (int i = start; i < end; i++) {
            if (Character.isSpaceChar(source.charAt(i))) {
                return "";
            }
        }
        return null;
    }
}