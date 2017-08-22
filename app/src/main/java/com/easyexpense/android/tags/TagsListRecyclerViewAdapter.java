package com.easyexpense.android.tags;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.easyexpense.android.R;
import com.easyexpense.commons.utils.CollectionUtils;
import com.geekmk.db.dto.CardTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mani on 13/03/17.
 */

public class TagsListRecyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private static final String TAG = "TagsListViewAdapter";

    private List<CardTag> tagsList = new ArrayList<>();

    private Context context;

    private static final int VIEW_TYPE_TAG = 1;
    private static final int VIEW_TYPE_EMPTY = 2;



    protected List<CardTag> list = new ArrayList<>();


    public TagsListRecyclerViewAdapter(List<CardTag> tagsList) {
        if (CollectionUtils.isNotEmpty(tagsList)) {
            list.clear();
            this.tagsList.clear();
            this.tagsList.addAll(tagsList);
            list.addAll(tagsList);
        }
    }

    public void addTag(CardTag tag){
        try {
            list.add(tag);
            tagsList.add(tag);
            if(list.size()!=1){
                notifyItemInserted(list.size()-1);
            }else{
                notifyDataSetChanged();
            }
        }catch (Exception e){
            Log.e(TAG,"Error inserting tag item"+e.getMessage());
        }
    }

    public void editTag(String tagName,int position){
        CardTag tag = tagsList.get(position);
        tag.setName(tagName);
        tag.save();
        notifyItemChanged(position);
    }

    public CardTag getTag(int position){
        return  tagsList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (CollectionUtils.isEmpty(list)){
            return VIEW_TYPE_EMPTY;
        }else{
            return VIEW_TYPE_TAG;
        }
    }


    public void removeItem(int position) {
        CardTag cardTag = list.get(position);
        cardTag.delete();
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    public void mockItemChange(int position){
        notifyItemChanged(position);
    }

    public void toggleItem(int position){
        CardTag tag  = list.get(position);
        tag.setDefault(!tag.isDefault());
        tag.save();
        notifyItemChanged(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        this.context=parent.getContext();

        if(viewType == VIEW_TYPE_EMPTY){

            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_tag_empty, parent, false);
            return new EmptyViewHolder(view);


        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_tag_list, parent, false);
            return new TagViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            int viewType = getItemViewType(position);
            if(viewType == VIEW_TYPE_TAG){
                TagViewHolder tagViewHolder = ((TagViewHolder)holder);
                tagViewHolder.tvTag.setText(list.get(position).getName());
                if(list.get(position).isDefault()){
                    tagViewHolder.ivDefault.setVisibility(View.VISIBLE);
                }else{
                    tagViewHolder.ivDefault.setVisibility(View.GONE);
                }
            }
        }catch ( Exception e){
            Log.e(TAG,"Cannot display cell");
        }

    }

    @Override
    public int getItemCount() {
        if(CollectionUtils.isEmpty(list)){
            return 1;
        }else{
            return list.size();
        }
    }


    class TagViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvTag;

        View ivDefault;

        public TagViewHolder(View itemView) {
            super(itemView);
            tvTag = (TextView) itemView.findViewById(R.id.tv_tag_item);
            ivDefault =itemView.findViewById(R.id.vIndicator);
            itemView.setOnClickListener(this);
         }

        @Override
        public void onClick(View v) {
            toggleItem(getAdapterPosition());
        }
    }


     class  EmptyViewHolder extends  RecyclerView.ViewHolder{
        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }


    protected List<CardTag> getFilteredResults(String constraint) {
        List<CardTag> results = new ArrayList<>();

        for (CardTag tag : tagsList) {
            if (tag.getName().toLowerCase().contains(constraint)) {
                results.add(tag);
            }
        }
        return results;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<CardTag>) results.values;

                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<CardTag> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = tagsList;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;
                return results;
            }
        };
    }

}
