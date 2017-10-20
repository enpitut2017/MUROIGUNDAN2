package com.muroigundan.task_app;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.VectorEnabledTintResources;
import android.view.View;

import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private Realm mRealm;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRealm = Realm.getDefaultInstance();

        mListView = (ListView) findViewById(R.id.listView);
        RealmResults<Task> tasks = mRealm.where(Task.class).findAll();
        SchedulerAdapter adapter = new SchedulerAdapter(tasks);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
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
