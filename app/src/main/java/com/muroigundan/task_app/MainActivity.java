package com.muroigundan.task_app;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private Realm mRealm;
    Button mButton1;
    Button mButton2;
    Button mButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRealm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_main);

        mButton1 = (Button) findViewById(R.id.button1);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton3 = (Button) findViewById(R.id.button3);
        mButton1.setText("最優先");
        mButton2.setText("二番目");
        mButton3.setText("三番目");
    }

    @Override
    public void onStart(){
        super.onStart();

        ArrayList<Integer> prilist = attachPriority();

        mButton1 = (Button) findViewById(R.id.button1);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton3 = (Button) findViewById(R.id.button3);

        RealmResults<Task> results = mRealm.where(Task.class).findAll();
       // results = results.sort("importance", Sort.DESCENDING);
        List<Task> ListResults = mRealm.copyFromRealm(results);
        if (prilist.size() >= 1) {
            Task task1 = ListResults.get(prilist.get(0));
            mButton1.setText(task1.getSubject());
        }if (ListResults.size() >= 2) {
            Task task2 = ListResults.get(prilist.get(1));
            mButton2.setText(task2.getSubject());
        }if (ListResults.size() >= 3) {
            Task task3 = ListResults.get(prilist.get(2));
            mButton3.setText(task3.getSubject());
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
        HashMap<Integer, Double> priorities = new HashMap<Integer, Double>();
        double imp;
        double diff;
        double priority;
        long now = System.currentTimeMillis();
        for (Task t : tasks) {
            imp = t.getImportance();
            diff = (t.getDate().getTime() - now) / 100000;//じかんにおとしこむ;

            priority = imp / diff;
            System.out.println(priority);
            priorities.put((int) t.getId(), priority);
        }
        ArrayList<Integer> rank = new ArrayList<Integer>();
        while(priorities.size() != 0){
            double max = -Double.MAX_VALUE;
            int id_max = 0;
            for (Map.Entry<Integer, Double> i: priorities.entrySet()) {
                if (max < i.getValue()) {
                    max = i.getValue();
                    id_max = i.getKey();
                }
            }
            priorities.remove(id_max);
            rank.add(id_max);
        }
        return rank;
    }
}
