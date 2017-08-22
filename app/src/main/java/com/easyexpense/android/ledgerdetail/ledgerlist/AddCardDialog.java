package com.easyexpense.android.ledgerdetail.ledgerlist;

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
import com.easyexpense.android.helper.ModelConstants;
import com.easyexpense.android.helper.utils.AppUtils;
import com.easyexpense.android.helper.utils.WidgetUtils;
import com.easyexpense.commons.utils.StringUtils;
import com.geekmk.db.dto.LedgerCard;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mani on 03/04/17.
 */

public class AddCardDialog  extends DialogFragment {


    @BindView(R.id.til_card_name)
    TextInputLayout tilCardName;



    private long boardId;

    private static final String TAG = "AddCardDialog";


    public static AddCardDialog newInstance(long boardId){
        AddCardDialog addCardDialog = new AddCardDialog();

        Bundle bundle = new Bundle();
        bundle.putLong(AppConstants.IntentConstants.BOARD_ID,boardId);
        addCardDialog.setArguments(bundle);

        return addCardDialog;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle bundle) {
        View rootView = layoutInflater.inflate(R.layout.alert_add_card, parent, false);
        ButterKnife.bind(this, rootView);

        if(getArguments()!=null ){
            boardId = getArguments().getLong(AppConstants.IntentConstants.BOARD_ID);

        }
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


    @OnClick(R.id.tvAddCard) void onAddCardClick() {

            String cardName = WidgetUtils.getTextFromTIL(tilCardName);
            if (StringUtils.isNotEmpty(cardName)) {
                if(LedgerCard.isNameUnique(cardName,boardId)){
                    WidgetUtils.removeErrorForTil(tilCardName);
                    Intent intent = new Intent();
                    intent.putExtra(ModelConstants.CallBackConstants.CARD_NAME, cardName);

                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    dismiss();
                }else{
                    WidgetUtils.setErrorToTil(tilCardName, getResources().getString(R.string.duplicate_input,cardName));
                }
        } else {
            WidgetUtils.setErrorToTil(tilCardName, getResources().getString(R.string.empty_input, getResources().getString(R.string.card)));
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
