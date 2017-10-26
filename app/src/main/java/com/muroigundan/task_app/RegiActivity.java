package com.muroigundan.task_app;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.DialogFragment;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;

import java.text.SimpleDateFormat;

public class RegiActivity extends AppCompatActivity {
    private Realm mRealm;
    EditText mSubjectEdit;
    EditText mDateEdit;
    EditText mRemarksEdit;
    Button mSave;
    Button mDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regi);
        mRealm = Realm.getDefaultInstance();
        mSubjectEdit = (EditText) findViewById(R.id.editText);
        mDateEdit = (EditText) findViewById(R.id.txtDate);
        mRemarksEdit = (EditText) findViewById(R.id.editText2);

        mSave = (Button) findViewById(R.id.save);
        mDelete = (Button) findViewById(R.id.delete);


        long taskId = getIntent().getLongExtra("task_id", -1);
        if (taskId != -1) {
            RealmResults<Task> results = mRealm.where(Task.class)
                    .equalTo("id", taskId).findAll();
            Task task = results.first();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String date = sdf.format(task.getDate());
            mDateEdit.setText(date);
            mSubjectEdit.setText(task.getSubject());
            mRemarksEdit.setText(task.getRemarks());
            mSave.setText("保存");
            mDelete.setVisibility(View.VISIBLE);
        } else {
            mDelete.setVisibility(View.INVISIBLE);
            mSave.setText("登録");
        }
    }

    public void onSaveTapped(View view) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date dateParse = new Date();
        try {
            dateParse = sdf.parse(mDateEdit.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Date date = dateParse;

        long taskId = getIntent().getLongExtra("task_id", -1);
        if (taskId != -1) {
            final RealmResults<Task> results = mRealm.where(Task.class)
                    .equalTo("id", taskId).findAll();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Task task = results.first();
                    task.setDate(date);
                    task.setSubject(mSubjectEdit.getText().toString());
                    task.setRemarks(mSubjectEdit.getText().toString());
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
                    task.setSubject(mSubjectEdit.getText().toString());
                    task.setRemarks(mRemarksEdit.getText().toString());
                }
            });
            Toast.makeText(this, "追加しました", Toast.LENGTH_SHORT).show();
            finish();
        }
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
