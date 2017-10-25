package com.muroigundan.task_app;

import android.app.Dialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.app.TimePickerDialog;
import android.widget.EditText;
import android.widget.Spinner;

import io.realm.Realm;

import static com.muroigundan.task_app.R.id.design_menu_item_text;
import static com.muroigundan.task_app.R.id.spinner3;

public class RegiActivity extends AppCompatActivity {
    private Realm mRealm;
    EditText mTitleEdit;
    Spinner mSpinner3, mSpinner5;
    String Month, Day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regi);
        mRealm = Realm.getDefaultInstance();
        mTitleEdit = (EditText) findViewById(R.id.editText);
        Spinner mSpinner3 = (Spinner) findViewById(R.id.spinner3);
        Spinner mSpinner5 = (Spinner) findViewById(R.id.spinner5);
    }

    public void onSaveTapped(View view) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date dateParse = new Date();
        try {
            Month = (String)mSpinner3.getSelectedItem();
            Day = (String)mSpinner5.getSelectedItem();
            dateParse = sdf.parse(Month + '/' + Day);
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
                task.setDeadline(date);
                task.setSubject(mTitleEdit.getText().toString());
            }
        });

    }

    public void regiSend_onClick(View v){
        finish();
    }
}
