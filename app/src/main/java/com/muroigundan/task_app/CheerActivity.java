package com.muroigundan.task_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Random;

public class CheerActivity extends AppCompatActivity {
    long taskId;

    AlertDialog.Builder builder;
    Button Buttoncheer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheer);
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
}
