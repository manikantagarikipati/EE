package com.easyexpense.android.expenseinput;

import java.util.List;

/**
 * Created by Mani on 24/03/17.
 */

public interface ExpensePresenter {

    ExpenseInputBean getBean();


    void systemSetPageInfo(long sourceCardId,boolean isSwap);

    void performTransaction(String providedAmount, long destinationCardId, List<Long> selectedIdList);

    void swapAccounts();
}
