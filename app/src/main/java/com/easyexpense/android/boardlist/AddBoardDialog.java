package com.easyexpense.android.boardlist;

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
import android.widget.TextView;

import com.easyexpense.android.R;
import com.easyexpense.android.helper.utils.AppUtils;
import com.easyexpense.android.helper.utils.WidgetUtils;
import com.easyexpense.android.tags.CallBackResultSet;
import com.easyexpense.commons.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mani on 13/03/17.
 */

public class AddBoardDialog extends DialogFragment{


    @BindView(R.id.til_board_name)
    TextInputLayout tilBoardName;

    @BindView(R.id.tv_label_title)
    TextView tvLabelTitle;

    private CallBackResultSet callBackResultSet;


    private static final String TAG = "AddBoardDialog";

    public static AddBoardDialog newInstance(){
        return new AddBoardDialog();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle bundle) {
        View rootView = layoutInflater.inflate(R.layout.alert_add_board, parent, false);
        ButterKnife.bind(this, rootView);
        return rootView;
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
        if (getActivity() instanceof CallBackResultSet){
            callBackResultSet = (CallBackResultSet) getActivity();
        }else{
            Log.e(TAG,"Enclosing activity should call DialogResultSetInterface");
        }
    }

    @OnClick(R.id.tv_add_board) void onAddBoardClick(){
        String ledgerName = WidgetUtils.getTextFromTIL(tilBoardName);
        if(StringUtils.isNotEmpty(ledgerName)){
            WidgetUtils.removeErrorForTil(tilBoardName);
            if(callBackResultSet !=null){
                callBackResultSet.onDialogResultSet(ledgerName);
            }
            dismiss();
        }   else{
            WidgetUtils.setErrorToTil(tilBoardName,getResources().getString(R.string.empty_input,getResources().getString(R.string.board_name)));
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
