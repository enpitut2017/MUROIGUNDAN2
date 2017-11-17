package com.muroigundan.task_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

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

        mRealm = Realm.getDefaultInstance();
        ArrayList<Integer> prilist = attachPriority();

        mButton1 = (Button) findViewById(R.id.button1);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton3 = (Button) findViewById(R.id.button3);
        task1 = null;
        task2 = null;
        task3 = null;

        RealmResults<Task> results = mRealm.where(Task.class).greaterThanOrEqualTo("date_and_time", new Date()).findAll();

        if (prilist.size() >= 1)
            task1 = mRealm.where(Task.class).equalTo("id", prilist.get(0)).findFirst();
        if (prilist.size() >= 2)
            task2 = mRealm.where(Task.class).equalTo("id", prilist.get(1)).findFirst();;
        if (prilist.size() >= 3)
            task3 = mRealm.where(Task.class).equalTo("id", prilist.get(2)).findFirst();;

        mButton1.setVisibility(View.VISIBLE);
        mButton2.setVisibility(View.VISIBLE);
        mButton3.setVisibility(View.VISIBLE);

        if (task1 != null) {
            mButton1.setText(task1.getSubject());
        } else{
            mButton1.setText("なんか予定登録しろ！！");
        }
        if (task2 != null) {
            mButton2.setText(task2.getSubject());
        } else {
            mButton2.setVisibility(View.GONE);
        }
        if (task3 != null) {
            mButton3.setText(task3.getSubject());
        } else {
            mButton3.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    //ボタンクリック処理
    public void RegiSend_onClick1(View v) {
        mRealm.close();
        if (task1 == null)
            startActivity(new Intent(this,  RegiActivity.class));
        else
            startActivity(new Intent(this,  RegiActivity.class)
                    .putExtra("task_id", task1.getId()));
    }
    public void RegiSend_onClick2(View v) {
        mRealm.close();
        startActivity(new Intent(this,  RegiActivity.class)
                .putExtra("task_id", task2.getId()));
    }
    public void RegiSend_onClick3(View v) {
        mRealm.close();
        startActivity(new Intent(this,  RegiActivity.class)
                .putExtra("task_id", task3.getId()));
    }

    public void ListSend_onClick(View v) {
        mRealm.close();
        Intent i = new Intent(this, TaskListActivity.class);
        startActivity(i);
    }

    public ArrayList attachPriority() {
        Date nowTime = new Date();
        //RealmResults<Task> tasks = mRealm.where(Task.class).findAll();
        RealmResults<Task> tasks = mRealm.where(Task.class).greaterThanOrEqualTo("date_and_time", new Date()).findAll();
        HashMap<Integer, Double> priorities = new HashMap<Integer, Double>();
        double imp;
        double diff;
        double priority;
        long now = System.currentTimeMillis();
        for (Task t : tasks) {
            imp = t.getImportance();
            diff = (t.getDate().getTime() - now) / 100000;//じかんにおとしこむ;

            priority = imp / diff;
            if(priority>0)
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
    public void CheerPage_onClick(View v) {
        Intent i = new Intent(this, CheerActivity.class);
        startActivity(i);
    }

}
