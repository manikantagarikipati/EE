package com.easyexpense.commons.android.fragment;

import android.support.v4.app.Fragment;


/**
 * Created by Manikanta on 6/05/2016.
 * @author Manikanta
 */
public abstract class MkFragment extends Fragment {

    public abstract void initListener();

    public abstract void initializeViewBean();

    public abstract void initComponents();

    public abstract void initPresenter();
}
