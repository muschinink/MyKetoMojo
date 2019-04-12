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

import com.redkant.mymojo.db.Mojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mMojoRecyclerView;
    public RecyclerView.Adapter mMojoAdapter;

    private MojoDatabaseHelper db;
    public List<Mojo> listMojo;

    private FragmentChart mChartFragment;

    public static final int ADD_MOJO_REQUEST = 1000;
    public static final int EDIT_MOJO_REQUEST = 1001;
    public static final int DELETE_MOJO_REQUEST = 1002;

/*
    public void EditMojoRequest() {
        Intent intent = new Intent(MainActivity.this, AddEditMojoActivity.class);
        startActivityForResult(intent, EDIT_MOJO_REQUEST);
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditMojoActivity.class);
                intent.putExtra("requestCode", ADD_MOJO_REQUEST);
                startActivityForResult(intent, ADD_MOJO_REQUEST);
            }
        });

        db = new MojoDatabaseHelper(this);

        mMojoRecyclerView = findViewById(R.id.rvMojo);
        mMojoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        listMojo = new ArrayList<>();
        db.getAllMojo(listMojo);
        mMojoAdapter = new MojoAdapter(this, listMojo);
        mMojoRecyclerView.setAdapter(mMojoAdapter);

        // fill chart
        mChartFragment = (FragmentChart)getSupportFragmentManager().findFragmentById(R.id.frChart);
        mChartFragment.setData(listMojo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        MojoDatabaseHelper db = new MojoDatabaseHelper(this);

        Mojo mojo = new Mojo();
        mojo.setID(data.getIntExtra("ID", 0));

        if (requestCode == ADD_MOJO_REQUEST || requestCode == EDIT_MOJO_REQUEST) {
            try {
                SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH);
                Date d = df.parse(data.getStringExtra("CREATE_DATE") + " " + data.getStringExtra("CREATE_TIME"));
                mojo.setCreateDate((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss")).format(d));

                mojo.setKetoNumber(Float.parseFloat(data.getStringExtra("KETONE")));
                mojo.setGlucoseNumber(Float.parseFloat(data.getStringExtra("GLUCOSE")));
                mojo.setWeight(Float.parseFloat(data.getStringExtra("WEIGHT")));

                if (requestCode == EDIT_MOJO_REQUEST && mojo.getID() != 0) {
                    db.updateMojo(mojo);
                }

                if (requestCode == ADD_MOJO_REQUEST) {
                    db.addMojo(mojo);
                }
            } catch (ParseException e) {
                Log.i("***", "error");
            }
        }

        if (requestCode == DELETE_MOJO_REQUEST && mojo.getID() != 0) {
            db.deleteMojo(mojo);
        }

        db.getAllMojo(listMojo);
        mMojoAdapter.notifyDataSetChanged();

        mChartFragment.setData(listMojo);
    }

}
