package com.easyexpense.android.ledgerdetail.ledgerlist;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easyexpense.android.R;
import com.easyexpense.android.helper.AppConstants;
import com.easyexpense.android.helper.ModelConstants;
import com.easyexpense.android.helper.utils.AppUtils;
import com.easyexpense.android.helper.utils.ExcelUtils;
import com.easyexpense.android.helper.utils.FragmentGenericDialog;
import com.easyexpense.android.helper.utils.WidgetUtils;
import com.easyexpense.android.ledgerdetail.CardsRecyclerViewAdapter;
import com.easyexpense.android.ledgerdetail.LedgerDetailActivity;
import com.easyexpense.android.transactionlist.TransactionListActivity;
import com.easyexpense.commons.android.fragment.MkFragment;
import com.easyexpense.commons.utils.CollectionUtils;
import com.geekmk.db.dto.Board;
import com.geekmk.db.dto.Ledger;
import com.geekmk.db.dto.LedgerCard;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mani on 01/03/17.
 */

public class LedgerListFragment extends MkFragment implements  LedgerListView{


    @BindView(R.id.iv_card_menu) ImageView ivCardMenu;

    @BindView(R.id.tv_title) TextView tvTitle;

    @BindView(R.id.rv_cards) RecyclerView recyclerView;

    @BindView(R.id.tv_ledger_balance) TextView tvLedgerBalance;

    private Ledger ledger;

    private LedgerListPresenter presenter;

    public static LedgerListFragment newInstance(long ledgerId){
        Bundle bundle = new Bundle();
        bundle.putLong(AppConstants.IntentConstants.INTENT_DATA,ledgerId);
        LedgerListFragment ledgerListFragment = new LedgerListFragment();
        ledgerListFragment.setArguments(bundle);
        return ledgerListFragment;
    }

