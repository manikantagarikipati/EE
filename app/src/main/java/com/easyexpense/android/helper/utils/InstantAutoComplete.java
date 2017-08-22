package com.easyexpense.android.helper.utils;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import com.easyexpense.commons.android.constants.GlobalConstants;

/**
 * Created by Mani on 17/04/17.
 */

public class InstantAutoComplete extends AutoCompleteTextView {

    private static Typeface mTypeface;

    public InstantAutoComplete(Context context) {
        super(context);
//        initFont(context);
    }

    public InstantAutoComplete(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
//        initFont(arg0);
    }

    public InstantAutoComplete(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
//        initFont(arg0);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    private void initFont(Context context) {
        if(!isInEditMode()) {
            setFilters(new InputFilter[]{new EditTextFilter()});
            if (mTypeface == null) {
                mTypeface = Typeface.createFromAsset(context.getAssets(), GlobalConstants.ViewFonts.SECONDARY_FONT);
            }
            setTypeface(mTypeface);
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            performFiltering(getText(), 0);
        }
    }
}

