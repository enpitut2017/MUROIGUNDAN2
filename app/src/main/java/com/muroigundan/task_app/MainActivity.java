package com.muroigundan.task_app;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

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
        RealmResults<Task> results = mRealm.where(Task.class).findAll();
        results = results.sort("importance", Sort.DESCENDING);
        List<Task> ListResults = mRealm.copyFromRealm(results);
        ArrayList<Integer> prilist = attachPriority();
        if (ListResults.size() == 0) {
            mButton1.setText("最優先");
            mButton2.setText("二番目");
            mButton3.setText("三番目");
        } else {
            if (ListResults.size() >= 1) {
                Task task1 = ListResults.get(prilist.get(0));
                mButton1.setText(task1.getSubject());
            } else {
                mButton1.setText("最優先");
            }
            if (ListResults.size() >= 2) {
                Task task2 = ListResults.get(prilist.get(1));
                mButton2.setText(task2.getSubject());
            } else {
                mButton2.setText("二番目");
            }
            if (ListResults.size() >= 3) {
                Task task3 = ListResults.get(prilist.get(0));
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
    public void RegiSend_onClick(View v) {

        Intent i = new Intent(this, RegiActivity.class);
        startActivity(i);
    }

    public void ListSend_onClick(View v) {
        Intent i = new Intent(this, TaskListActivity.class);
        startActivity(i);
    }

    public ArrayList attachPriority() {
        RealmResults<Task> tasks = mRealm.where(Task.class).findAll();
        HashMap<Integer, Float> priorities = new HashMap<Integer, Float>();
        int imp;
        float diff;
        float priority;
        long now = System.currentTimeMillis();
        for (Task t : tasks) {
            imp = t.getImportance();
            diff = t.getDate().getTime() - now;//じかんにおとしこむ;
            priority = (float) (imp * 1 / Math.sqrt(diff));
            priorities.put((int) t.getId(), priority);
        }
        ArrayList<Integer> rank = new ArrayList<Integer>();
        while(priorities.size() != 0){
            float max = 0;
            int id_max = 0;
            for (int i = 0; i < priorities.size(); i++) {
                if (max < priorities.get(i)) {
                    max = priorities.get(i);
                    id_max = i;
                }
            }
            priorities.remove(id_max);
            rank.add(id_max);
        }
        return rank;
    }
}
