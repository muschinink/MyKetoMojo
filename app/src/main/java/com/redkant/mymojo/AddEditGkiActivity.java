package com.redkant.mymojo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.redkant.mymojo.MainActivity.ADD_MOJO_REQUEST;
import static com.redkant.mymojo.MainActivity.DELETE_GKI_REQUEST;
import static com.redkant.mymojo.MainActivity.EDIT_GKI_REQUEST;

public class AddEditGkiActivity extends AppCompatActivity {

    int mID = 0;
    EditText etCreateDate;
    EditText etCreateTime;
    NumberPicker mKetoneNumberPickerInt;
    NumberPicker mKetoneNumberPickerFloat;
    NumberPicker mGlucoseNumberPicker;
    EditText etNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_mojo);

        etCreateDate = (EditText)findViewById(R.id.etCreateDate);
        etCreateDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dFragment = new DatePickerFragment();
                dFragment.show(getSupportFragmentManager(), "Date Picker");
            }
        });

        etCreateTime = (EditText)findViewById(R.id.etCreateTime);
        etCreateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dFragment = new TimePickerFragment();
                dFragment.show(getSupportFragmentManager(), "Time Picker");
            }
        });

        mKetoneNumberPickerInt = (NumberPicker)findViewById(R.id.KetoneNumberPickerInt);
        mKetoneNumberPickerInt.setMinValue(0);
        mKetoneNumberPickerInt.setMaxValue(10);
        mKetoneNumberPickerInt.setWrapSelectorWheel(false);
        mKetoneNumberPickerInt.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        mKetoneNumberPickerFloat = (NumberPicker)findViewById(R.id.KetoneNumberPickerFloat);
        mKetoneNumberPickerFloat.setMinValue(0);
        mKetoneNumberPickerFloat.setMaxValue(9);
        mKetoneNumberPickerFloat.setWrapSelectorWheel(false);
        mKetoneNumberPickerFloat.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        mGlucoseNumberPicker = (NumberPicker)findViewById(R.id.GlucoseNumberPicker);
        mGlucoseNumberPicker.setMinValue(0);
        mGlucoseNumberPicker.setMaxValue(200);
        mGlucoseNumberPicker.setWrapSelectorWheel(false);

        etNote = (EditText)findViewById(R.id.etNote);

        int requestCode = getIntent().getIntExtra("requestCode", 0);

        if (requestCode == DELETE_GKI_REQUEST) {
            Intent answerIntent = new Intent();
            answerIntent.putExtra("ID", getIntent().getIntExtra("ID", 0));
            setResult(RESULT_OK, answerIntent);
            finish();
        }

        if (requestCode == EDIT_GKI_REQUEST) {

            mID = getIntent().getIntExtra("ID", 0);

            etCreateDate.setText(getIntent().getStringExtra("CREATE_DATE"));
            etCreateTime.setText(getIntent().getStringExtra("CREATE_TIME"));

            String[] ket = getIntent().getStringExtra("KETONE").trim().split("\\.");

            if (ket.length == 2) {
                mKetoneNumberPickerInt.setValue(Integer.valueOf(ket[0]));
                mKetoneNumberPickerFloat.setValue(Integer.valueOf(ket[1]));
            }
            else {
                mKetoneNumberPickerInt.setValue(0);
                mKetoneNumberPickerFloat.setValue(0);
            }

            mGlucoseNumberPicker.setValue(Integer.valueOf(getIntent().getStringExtra("GLUCOSE")));

            etNote.setText(getIntent().getStringExtra("NOTE"));
        }

        if (requestCode == ADD_MOJO_REQUEST) {
            etCreateDate.setText((new SimpleDateFormat("dd.MM.yyyy")).format(new Date()));
            etCreateTime.setText((new SimpleDateFormat("HH:mm")).format(new Date()));
            mGlucoseNumberPicker.setValue(75);
        }

        if (savedInstanceState != null) {
            mKetoneNumberPickerInt.setValue(savedInstanceState.getInt("KetoneInt"));
            mKetoneNumberPickerFloat.setValue(savedInstanceState.getInt("KetoneFloat"));
            mGlucoseNumberPicker.setValue(savedInstanceState.getInt("Glucose"));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("KetoneInt", mKetoneNumberPickerInt.getValue());
        outState.putInt("KetoneFloat", mKetoneNumberPickerFloat.getValue());
        outState.putInt("Glucose", mGlucoseNumberPicker.getValue());
    }

    public void bOKAddEditMojoClick(View view) {
        if (!checkValues()) {
            return;
        }

        Intent answerIntent = new Intent();

        answerIntent.putExtra("ID", mID);
        answerIntent.putExtra("CREATE_DATE", ((EditText)findViewById(R.id.etCreateDate)).getText().toString());
        answerIntent.putExtra("CREATE_TIME", ((EditText)findViewById(R.id.etCreateTime)).getText().toString());
        answerIntent.putExtra("KETONE", String.valueOf(mKetoneNumberPickerInt.getValue()) + "." + mKetoneNumberPickerFloat.getValue());
        answerIntent.putExtra("GLUCOSE", String.valueOf(mGlucoseNumberPicker.getValue()));
        answerIntent.putExtra("NOTE", ((EditText)findViewById(R.id.etNote)).getText().toString());

        setResult(RESULT_OK, answerIntent);
        finish();
    }

    private boolean checkValues() {
        if (mKetoneNumberPickerInt.getValue() == 0 && mKetoneNumberPickerFloat.getValue() == 0) {
            Toast.makeText(this, "Индекс кетонов не может быть равен 0.0", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
