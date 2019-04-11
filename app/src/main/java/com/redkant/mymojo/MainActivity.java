package com.redkant.mymojo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.redkant.mymojo.db.Mojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mMojoRecyclerView;
    private RecyclerView.Adapter mMojoAdapter;

    private MojoDatabaseHelper db;

    private static final int ADD_MOJO_REQUEST = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditMojoActivity.class);
                startActivityForResult(intent, ADD_MOJO_REQUEST);
            }
        });

        db = new MojoDatabaseHelper(this);

        mMojoRecyclerView = findViewById(R.id.rvMojo);
        mMojoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        RefreshDB();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_MOJO_REQUEST && resultCode == RESULT_OK) {
            // insert data to DB
            MojoDatabaseHelper db = new MojoDatabaseHelper(this);

            try {
                Date d;

                SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH);
                d = df.parse(data.getStringExtra("CREATE_DATE") + " " + data.getStringExtra("CREATE_TIME"));

                Mojo mojo = new Mojo();
                mojo.setCreateDate((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss")).format(d));
                mojo.setKetoNumber(Float.parseFloat(data.getStringExtra("KETONE")));
                mojo.setGlucoseNumber(Float.parseFloat(data.getStringExtra("GLUCOSE")));
                mojo.setWeight(Float.parseFloat(data.getStringExtra("WEIGHT")));
                db.addMojo(mojo);

                RefreshDB();

            } catch (ParseException e) {
                Log.i("***", "error");
            }

        }
    }

    private void RefreshDB() {
        List<Mojo> list =  db.getAllMojo();
        mMojoAdapter = new MojoAdapter(this, list);
        mMojoRecyclerView.setAdapter(mMojoAdapter);
    }
}
