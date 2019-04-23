package com.redkant.mymojo;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.redkant.mymojo.db.Mojo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class GKIMeasurements extends Fragment {

    private static RecyclerView.Adapter mMojoAdapter;
    private static MojoDatabaseHelper db;
    private static List<Mojo> listMojo;
    SharedPreferences mPreferences;
    private static FragmentChart mChartFragment;

    public GKIMeasurements() {
        // Required empty public constructor
    }

    public static FragmentChart getmChartFragment() {
        return mChartFragment;
    }

    public static MojoDatabaseHelper getDb() {
        return db;
    }

    public static List<Mojo> getListMojo() {
        return listMojo;
    }

    public static RecyclerView.Adapter getmMojoAdapter() {
        return mMojoAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gkimeasurements, container, false);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        listMojo = new ArrayList<>();
        db = new MojoDatabaseHelper(getContext());

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, Integer.parseInt("-" + mPreferences.getInt("DaysCount", 7)));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);
        db.getFilteredMojo(listMojo, "create_date >= '" + df.format(c.getTime()) + "'");

        mMojoAdapter = new MojoAdapter(getContext(), listMojo);

        RecyclerView recyclerView = rootView.findViewById(R.id.rvMojo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mMojoAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mChartFragment = (FragmentChart)getChildFragmentManager().findFragmentById(R.id.frChart);
        if (mChartFragment != null) {
            mChartFragment.setData(listMojo);
        }

        return rootView;
    }

}
