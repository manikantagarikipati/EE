package com.easyexpense.android.expenseinput;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.geekmk.db.dto.LedgerCard;

import java.util.List;

/**
 * Created by Mani on 10/04/17.
 */

public class CardSpinnerAdapter extends ArrayAdapter<LedgerCard> {
    public CardSpinnerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<LedgerCard> objects) {
        super(context, resource, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setText(this.getItem(position).getName());
        label.setTextColor(Color.WHITE);
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setText(this.getItem(position).getName());
        return label;
    }
}