    @OnClick(R.id.iv_card_menu) void onCardMenClick(View v){
        PopupMenu myPopup = new PopupMenu(getContext(), v);
        myPopup.setGravity(Gravity.END);
        if(ledger!=null){
            int menuRes;
            if(ledger.isDefault()){
                menuRes = R.menu.menu_default_ledger_list;
            }else{
                menuRes = R.menu.menu_item_options;
            }
            myPopup.getMenuInflater().inflate(menuRes, myPopup.getMenu());
            myPopup.show();
            myPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_create_new_item:
                            ((LedgerDetailActivity)getActivity()).createNewLedgerChild(ledger);
                            return false;
                        case R.id.menu_delete_item:
                            confirmDelete();
                            return false;
                        case R.id.menu_edit_item:
                            displayRenameDialog();
                            return false;
                        case R.id.menu_export_excel:
                            try {
                                List<LedgerCard> ledgerCardList = LedgerCard.getLedgerCardListForLedger(ledger.getId());
                                if(CollectionUtils.isNotEmpty(ledgerCardList)){
                                    createAndExportExcelForLedger();

                                }else{
                                    Toast.makeText(getContext(),"No Cards added to create excel",Toast.LENGTH_LONG).show();
                                }
                            }catch (Exception e){

                            }


                            return false;
                    }
                    return false;
                }
            });
        }

    }

    private void createAndExportExcelForLedger() {

        new AsyncTask<Long, Void, String>() {
            @Override
            protected String doInBackground(Long... params) {
                int storagePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(storagePermission == PackageManager.PERMISSION_GRANTED) {
                    try {
                        final File createdFile = ExcelUtils.createExcelSheetForLedgerList(ledger.getId());

                        Snackbar snackbar = Snackbar.make(recyclerView, "Excel Created Successfully", Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("View", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Uri uri = FileProvider.getUriForFile(getActivity(),"com.easyexpense.commons",createdFile);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.setDataAndType(uri, "application/vnd.ms-excel");

                                if(intent.resolveActivity(getActivity().getPackageManager())!=null){
                                    getActivity().startActivity(intent);
                                }else {
                                    Snackbar.make(recyclerView, "No applications available to open this type of file, " +
                                            "Goto EasyExpense Folder inside your storage to view file", Snackbar.LENGTH_INDEFINITE).show();
                                }
                            }
                        });
                        snackbar.show();

                    } catch (Exception e) {
                        AppUtils.displaySnackBar(recyclerView, "Cannot generate excel report, Please try later");
                    }
                }else {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},14);
                    WidgetUtils.displaySnackBar("Provide permission to access storage",recyclerView);
                }

                return null;
            }
        }.execute(ledger.getId());
    }

    private void confirmDelete() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
        .setTitle("Are you sure?")
        .setMessage("You'll lose all the created cards!")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.deleteLedger();
            }
        })
        .setNegativeButton("No", null);

        builder.show();



    }

    private void displayRenameDialog() {

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment previousFragment    =  getChildFragmentManager().findFragmentByTag("dialog");

        if (previousFragment != null) {
            ft.remove(previousFragment);
        }
        ft.addToBackStack(null);
        Map<String,Object> info = new HashMap<>();
        info.put(AppConstants.IntentConstants.GENERIC_NAME,ledger.getName());
        FragmentGenericDialog dialogFrag = FragmentGenericDialog.newInstance(info);
        dialogFrag.setTargetFragment(this, 12);
        dialogFrag.show(ft, "dialog");
    }


    @OnClick(R.id.tv_title) void onTitleClick(){
        AppUtils.navigateTransaction(ledger.getId(),AppConstants.TransactionListConstants.LEDGER ,TransactionListActivity.class,getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_item, container, false);
        ButterKnife.bind(this,view);

        if(getArguments()!=null){
            long ledgerId = getArguments().getLong(AppConstants.IntentConstants.INTENT_DATA);
            this.ledger = Ledger.load(Ledger.class,ledgerId);
            initPresenter();
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(ledger !=null){

//            ArrayAdapter<CharSequence> timeFrameAdapter = ArrayAdapter.createFromResource(getContext(),
//                    R.array.item_time_frame_entries, R.layout.dialog_spinner_item);
//
//            timeFrameAdapter.setDropDownViewResource(R.layout.dialog_spinner_drop_down_item);
//
//            spinnerTimeFrame.setAdapter(timeFrameAdapter);
//
//            spinnerTimeFrame.post(new Runnable() {
//                @Override
//                public void run() {
//                    spinnerTimeFrame.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            String item = (String) parent.getAdapter().getItem(position);
//                            switch (item){
//                                case "Custom Range":
//                                    displayDialogForDateSelection();
//                                    break;
//                            }
//                        }
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {
//
//                        }
//                    });
//                }
//            });

            tvTitle.setText(ledger.getName());

            tvLedgerBalance.setText((int)ledger.getAmount()+"");
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL,false);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);
            CardsRecyclerViewAdapter cardsRecyclerViewAdapter = new CardsRecyclerViewAdapter(getActivity());
            List<LedgerCard> ledgerCardList = LedgerCard.getLedgerCardListForLedger(ledger.getId());
            cardsRecyclerViewAdapter.updateCardsList(ledgerCardList);
            recyclerView.setAdapter(cardsRecyclerViewAdapter);
        }
    }


    @OnClick(R.id.tv_add_card) void addCard(){



        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment previousFragment    =  getChildFragmentManager().findFragmentByTag("dialog");

        if (previousFragment != null) {
            ft.remove(previousFragment);
        }
        ft.addToBackStack(null);

        AddCardDialog dialogFrag = AddCardDialog.newInstance(ledger.getBoardId());
        dialogFrag.setTargetFragment(this, 1);
        dialogFrag.show(ft, "dialog");


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == Activity.RESULT_OK){
                    if(presenter!=null){
                        String cardName = data.getStringExtra(ModelConstants.CallBackConstants.CARD_NAME);

                        presenter.userPressedAddCardToLedger(cardName,"0");
                    }
                }
                break;
            case 12:
                String name = data.getStringExtra(AppConstants.IntentConstants.GENERIC_NAME);
                ledger.setName(name);
                presenter.updateLedgerName(name);
                break;
        }
    }

    private void displayDialogForDateSelection() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_custom_date_selection, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(),"Done Pressed",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public void setItemBean(Ledger itemBean){
        this.ledger = itemBean;
        initPresenter();
    }

    public Ledger getItemBean(){
        return this.ledger;
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initializeViewBean() {

    }

    @Override
    public void initComponents() {

    }

    @Override
    public void initPresenter() {
            presenter = new LedgerListPresenterImpl(ledger,this);
    }

    @Override
    public void userPressedAddCardToLedgerSuccess(LedgerCard ledgerCard) {
        ((CardsRecyclerViewAdapter)recyclerView.getAdapter()).addCard(ledgerCard);
        //update main balance
        Board  board = Board.load(Board.class,ledgerCard.getBoardId());
        ((LedgerDetailActivity)getActivity()).updateToolbarDetails(null,board.getAmount()+"");
        Ledger ledger = Ledger.load(Ledger.class,ledgerCard.getLedgerId());
        tvLedgerBalance.setText(ledger.getAmount()+"");
    }

    @Override
    public void userPressedAddCardToLedgerFailure(String reason) {

    }

    @Override
    public void userPressedDeleteLedgerSuccess() {
        ((LedgerDetailActivity)getActivity()).userDeletedLedgerRefreshList();
    }

    @Override
    public void updateLedgerNameSuccess() {
        tvTitle.setText(ledger.getName());
    }
}
