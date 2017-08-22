package com.easyexpense.commons.android.widgets.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.easyexpense.commons.android.constants.GlobalConstants;

/**
 * @author Manikanta
 * Overrides app default edittext settings
 */
public class SecondaryEditText extends TextInputEditText {

    private static Typeface mTypeface;

    public SecondaryEditText(final Context context) {
        super(context);
        initFont(context);
    }

    public SecondaryEditText(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        initFont(context);
    }

    private void initFont(Context context) {
        if (!isInEditMode()){
            if (mTypeface == null) {
                mTypeface = Typeface.createFromAsset(context.getAssets(), GlobalConstants.ViewFonts.SECONDARY_FONT);
            }
        setTypeface(mTypeface);

        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }
    }
    public SecondaryEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFont(context);
    }

}
