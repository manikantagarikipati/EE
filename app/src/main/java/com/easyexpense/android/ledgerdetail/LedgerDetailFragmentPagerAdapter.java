package com.easyexpense.android.ledgerdetail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.easyexpense.android.ledgerdetail.ledgerlist.LedgerListFragment;
import com.geekmk.db.dto.Ledger;

import java.util.List;

/**
 * Created by Mani on 01/03/17.
 */

public class LedgerDetailFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private static final  String TAG = "LedgerDetailFragmentPagerAdapter";

    private List<Ledger> ledgerList;

    private FragmentManager mFragmentManager;


    public LedgerDetailFragmentPagerAdapter(FragmentManager fm, List<Ledger> ledgerList) {
        super(fm);
        this.ledgerList = ledgerList;
        this.mFragmentManager = fm;
    }


    @Override
    public float getPageWidth(int position) {
        try {
            int size = ledgerList.size()-1;

            if(position == size){
                return super.getPageWidth(position);
            }else{
                return 0.92f;
            }
        }catch (Exception e){
            return super.getPageWidth(position);
        }
    }

    @Override
    public int getCount() {
        return ledgerList.size();
    }

    @Override
    public Fragment getItem(int position) {

        Ledger itemBean = ledgerList.get(position);

        //Create a new instance of the fragment and return it.
        LedgerListFragment ledgerListFragment =  LedgerListFragment.newInstance(itemBean.getId());

        return ledgerListFragment;
    }


    /**
     * This method is only gets called when we invoke {@link #notifyDataSetChanged()} on this adapter.
     * Returns the index of the currently active fragments.
     * There could be minimum two and maximum three active fragments(suppose we have 3 or more  fragments to show).
     * If there is only one fragment to show that will be only active fragment.
     * If there are only two fragments to show, both will be in active state.
     * PagerAdapter keeps left and right fragments of the currently visible fragment in ready/active state so that it could be shown immediate on swiping.
     * Currently Active Fragments means one which is currently visible one is before it and one is after it.
     *
     * @param object Active Fragment reference
     * @return Returns the index of the currently active fragments.
     */
    @Override
    public int getItemPosition(Object object) {
        LedgerListFragment fragment = (LedgerListFragment)object;
        Ledger itemBean = fragment.getItemBean();
        int position = ledgerList.indexOf(itemBean);

        if (position >= 0) {
            // The current data matches the data in this active fragment, so let it be as it is.
            return position;
        } else {
            // Returning POSITION_NONE means the current data does not matches the data this fragment is showing right now.  Returning POSITION_NONE constant will force the fragment to redraw its view layout all over again and show new data.
            return POSITION_NONE;
        }
    }

    public void updateLedgerList(List<Ledger> ledgerList) {
        this.ledgerList.clear();
        this.ledgerList.addAll(ledgerList);
        notifyDataSetChanged();
    }
    public void addLedger(Ledger itemBean) {
        ledgerList.add(itemBean);
        notifyDataSetChanged();
    }
}
