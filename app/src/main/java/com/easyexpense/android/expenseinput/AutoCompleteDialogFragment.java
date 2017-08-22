package com.easyexpense.android.expenseinput;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.easyexpense.android.R;
import com.easyexpense.android.helper.AppConstants;
import com.easyexpense.commons.utils.CollectionUtils;
import com.geekmk.db.dto.LedgerCard;

import java.util.List;

/**
 * Created by Mani on 17/04/17.
 */

public class AutoCompleteDialogFragment extends DialogFragment {

    private AutoCompleteTextView autoCompleteTextView;

    private boolean isSource;


    /*
    * @param isInCompleteInfo accepted to check if user should select data from suggestions list
    * */
    public static AutoCompleteDialogFragment newInstance(boolean isSource){
        AutoCompleteDialogFragment newFragment = new AutoCompleteDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(AppConstants.IntentConstants.INTENT_DATA, isSource);
        newFragment.setArguments(bundle);
        return newFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =   inflater.inflate(R.layout.fragment_autocomplete,container,false);

        autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.atv_results);

        if(getArguments()!=null){
            isSource = getArguments().getBoolean(AppConstants.IntentConstants.INTENT_DATA);

            List<LedgerCard> searchResults;

            if(isSource){
                searchResults = ((ExpenseInputActivity)getActivity()).getBean().getSourceCardList();
            }else{
                searchResults = ((ExpenseInputActivity)getActivity()).getBean().getDestinationCardList();
            }

            if(CollectionUtils.isNotEmpty(searchResults)){
                ArrayAdapter<LedgerCard> searchResultsAdapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_auto_complete, searchResults);
                autoCompleteTextView.setAdapter(searchResultsAdapter);
                autoCompleteTextView.setDropDownAnchor(view.findViewById(R.id.cv_root).getId());
                autoCompleteTextView.setDropDownVerticalOffset(8);
            }

            autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId == EditorInfo.IME_ACTION_DONE){
                        getDialog().dismiss();
                    }
                    return false;
                }
            });
        }

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LedgerCard selectedCard = (LedgerCard) parent.getItemAtPosition(position);
                ((ExpenseInputActivity)getActivity()).setAccountInfo(isSource,selectedCard.getName(),selectedCard.getId());
                getDialog().dismiss();
            }
        });

        view.findViewById(R.id.iv_action_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }





    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog!=null && dialog.getWindow()!=null){
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.TOP);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(),R.color.white_grey)));
        }
    }
}
