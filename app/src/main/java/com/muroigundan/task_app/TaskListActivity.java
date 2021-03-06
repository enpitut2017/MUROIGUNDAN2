package com.muroigundan.task_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmResults;

public class TaskListActivity extends AppCompatActivity {
    private Realm mRealm;
    private ListView mListView;
    Button button7;
    private int state = 0;
    RealmResults<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        mRealm = Realm.getDefaultInstance();
        button7 = (Button)findViewById( R.id.button7);

        mListView = (ListView) findViewById(R.id.listView);
        //RealmResults<Task> tasks = mRealm.where(Task.class).findAll();
        // 過去のタスクを除いたリストを取得
        TimeZone timezone = TimeZone.getTimeZone("Asia/Tokyo");
        Calendar calendar = Calendar.getInstance(timezone);

        Date now = new Date(
            calendar.get(Calendar.YEAR) - 1900,
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND)
        );
        // Date now = new Date(String.valueOf(timezone));

        tasks = mRealm.where(Task.class).greaterThanOrEqualTo("date_and_time", now).findAll();
        button7.setText("過去のタスクを表示する");
        // 今後、重要度重み付けによってソート予定
        tasks = tasks.sort("date_and_time");

        SchedulerAdapter adapter = new SchedulerAdapter(tasks);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = (Task) parent.getItemAtPosition(position);
                startActivity(new Intent(TaskListActivity.this,  CheerActivity.class)
                        .putExtra("task_id", task.getId()));
            }
        });
    }

    public void hyouji_onClick(View v){
        TimeZone timezone = TimeZone.getTimeZone("Asia/Tokyo");
        Calendar calendar = Calendar.getInstance(timezone);

        Date now = new Date(
                calendar.get(Calendar.YEAR) - 1900,
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND)
        );
        //RealmResults<Task> tasks;
        if(state == 0) {
            tasks = mRealm.where(Task.class).greaterThanOrEqualTo("date_and_time", now).findAll();
            button7.setText("〆切の過ぎたタスクを表示する");
            state = 1;
        }else{
            tasks = mRealm.where(Task.class).lessThanOrEqualTo("date_and_time", now).findAll();
            button7.setText("現在のタスクを表示する");
            state = 0;
        }
        // 今後、重要度重み付けによってソート予定
        tasks = tasks.sort("date_and_time");

        SchedulerAdapter adapter = new SchedulerAdapter(tasks);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = (Task) parent.getItemAtPosition(position);
                startActivity(new Intent(TaskListActivity.this,  CheerActivity.class)
                        .putExtra("task_id", task.getId()));
            }
        });
    }

    public void ListSend_onClick(View v){
        finish();
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
}

