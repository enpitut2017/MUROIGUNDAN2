package com.muroigundan.task_app;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import java.util.Calendar;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TimePicker;
import java.util.Locale;


public class myTimePicker extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar cal = Calendar.getInstance();
        return new TimePickerDialog(
                getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        EditText txtDate = (EditText) getActivity().findViewById(R.id.txtTime);
                        txtDate.setText(
                                String.format(Locale.JAPAN, "%02d:%02d", hourOfDay, minute));
                    }
                },
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
        );
    }
}