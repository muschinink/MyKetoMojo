package com.redkant.mymojo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditMojoActivity extends AppCompatActivity {

    EditText etCreateDate;
    EditText etCreateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_mojo);

        EditText etDate = (EditText)findViewById(R.id.etCreateDate);
        etDate.setText((new SimpleDateFormat("dd.MM.yyyy")).format(new Date()));

        EditText etTime = (EditText)findViewById(R.id.etCreateTime);
        etTime.setText((new SimpleDateFormat("HH:mm")).format(new Date()));

        etCreateTime = (EditText)findViewById(R.id.etCreateTime);
        etCreateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dFragment = new TimePickerFragment();
                dFragment.show(getSupportFragmentManager(), "Time Picker");
            }
        });

        etCreateDate = (EditText)findViewById(R.id.etCreateDate);
        etCreateDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dFragment = new DatePickerFragment();
                dFragment.show(getSupportFragmentManager(), "Date Picker");
            }
        });

    }

    public void bOKAddEditMojoClick(View view) {
        Intent answerIntent = new Intent();

        answerIntent.putExtra("CREATE_DATE", ((EditText)findViewById(R.id.etCreateDate)).getText().toString());
        answerIntent.putExtra("CREATE_TIME", ((EditText)findViewById(R.id.etCreateTime)).getText().toString());
        answerIntent.putExtra("KETONE", ((EditText)findViewById(R.id.etKetone)).getText().toString());
        answerIntent.putExtra("GLUCOSE", ((EditText)findViewById(R.id.etGlucose)).getText().toString());
        answerIntent.putExtra("WEIGHT", ((EditText)findViewById(R.id.etWeight)).getText().toString());

        setResult(RESULT_OK, answerIntent);
        finish();
    }

    public void bCancelAddEditMojoClick(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public static class DatePickerFragment extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year, month, day;

            EditText et = (EditText)getActivity().findViewById(R.id.etCreateDate);

            Date d;
            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

            if (!et.getText().equals("")) {
                try {
                    d = df.parse(et.getText().toString());
                    c.setTime(d);
                } catch (ParseException e) {
                    Log.i("***", et.getText().toString());
                }
            }

            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        // called when a date has been selected
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(year, month, day, 0, 0, 0);
            Date chosenDate = cal.getTime();

            EditText et = (EditText)getActivity().findViewById(R.id.etCreateDate);
            et.setText((new SimpleDateFormat("dd.MM.yyyy")).format(chosenDate));

        }
    }

    public static class TimePickerFragment extends AppCompatDialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour, minute;

            EditText et = (EditText)getActivity().findViewById(R.id.etCreateTime);
            Date d;
            SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

            if (!et.getText().equals("")) {
                try {
                    d = df.parse(et.getText().toString());
                    c.setTime(d);
                } catch (ParseException e) {
                    Log.i("***", et.getText().toString());
                }
            }

            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(),this,hour,minute,true);

        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
            EditText et = (EditText)getActivity().findViewById(R.id.etCreateTime);
            et.setText(String.format("%02d",hourOfDay) + ":" + String.format("%02d",minute));
        }
    }
}
