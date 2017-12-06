package com.muroigundan.task_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private Realm mRealm;
    private ListView mListView;
    Button mButton1;
    Button mButton2;
    Button mButton3;
    Button mButton4;
    Task task1;
    Task task2;
    Task task3;
    Task task4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mButton1 = (Button) findViewById(R.id.button1);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton3 = (Button) findViewById(R.id.button3);
        mButton4 = (Button) findViewById(R.id.button4);
        /*mButton1.setText("最優先");
        mButton2.setText("二番目");
        mButton3.setText("三番目");
        mButton4.setText("4番目");

        mButton4 = (Button) findViewById(R.id.button4);
        mButton1.setText("最優先");
        mButton2.setText("二番目");
        mButton3.setText("三番目");*/
        mButton1.setTypeface(Typeface.createFromAsset(getAssets(), "maruFont.otf"));
        mButton2.setTypeface(Typeface.createFromAsset(getAssets(), "maruFont.otf"));
        mButton3.setTypeface(Typeface.createFromAsset(getAssets(), "maruFont.otf"));
        mButton4.setTypeface(Typeface.createFromAsset(getAssets(), "maruFont.otf"));
    }

    @Override
    public void onStart() {
        super.onStart();

        mRealm = Realm.getDefaultInstance();
        ArrayList<Integer> prilist = attachPriority();

        mButton1 = (Button) findViewById(R.id.button1);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton3 = (Button) findViewById(R.id.button3);
        mButton4 = (Button) findViewById(R.id.button4);

        task1 = null;
        task2 = null;
        task3 = null;
        task4 = null;

        //RealmResults<Task> results = mRealm.where(Task.class).greaterThanOrEqualTo("date_and_time", new Date()).findAll();

        if (prilist.size() >= 1)
            task1 = mRealm.where(Task.class).equalTo("id", prilist.get(0)).findFirst();
        if (prilist.size() >= 2)
            task2 = mRealm.where(Task.class).equalTo("id", prilist.get(1)).findFirst();
        if (prilist.size() >= 3)
            task3 = mRealm.where(Task.class).equalTo("id", prilist.get(2)).findFirst();
        if (prilist.size() >= 4)
            task4 = mRealm.where(Task.class).equalTo("id", prilist.get(3)).findFirst();

        mButton1.setVisibility(View.VISIBLE);
        mButton2.setVisibility(View.VISIBLE);
        mButton3.setVisibility(View.VISIBLE);
        mButton4.setVisibility(View.VISIBLE);

        if (task1 != null) {
            show(task1,mButton1);
        } else {
            mButton1.setText("なんか予定登録しましょう！！");
            mButton1.setTypeface(Typeface.createFromAsset(getAssets(), "maruFont.otf"));
        }
        if (task2 != null) {
            show(task2, mButton2);
        } else {
            findViewById(R.id.sublayout1).setVisibility(View.GONE);
        }
        if (task3 != null) {
            show(task3, mButton3);
        } else {
            findViewById(R.id.sublayout2).setVisibility(View.GONE);
        }
        if (task4 != null) {
            show(task4, mButton4);
        } else {
            mButton4.setVisibility(View.GONE);
        }
    }

    private int time_limit(Task t) {
        double diff;
        long now = System.currentTimeMillis();
        diff = (t.getDate().getTime() - now);//じかんにおとしこむ;
        double before_1day = 24 * 60 * 60 * 1000;
        double before_3day = before_1day * 3;
        if (diff < before_1day) return 1;
        if (diff < before_3day) return 2;
        else return 3;
    }
    //文字・色の濃さ
    private void show(Task t, Button b) {
        b.setText(t.getSubject());
        int red = Color.red(t.getColor());
        int green = Color.green(t.getColor());
        int blue = Color.blue(t.getColor());
        if(blue > 0){
            b.setTextColor(Color.WHITE);
        }
        switch(time_limit(t)){
            case 1:
                b.setTextSize(75);
                b.setBackgroundColor(Color.argb(250, red, green, blue));
                break;
            case 2:
                b.setTextSize(40);
                b.setBackgroundColor(Color.argb(150, red, green, blue));
                break;
            case 3:
                b.setTextSize(15);
                b.setBackgroundColor(Color.argb(100, red, green, blue));
                break;
            default:
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
            startActivity(new Intent(this, RegiActivity.class));
        else
            startActivity(new Intent(this,  CheerActivity.class)
                    .putExtra("task_id", task1.getId()));
    }

    public void RegiSend_onClick2(View v) {
        mRealm.close();

        startActivity(new Intent(this,  CheerActivity.class)

                .putExtra("task_id", task2.getId()));
    }

    public void RegiSend_onClick3(View v) {
        mRealm.close();

        startActivity(new Intent(this,  CheerActivity.class)

                .putExtra("task_id", task3.getId()));
    }

    public void RegiSend_onClick4(View v) {
        mRealm.close();

        startActivity(new Intent(this,  CheerActivity.class)

                .putExtra("task_id", task4.getId()));
    }

    public void ListSend_onClick(View v) {
        mRealm.close();
        Intent i = new Intent(this, TaskListActivity.class);
        startActivity(i);
    }

    public ArrayList attachPriority() {
        TimeZone timezone = TimeZone.getTimeZone("Asia/Tokyo");
        Calendar calendar = Calendar.getInstance(timezone);

        Date nowTime = new Date(
                calendar.get(Calendar.YEAR) - 1900,
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND)
        );
        RealmResults<Task> tasks = mRealm.where(Task.class).greaterThanOrEqualTo("date_and_time", nowTime
        ).findAll();
        HashMap<Integer, Double> priorities = new HashMap<Integer, Double>();
        double imp;
        double diff;
        double priority;
        long now = System.currentTimeMillis() - 9 * 60 * 60 * 1000;
        for (Task t : tasks) {
            imp = t.getImportance();
            diff = (t.getDate_and_time().getTime() - now) / 100000;//じかんにおとしこむ;
            priority = imp / diff;
            if (priority > 0)
                priorities.put((int) t.getId(), priority);
        }
        ArrayList<Integer> rank = new ArrayList<Integer>();
        while (priorities.size() != 0) {
            double max = -Double.MAX_VALUE;
            int id_max = 0;
            for (Map.Entry<Integer, Double> i : priorities.entrySet()) {
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




    public void RegiSend_onClick(View v) {
        Intent i = new Intent(this, RegiActivity.class);
        startActivity(i);
    }
}

