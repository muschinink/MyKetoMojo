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

import com.redkant.mymojo.db.Gki;
import com.redkant.mymojo.db.MojoDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class GkiMeasurements extends Fragment {

    private static RecyclerView.Adapter sGkiAdapter;
    private static MojoDatabaseHelper db;
    private static List<Gki> sGkiList;
    SharedPreferences mPreferences;
    private static FragmentChart sChartFragment;

    public GkiMeasurements() {
        // Required empty public constructor
    }

    public static FragmentChart getChartFragment() {
        return sChartFragment;
    }

    public static MojoDatabaseHelper getDb() {
        return db;
    }

    public static List<Gki> getGkiList() {
        return sGkiList;
    }

    public static RecyclerView.Adapter getGkiAdapter() {
        return sGkiAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gkimeasurements, container, false);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        sGkiList = new ArrayList<>();
        db = new MojoDatabaseHelper(getContext());

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, Integer.parseInt("-" + mPreferences.getInt("DaysCount", 7)));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);
        db.getGkiFiltered(sGkiList, "create_date >= '" + df.format(c.getTime()) + "'");

        sGkiAdapter = new GkiAdapter(getContext(), sGkiList);

        RecyclerView recyclerView = rootView.findViewById(R.id.rvGki);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(sGkiAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        sChartFragment = (FragmentChart)getChildFragmentManager().findFragmentById(R.id.frChart);
        if (sChartFragment != null) {
            sChartFragment.setData(sGkiList);
        }

        return rootView;
    }

}
