package com.muroigundan.task_app;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by local-user on 2017/10/20.
 */

public class SchedulerApplication extends Application {
    Realm mRealm;
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfig);

        mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    Task task = new Task();
                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd");
                    SimpleDateFormat sdfTime = new SimpleDateFormat("h:mm");

                    task.setId(0);
                    task.setSubject("線形代数　課題");
                    task.setDate(sdfDate.parse("2017/11/1"));
                    task.setTime(sdfTime.parse("00:00"));
                    task.setImportance(2);
                    realm.insertOrUpdate(task);

                    //入力


                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
