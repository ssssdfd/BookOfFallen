package com.lmmobi.lereade.datasources;

import android.app.Application;

import com.lmmobi.lereade.green.DaoSession;
import com.lmmobi.lereade.green.Ref;
import com.lmmobi.lereade.green.RefApp;

public class RefSource{
    Application application;
    DaoSession daoSession;
    public RefSource(Application application) {
        this.application = application;
    }

    public void insertReference(Ref ref) {
        daoSession = ((RefApp) application).getDaoSession();
        daoSession.getRefDao().insert(ref);
    }

    public  Ref getReference(){
        daoSession = ((RefApp) application).getDaoSession();
        return  daoSession.getRefDao().load(1L);
    }
}
