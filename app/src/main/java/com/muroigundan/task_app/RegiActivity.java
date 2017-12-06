package com.muroigundan.task_app;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.DialogFragment;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;
import java.io.*;



public class RegiActivity extends AppCompatActivity {

    private Realm mRealm;
    EditText mSubjectEdit;
    EditText mDateEdit;
    EditText mTimeEdit;
    EditText mRemarksEdit;
    Button mSave;
    Button mDelete;
    SeekBar mSeekBar;

    private int notificationId = 0;
    AlarmManager alarm;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regi);

        mRealm = Realm.getDefaultInstance();
        mSubjectEdit = (EditText) findViewById(R.id.editText);
        mDateEdit = (EditText) findViewById(R.id.txtDate);
        /*// キーボード非表示処理
        mDateEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                EditText edittext = (EditText) v;

                // カーソル位置を退避
                int inStartSel = edittext.getSelectionStart();
                int inEndSel = edittext.getSelectionEnd();

                int inType = edittext.getInputType();       // Backup the input type
                edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                edittext.onTouchEvent(event);               // Call native handler
                edittext.setInputType(inType);              // Restore input type

                // カーソル位置を戻す
                edittext.setSelection(inStartSel,inEndSel);

                return true; // Consume touch event
            }
        });*/
        mTimeEdit = (EditText) findViewById(R.id.txtTime);
        /*mTimeEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                EditText edittext = (EditText) v;

                // カーソル位置を退避
                int inStartSel = edittext.getSelectionStart();
                int inEndSel = edittext.getSelectionEnd();

                int inType = edittext.getInputType();       // Backup the input type
                edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                edittext.onTouchEvent(event);               // Call native handler
                edittext.setInputType(inType);              // Restore input type

                // カーソル位置を戻す
                edittext.setSelection(inStartSel,inEndSel);


                return true; // Consume touch event
            }
        });*/
        mRemarksEdit = (EditText) findViewById(R.id.editText2);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mSave = (Button) findViewById(R.id.save);
        mDelete = (Button) findViewById(R.id.delete);


        long taskId = getIntent().getLongExtra("task_id", -1);
        if (taskId != -1) {
            RealmResults<Task> results = mRealm.where(Task.class)
                    .equalTo("id", taskId).findAll();
            Task task = results.first();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm");
            String date = sdf1.format(task.getDate());
            String time = sdf2.format(task.getTime());
            mDateEdit.setText(date);
            mTimeEdit.setText(time);
            mSubjectEdit.setText(task.getSubject());
            mRemarksEdit.setText(task.getRemarks());
            mSave.setText("保存");
            mDelete.setVisibility(View.VISIBLE);

            mSeekBar.setProgress(task.getImportance());
        } else {
            mDelete.setVisibility(View.INVISIBLE);
            mSave.setText("登録");
        }
    }

    public void onSaveTapped(View view) {

        //通知
        Intent bootIntent = new Intent(RegiActivity.this, NotificatReciver.class);
        bootIntent.putExtra("notificationId", notificationId);
        bootIntent.putExtra("todo", mSubjectEdit.getText());
        alarmIntent = PendingIntent.getBroadcast(RegiActivity.this, 0,
                bootIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        String tPicker_ym = mDateEdit.getText().toString();
        String tPicker_hm  =  mTimeEdit.getText().toString();
        String y = tPicker_ym.substring(0,4);
        String mon = tPicker_ym.substring(5,7);
        String d = tPicker_ym.substring(8,10);
        String h = tPicker_hm.substring(0,2);
        String m = tPicker_hm.substring(3,5);
        int Year = Integer.parseInt(y);
        int Month = Integer.parseInt(mon) -1;
        int Day = Integer.parseInt(d);
        int Hour = Integer.parseInt(h);
        int Minute = Integer.parseInt(m);


        Calendar setAl = Calendar.getInstance();
        setAl.set(Calendar.YEAR,Year);
        setAl.set(Calendar.MONTH,Month);
        setAl.set(Calendar.DATE,Day);

        setAl.set(Calendar.HOUR_OF_DAY, Hour);
        setAl.set(Calendar.MINUTE, Minute);
        setAl.set(Calendar.SECOND, 0);
        long alarmStartTime = setAl.getTimeInMillis();


        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("h:mm");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy/MM/dd h:mm");
        Date dateParse1 = new Date();
        Date dateParse2 = new Date();
        Date dateParse3 = new Date();
        try {
            dateParse1 = sdf1.parse(mDateEdit.getText().toString());
            dateParse2 = sdf2.parse(mTimeEdit.getText().toString());
            dateParse3 = sdf3.parse(mDateEdit.getText().toString()+" "+mTimeEdit.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Date date = dateParse1;
        final Date time = dateParse2;
        final Date date_and_time = dateParse3;


        if (mSubjectEdit.getText().toString().trim().length() == 0) {
            Snackbar.make(findViewById(android.R.id.content),
                    "件名を入力してください。", Snackbar.LENGTH_LONG)
                    .setAction("戻る", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //finish();
                        }
                    })
                    .setActionTextColor(Color.YELLOW)
                    .show();;
        } else if (mDateEdit.getText().toString().length() == 0 || mTimeEdit.getText().toString().length() == 0) {
            Snackbar.make(findViewById(android.R.id.content),
                    "日付、時刻を入力してください。", Snackbar.LENGTH_LONG)
                    .setAction("戻る", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //finish();
                        }
                    })
                    .setActionTextColor(Color.YELLOW)
                    .show();
        } else {

            long taskId = getIntent().getLongExtra("task_id", -1);
            if (taskId != -1) {
                final RealmResults<Task> results = mRealm.where(Task.class)
                        .equalTo("id", taskId).findAll();
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Task task = results.first();
                        task.setDate(date);
                        task.setTime(time);
                        task.setDate_and_time(date_and_time);
                        task.setSubject(mSubjectEdit.getText().toString());
                        task.setRemarks(mRemarksEdit.getText().toString());
                        task.setImportance(mSeekBar.getProgress());
                    }
                });
                Snackbar.make(findViewById(android.R.id.content),
                        "アップデートしました", Snackbar.LENGTH_LONG)
                        .setAction("戻る", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        })
                        .setActionTextColor(Color.YELLOW)
                        .show();
            } else {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Number maxId = realm.where(Task.class).max("id");
                        long nextId = 0;
                        if (maxId != null) nextId = maxId.longValue() + 1;
                        Task task = realm.createObject(Task.class, new Long(nextId));
                        task.setDate(date);
                        task.setTime(time);
                        task.setDate_and_time(date_and_time);
                        task.setSubject(mSubjectEdit.getText().toString());
                        task.setRemarks(mRemarksEdit.getText().toString());
                        task.setImportance(mSeekBar.getProgress());
                    }
                });
                //通知設定
                alarm.set(
                        AlarmManager.RTC_WAKEUP,
                        alarmStartTime,
                        alarmIntent
                );
                notificationId++;

                Toast.makeText(this, "追加しました ", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void onDeleteTapped(View view) {

        Snackbar.make(findViewById(android.R.id.content),
                "削除しますか？", Snackbar.LENGTH_LONG)
                .setAction("いいえ", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .setAction("はい",  new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final long taskId = getIntent().getLongExtra("task_id", -1);
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
                        //通知のキャンセル
                        alarm.cancel(alarmIntent);
                        finish();
                    }
                })
                .setActionTextColor(Color.YELLOW)
                .show();
    }

    public void regiSend_onClick(View v){
        finish();
    }

    public void date_onClick(View v) {
        DialogFragment dialog = new myDatePicker();
        dialog.show(getFragmentManager(), "dialog_basic");
    }

    public void time_onClick(View v) {
        DialogFragment dialog = new myTimePicker();
        dialog.show(getFragmentManager(), "dialog_basic");
    }
}
