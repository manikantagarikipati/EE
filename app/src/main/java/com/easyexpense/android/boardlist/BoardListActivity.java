package com.easyexpense.android.boardlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;

import com.easyexpense.android.R;
import com.easyexpense.android.helper.AppConstants;
import com.easyexpense.android.helper.utils.AppUtils;
import com.easyexpense.android.tags.CallBackResultSet;
import com.easyexpense.commons.android.activity.MkActivity;
import com.easyexpense.commons.android.marshmallow.MarshMallowPermission;
import com.geekmk.db.dto.Board;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BoardListActivity extends MkActivity implements BoardListView,CallBackResultSet {

    private BoardListPresenter presenter;

    private BoardListRecyclerViewAdapter boardListRecyclerViewAdapter;

    private Paint p = new Paint();

    @BindView(R.id.cl_parent)
    CoordinatorLayout coordinatorLayout;

    private boolean isDataChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);
        ButterKnife.bind(this);
        initToolBar();
        initPresenter();
        initComponents();
        if(savedInstanceState!=null){
            isDataChanged = savedInstanceState.getBoolean("SIS");
        }
        presenter.systemFetchBoardList();
    }

    @Override
    public void initComponents() {
    }

    @Override
    public void initPresenter() {

        presenter = new BoardListPresenterImpl(this);
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
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void systemFetchBoardListSuccess(List<Board> boardList) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_board_list);
        boardListRecyclerViewAdapter = new BoardListRecyclerViewAdapter(boardList,this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(boardListRecyclerViewAdapter);
        initSwipe(recyclerView);

        try{
            getSupportActionBar().setSubtitle(Board.sumOfLedgers()+"");
        }catch (Exception e){}
    }

    private void initSwipe(RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    boardListRecyclerViewAdapter.mockItemChange(position);
                    Board board = boardListRecyclerViewAdapter.getItem(position);
                    if(!board.isDefault()){
                        new AlertDialog.Builder(BoardListActivity.this)
                                .setTitle("Delete Board")
                                .setMessage("Are you sure you want to delete this board?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        isDataChanged = true;
                                        Board board = boardListRecyclerViewAdapter.getItem(position);
                                        presenter.deleteBoard(board.getId());
                                        boardListRecyclerViewAdapter.removeItem(position);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {}})
                                .show();
                    }else{
                        AppUtils.displaySnackBar(coordinatorLayout,getResources().getString(R.string.cannot_delete_def_board));
                    }
                }else {
                    boardListRecyclerViewAdapter.mockItemChange(position);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                   if(dX<0){
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_sweep_white_24dp);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }


    @Override
    public void addBoardSuccess(Board board) {
        boardListRecyclerViewAdapter.addBoard(board);
    }

    @OnClick(R.id.fab_add_board) void onAddBoardClick(){
        AddBoardDialog addTagDialog= AddBoardDialog.newInstance();
        addTagDialog.show(getSupportFragmentManager(),getResources().getString(R.string.create_board));
    }

    @Override
    public void onDialogResultSet(Object resultObject) {
        if(resultObject instanceof String){
            presenter.addBoard((String) resultObject);
        }
    }

    @Override
    public void onAdapterResultSet(Object selectedItem) {

        if(selectedItem instanceof Board){
            Board ledgerBean = (Board)selectedItem;
            Intent intent = new Intent();
            intent.putExtra(AppConstants.IntentConstants.INTENT_DATA,ledgerBean.getId());
            setResult(RESULT_OK,intent);
            finish();
        }
    }


    @Override
    public void onAdapterLongClick(Object selectedItem) {
        if(selectedItem instanceof  Board){
            MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if(marshMallowPermission.checkPermissionForShortCut()){
                    AppUtils.createShortCutForLedger(this,(Board)selectedItem);
                }else{
                    marshMallowPermission.requestPermissionForShortCut();
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean("SIS",isDataChanged);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(AppConstants.IntentConstants.IS_DATA_CHANGED,isDataChanged);
        setResult(RESULT_OK,intent);
        finish();
    }
}
