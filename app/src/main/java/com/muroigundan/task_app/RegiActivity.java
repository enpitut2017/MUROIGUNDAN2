package com.muroigundan.task_app;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.DialogFragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import io.realm.Realm;


public class RegiActivity extends AppCompatActivity {
    private Realm mRealm;
    EditText mTitleEdit;
    EditText mDateEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regi);
        mRealm = Realm.getDefaultInstance();
        mTitleEdit = (EditText) findViewById(R.id.editText);
        mDateEdit = (EditText) findViewById(R.id.txtDate);
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
        mRealm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm) {
                Number maxId = realm.where(Task.class).max("id");
                long nextId = 0;
                if (maxId != null) nextId = maxId.longValue() + 1;
                Task task = realm.createObject(Task.class, new Long(nextId));
                task.setDate(date);
                task.setSubject(mTitleEdit.getText().toString());
            }
        });
        Toast.makeText(this, "追加しました", Toast.LENGTH_SHORT).show();
        finish();
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
