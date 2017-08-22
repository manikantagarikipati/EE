package com.easyexpense.android.helper.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easyexpense.android.R;
import com.easyexpense.commons.utils.DimenUtils;
import com.easyexpense.commons.utils.StringUtils;

/**
 * Created by Manikanta on 23/06/16.
 */
public class WidgetUtils {


    private WidgetUtils(){}

    public static void setBackGroundToIv(ImageView imageView,Drawable drawable){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageView.setBackground(drawable);
        }else{
            imageView.setBackgroundDrawable(drawable);
        }

    }

    public static String getTextFromTIL(TextInputLayout textInputLayout) {
        if (textInputLayout != null && textInputLayout.getEditText() != null) {
            return getText(textInputLayout.getEditText());
        } else {
            return "";
        }
    }



    public static String getTextFromET(EditText editText) {
        if (editText != null ) {
            return editText.getText().toString();
        } else {
            return "";
        }
    }

    public static void setErrorToTil(TextInputLayout textInputLayout,String error){
        if(textInputLayout!=null){
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(error);
        }
    }

    public static void removeErrorForTil(TextInputLayout textInputLayout){
        if(textInputLayout!=null){
            textInputLayout.setErrorEnabled(false);
            textInputLayout.setError("");
        }
    }
    public static String getTextFromTILWithoutSpaces(TextInputLayout textInputLayout) {
        if (textInputLayout != null && textInputLayout.getEditText() != null) {
            return getTextWithoutSpaces(textInputLayout.getEditText());
        } else {
            return "";
        }
    }

    public static String getTextWithoutSpaces(TextView view) {
        if (view != null) {
            return view.getText().toString().trim().replace(" ","");
        } else {
            return "";
        }
    }


    public static String getText(TextView view) {
        if (view != null) {
            return view.getText().toString().trim();
        } else {
            return "";
        }
    }


    public static void setShapeBackground(Drawable background,int color){
        if (background instanceof ShapeDrawable) {
            // cast to 'ShapeDrawable'
            ShapeDrawable shapeDrawable = (ShapeDrawable) background;
            shapeDrawable.getPaint().setColor(color);
        } else if (background instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            gradientDrawable.setColor(color);
        } else if (background instanceof ColorDrawable) {
            // alpha value may need to be set again after this call
            ColorDrawable colorDrawable = (ColorDrawable) background;
            colorDrawable.setColor(color);
        }
    }

    public static void setTextInputLayoutInfo(TextInputLayout til, String data) {
        if(til!=null && til.getEditText()!=null && StringUtils.isNotEmpty(data)){
            til.getEditText().getText().clear();
            til.getEditText().append(data);

        }
    }



    public static void setEditTextInfo(EditText editText, String data) {
        if(editText!=null && StringUtils.isNotEmpty(data)){
            editText.getText().clear();
            editText.append(data);

        }
    }

    public static void clearEditTextInfo(EditText editText){
        try{
            if(editText!=null && StringUtils.isNotEmpty(editText.getText().toString().trim())){
                editText.setText("");
                editText.clearFocus();
            }
        }catch (Exception e){}

    }


    public static View getSeparatorView(Context context){

        View view = new View(context);
        view.setBackgroundColor(ContextCompat.getColor(context,R.color.transparent_grey));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, DimenUtils.getDimensInPxFromDp(1,context));

        int margins = DimenUtils.getDimensInPxFromDp(16,context);

        layoutParams.setMargins(margins,margins,margins,margins);
        view.setLayoutParams(layoutParams);

        return view;
    }

    public static void displaySnackBar(String message, View coordinatorLayout) {
        Snackbar.make(coordinatorLayout,message,Snackbar.LENGTH_SHORT).show();
    }
}
