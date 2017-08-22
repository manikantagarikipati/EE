package com.easyexpense.android.ledgerdetail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.easyexpense.android.R;
import com.easyexpense.android.boardlist.BoardListActivity;
import com.easyexpense.android.helper.AppConstants;
import com.easyexpense.android.helper.utils.AppUtils;
import com.easyexpense.android.helper.utils.ExcelUtils;
import com.easyexpense.android.helper.utils.GenericDialog;
import com.easyexpense.android.helper.utils.GenericDialogCallBack;
import com.easyexpense.android.helper.utils.WidgetUtils;
import com.easyexpense.commons.android.activity.MkActivity;
import com.easyexpense.commons.utils.StringUtils;
import com.geekmk.db.dto.Ledger;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LedgerDetailActivity extends MkActivity implements LedgerDetailView,GenericDialogCallBack {

    private LedgerDetailPresenter presenter;

    private LedgerDetailFragmentPagerAdapter mPagerAdapter;

    @BindView(R.id.vp_board_items)
    ViewPager vpBoardItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_detail);
        ButterKnife.bind(this);
        initComponents();
        initPresenter();
        initToolBar();
        if(savedInstanceState!=null){
            long boardId = savedInstanceState.getLong("SIS");
            presenter.systemFetchBoard(boardId);
        }
        else if(getIntent()!=null && getIntent().getExtras()!=null){
            long boardId = getIntent().getExtras().getLong(AppConstants.IntentConstants.INTENT_DATA);
            presenter.systemFetchBoard(boardId);
        }else{
            presenter.systemFetchDefaultBoard();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("SIS",presenter.getEnclosingBean().getBoard().getId());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this,BoardListActivity.class);
                startActivityForResult(intent, AppConstants.IntentConstants.LEDGER_LIST_REQUEST_CODE);
                return true;
            case R.id.action_rename_board:
                displayEditDialog();
                return true;
            case R.id.action_export_excel:
                performExportExcel();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void performExportExcel() {
        int storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(storagePermission == PackageManager.PERMISSION_GRANTED) {
            createAndExportExcel();
        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},14);
            WidgetUtils.displaySnackBar("Provide permission to access storage",(CoordinatorLayout) findViewById(R.id.cl_parent));
        }
    }

    private void createAndExportExcel() {

        try {
            long boardId = presenter.getEnclosingBean().getBoard().getId();
            final File createdFile = ExcelUtils.createCardsExcelSheetForBoard(boardId);

            Snackbar snackbar = Snackbar.make(findViewById(R.id.cl_parent), "Excel Created Successfully", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("View", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = FileProvider.getUriForFile(LedgerDetailActivity.this,"com.easyexpense.commons",createdFile);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(uri, "application/vnd.ms-excel");

                    if(intent.resolveActivity(getPackageManager())!=null){
                        startActivity(intent);
                    }else {
                        Snackbar.make(findViewById(R.id.cl_parent), "No applications available to open this type of file, " +
                                "Goto EasyExpense Folder inside your storage to view file", Snackbar.LENGTH_INDEFINITE).show();
                    }
                }
            });
            snackbar.show();

        } catch (Exception e) {
            AppUtils.displaySnackBar((CoordinatorLayout) findViewById(R.id.cl_parent), "Cannot perform Excel export, Please try later");
        }

    }

    private void displayEditDialog() {
        try {
            String name = presenter.getEnclosingBean().getBoard().getName();
            Map<String,Object> info = new HashMap<>();
            info.put(AppConstants.IntentConstants.GENERIC_NAME,name);
            GenericDialog addTagDialog = GenericDialog.newInstance(info,11);
            addTagDialog.show(getSupportFragmentManager(), "generic");
        }catch (Exception e){
            Log.e("Ledger Detail",e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case AppConstants.IntentConstants.LEDGER_LIST_REQUEST_CODE:
                    if(data!=null && data.getExtras()!=null) {
                        boolean isDataChanged = data.getExtras().getBoolean(AppConstants.IntentConstants.IS_DATA_CHANGED);
                        if(isDataChanged){
                            //in case if user has deleted the currently displaying board
                            presenter.checkIfBoardExists();
                        }else{
                            long boardId = data.getExtras().getLong(AppConstants.IntentConstants.INTENT_DATA);
                            presenter.systemFetchBoard(boardId);
                        }
                    }
                    break;
                case AppConstants.IntentConstants.TRANSACTION_LIST_REQUEST_CODE:
                    try {
                        long boardId = data.getExtras().getLong("id");
                        presenter.systemFetchBoard(boardId);
                    }catch (Exception e){

                    }
                    break;
            }
        }else if(requestCode == 14){
            if(resultCode == PackageManager.PERMISSION_GRANTED){
                performExportExcel();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_board, menu);
        return true;
    }
    @Override
    public void initComponents() {

    }

    @Override
    public void initPresenter() {
        presenter = new LedgerDetailPresenterImpl(this);
    }

    @Override
    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            //we don't wanna show any title by default
            getSupportActionBar().setTitle("");
            toolbar.setNavigationIcon(ContextCompat.getDrawable(this,R.drawable.ic_expense_manager));
        }
    }

    @Override
    public void systemFetchHomeInfoSuccess(LedgerDetailBean bean) {
        List<Ledger> ledgerList = bean.getLedgerList();

        if(mPagerAdapter!=null){

            mPagerAdapter = null;

        }
            mPagerAdapter = new LedgerDetailFragmentPagerAdapter(getSupportFragmentManager(),
                    ledgerList);
            vpBoardItems.setAdapter(mPagerAdapter);
            int margin = (int) getResources().getDimension(R.dimen.pager_margin);

            vpBoardItems.setPageMargin(margin);

            if (ledgerList.size() > 2) {
                vpBoardItems.setOffscreenPageLimit(3);
            } else {
                vpBoardItems.setOffscreenPageLimit(ledgerList.size());
            }

        updateToolbarDetails(bean.getBoard().getName(),""+((int)bean.getBoard().getAmount()));

    }

    @Override
    public void updateToolbarDetails(String title, String subTitle) {
        if(getSupportActionBar()!=null){
            if(StringUtils.isNotEmpty(title))
            getSupportActionBar().setTitle(title);
            if(StringUtils.isNotEmpty(subTitle))
            getSupportActionBar().setSubtitle(subTitle);
        }
    }

    @Override
    public void userPressedCreateChildLedgerSuccess(Ledger ledger) {
        presenter.systemFetchBoard(presenter.getEnclosingBean().getBoard().getId());

//        mPagerAdapter.addLedger(ledger);
    }

    public void createNewLedgerChild(Ledger parentLedger){

        try {
            Map<String,Object> info = new HashMap<>();
            info.put(AppConstants.IntentConstants.GENERIC_NAME,"");
            info.put(AppConstants.IntentConstants.LEDGER_ID,parentLedger.getId());
            GenericDialog addTagDialog = GenericDialog.newInstance(info,12);
            addTagDialog.show(getSupportFragmentManager(), "generic");
        }catch (Exception e){
            Log.e("Ledger Detail",e.getMessage());
        }
    }


    @Override
    public void onDialogResultSet(Object resultObject,int requestCode) {
        switch (requestCode){
            case 11:
                if(resultObject instanceof Map){
                    Map<String,Object> info = (Map)resultObject;
                    String name = (String) info.get(AppConstants.IntentConstants.GENERIC_NAME);
                    presenter.renameBoard(name);
                    updateToolbarDetails(name,null);
                }

                break;
            case 12:
                if(resultObject instanceof Map){
                    Map<String,Object> info = (Map)resultObject;
                    String name = (String) info.get(AppConstants.IntentConstants.GENERIC_NAME);
                    long ledgerId = (long)info.get(AppConstants.IntentConstants.LEDGER_ID);
                    Ledger ledger = Ledger.load(Ledger.class,ledgerId);
                    presenter.userPressedCreateNewLedger(ledger,name);
                }

                break;
        }
    }

    public void userDeletedLedgerRefreshList(){
        long id = presenter.getEnclosingBean().getBoard().getId();
        presenter.systemFetchBoard(id);
    }
}
