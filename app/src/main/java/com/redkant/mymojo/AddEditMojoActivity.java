package com.redkant.mymojo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.redkant.mymojo.MainActivity.ADD_MOJO_REQUEST;
import static com.redkant.mymojo.MainActivity.DELETE_MOJO_REQUEST;
import static com.redkant.mymojo.MainActivity.EDIT_MOJO_REQUEST;

public class AddEditMojoActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    int mID = 0;
    EditText etCreateDate;
    EditText etCreateTime;
    EditText etKetone;
    EditText etGlucose;
    EditText etWeight;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_mojo);

        etCreateDate = (EditText)findViewById(R.id.etCreateDate);
        etCreateTime = (EditText)findViewById(R.id.etCreateTime);
        etKetone = (EditText) findViewById(R.id.etKetone);
        etGlucose = (EditText) findViewById(R.id.etGlucose);
        etWeight = (EditText) findViewById(R.id.etWeight);

        int requestCode = getIntent().getIntExtra("requestCode", 0);

        if (requestCode == DELETE_MOJO_REQUEST) {
            Intent answerIntent = new Intent();
            answerIntent.putExtra("ID", getIntent().getIntExtra("ID", 0));
            setResult(RESULT_OK, answerIntent);
            finish();
        }

        if (requestCode == EDIT_MOJO_REQUEST) {

            mID = getIntent().getIntExtra("ID", 0);

            etCreateDate.setText(getIntent().getStringExtra("CREATE_DATE"));
            etCreateTime.setText(getIntent().getStringExtra("CREATE_TIME"));
            etKetone.setText(getIntent().getStringExtra("KETONE"));
            etGlucose.setText(getIntent().getStringExtra("GLUCOSE"));
            etWeight.setText(getIntent().getStringExtra("WEIGHT"));
        }

        if (requestCode == ADD_MOJO_REQUEST) {
            etCreateDate.setText((new SimpleDateFormat("dd.MM.yyyy")).format(new Date()));
            etCreateDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment dFragment = new DatePickerFragment();
                    dFragment.show(getSupportFragmentManager(), "Date Picker");
                }
            });

            etCreateTime.setText((new SimpleDateFormat("HH:mm")).format(new Date()));
            etCreateTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment dFragment = new TimePickerFragment();
                    dFragment.show(getSupportFragmentManager(), "Time Picker");
                }
            });

            etKetone.setText("0");
            etGlucose.setText("0");
            etWeight.setText("0");
        }

        final SeekBar ketoneSeekBar = (SeekBar)findViewById(R.id.sbKetone);
        ketoneSeekBar.setProgress((int)(Float.valueOf(etKetone.getText().toString())*10));
        ketoneSeekBar.setOnSeekBarChangeListener(this);


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float f = progress / 10f;
        etKetone.setText(String.valueOf(f));

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public void bOKAddEditMojoClick(View view) {
        Intent answerIntent = new Intent();

        answerIntent.putExtra("ID", mID);
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
