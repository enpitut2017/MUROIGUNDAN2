package com.muroigundan.task_app;


import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmResults;

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
    Intent intent;

   // private int notificationId = 0;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    //SharedPreferences.Editor e = pref.edit();

    AlarmManager alarm;
    private PendingIntent alarmIntent;
    AlarmManager alarm2;
    private PendingIntent alarmIntent2;
    AlarmManager alarm3;
    private PendingIntent alarmIntent3;
    private int color_id;

    AlertDialog.Builder builder_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regi);
        //pref.edit().putInt("notificationId", 0);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        mRealm = Realm.getDefaultInstance();
        mSubjectEdit = (EditText) findViewById(R.id.editText);
        mDateEdit = (EditText) findViewById(R.id.txtDate);
        mTimeEdit = (EditText) findViewById(R.id.txtTime);
        mRemarksEdit = (EditText) findViewById(R.id.editText2);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mSave = (Button) findViewById(R.id.save);
        mDelete = (Button) findViewById(R.id.delete);
        mSpinner = (Spinner) findViewById(R.id.spinner);
        intent = new Intent(this, MainActivity.class);
        long taskId = getIntent().getLongExtra("task_id", -1);
        if (taskId != -1) {
            RealmResults<Task> results = mRealm.where(Task.class)
                    .equalTo("id", taskId).findAll();
            Task task = results.first();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("kk:mm");
            String date = sdf1.format(task.getDate());
            String time = sdf2.format(task.getTime());
            mDateEdit.setText(date);
            mTimeEdit.setText(time);
            mSubjectEdit.setText(task.getSubject());
            mRemarksEdit.setText(task.getRemarks());
            mSave.setText("保存");
            mDelete.setVisibility(View.VISIBLE);


            //String[] task_color = getResources().getStringArray(R.array.spinner_items);
            int[] color_List = {Color.RED, Color.GREEN, Color.BLUE,Color.YELLOW};
            int color_id = -1;
            for (int i = 0; i < 4; i++) {
                if (task.getColor() == color_List[i])
                    color_id = i;
            }

            mSpinner.setSelection(color_id);


            //mSpinner.setBackgroundColor(task.getColor());
            mSeekBar.setProgress(task.getImportance());
        } else {
            mDelete.setVisibility(View.INVISIBLE);
            mSave.setText("登録");
        }

        builder_delete = new AlertDialog.Builder(this);
        builder_delete.setPositiveButton("はい", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // ボタンをクリックしたときの動作
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
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //通知のキャンセル
                AlarmManager am = (AlarmManager) getSystemService(getApplicationContext().ALARM_SERVICE);
                Intent i = new Intent(getApplicationContext(),NotificatReciver.class);
                PendingIntent p = PendingIntent.getBroadcast(getApplicationContext(),(int)taskId+100000,
                        i, PendingIntent.FLAG_ONE_SHOT);
                am.cancel(p);
                AlarmManager am2 = (AlarmManager) getSystemService(getApplicationContext().ALARM_SERVICE);
                Intent i2 = new Intent(getApplicationContext(),NotificatReciver.class);
                PendingIntent p2 = PendingIntent.getBroadcast(getApplicationContext(),(int)taskId+200000,
                        i2, PendingIntent.FLAG_ONE_SHOT);
                am.cancel(p2);
                AlarmManager am3 = (AlarmManager) getSystemService(getApplicationContext().ALARM_SERVICE);
                Intent i3 = new Intent(getApplicationContext(),NotificatReciver.class);
                PendingIntent p3 = PendingIntent.getBroadcast(getApplicationContext(),(int)taskId+300000,
                        i3, PendingIntent.FLAG_ONE_SHOT);
                am.cancel(p3);
                //alarmIntent.cancel();
            }
        });
        builder_delete.setNegativeButton("いいえ", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {

            }});
        builder_delete.setMessage("タスクを削除しますか？");
    }

    public void onSaveTapped(View view) {
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
            Toast.makeText(this, "件名を入力してください", Toast.LENGTH_SHORT).show();
            /*Snackbar.make(findViewById(android.R.id.content),
                    "件名を入力してください。", Snackbar.LENGTH_LONG)
                    .setAction("戻る", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //finish();
                        }
                    })
                    .setActionTextColor(Color.YELLOW)
                    .show();*/
        } else if (mDateEdit.getText().toString().length() == 0 || mTimeEdit.getText().toString().length() == 0) {
            Toast.makeText(this, "日付、時刻を入力してください", Toast.LENGTH_SHORT).show();
            /*Snackbar.make(findViewById(android.R.id.content),
                    "日付、時刻を入力してください。", Snackbar.LENGTH_LONG)
                    .setAction("戻る", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //finish();
                        }
                    })
                    .setActionTextColor(Color.YELLOW)
                    .show();*/
        } else if(date_and_time.getTime() - nowTime.getTime() < 0){
            Toast.makeText(this, "期日が過ぎています。", Toast.LENGTH_SHORT).show();

        } else {
            long taskId = getIntent().getLongExtra("task_id", -1);


            String tPicker_ym = mDateEdit.getText().toString();
            String tPicker_hm = mTimeEdit.getText().toString();
            String y = tPicker_ym.substring(0,4);
            String mon = tPicker_ym.substring(5,7);
            String d = tPicker_ym.substring(8,10);
            String h = tPicker_hm.substring(0,2);
            String m = tPicker_hm.substring(3,5);
            int year = Integer.parseInt(y);
            int month = Integer.parseInt(mon)-1;
            int day = Integer.parseInt(d);
            int hour = Integer.parseInt(h);
            int minute = Integer.parseInt(m);

            Date date_now = new Date();
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
                        String[] task_color = getResources().getStringArray(R.array.spinner_items);
                        color_id = -1;
                        for (int i = 0; i < 4; i++) {
                            if (str.equals(task_color[i]))
                                color_id = i;
                        }
                        switch (color_id) {
                            case 0: color = Color.RED; break;
                            case 1: color = Color.GREEN; break;
                            case 2: color = Color.BLUE; break;
                            case 3: color = Color.YELLOW; break;
                            default:
                        }
                        task.setColor(color);
                    }
                });
                Toast.makeText(this, "更新しました", Toast.LENGTH_SHORT).show();
                /*Snackbar.make(findViewById(android.R.id.content),
                        "アップデートしました", Snackbar.LENGTH_LONG)
                        .setAction("戻る", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        })
                        .setActionTextColor(Color.YELLOW)
                        .show();*/
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
                        //task.setNotiId(int(nextId));
                        task.setDate_and_time(date_and_time);
                        task.setSubject(mSubjectEdit.getText().toString());
                        task.setRemarks(mRemarksEdit.getText().toString());
                        task.setImportance(mSeekBar.getProgress());
                        String str = (String)mSpinner.getSelectedItem();
                        int color = 0;
                        String[] task_color = getResources().getStringArray(R.array.spinner_items);
                        int color_id = -1;
                        for (int i = 0; i < 4; i++) {
                            if (str.equals(task_color[i]))
                                color_id = i;
                        }
                        switch (color_id) {
                            case 0: color = Color.RED; break;
                            case 1: color = Color.GREEN; break;
                            case 2: color = Color.BLUE; break;
                            case 3: color = Color.YELLOW; break;
                            default:
                        }

                        task.setColor(color);
                    }
                });

                //通知
                Number noti_id = mRealm.where(Task.class).max("id");
                String S =  mSubjectEdit.getText()+"の期日が過ぎました、、、";
                Intent bootIntent = new Intent(RegiActivity.this, NotificatReciver.class);
                bootIntent.putExtra("notificationId", pref.getInt("NotificationID",0));
                bootIntent.putExtra("todo", S);
                alarmIntent = PendingIntent.getBroadcast(RegiActivity.this, noti_id.intValue()+100000,
                        bootIntent, PendingIntent.FLAG_ONE_SHOT);
                alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                Calendar setAl = Calendar.getInstance();
                setAl.set(Calendar.YEAR,year);
                setAl.set(Calendar.MONTH,month);
                setAl.set(Calendar.DAY_OF_MONTH,day);
                setAl.set(Calendar.HOUR_OF_DAY, hour);
                setAl.set(Calendar.MINUTE, minute);
                setAl.set(Calendar.SECOND, 0);
                long alarmStartTime = setAl.getTimeInMillis();
                alarm.set(
                        AlarmManager.RTC_WAKEUP,
                        alarmStartTime,
                        alarmIntent
                );
                int day2 = day-1;
                year = year-1900;
                Date date_set2 = new Date(year,month,day2,hour,minute,0);
                int iii = date_set2.compareTo(date_now);
                if(date_set2.compareTo(date_now)==1) {
                    String S2 = mSubjectEdit.getText() + "の期日が1日後になっています!!";
                    Intent bootIntent2 = new Intent(RegiActivity.this, NotificatReciver.class);
                    bootIntent2.putExtra("notificationId", pref.getInt("NotificationID", 0));
                    bootIntent2.putExtra("todo", S2);
                    alarmIntent2 = PendingIntent.getBroadcast(RegiActivity.this, noti_id.intValue() + 200000,
                            bootIntent2, PendingIntent.FLAG_ONE_SHOT);
                    alarm2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Calendar setAl2 = Calendar.getInstance();
                    setAl2.set(Calendar.YEAR, year);
                    setAl2.set(Calendar.MONTH, month);
                    setAl2.set(Calendar.DAY_OF_MONTH, day2);
                    setAl2.set(Calendar.HOUR_OF_DAY, hour);
                    setAl2.set(Calendar.MINUTE, minute);
                    setAl2.set(Calendar.SECOND, 0);
                    long alarmStartTime2 = setAl2.getTimeInMillis();
                    alarm2.set(
                            AlarmManager.RTC_WAKEUP,
                            alarmStartTime2,
                            alarmIntent2
                    );
                }
                int day3 = day-3;
                Date date_set3 = new Date(year,month,day3,hour,minute,0);
                if(date_set3.compareTo(date_now)!=-1) {
                    String S3 = mSubjectEdit.getText() + "の期日が3日後になっています!!";
                    Intent bootIntent3 = new Intent(RegiActivity.this, NotificatReciver.class);
                    bootIntent3.putExtra("notificationId", pref.getInt("NotificationID", 0));
                    bootIntent3.putExtra("todo", S3);
                    alarmIntent3 = PendingIntent.getBroadcast(RegiActivity.this, noti_id.intValue() + 300000,
                            bootIntent3, PendingIntent.FLAG_ONE_SHOT);
                    alarm3 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Calendar setAl3 = Calendar.getInstance();
                    setAl3.set(Calendar.YEAR, year);
                    setAl3.set(Calendar.MONTH, month);
                    setAl3.set(Calendar.DAY_OF_MONTH, day3);
                    setAl3.set(Calendar.HOUR_OF_DAY, hour);
                    setAl3.set(Calendar.MINUTE, minute);
                    setAl3.set(Calendar.SECOND, 0);
                    long alarmStartTime3 = setAl3.getTimeInMillis();
                    alarm3.set(
                            AlarmManager.RTC_WAKEUP,
                            alarmStartTime3,
                            alarmIntent3
                    );
                }
                editor.putInt("NotificationID",pref.getInt("NotificationID",0)+1).commit();
                Toast.makeText(this, "追加しました" , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void onDeleteTapped(View view) {

        /*Snackbar.make(findViewById(android.R.id.content),
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
                        //alarm.cancel(alarmIntent);
                        finish();
                    }
                })
                .setActionTextColor(Color.YELLOW)
                .show();*/
        builder_delete.show();
        //finish();
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
