package com.easyexpense.android.transactionlist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.easyexpense.android.R;
import com.easyexpense.android.helper.AppConstants;
import com.easyexpense.android.tags.CallBackResultSet;
import com.easyexpense.android.widgets.DatePickerFragment;
import com.easyexpense.commons.android.activity.MkActivity;
import com.easyexpense.commons.utils.CollectionUtils;
import com.geekmk.db.dto.CardTag;
import com.geekmk.db.dto.CardTransaction;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TransactionListActivity extends MkActivity implements TransactionListView,CallBackResultSet {

    private TransactionListPresenter presenter;


    @BindView(R.id.menu)
    FloatingActionMenu floatingActionMenu;

    private TransactionListRecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
        ButterKnife.bind(this);
        initToolBar();
        initPresenter();
        presenter.onCreate(savedInstanceState);
        try {
            long ledgerId = getIntent().getExtras().getLong(AppConstants.IntentConstants.INTENT_DATA);
            String scenario = getIntent().getExtras().getString(AppConstants.IntentConstants.CURRENT_SCENARIO);

            if(ledgerId==0)
                finish();

         presenter.systemFetchTransactionListForLedger(ledgerId,scenario);


        }catch (Exception e){
            finish();
        }
    }

    @Override
    public void initComponents() {

    }

    @OnClick(R.id.menu_item_sort_tag) void sortTag(){
        floatingActionMenu.close(true);
        if(CollectionUtils.isNotEmpty(presenter.getEnclosingTransactions())){
            long boardId = presenter.getEnclosingTransactions().get(0).getEnclosingBoard();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment previousFragment    =  getSupportFragmentManager().findFragmentByTag("dialog");

            if (previousFragment != null) {
                ft.remove(previousFragment);
            }
            ft.addToBackStack(null);

            TagFilterDialog dialogFrag = TagFilterDialog.newInstance(boardId);
            dialogFrag.show(ft, "dialog");
        }else{
            Toast.makeText(this,"No Transactions available to perform tag filter",Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.menu_item_clear_filters) void clearFilters(){
        floatingActionMenu.close(true);
        try{

            recyclerViewAdapter.updateList(presenter.getEnclosingTransactions());

        }catch (Exception e){}

    }

    @OnClick(R.id.menu_item_sort_date) void sortDate(){
        floatingActionMenu.close(true);
        DatePickerFragment newFragment = new DatePickerFragment();

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDialogResultSet(Object resultObject) {
        if(resultObject instanceof Long){
            performDateFilters((long)resultObject);
        }
    }

    @Override
    public void onAdapterResultSet(Object selectedItem) {

    }

    @Override
    public void onAdapterLongClick(Object selectedItem) {

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void initPresenter() {
        presenter = new TransactionListPresenterImpl(this);
    }

    @Override
    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Transactions");
    }

    @Override
    public void systemFetchHomeInfoSuccess(List<CardTransaction> transactionBeanList) {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_transaction_list);
         recyclerViewAdapter = new TransactionListRecyclerViewAdapter();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.updateList(transactionBeanList);

    }

    public void performTagFilters(List<String> stringList){


        if(CollectionUtils.isNotEmpty(stringList)){
            List<CardTransaction> cardTransactions = presenter.getEnclosingTransactions();
            List<CardTransaction> filteredTransaction = new ArrayList<>();
            for(CardTransaction cardTransaction:cardTransactions){
                long attachedTag = cardTransaction.getAttachedTags();
                if(attachedTag!=0 ){
                    CardTag cardTag = CardTag.load(CardTag.class,attachedTag);
                    if(stringList.contains(cardTag.getName())){
                        filteredTransaction.add(cardTransaction);
                    }
                }
            }


            recyclerViewAdapter.updateList(filteredTransaction);

        }
    }

    public void performDateFilters(long selectedDate) {

        if (selectedDate != 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selectedDate);

            List<CardTransaction> cardTransactions = presenter.getEnclosingTransactions();
            List<CardTransaction> filteredTransaction = new ArrayList<>();
            for (CardTransaction cardTransaction : cardTransactions) {
                long timeStamp = cardTransaction.getCreatedTS();
                Calendar existingTime = Calendar.getInstance();
                existingTime.setTimeInMillis(timeStamp);

                if (isSameDay(calendar, existingTime)) {
                    filteredTransaction.add(cardTransaction);
                }
            }
            recyclerViewAdapter.updateList(filteredTransaction);
        }
    }


    public boolean isSameDay(Calendar cal1,Calendar cal2){
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

}
