package com.redkant.mymojo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEditMojoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_mojo);

        EditText et = (EditText)findViewById(R.id.etCreateDate);

        et.setText((new SimpleDateFormat("dd.MM.yyyy HH:mm")).format(new Date()));

    }
}
