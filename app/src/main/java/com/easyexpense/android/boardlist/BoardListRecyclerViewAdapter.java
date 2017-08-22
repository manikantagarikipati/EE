package com.easyexpense.android.boardlist;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easyexpense.android.R;
import com.easyexpense.android.helper.utils.WidgetUtils;
import com.easyexpense.android.tags.CallBackResultSet;
import com.easyexpense.commons.utils.CollectionUtils;
import com.geekmk.db.dto.Board;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mani on 19/03/17.
 */

public class BoardListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "BoardListViewAdapter";

    private static final int VIEW_TYPE_BOARD = 1;
    private static final int VIEW_TYPE_EMPTY = 2;

    private List<Board> boardList = new ArrayList<>();

    private int pos = 0;

    private Context context;

    private CallBackResultSet callBackResultSet;

    public BoardListRecyclerViewAdapter(List<Board> boardList, CallBackResultSet callBackResultSet) {
        this.callBackResultSet = callBackResultSet;
        if (CollectionUtils.isNotEmpty(boardList)) {
            this.boardList.addAll(boardList);
        }
    }

    public void mockItemChange(int position){
        notifyItemChanged(position);
    }

    public void removeItem(int position) {
        boardList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, boardList.size());
    }

    public Board getItem(int position){
        return boardList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (CollectionUtils.isEmpty(boardList)){
            return VIEW_TYPE_EMPTY;
        }else{
            return VIEW_TYPE_BOARD;
        }
    }

    public void addBoard(Board bean){
        boardList.add(bean);
        notifyItemInserted(boardList.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        this.context=parent.getContext();

        if(viewType == VIEW_TYPE_EMPTY){

            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_board_list_empty, parent, false);
            return new EmptyViewHolder(view);


        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_board_list, parent, false);
            return new BoardViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            int viewType = getItemViewType(position);
            if(viewType == VIEW_TYPE_BOARD){
                BoardViewHolder boardViewHolder = ((BoardViewHolder) holder);
                Board bean = boardList.get(position);
                if(bean.isDefault()){
                    this.pos = position;
                    WidgetUtils.setBackGroundToIv(boardViewHolder.ivDefault,ContextCompat.getDrawable(context,R.drawable.circle_with_tick));
                }else{
                    WidgetUtils.setBackGroundToIv(boardViewHolder.ivDefault,ContextCompat.getDrawable(context,R.drawable.circle_green));
                }

                boardViewHolder.tvName.setText(bean.getName());

                boardViewHolder.tvBalance.setText((int)bean.getAmount()+"");

            }
        }catch ( Exception e){
            Log.e(TAG,"Cannot display cell");
        }

    }

    @Override
    public int getItemCount() {
        if(CollectionUtils.isEmpty(boardList)){
            return 1;
        }else{
            return boardList.size();
        }
    }


    class BoardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        TextView tvName;

        ImageView ivDefault;

        TextView tvBalance;


        public BoardViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_board_name);
            tvBalance = (TextView) itemView.findViewById(R.id.tv_board_balance);
            ivDefault =(ImageView) itemView.findViewById(R.id.iv_default_board);
            itemView.setOnClickListener(this);
            ivDefault.setOnClickListener(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                itemView.setOnLongClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            int updatedDefaultPosition = getAdapterPosition();
            if(v.getId() == R.id.iv_default_board){
                if(updatedDefaultPosition!=pos){
                    Board board = boardList.get(pos);
                    board.setDefault(false);
                    board.save();
                    notifyItemChanged(pos);
                    Board updatedBoard = boardList.get(updatedDefaultPosition);
                    updatedBoard.setDefault(true);
                    updatedBoard.save();
                    notifyItemChanged(updatedDefaultPosition);
                    Toast.makeText(context,updatedBoard.getName()+" is made as default ledger",
                            Toast.LENGTH_SHORT).show();
                }
            }else{
                if(callBackResultSet!=null){
                    callBackResultSet.onAdapterResultSet(boardList.get(updatedDefaultPosition));
                }
            }

        }

        @Override
        public boolean onLongClick(View v) {
            callBackResultSet.onAdapterLongClick(boardList.get(getAdapterPosition()));
            return true;
        }
    }

    class  EmptyViewHolder extends  RecyclerView.ViewHolder{
        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

}
