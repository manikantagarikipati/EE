package com.easyexpense.android.ledgerdetail;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easyexpense.android.R;
import com.easyexpense.android.expenseinput.ExpenseInputActivity;
import com.easyexpense.android.helper.AppConstants;
import com.easyexpense.android.helper.utils.AppUtils;
import com.easyexpense.android.helper.utils.ExcelUtils;
import com.easyexpense.android.helper.utils.WidgetUtils;
import com.easyexpense.android.transactionlist.TransactionListActivity;
import com.easyexpense.commons.utils.CollectionUtils;
import com.easyexpense.commons.utils.StringUtils;
import com.geekmk.db.dto.LedgerCard;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.easyexpense.android.R.id.menu_delete_card;
import static com.easyexpense.android.R.id.menu_edit_card;
import static com.easyexpense.android.R.id.menu_export_excel;
import static com.easyexpense.android.R.id.menu_view_transactions;
import static com.easyexpense.android.helper.AppConstants.IntentConstants.TRANSACTION_LIST_REQUEST_CODE;

/**
 * Created by Mani on 20/02/17.
 */

public class CardsRecyclerViewAdapter extends RecyclerView.Adapter<CardsRecyclerViewAdapter.ViewHolder> {

    private List<LedgerCard> ledgerCardList = new ArrayList<>();

    private Activity activity;

    private RecyclerView recyclerView;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    public CardsRecyclerViewAdapter(Activity activity){
        this.activity = activity;

    }
    public void updateCardsList(List<LedgerCard> cardBeen){
        if(CollectionUtils.isNotEmpty(cardBeen)){
            ledgerCardList.addAll(cardBeen);
        }
    }

    public void addCard(LedgerCard ledgerCard){
        ledgerCardList.add(ledgerCard);
        notifyItemInserted(ledgerCardList.size()-1);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_card,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        try{
            LedgerCard ledgerCard = ledgerCardList.get(position);
            holder.tvAmount.setText((int)ledgerCard.getAmount()+"");

            holder.tvTitle.setText(ledgerCard.getName());

//            if(cardBean.isCredit()){
//                holder.tvAmount.setTextColor(ContextCompat.getColor(holder.tvTitle.getContext(),R.color.credit_color));
//            }
//            holder.draggableCardView.setCardBean(cardBean);
        }catch (Exception e){
            Log.e(CardsRecyclerViewAdapter.class.getName(),"Error while setting info in cardView");
        }
    }

    @Override
    public int getItemCount() {
        return ledgerCardList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.tv_amount)
        TextView tvAmount;

        @BindView(R.id.dcv_item)
        CardView draggableCardView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            LedgerCard ledgerCard = ledgerCardList.get(position);
            long ledgerCardId = ledgerCard.getId();
            AppUtils.intentNavigateWithResult(ledgerCardId, ExpenseInputActivity.class,activity,TRANSACTION_LIST_REQUEST_CODE);
        }

        @Override
        public boolean onLongClick(View v) {
            final int position = getAdapterPosition();
            PopupMenu myPopup = new PopupMenu(activity, v);
            myPopup.setGravity(Gravity.END);
            myPopup.getMenuInflater().inflate(R.menu.card_options, myPopup.getMenu());
            myPopup.show();
            myPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case menu_edit_card:
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);

                            LayoutInflater inflater = activity.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.alert_edit_card, null);
                            dialogBuilder.setView(dialogView);
                            LedgerCard ledgerCard = ledgerCardList.get(position);

                            final TextInputLayout editText = (TextInputLayout) dialogView.findViewById(R.id.til_edit_card);
                            WidgetUtils.setTextInputLayoutInfo(editText,ledgerCard.getName());
                            dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String cardName = WidgetUtils.getTextFromTIL(editText);
                                    LedgerCard ledgerCard = ledgerCardList.get(position);
                                    if (StringUtils.isNotEmpty(cardName)) {
                                        if(LedgerCard.isNameUnique(cardName,ledgerCard.getBoardId())){
                                            WidgetUtils.removeErrorForTil(editText);
                                            ledgerCard.setName(cardName);
                                            ledgerCard.save();
                                            notifyItemChanged(position);
                                        }else{
                                            WidgetUtils.setErrorToTil(editText, activity.getResources().getString(R.string.duplicate_input,cardName));
                                        }
                                    } else {
                                        WidgetUtils.setErrorToTil(editText, activity.getResources().getString(R.string.empty_input, activity.getResources().getString(R.string.card)));
                                    }
                                }
                            });
                            dialogBuilder.setNegativeButton("No",null);
                            AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.show();

                            break;
                        case menu_delete_card:
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                                    .setTitle("Are you sure?")
                                    .setMessage("This action cannot be undone!")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            LedgerCard ledgerCard = ledgerCardList.get(position);
                                            ledgerCard.delete();
                                            ledgerCardList.remove(position);
                                            notifyItemRemoved(position);
                                        }
                                    })
                                    .setNegativeButton("No", null);

                            builder.show();

                            break;
                        case menu_view_transactions:
                            LedgerCard ledgerCard1 = ledgerCardList.get(position);
                            long ledgerCardId = ledgerCard1.getId();
                            AppUtils.navigateTransaction(ledgerCardId,
                                    AppConstants.TransactionListConstants.CARD,
                                    TransactionListActivity.class,activity);
                            break;
                        case menu_export_excel:
                            LedgerCard ledgerCard2 = ledgerCardList.get(position);
                            createAndExportCardExcel(ledgerCard2.getId());
                            break;

                    }

                    return false;
                }
            });

            return true;
        }
    }

    public void createAndExportCardExcel(long cardId){
        int storagePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(storagePermission == PackageManager.PERMISSION_GRANTED) {
            try {
                final File createdFile = ExcelUtils.createTransactionsExcelSheetForCard(cardId);

                Snackbar snackbar = Snackbar.make(recyclerView, "Excel Created Successfully", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("View", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = FileProvider.getUriForFile(activity,"com.easyexpense.commons",createdFile);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(uri, "application/vnd.ms-excel");

                        if(intent.resolveActivity(activity.getPackageManager())!=null){
                            activity.startActivity(intent);
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
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},14);
        WidgetUtils.displaySnackBar("Provide permission to access storage",recyclerView);
    }

    }
}
