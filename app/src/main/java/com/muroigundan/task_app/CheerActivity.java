package com.muroigundan.task_app;

import android.app.AlarmManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

import io.realm.Realm;

public class CheerActivity extends AppCompatActivity {
    private Realm mRealm;
    AlarmManager alarm;
    long taskId;
    int state = 1;
    ImageView imageView;
    Button trans1;
    Button trans2;

    AlertDialog.Builder builder;
    Button Buttoncheer;

    RegiActivity ra = new RegiActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheer);

        mRealm = Realm.getDefaultInstance();
        imageView = (ImageView)findViewById(R.id.imageView);
        trans1 = (Button)findViewById(R.id.trans1);
        trans2 = (Button)findViewById(R.id.trans2);

        builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // ボタンをクリックしたときの動作
            }
        });

        taskId = getIntent().getLongExtra("task_id", -1);
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
            imageView.setImageResource(R.drawable.doing);
            trans2.setVisibility(View.VISIBLE);
            trans1.setText("中断");
            trans2.setText("完了");
            state = 2;
        } else if (state == 2) {
            imageView.setImageResource(R.drawable.suspend);
            trans2.setVisibility(View.VISIBLE);
            trans1.setText("再開");
            trans2.setVisibility(View.GONE);
            state = 3;
        } else if (state == 3) {
            imageView.setImageResource(R.drawable.doing);
            trans2.setVisibility(View.VISIBLE);
            trans1.setText("中断");
            trans2.setText("完了");
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
            imageView.setImageResource(R.drawable.done);
            trans1.setVisibility(View.VISIBLE);
            trans2.setVisibility(View.GONE);
            trans1.setText("タスク削除");
            state = 4;
        }
    }

}
