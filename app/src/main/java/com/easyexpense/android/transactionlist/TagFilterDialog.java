package com.easyexpense.android.transactionlist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;

import com.easyexpense.android.R;
import com.easyexpense.android.helper.AppConstants;
import com.easyexpense.android.helper.utils.AppUtils;
import com.geekmk.db.dto.CardTag;
import com.libaml.android.view.chip.ChipLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mani on 26/04/17.
 */

public class TagFilterDialog extends AppCompatDialogFragment {

    @BindView(R.id.chipText)  ChipLayout chip;

    public static TagFilterDialog newInstance(long boardId){
        TagFilterDialog tagFilterDialog =  new TagFilterDialog();
        Bundle bundle = new Bundle();
        bundle.putLong(AppConstants.IntentConstants.INTENT_DATA,boardId);
        tagFilterDialog.setArguments(bundle);
        return tagFilterDialog;
    }



    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle bundle) {
        View rootView = layoutInflater.inflate(R.layout.tag_filter_dialog, parent, false);
        ButterKnife.bind(this, rootView);

        if(getArguments()!=null){

            long boardId = getArguments().getLong(AppConstants.IntentConstants.INTENT_DATA);
            List<CardTag> cardTagList = CardTag.getTagsForBoard(boardId);
            ArrayAdapter<CardTag> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,cardTagList);
            chip.setAdapter(adapter);
        }

//
//        chip.setOnClickListener(ClickListener);
//        chip.setOnItemClickListener(ItemClickListener);
//        chip.addLayoutTextChangedListener(TextChangedListener);
//        chip.setOnFocusChangeListener(FocusChangeListener);
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

        ((TransactionListActivity)getActivity()).performTagFilters(chip.getText());
        dismiss();
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
