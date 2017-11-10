package com.muroigundan.task_app;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmMigration;
import io.realm.RealmResults;
import io.realm.Sort;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {
    private Realm mRealm;
    private ListView mListView;
    Button mButton1;
    Button mButton2;
    Button mButton3;
    Task task1;
    Task task2;
    Task task3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRealm = Realm.getDefaultInstance();

        mButton1 = (Button) findViewById(R.id.button1);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton3 = (Button) findViewById(R.id.button3);

        RealmResults<Task> results = mRealm.where(Task.class).findAll();
        results = results.sort("importance", Sort.DESCENDING);
        List<Task> ListResults = mRealm.copyFromRealm(results);

        if (ListResults.size() == 0) {
            mButton1.setText("最優先");
            mButton2.setText("二番目");
            mButton3.setText("三番目");
        } else {
            if (ListResults.size() >= 1) {
                task1 = ListResults.get(0);
                mButton1.setText(task1.getSubject());
            } else {
                mButton1.setText("最優先");
            }
            if (ListResults.size() >= 2) {
                task2 = ListResults.get(1);
                mButton2.setText(task2.getSubject());
            } else {
                mButton2.setText("二番目");
            }
            if (ListResults.size() >= 3) {
                task3 = ListResults.get(2);
                mButton3.setText(task3.getSubject());
            } else {
                mButton3.setText("三番目");
            }
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        mRealm = Realm.getDefaultInstance();

        mButton1 = (Button) findViewById(R.id.button1);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton3 = (Button) findViewById(R.id.button3);

        RealmResults<Task> results = mRealm.where(Task.class).findAll();
        results = results.sort("importance", Sort.DESCENDING);
        List<Task> ListResults = mRealm.copyFromRealm(results);
        if (ListResults.size() == 0) {
            mButton1.setText("最優先");
            mButton2.setText("二番目");
            mButton3.setText("三番目");
        } else {
            if (ListResults.size() >= 1) {
                task1 = ListResults.get(0);
                mButton1.setText(task1.getSubject());
            } else {
                mButton1.setText("最優先");
            }
            if (ListResults.size() >= 2) {
                task2 = ListResults.get(1);
                mButton2.setText(task2.getSubject());
            } else {
                mButton2.setText("二番目");
            }
            if (ListResults.size() >= 3) {
                task3 = ListResults.get(2);
                mButton3.setText(task3.getSubject());
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
    public void RegiSend_onClick1(View v) {
        /*Intent i = new Intent(this, RegiActivity.class);
        startActivity(i);*/
            startActivity(new Intent(this,  RegiActivity.class)
                    .putExtra("task_id", task1.getId()));
    }
    public void RegiSend_onClick2(View v) {
        /*Intent i = new Intent(this, RegiActivity.class);
        startActivity(i);*/
        startActivity(new Intent(this,  RegiActivity.class)
                .putExtra("task_id", task2.getId()));
    }
    public void RegiSend_onClick3(View v) {
        /*Intent i = new Intent(this, RegiActivity.class);
        startActivity(i);*/
        startActivity(new Intent(this,  RegiActivity.class)
                .putExtra("task_id", task3.getId()));
    }

    public void ListSend_onClick(View v) {
        Intent i = new Intent(this, TaskListActivity.class);
        startActivity(i);
    }

}
