package com.muroigundan.task_app;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.app.TimePickerDialog;

import io.realm.Realm;

public class RegiActivity extends AppCompatActivity {
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regi);
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
