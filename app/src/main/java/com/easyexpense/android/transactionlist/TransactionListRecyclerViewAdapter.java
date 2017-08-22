package com.easyexpense.android.transactionlist;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easyexpense.android.R;
import com.easyexpense.android.helper.utils.AppUtils;
import com.easyexpense.commons.utils.StringUtils;
import com.geekmk.db.constants.DefaultConstants;
import com.geekmk.db.dto.CardTransaction;
import com.geekmk.db.dto.LedgerCard;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Mani on 17/04/17.
 */

public class TransactionListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<CardTransaction> cardTransactionList = new ArrayList<>();

    private Context context;

    public void updateList(List<CardTransaction> updatedList){

        if(updatedList!=null){
            cardTransactionList.clear();
            cardTransactionList.addAll(updatedList);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_transaction_list, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        try {

            ViewHolder tagViewHolder = ((ViewHolder)holder);

            CardTransaction cardTransaction = cardTransactionList.get(position);
            LedgerCard ledgerCard = LedgerCard.load(LedgerCard.class,cardTransaction.getSourceCard());


            tagViewHolder.tvSourceName.setText(ledgerCard.getName());

            if(StringUtils.isNotEmpty(cardTransaction.getComment())){
                tagViewHolder.tvSourceComments.setVisibility(View.VISIBLE);
                tagViewHolder.tvSourceComments.setText(cardTransaction.getComment());
            }else{
                tagViewHolder.tvSourceComments.setVisibility(View.GONE);
            }


            tagViewHolder.tvSourceDate.setText(AppUtils.convertDateFormat(cardTransaction.getCreatedTS()));
            LedgerCard destCard = LedgerCard.load(LedgerCard.class,cardTransaction.getDestinationCard());

            tagViewHolder.tvDestinationAmount.setText((int)cardTransaction.getAmount()+"");
            int color = -1;

            switch (cardTransaction.getTransactionType()){

                case DefaultConstants.TransactionConstants.TRANSACTION_EXPENSE:
                    color = ContextCompat.getColor(context,R.color.expense_color);
                    break;
                case DefaultConstants.TransactionConstants.TRANSACTION_INCOME:
                    color = ContextCompat.getColor(context,R.color.income_color);
                    break;
                default:
                    color = ContextCompat.getColor(context,R.color.black);

            }
            tagViewHolder.tvDestinationAmount.setTextColor(color);
            tagViewHolder.tvDestinationAccountName.setText(destCard.getName());

            if(StringUtils.isNotEmpty(cardTransaction.getImagePath())){
                tagViewHolder.ivAttachmentInfo.setVisibility(View.VISIBLE);
            }else{
                tagViewHolder.ivAttachmentInfo.setVisibility(View.INVISIBLE);
            }
        }catch ( Exception e){
            Log.e(TAG,"Cannot display cell");
        }
    }

    @Override
    public int getItemCount() {
        return cardTransactionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvSourceName;

        TextView tvSourceComments;

        TextView tvDestinationAmount;

        TextView tvDestinationAccountName;

        FloatingActionButton ivAttachmentInfo;

        TextView tvSourceDate;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSourceName = (TextView) itemView.findViewById(R.id.tv_source_name);
            tvSourceComments = (TextView) itemView.findViewById(R.id.tv_source_comments);

            tvDestinationAmount = (TextView) itemView.findViewById(R.id.tv_destination_amount);
            tvDestinationAccountName = (TextView) itemView.findViewById(R.id.tv_destination_account);
            ivAttachmentInfo = (FloatingActionButton) itemView.findViewById(R.id.iv_attachment);
            tvSourceDate = (TextView) itemView.findViewById(R.id.tv_source_date);

            ivAttachmentInfo.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int pos  = getAdapterPosition();
            CardTransaction cardTransaction = cardTransactionList.get(pos);
            if(cardTransaction.getImagePath()!=null){
                Uri imageUri = Uri.parse(cardTransaction.getImagePath());
                AppUtils.displayImage(context,imageUri);
            }
        }
    }
}
