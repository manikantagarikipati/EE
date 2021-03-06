package com.easyexpense.commons.android.widgets.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.easyexpense.commons.android.constants.GlobalConstants;


/**
 * Custom Text view overriding the system textview which overrides the default settings
 * of the apps text view
 * @author Manikanta
 */
public class PrimaryTextView extends TextView {

    /*
     * Caches typefaces based on their file path and name, so that they don't have to be created every time when they are referenced.
     */
    private static Typeface mTypeface;

    public PrimaryTextView(final Context context) {
        this(context, null);
        initView(context);
    }

    public PrimaryTextView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
        initView(context);
    }

    private void initView(Context context) {
        if(!isInEditMode()){
            if (mTypeface == null) {
                mTypeface = Typeface.createFromAsset(context.getAssets(), GlobalConstants.ViewFonts.PRIMARY_FONT);
            }
            setTypeface(mTypeface);
        }

    }

    public PrimaryTextView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

}
