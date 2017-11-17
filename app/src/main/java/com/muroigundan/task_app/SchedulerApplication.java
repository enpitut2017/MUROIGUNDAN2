package com.muroigundan.task_app;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by local-user on 2017/10/20.
 */

public class SchedulerApplication extends Application {
    Realm mRealm;
    public static final int PREFERENCE_INIT = 0;
    public static final int PREFERENCE_BOOTED = 1;
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfig);

        mRealm = Realm.getDefaultInstance();

        //初回起動時のみの処理
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        //int state = sp.getInt("STATE", 0);

        if (sp.getInt("INIT_STATE", PREFERENCE_INIT) == PREFERENCE_INIT) {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                  try {

                        Task task = new Task();
                        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd");
                        SimpleDateFormat sdfTime = new SimpleDateFormat("h:mm");
                        SimpleDateFormat sdfDate_and_Time = new SimpleDateFormat("yyyy/MM/dd h:mm");

                        task.setId(0);
                        task.setSubject("線形代数　課題");
                        task.setDate(sdfDate.parse("2017/12/1"));
                        task.setTime(sdfTime.parse("18:00"));
                        task.setDate_and_time(sdfDate_and_Time.parse("2017/12/1 18:00"));
                        task.setImportance(2);
                        realm.insertOrUpdate(task);

                        task.setId(1);
                        task.setSubject("線形代数2　課題");
                        task.setDate(sdfDate.parse("2017/12/1"));
                        task.setTime(sdfTime.parse("15:15"));
                        task.setDate_and_time(sdfDate_and_Time.parse("2017/12/1 15:15"));
                        task.setImportance(4);
                        realm.insertOrUpdate(task);

                        task.setId(2);
                        task.setSubject("バイト　面接");
                        task.setDate(sdfDate.parse("2017/10/30"));
                        task.setTime(sdfTime.parse("19:30"));
                        task.setDate_and_time(sdfDate_and_Time.parse("2017/10/30 19:30"));
                        task.setImportance(5);
                        realm.insertOrUpdate(task);

                        task.setId(3);
                        task.setSubject("パターン認識　テスト");
                        task.setDate(sdfDate.parse("2016/12/31"));
                        task.setTime(sdfTime.parse("17:15"));
                        task.setDate_and_time(sdfDate_and_Time.parse("2016/12/31 17:15"));
                        task.setImportance(4);
                        realm.insertOrUpdate(task);

                        task.setId(4);
                        task.setSubject("食料品買い出し");
                        task.setDate(sdfDate.parse("2017/10/30"));
                        task.setTime(sdfTime.parse("18:00"));
                        task.setDate_and_time(sdfDate_and_Time.parse("2017/10/30 18:00"));
                        task.setImportance(2);
                        realm.insertOrUpdate(task);

                        task.setId(5);
                        task.setSubject("パスポート取得");
                        task.setDate(sdfDate.parse("2016/12/31"));
                        task.setTime(sdfTime.parse("00:00"));
                        task.setDate_and_time(sdfDate_and_Time.parse("2016/12/31 00:00"));
                        task.setImportance(1);
                        realm.insertOrUpdate(task);

                        task.setId(6);
                        task.setSubject("画像認識　課題");
                        task.setDate(sdfDate.parse("2016/1/15"));
                        task.setTime(sdfTime.parse("12:00"));
                        task.setDate_and_time(sdfDate_and_Time.parse("2016/1/15 12:00"));
                        task.setImportance(1);
                        realm.insertOrUpdate(task);

                        task.setId(7);
                        task.setSubject("研究室見学");
                        task.setDate(sdfDate.parse("2016/10/27"));
                        task.setTime(sdfTime.parse("18:00"));
                        task.setDate_and_time(sdfDate_and_Time.parse("2016/10/27 18:00"));
                        task.setImportance(5);
                        realm.insertOrUpdate(task);


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                }
            });
            sp.edit().putInt("INIT_STATE", PREFERENCE_BOOTED).commit();
        }
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        mRealm.close();
    }
}
