package com.redkant.mymojo;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.redkant.mymojo.db.Body;
import com.redkant.mymojo.db.MojoDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BodyMeasurements extends Fragment {

    private static RecyclerView.Adapter sBodyAdapter;
    private static MojoDatabaseHelper db;
    private static List<Body> sBodyList;
    SharedPreferences mPreferences;

    public BodyMeasurements() {
        // Required empty public constructor
    }

    public static MojoDatabaseHelper getDb() {
        return db;
    }

    public static List<Body> getBodyList() {
        return sBodyList;
    }

    public static RecyclerView.Adapter getBodyAdapter() {
        return sBodyAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_bodymeasurements, container, false);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        sBodyList = new ArrayList<>();
        db = new MojoDatabaseHelper(getContext());

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, Integer.parseInt("-" + mPreferences.getInt("DaysCount", 7)));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);
        db.getBodyFiltered(sBodyList, "create_date >= '" + df.format(c.getTime()) + "'");

        sBodyAdapter = new BodyAdapter(getContext(), sBodyList);

        RecyclerView recyclerView = rootView.findViewById(R.id.rvBody);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(sBodyAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }
}
