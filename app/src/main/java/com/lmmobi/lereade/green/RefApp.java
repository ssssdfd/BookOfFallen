package com.lmmobi.lereade.green;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class RefApp extends Application {
    private DaoSession mDaoSession;
    @Override
    public void onCreate() {
        super.onCreate();
        mDaoSession = new DaoMaster(new DaoMaster.DevOpenHelper(this, "greendao_ref.db").getWritableDb()).newSession();

    }
    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}
