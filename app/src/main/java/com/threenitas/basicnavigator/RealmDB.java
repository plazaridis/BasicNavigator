package com.threenitas.basicnavigator;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmDB extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("myrealm.realm").build();
        Realm.setDefaultConfiguration(config);
    }
}
