package com.muroigundan.task_app;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class CheerActivity extends AppCompatActivity {

    AlertDialog.Builder builder;
    Button Buttoncheer;
    Button startButton, stopButton;
    TextView timerText;

    Timer timer;
    CountUpTimerTask timerTask = null;
    Handler handler = new Handler();
    long count = 0;

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
        timerText = (TextView)findViewById(R.id.timer);
        timerText.setText("00:00.0");

        // タイマー開始
        startButton = (Button)findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // タイマーが走っている最中にボタンをタップされたケース
                if(null != timer){
                    timer.cancel();
                    timer = null;
                }

                // Timer インスタンスを生成
                timer = new Timer();

                // TimerTask インスタンスを生成
                timerTask = new CountUpTimerTask();

                // スケジュールを設定 100msec
                // public void schedule (TimerTask task, long delay, long period)
                timer.schedule(timerTask, 0, 100);

                // カウンター
                count = 0;
                timerText.setText("00:00.0");

            }
        });

        // タイマー終了
        stopButton = (Button)findViewById(R.id.stop_button);
        stopButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(null != timer){
                    // Cancel
                    timer.cancel();
                    timer = null;
                    timerText.setText("00:00.0");
                }
            }
        });
    }

    class CountUpTimerTask extends TimerTask {
        @Override
        public void run() {
            // handlerを使って処理をキューイングする
            handler.post(new Runnable() {
                public void run() {
                    count++;
                    long mm = count*100 / 1000 / 60;
                    long ss = count*100 / 1000 % 60;
                    long ms = (count*100 - ss * 1000 - mm * 1000 * 60)/100;
                    // 桁数を合わせるために02d(2桁)を設定
                    timerText.setText(String.format("%1$02d:%2$02d.%3$01d", mm, ss, ms));
                }
            });
        }

    }

    public void cheer_btn(View v) {
        Random rnd = new Random();
        String[] msg = getResources().getStringArray(R.array.message);
        int r = rnd.nextInt(msg.length);
        builder.setMessage(msg[r]);
        builder.show();
    }
}
