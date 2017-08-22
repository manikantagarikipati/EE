package com.easyexpense.android.expenseinput;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easyexpense.android.R;
import com.easyexpense.android.helper.AppConstants;
import com.easyexpense.android.helper.utils.AppUtils;
import com.easyexpense.android.helper.utils.WidgetUtils;
import com.easyexpense.android.tags.CallBackResultSet;
import com.easyexpense.android.tags.GenericAdapterCallBack;
import com.easyexpense.android.tags.TagsActivity;
import com.easyexpense.android.widgets.DatePickerFragment;
import com.easyexpense.android.widgets.WidgetCalculator;
import com.easyexpense.commons.android.activity.MkActivity;
import com.easyexpense.commons.utils.CollectionUtils;
import com.easyexpense.commons.utils.FileUtils;
import com.easyexpense.commons.utils.JsonUtils;
import com.easyexpense.commons.utils.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.geekmk.db.dto.CardTag;
import com.geekmk.db.dto.LedgerCard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ExpenseInputActivity extends MkActivity implements CallBackResultSet,
        WidgetCalculator.CalculatorInf,ExpenseView,GenericAdapterCallBack {

    @BindView(R.id.tv_select_date)
    TextView tvSelectDate;

    @BindView(R.id.tv_amount)
    EditText tvAmount;

    @BindView(R.id.tv_source)
    TextView tvSourceAccount;

    @BindView(R.id.tv_destination)
    TextView tvDestinationAccount;


    private static final String TAG = "ExpenseInputActivity";

    private ExpensePresenter presenter;

    @BindView(R.id.tv_current_op) TextView tvCurrentOp;

    @BindView(R.id.tv_exp_1) TextView tvExp1;

    private WidgetCalculator widgetCalculator;

    private Uri fileUri;

    private List<Long> selectedIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_input);
        ButterKnife.bind(this);
        if(savedInstanceState!=null){
            try{
                String selectedTags = savedInstanceState.getString("selectedTags");
                selectedIdList = JsonUtils.parseJson(selectedTags, new TypeReference<List<Long>>() {});
            }catch (Exception e){}
        }
        initToolBar();
        initComponents();
        initPresenter();

        if(getIntent().getExtras()!=null){
            long sourcedId = getIntent().getExtras().getLong(AppConstants.IntentConstants.INTENT_DATA);
            presenter.systemSetPageInfo(sourcedId,false);
        }
    }


    @OnClick(R.id.fab_swap_accounts) void swapAccounts(View view){
        //todo perform rotation
        presenter.swapAccounts();
    }



    public ExpenseInputBean getBean(){
        return presenter.getBean();
    }

    public void setAccountInfo(boolean isSource,String data,long id){
            if(isSource){
                tvSourceAccount.setText(data);
                presenter.getBean().setSourceCardId(id);
            }else{
                tvDestinationAccount.setText(data);
                presenter.getBean().setDestinationCardId(id);
            }
    }

    @Override
    public void initComponents() {
        long currentTimeInMillis = AppUtils.getCurrentDate();
        tvSelectDate.setText(AppUtils.convertDateFormat(currentTimeInMillis));
        widgetCalculator = (WidgetCalculator) findViewById(R.id.widget_calculator);

        if(selectedIdList==null){
            selectedIdList = new ArrayList<>();
        }
        widgetCalculator.setCalcInf(this);

        tvAmount.setKeyListener(null);
    }


    @OnClick(R.id.tv_select_date) void onDateChangeClick(){
        DatePickerFragment newFragment = new DatePickerFragment();

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    @Override
    public void initPresenter() {
        presenter = new ExpensePresenterImpl(this);
    }

    @Override
    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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

    @OnClick(R.id.iv_manage_tags) void onManageTagsClick(){
        try{
            LedgerCard ledgerCard  = LedgerCard.load(LedgerCard.class,presenter.getBean().getDestinationCardId());
            AppUtils.intentNavigateWithResult(ledgerCard.getId(),TagsActivity.class,this,14);
        }catch ( Exception e){
            WidgetUtils.displaySnackBar("Cannot read destination card value",(CoordinatorLayout) findViewById(R.id.cl_parent));
        }

    }

    @OnClick(R.id.tv_source) void onSourceClick(){
        displayAccountsListAutoComplete(true);
    }

    @OnClick(R.id.tv_destination) void onDestinationClick(){
        displayAccountsListAutoComplete(false);
    }

    private void displayAccountsListAutoComplete(boolean isSource) {


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment previousFragment    =  getSupportFragmentManager().findFragmentByTag("navigate Dialog");

        if (previousFragment != null) {
            ft.remove(previousFragment);
        }
        ft.addToBackStack(null);

        AutoCompleteDialogFragment newFragment = AutoCompleteDialogFragment.newInstance(isSource);
        newFragment.show(ft, "navigate Dialog");

    }
    @Override
    public void onDialogResultSet(Object resultObject) {
        if(resultObject instanceof Long){
            tvSelectDate.setText(AppUtils.convertDateFormat((long)resultObject));
        }else if(resultObject instanceof  String) {
            String addedComment = (String) resultObject;

                presenter.getBean().setComment(addedComment);
            if(StringUtils.isNotEmpty(addedComment)){
                widgetCalculator.setCommentBackgroundColor(ContextCompat.getColor(this,R.color.colorAccent));
            }else{
                widgetCalculator.setCommentBackgroundColor(ContextCompat.getColor(this,R.color.black));
            }
        }
    }


    @Override
    public void onAdapterResultSet(Object selectedItem) {

    }

    @Override
    public void onAdapterLongClick(Object selectedItem) {

    }

    private void displayImagePickOptions() {

        final CharSequence options[] = new CharSequence[] {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image from?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedOption = options[which].toString();
                if(selectedOption.equalsIgnoreCase("Camera")){
                    dispatchCameraIntent();
                }else{
                    navigateToGallery();
                }
            }
        });
        builder.show();
    }

    private void navigateToGallery() {
        int storagePermission = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(storagePermission == PackageManager.PERMISSION_GRANTED) {
            Intent intent;
            if (Build.VERSION.SDK_INT < 19) {
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), AppConstants.MediaConstants.ADD_IMAGE_VIA_GALLERY);
            } else {
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), AppConstants.MediaConstants.ADD_IMAGE_VIA_GALLERY);
            }
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},14);
            WidgetUtils.displaySnackBar("Provide permission to access storage",(CoordinatorLayout) findViewById(R.id.cl_parent));
        }
    }

    private void dispatchCameraIntent() {

        int permissionState = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if(permissionState == PackageManager.PERMISSION_GRANTED){
            Intent imageCaptureIntent = new Intent();
            imageCaptureIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            if(imageCaptureIntent.resolveActivity(getPackageManager())!=null){
                int storagePermission = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(storagePermission == PackageManager.PERMISSION_GRANTED){
                    try {
                        fileUri = FileUtils.getUniqueFileUri(this);
                        imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
                        startActivityForResult(imageCaptureIntent,13);
                    } catch (IOException e) {
                        WidgetUtils.displaySnackBar("Cannot create image file",(CoordinatorLayout) findViewById(R.id.cl_parent));
                    }
                }else{
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},14);
                    WidgetUtils.displaySnackBar("Provide permission to access storage",(CoordinatorLayout) findViewById(R.id.cl_parent));
                }
            }else{
                WidgetUtils.displaySnackBar("There are no camera applications to capture picture",(CoordinatorLayout) findViewById(R.id.cl_parent));
            }
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},12);
            WidgetUtils.displaySnackBar("Provide permission to access camera",(CoordinatorLayout) findViewById(R.id.cl_parent));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 12:
                if(resultCode == PackageManager.PERMISSION_GRANTED || resultCode == RESULT_OK){
                    onCameraSelected();
                }
                break;
            case 13:
                if(resultCode == RESULT_OK){
                    WidgetUtils.displaySnackBar("Image Captured Successfully",(CoordinatorLayout) findViewById(R.id.cl_parent));
                }else{
                    fileUri = null;
                    WidgetUtils.displaySnackBar("Cannot Capture Image",(CoordinatorLayout) findViewById(R.id.cl_parent));
                }
                break;
            case AppConstants.MediaConstants.ADD_IMAGE_VIA_GALLERY:
                if(resultCode == RESULT_OK){
                    try{
                        fileUri = data.getData();
                        if(fileUri == null){
                            WidgetUtils.displaySnackBar(getResources().getString
                                    (R.string.msg_invalid_image_selection),(CoordinatorLayout) findViewById(R.id.cl_parent));

                        }
                    }catch(Exception e){
                        WidgetUtils.displaySnackBar(getResources().getString
                                (R.string.select_another_image),(CoordinatorLayout) findViewById(R.id.cl_parent));
                    }
                    break;
                }
                break;
            case 14:
                if(resultCode == RESULT_OK){
                    long sourcedId = getIntent().getExtras().getLong(AppConstants.IntentConstants.INTENT_DATA);
                    presenter.systemSetPageInfo(sourcedId,false);
                }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("fileUri",fileUri);
        String selectedString = JsonUtils.toJson(selectedIdList);
        outState.putString("selectedTags",selectedString);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fileUri = savedInstanceState.getParcelable("fileUri");
    }

    // calculator call backs
    @Override
    public void setCurrentOperation(String currentOperation) {
        tvCurrentOp.setText(currentOperation);
    }

    @Override
    public void setExpression1(String expression1) {
        tvExp1.setText(expression1);
    }

    @Override
    public void setAmount(String amount) {
        tvAmount.setText(amount);
    }

    @Override
    public void resetAllInfo() {
        tvAmount.setText("");
        tvCurrentOp.setText("");
        tvExp1.setText("");
    }

    @Override
    public EditText getAmountEditText() {
        return tvAmount;
    }

    @Override
    public String getAmountText() {
        return tvAmount.getEditableText().toString();
    }

    @Override
    public void clearAmount() {
        tvAmount.getEditableText().clear();
    }

    @Override
    public void onCommentSelected() {
        AddCommentDialog addTagDialog= AddCommentDialog.newInstance(presenter.getBean().getComment());
        addTagDialog.show(getSupportFragmentManager(),getResources().getString(R.string.create_board));
    }

    @Override
    public void onSubmitSelected() {
        try{
            String providedAmount = WidgetUtils.getTextFromET(tvAmount);
            if(StringUtils.isNotEmpty(providedAmount)){

                presenter.getBean().setSelectedImage(fileUri);

                LedgerCard ledgerCard  = LedgerCard.load(LedgerCard.class,presenter.getBean().getDestinationCardId());

                LedgerCard sourceCard = LedgerCard.load(LedgerCard.class,presenter.getBean().getSourceCardId());;

                if(ledgerCard!=null && sourceCard!=null){
                    presenter.getBean().setSourceCardId(sourceCard.getId());
                    presenter.performTransaction(providedAmount,ledgerCard.getId(),selectedIdList);
                }else{
                    WidgetUtils.displaySnackBar("Source And destination cannot be empty",(CoordinatorLayout) findViewById(R.id.cl_parent));
                }
            }else{
                WidgetUtils.displaySnackBar("Amount Cannot be empty",(CoordinatorLayout) findViewById(R.id.cl_parent));
            }
        }catch ( Exception e){
            WidgetUtils.displaySnackBar("Cannot perform transaction, Please try later",(CoordinatorLayout) findViewById(R.id.cl_parent));
        }
    }

    @Override
    public void onCameraSelected() {
        if(fileUri == null){
            displayImagePickOptions();
        }else{
            AppUtils.displayImage(this,fileUri);
        }
    }

    @Override
    public void systemSetPageInfoSuccess(List<LedgerCard> sourceList,List<LedgerCard> destinationList) {

        try{
            LedgerCard  sourceLedgerCard = LedgerCard.load(LedgerCard.class, getBean().getSourceCardId());
            tvSourceAccount.setText(sourceLedgerCard.getName());

            LedgerCard  destinationLedgerCard = LedgerCard.load(LedgerCard.class, getBean().getDestinationCardId());


            presenter.getBean().setDestinationCardId(destinationLedgerCard.getId());
            tvDestinationAccount.setText(destinationLedgerCard.getName());
            if(CollectionUtils.isNotEmpty(destinationList)){
                setTagsInfo(destinationLedgerCard);
            }
        }catch (Exception e){}
    }

    private void setTagsInfo(LedgerCard ledgerCard) {

        List<CardTag> cardTagList = CardTag.getTagsForCardOrderedByDefault(ledgerCard.getId());
        markPreviouslySelectedTags(cardTagList);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_tags_chips);
        TagsHorizontalRecyclerViewAdapter tagsHorizontalRecyclerViewAdapter = new TagsHorizontalRecyclerViewAdapter(this);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(tagsHorizontalRecyclerViewAdapter);
        if(CollectionUtils.isNotEmpty(cardTagList)){
            tagsHorizontalRecyclerViewAdapter.updateTagsList(cardTagList);
        }else{
            tagsHorizontalRecyclerViewAdapter.notifyDataSetChanged();
        }
    }



    private void markPreviouslySelectedTags(List<CardTag> cardTags) {
        List<Long> selectedCardList  = new ArrayList<>();
        for (CardTag cardTag:cardTags){
            boolean isSelected = selectedIdList.contains(cardTag.getId());
            if(isSelected){
                selectedCardList.add(cardTag.getId());
            }
            cardTag.setSelected(selectedIdList.contains(cardTag.getId()));

        }
        //in case of any tags removed in the list page we are taking care of it by clearing the current stuff
        selectedIdList.clear();
        selectedIdList.addAll(selectedCardList);
    }

    @Override
    public void systemSetPageInfoFailure(String reason) {
        Toast.makeText(this,reason,Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void userPressedTransactionSuccess(long boardId) {
        Intent data = new Intent();
        data.putExtra("id",boardId);
        setResult(RESULT_OK,data);
        finish();
    }

    @Override
    public void onAdapterItemSelected(long id) {
        selectedIdList.clear();
        selectedIdList.add(id);
    }

    @Override
    public void onAdapterItemDeSelected(long id) {
        selectedIdList.remove(id);
    }
}
