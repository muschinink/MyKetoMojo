package com.redkant.mymojo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;

import com.redkant.mymojo.db.Mojo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<Mojo> mojoList = new ArrayList<>();
    private ArrayAdapter<Mojo> listViewAdapter;
    private RecyclerView mMojoRecyclerView;
    private RecyclerView.Adapter mMojoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MojoDatabaseHelper db = new MojoDatabaseHelper(this);
        db.createDefaultMojoIfNeed();

        List<Mojo> list =  db.getAllNotes();
        this.mojoList.addAll(list);



        mMojoRecyclerView = findViewById(R.id.rvMojo);
        mMojoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mMojoAdapter = new MojoAdapter(this, list);
        mMojoRecyclerView.setAdapter(mMojoAdapter);

    }
}
