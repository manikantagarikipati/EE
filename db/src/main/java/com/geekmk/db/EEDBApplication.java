package com.geekmk.db;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

/**
 * Created by Mani on 03/04/17.
 */

public class EEDBApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Configuration configuration = new Configuration.Builder(this).setDatabaseVersion(1).create();

        ActiveAndroid.initialize(configuration);

    }
}
