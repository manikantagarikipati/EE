package com.easyexpense.android.expenseinput;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easyexpense.android.R;
import com.easyexpense.android.tags.GenericAdapterCallBack;
import com.easyexpense.commons.utils.CollectionUtils;
import com.geekmk.db.dto.CardTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mani on 12/03/17.
 */

public class TagsHorizontalRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CardTag> tagsList = new ArrayList<>();

    private Context context;

    private static final String TAG = TagsHorizontalRecyclerViewAdapter.class.getName();

    private GenericAdapterCallBack genericAdapterCallBack;

    private static final int VIEW_TYPE_TAG = 1;
    private static final int VIEW_TYPE_EMPTY = 2;

    private int pos = 0;

    public TagsHorizontalRecyclerViewAdapter(GenericAdapterCallBack genericAdapterCallBack){
        this.genericAdapterCallBack = genericAdapterCallBack;
    }

    public void updateTagsList(List<CardTag> latestTagsList){
        if(CollectionUtils.isNotEmpty(latestTagsList)){
            tagsList.clear();
            tagsList.addAll(latestTagsList);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();


        if(viewType == VIEW_TYPE_EMPTY){

            View tagView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_tag, parent, false);
            return new EmptyView(tagView);


        }else{
            View tagView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_tag, parent, false);
            return new ViewHolder(tagView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try{
            int viewType = getItemViewType(position);
            if(viewType == VIEW_TYPE_TAG) {
                ViewHolder viewHolder = ((ViewHolder)holder);
                CardTag tag = tagsList.get(position);
                viewHolder.tvTag.setText(tag.getName());
                if (tag.isSelected()) {
                    this.pos = position;
                    viewHolder.tvTag.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                } else {
                    viewHolder.tvTag.setTextColor(ContextCompat.getColor(context, R.color.grey));
                }
            }else{
                EmptyView viewHolder = ((EmptyView)holder);
                viewHolder.tvTag.setText("No Tags Available");
                viewHolder.tvTag.setTextColor(ContextCompat.getColor(context, R.color.grey));
            }
        }catch (Exception e){
            Log.e(TAG,"Error displaying tag");
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (CollectionUtils.isEmpty(tagsList)){
            return VIEW_TYPE_EMPTY;
        }else{
            return VIEW_TYPE_TAG;
        }
    }

    @Override
    public int getItemCount() {
        if(CollectionUtils.isEmpty(tagsList)){
            return 1;
        }else{
            return tagsList.size();
        }
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvTag;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTag = (TextView) itemView.findViewById(R.id.tv_tag);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int updatedPosition = getAdapterPosition();

                CardTag tag = tagsList.get(pos);
                tag.setSelected(false);
                tag.save();
                notifyItemChanged(pos);

                CardTag clickedTag= tagsList.get(updatedPosition);
                clickedTag.setSelected(true);
                clickedTag.save();
                notifyItemChanged(updatedPosition);
                if(genericAdapterCallBack!=null){
                    genericAdapterCallBack.onAdapterItemSelected(clickedTag.getId());
                }


        }
    }

    class  EmptyView extends RecyclerView.ViewHolder{

        TextView tvTag;

        public EmptyView(View itemView) {
            super(itemView);
            tvTag = (TextView) itemView.findViewById(R.id.tv_tag);
        }

    }
}
