package com.easyexpense.android.tags;

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
import com.easyexpense.android.helper.AppConstants;
import com.easyexpense.android.helper.ModelConstants;
import com.easyexpense.android.helper.utils.AppUtils;
import com.easyexpense.android.helper.utils.WidgetUtils;
import com.easyexpense.commons.utils.StringUtils;
import com.geekmk.db.dto.CardTag;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mani on 13/03/17.
 */

public class AddTagDialog extends DialogFragment{


    @BindView(R.id.til_tag_name)
    TextInputLayout tilTagName;

    @BindView(R.id.tv_label_title)
    TextView tvLabelTitle;

    private CallBackResultSet callBackResultSet;

    private boolean isEditMode;

    private int tagPosition;

    private long tagId;

    private long cardId;


    private static final String TAG = "AddTagDialog";

    public static AddTagDialog newInstance(long tagId,int position,long cardId){
        AddTagDialog addTagDialog = new AddTagDialog();
        Bundle bundle = new Bundle();
        bundle.putLong(AppConstants.IntentConstants.CARD_ID,cardId);
        if(tagId != 0){
            bundle.putLong(AppConstants.IntentConstants.INTENT_DATA,tagId);
            bundle.putInt(AppConstants.IntentConstants.TAG_POSITION,position);
        }
        addTagDialog.setArguments(bundle);

        return addTagDialog;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle bundle) {
        View rootView = layoutInflater.inflate(R.layout.alert_add_tag, parent, false);
        ButterKnife.bind(this, rootView);
        cardId = getArguments().getLong(AppConstants.IntentConstants.CARD_ID);
        checkIfEditCaseAndSetInfo();
        return rootView;
    }

    private void checkIfEditCaseAndSetInfo() {


        if(getArguments()!=null && getArguments().getLong(AppConstants.IntentConstants.INTENT_DATA)!=0){
            tagId = getArguments().getLong(AppConstants.IntentConstants.INTENT_DATA);
            CardTag cardTag = CardTag.load(CardTag.class,tagId);
            WidgetUtils.setTextInputLayoutInfo(tilTagName,cardTag.getName());
            tvLabelTitle.setText(getResources().getString(R.string.title_label_edit_tag));
            isEditMode = true;
            tagPosition = getArguments().getInt(AppConstants.IntentConstants.TAG_POSITION);
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
        if (getActivity() instanceof CallBackResultSet){
            callBackResultSet = (CallBackResultSet) getActivity();
        }else{
            Log.e(TAG,"Enclosing activity should call DialogResultSetInterface");
        }
    }

    @OnClick(R.id.tvAddTag) void onAddTagClick(){
        String tagName = WidgetUtils.getTextFromTIL(tilTagName);
        if(StringUtils.isNotEmpty(tagName)){
            WidgetUtils.removeErrorForTil(tilTagName);

            if(!CardTag.isDuplicate(tagName,cardId)){
                if(callBackResultSet !=null){
                    Map<String,Object> info = new HashMap<>();
                    info.put(ModelConstants.CallBackConstants.IS_EDIT_MODE,isEditMode);
                    info.put(ModelConstants.CallBackConstants.TAG_INFO,tagName);
                    info.put(AppConstants.IntentConstants.TAG_POSITION,tagPosition);
                    info.put(AppConstants.IntentConstants.TAG_ID,tagId);
                    callBackResultSet.onDialogResultSet(info);
                }
                dismiss();
            }else{
                WidgetUtils.setErrorToTil(tilTagName,getResources().getString(R.string.duplicate_input,tagName));
            }
        }   else{
            WidgetUtils.setErrorToTil(tilTagName,getResources().getString(R.string.empty_input,getResources().getString(R.string.tag)));
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
