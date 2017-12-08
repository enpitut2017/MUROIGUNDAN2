package com.muroigundan.task_app;

import android.app.AlarmManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Random;

import io.realm.Realm;

public class CheerActivity extends AppCompatActivity {
    private Realm mRealm;
    AlarmManager alarm;
    long taskId;
    int state = 1;
    ImageButton imageButton;
    Button trans1;
    Button trans2;
    TextView title_text;
    TextView deadline_text;
    TextView tap_text;

    AlertDialog.Builder builder;
    Button Buttoncheer;

    RegiActivity ra = new RegiActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheer);

        mRealm = Realm.getDefaultInstance();
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        trans1 = (Button)findViewById(R.id.trans1);
        trans2 = (Button)findViewById(R.id.trans2);
        title_text = (TextView)findViewById(R.id.title_text);
        deadline_text = (TextView)findViewById(R.id.deadline_text);
        tap_text = (TextView)findViewById(R.id.tap_text);

        builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // ボタンをクリックしたときの動作
            }
        });
        builder.setTitle("クマからの伝言");

        taskId = getIntent().getLongExtra("task_id", -1);
    }

    public void onStart() {
        super.onStart();
        Task task = mRealm.where(Task.class).equalTo("id", taskId).findFirst();
        title_text.setText(task.getSubject());
        /*deadline_text.setText(new SimpleDateFormat("締切：yyyy年MM月dd日 ").format(task.getDate())
                + (new SimpleDateFormat("hh時mm分").format(task.getTime())));*/
        deadline_text.setText(new SimpleDateFormat("締切：yyyy年MM月dd日 ").format(task.getDate())
                + (new SimpleDateFormat("kk:mm").format(task.getTime())));

    }

    public void cheer_btn(View v){
        Random rnd = new Random();
        String[] msg = getResources().getStringArray(R.array.message);
        int r = rnd.nextInt(msg.length);
        builder.setMessage(msg[r]);
        builder.show();
    }

    public void detail_btn(View v){
        startActivity(new Intent(this,  RegiActivity.class)
                .putExtra("task_id", taskId));
    }

    public void trans1_tap(View v) {
        if (state == 1) {
            imageButton.setImageResource(R.drawable.doing);
            trans2.setVisibility(View.VISIBLE);
            trans1.setText("休憩する");
            trans2.setText("タスク終わりました");
            state = 2;
        } else if (state == 2) {
            imageButton.setImageResource(R.drawable.suspend);
            trans2.setVisibility(View.VISIBLE);
            trans1.setText("作業を再開する");
            trans2.setVisibility(View.GONE);
            state = 3;
        } else if (state == 3) {
            imageButton.setImageResource(R.drawable.doing);
            trans2.setVisibility(View.VISIBLE);
            trans1.setText("休憩する");
            trans2.setText("タスク終わりました");
            state = 2;
        } else if (state == 4) {
            if (taskId != -1) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Task task = realm.where(Task.class)
                                .equalTo("id", taskId).findFirst();
                        task.deleteFromRealm();
                    }
                });
            }
            finish();
        }
    }

    public void trans2_tap(View v){
        if(state == 2){
            imageButton.setImageResource(R.drawable.done);
            trans1.setVisibility(View.VISIBLE);
            trans2.setVisibility(View.GONE);
            trans1.setText("タスクを削除する");
            state = 4;
        }
    }

}
