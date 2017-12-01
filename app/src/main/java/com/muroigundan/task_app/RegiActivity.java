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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    Spinner mSpinner;

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
        mTimeEdit = (EditText) findViewById(R.id.txtTime);
        mRemarksEdit = (EditText) findViewById(R.id.editText2);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mSave = (Button) findViewById(R.id.save);
        mDelete = (Button) findViewById(R.id.delete);
        mSpinner = (Spinner) findViewById(R.id.spinner);

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
            int color = 0;
            switch (task.getColor()) {
                case Color.RED: color = 0; break;
                case Color.BLUE: color = 1; break;
                case Color.GREEN: color = 2; break;
                case Color.YELLOW: color = 3; break;
            }
            mSpinner.setSelection(color);
            //mSpinner.setBackgroundColor(task.getColor());
            mSeekBar.setProgress(task.getImportance());
        } else {
            mDelete.setVisibility(View.INVISIBLE);
            mSave.setText("登録");
        }
    }

    public void onSaveTapped(View view) {

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
            int year = Integer.parseInt(y);
            int month = Integer.parseInt(mon);
            int day = Integer.parseInt(d);
            int hour = Integer.parseInt(h);
            int minute = Integer.parseInt(m);


            Calendar setAl = Calendar.getInstance();
            //setAl.set(Calendar.YEAR,year);
            //setAl.set(Calendar.MONTH,month);
            //setAl.set(Calendar.DAY_OF_MONTH,day);
            //setAl.set(year,month,day);
            setAl.set(Calendar.HOUR_OF_DAY, hour);
            setAl.set(Calendar.MINUTE, minute);
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
                        String str = (String)mSpinner.getSelectedItem();
                        int color = 0;
                        switch (str) {
                            case "Red": color = Color.RED; break;
                            case "Green": color = Color.GREEN; break;
                            case "Blue": color = Color.BLUE; break;
                            case "Yellow": color = Color.YELLOW; break;
                            default:
                        }
                        task.setColor(color);
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
                        String str = (String)mSpinner.getSelectedItem();
                        int color = 0;
                        switch (str) {
                            case "Red": color = Color.RED; break;
                            case "Green": color = Color.GREEN; break;
                            case "Blue": color = Color.BLUE; break;
                            case "Yellow": color = Color.YELLOW; break;
                            default:
                        }
                        task.setColor(color);
                    }
                });
                alarm.set(
                        AlarmManager.RTC_WAKEUP,
                        alarmStartTime,
                        alarmIntent
                );
                notificationId++;

                Toast.makeText(this, "追加しました" , Toast.LENGTH_SHORT).show();
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
