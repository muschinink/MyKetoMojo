package com.redkant.mymojo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;


public class FragmentChart extends Fragment {

    private LineChart mLineChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chart, null);

        mLineChart = (LineChart)v.findViewById(R.id.chart);

        ArrayList<String> xAXES = new ArrayList<>();
        List<Entry> yAXESsin = new ArrayList<>();
        List<Entry> yAXEScos = new ArrayList<>();

        double x = 0;
        int numDataPoints = 1000;

        for (int i=0; i < numDataPoints; i++) {
            float sinFunction = Float.parseFloat(String.valueOf(Math.sin(x)));
            float cosFunction = Float.parseFloat(String.valueOf(Math.cos(x)));
            x = x + 0.1;

            yAXESsin.add(new Entry(i, sinFunction));
            yAXEScos.add(new Entry(i, cosFunction));
            xAXES.add(i, String.valueOf(x));
        }

        String[] xaxes = new String[xAXES.size()];

        for (int i = 0; i < xAXES.size(); i++) {
            xaxes[i] = xAXES.get(i);
        }

        LineDataSet lineDataSet1 = new LineDataSet(yAXEScos, "cos");
        lineDataSet1.setColor(Color.BLUE);

        LineDataSet lineDataSet2 = new LineDataSet(yAXESsin, "sin");
        lineDataSet2.setColor(Color.RED);
        List<ILineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet2);

        LineData lineData = new LineData(lineDataSets);
        mLineChart.setData(lineData);

        mLineChart.setVisibleXRangeMaximum(20f);
        mLineChart.invalidate();


//        return super.onCreateView(inflater, container, savedInstanceState);
        return v;
    }
}
