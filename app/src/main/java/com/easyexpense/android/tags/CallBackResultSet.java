package com.easyexpense.android.tags;

/**
 * Created by Mani on 13/03/17.
 */

public interface CallBackResultSet {

    void onDialogResultSet(Object resultObject);

    void onAdapterResultSet(Object selectedItem);

    void onAdapterLongClick(Object selectedItem);
}
