package com.easyexpense.android.helper.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.easyexpense.android.R;
import com.easyexpense.android.helper.AppConstants;
import com.easyexpense.commons.utils.CollectionUtils;
import com.easyexpense.commons.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mani on 04/04/17.
 */

public class GenericDialog extends DialogFragment {

    @BindView(R.id.til_tag_name)
    TextInputLayout tilTagName;

    private GenericDialogCallBack callBackResultSet;


    private static final String TAG = "GenericDialog";

    private int requestCode =0;

    private long ledgerId = 0;


    public static GenericDialog newInstance(Map<String,Object> existingInfo,int requestCode){
        GenericDialog genericDialog = new GenericDialog();
        Bundle bundle = new Bundle();
        if(CollectionUtils.isNotEmptyMap(existingInfo)) {
            String name = (String) existingInfo.get(AppConstants.IntentConstants.GENERIC_NAME);
            bundle.putString(AppConstants.IntentConstants.GENERIC_NAME, name);
            Object ledgerId = existingInfo.get(AppConstants.IntentConstants.LEDGER_ID);
            if(ledgerId!=null && ledgerId instanceof Long){
                bundle.putLong(AppConstants.IntentConstants.LEDGER_ID,(long)ledgerId);
            }
        }
        bundle.putInt(AppConstants.IntentConstants.REQUEST_CODE,requestCode);
        genericDialog.setArguments(bundle);
        return genericDialog;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle bundle) {
        View rootView = layoutInflater.inflate(R.layout.alert_generic_dialog, parent, false);
        ButterKnife.bind(this, rootView);
        checkIfEditCaseAndSetInfo();
        return rootView;
    }

    private void checkIfEditCaseAndSetInfo() {

        if(getArguments()!=null){
            String providedTag = getArguments().getString(AppConstants.IntentConstants.GENERIC_NAME);
            Object id = getArguments().getLong(AppConstants.IntentConstants.LEDGER_ID);
            if(id instanceof Long){
                ledgerId = (long)id;
            }
            requestCode = getArguments().getInt(AppConstants.IntentConstants.REQUEST_CODE);
            if(StringUtils.isNotEmpty(providedTag)){
                WidgetUtils.setTextInputLayoutInfo(tilTagName,providedTag);
                tilTagName.setHint("Edit");
            }

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if(dialog.getWindow()!=null){
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow()!=null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }

        AppUtils.showKeyBoard(getContext());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof GenericDialogCallBack){
            callBackResultSet = (GenericDialogCallBack) getActivity();
        }else{
            Log.e(TAG,"Enclosing activity should call DialogResultSetInterface");
        }
    }

    @OnClick(R.id.tvDone) void onAddTagClick(){
        String addedName = WidgetUtils.getTextFromTIL(tilTagName);
        if(StringUtils.isNotEmpty(addedName)){
            WidgetUtils.removeErrorForTil(tilTagName);
            Map<String,Object> info = new HashMap<>();
            info.put(AppConstants.IntentConstants.GENERIC_NAME,addedName);
            info.put(AppConstants.IntentConstants.LEDGER_ID,ledgerId);
            if(callBackResultSet !=null){
                callBackResultSet.onDialogResultSet(info,requestCode);
            }
            dismiss();
        }   else{
            WidgetUtils.setErrorToTil(tilTagName,getResources().getString(R.string.empty_input,getResources().getString(R.string.name)));
        }
    }

    @OnClick(R.id.cancel_dialog) void onCancelDialogClick(){

        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        AppUtils.closeKeyBoard(getContext());
    }

}
