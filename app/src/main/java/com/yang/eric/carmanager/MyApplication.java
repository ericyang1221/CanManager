package com.yang.eric.carmanager;

import android.app.Application;

import com.yang.eric.carmanager.tools.CrashHandler;

/**
 * Created by erichyang on 2018/2/23.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = new CrashHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }
}