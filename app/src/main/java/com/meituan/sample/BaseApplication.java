package com.meituan.sample;

import android.app.Application;
import android.content.Context;

import com.meituan.robust.PatchExecutor;

public class BaseApplication extends Application {
    public static Context appContext;
    @Override
    public void onCreate() {
        super.onCreate();
        appContext=this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        new PatchExecutor(base, new PatchManipulateImp(), new RobustCallBackSample()).start();
    }
}
