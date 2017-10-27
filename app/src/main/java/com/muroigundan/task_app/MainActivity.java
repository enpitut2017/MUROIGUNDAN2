package com.muroigundan.task_app;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmMigration;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {
    private Realm mRealm;
    Button mButton1;
    Button mButton2;
    Button mButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRealm = Realm.getDefaultInstance();

        mButton1 = (Button) findViewById(R.id.button1);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton3 = (Button) findViewById(R.id.button3);

        long key1 = -1, key2 = -1, key3 = -1;
        RealmResults<Task> results;
        Task task;

        Number maxId = mRealm.where(Task.class).max("id");
        if (maxId == null) {
            mButton1.setText("最優先");
            mButton2.setText("二番目");
            mButton3.setText("三番目");
        } else {
            long id = maxId.longValue();
            if (id >= 0) {
                key1 = mRealm.where(Task.class).max("importance").longValue();
                results = mRealm.where(Task.class).equalTo("importance", key1).findAll();
                //task = mRealm.where(Task.class).max("importance");
                task = results.first();
                mButton1.setText(task.getSubject());
            } else {
                mButton1.setText("最優先");
            }
            if (id >= 1) {
                key2 = mRealm.where(Task.class).notEqualTo("id", key1).max("importance").longValue();
                results = mRealm.where(Task.class).equalTo("importance", key2).findAll();
                task = results.first();
                mButton2.setText(task.getSubject());
            } else {
                mButton2.setText("二番目");
            }
            if (id >= 2) {
                key3 = mRealm.where(Task.class).notEqualTo("id", key2)
                        .notEqualTo("id", key1)
                        .max("importance").longValue();
                results = mRealm.where(Task.class).equalTo("importance", key3).findAll();
                task = results.first();
                mButton3.setText(task.getSubject());
            } else {
                mButton3.setText("三番目");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    //ボタンクリック処理
    public void RegiSend_onClick(View v) {
        Intent i = new Intent(this, RegiActivity.class);
        startActivity(i);
    }
    public void ListSend_onClick(View v) {
        Intent i = new Intent(this, TaskListActivity.class);
        startActivity(i);
    }
}
