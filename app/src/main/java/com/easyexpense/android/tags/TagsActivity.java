package com.easyexpense.android.tags;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.easyexpense.android.R;
import com.easyexpense.android.helper.AppConstants;
import com.easyexpense.android.helper.ModelConstants;
import com.easyexpense.commons.android.activity.MkActivity;
import com.geekmk.db.dto.CardTag;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class TagsActivity extends MkActivity implements
        SearchView.OnQueryTextListener,CallBackResultSet,TagsView {

    private TagsListRecyclerViewAdapter tagsListRecyclerViewAdapter;

    private Paint p = new Paint();

    private long tagCardId;

    private  TagsPresenter presenter;

    private boolean isDataChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        ButterKnife.bind(this);
        if(getIntent()!=null && getIntent().getExtras()!=null){
            tagCardId = getIntent().getExtras().getLong(AppConstants.IntentConstants.INTENT_DATA);
        }
        initToolBar();
        initPresenter();
        initComponents();

    }


    @OnClick(R.id.fab) void onAddTagPressed(){
        AddTagDialog addTagDialog=AddTagDialog.newInstance(0,0,tagCardId);
        addTagDialog.show(getSupportFragmentManager(),getResources().getString(R.string.create_tag));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tags, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(true);

        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public void initComponents() {
      setTagsListRecyclerViewAdapter();

    }

    private void setTagsListRecyclerViewAdapter(){

        List<CardTag> cardTagList = CardTag.getTagsForCard(tagCardId);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_tags);
        tagsListRecyclerViewAdapter = new TagsListRecyclerViewAdapter(cardTagList);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(tagsListRecyclerViewAdapter);
        initSwipe(recyclerView);


    }
    private void initSwipe(RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    isDataChanged = true;
                    tagsListRecyclerViewAdapter.removeItem(position);
                } else {
                    tagsListRecyclerViewAdapter.mockItemChange(position);
                    CardTag tag = tagsListRecyclerViewAdapter.getTag(position);
                    AddTagDialog addTagDialog=AddTagDialog.newInstance(tag.getId(),position,tagCardId);
                    addTagDialog.show(getSupportFragmentManager(),getResources().getString(R.string.create_tag));
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_mode_edit_white_24dp);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else if(dX<0){
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
    public void initPresenter() {
        presenter = new TagsPresenterImpl(this);
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
    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(tagsListRecyclerViewAdapter!=null){
            tagsListRecyclerViewAdapter.getFilter().filter(newText);
        }
        return true;
    }

    @Override
    public void onDialogResultSet(Object resultObject) {
        if(resultObject instanceof Map){

            Map<String,Object> resultInfo = (Map)resultObject;

            boolean isEditMode = (boolean)resultInfo.get(ModelConstants.CallBackConstants.IS_EDIT_MODE);
            int position = (int)resultInfo.get(AppConstants.IntentConstants.TAG_POSITION);
            String tagName = (String) resultInfo.get(ModelConstants.CallBackConstants.TAG_INFO);
            if(isEditMode){
                tagsListRecyclerViewAdapter.editTag(tagName,position);
            }else{
                presenter.addTag(tagCardId,tagName);
            }
        }
    }

    @Override
    public void onAdapterResultSet(Object selectedItem) {

    }

    @Override
    public void onAdapterLongClick(Object selectedItem) {

    }

    @Override
    public void onBackPressed() {
        if(isDataChanged){
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public void addTagSuccess(long cardId) {
        isDataChanged  = true;
        CardTag cardTag = CardTag.load(CardTag.class,cardId);
        if(tagsListRecyclerViewAdapter!=null){
            tagsListRecyclerViewAdapter.addTag(cardTag);
        }else{
            setTagsListRecyclerViewAdapter();
        }

    }
}
