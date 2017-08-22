package com.easyexpense.android.helper.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.easyexpense.android.R;
import com.easyexpense.android.helper.AppConstants;
import com.easyexpense.commons.utils.CollectionUtils;
import com.easyexpense.commons.utils.StringUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mani on 04/04/17.
 */

public class FragmentGenericDialog extends DialogFragment {

    @BindView(R.id.til_tag_name)
    TextInputLayout tilTagName;


    private static final String TAG = "FragmentGenericDialog";


    public static FragmentGenericDialog newInstance(Map<String,Object> existingInfo){
        FragmentGenericDialog genericDialog = new FragmentGenericDialog();
        Bundle bundle = new Bundle();
        if(CollectionUtils.isNotEmptyMap(existingInfo)) {
            String name = (String) existingInfo.get(AppConstants.IntentConstants.GENERIC_NAME);
            bundle.putString(AppConstants.IntentConstants.GENERIC_NAME, name);
            Object ledgerId = existingInfo.get(AppConstants.IntentConstants.LEDGER_ID);
            if(ledgerId!=null && ledgerId instanceof Long){
                bundle.putLong(AppConstants.IntentConstants.LEDGER_ID,(long)ledgerId);
            }
        }
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

    @OnClick(R.id.tvDone) void onAddTagClick(){
        String addedName = WidgetUtils.getTextFromTIL(tilTagName);
        if(StringUtils.isNotEmpty(addedName)){
            WidgetUtils.removeErrorForTil(tilTagName);
            Intent intent = new Intent();
            intent.putExtra(AppConstants.IntentConstants.GENERIC_NAME, addedName);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
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
