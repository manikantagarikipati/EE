package com.easyexpense.commons.android.activity;


import android.support.v7.app.AppCompatActivity;

/**
 * Created by Manikanta on 6/05/2016.
 * @author Manikanta
 */
public abstract class MkActivity extends AppCompatActivity {

    public abstract void initComponents();

    public abstract void initPresenter();

    public abstract void initToolBar();

}
