package com.easyexpense.commons.android.widgets.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.easyexpense.commons.android.constants.GlobalConstants;


/**
 * @author Manikanta
 * Custom Button to override android default button settings
 */
public class PrimaryButton extends Button {

    private static Typeface mTypeface;

    public PrimaryButton(final Context context) {
        this(context, null);
        initView(context);
    }

    public PrimaryButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);

    }

    public PrimaryButton(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }


    private void initView(Context context) {
        if(!isInEditMode()) {
            if (mTypeface == null) {
                mTypeface = Typeface.createFromAsset(context.getAssets(), GlobalConstants.ViewFonts.PRIMARY_FONT);
            }
            setTypeface(mTypeface);
        }
    }
}