package com.redkant.mymojo;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.redkant.mymojo.db.Mojo;

import java.util.ArrayList;
import java.util.List;


public class FragmentChart extends Fragment {

    public BarChart mChart;
    private View mViewFragment;

    public void setData(List<Mojo> list) {
        List<BarEntry> yVals = new ArrayList<>();

        for (int i = list.size() - 1; i >= 0; i--) {
            float gki = list.get(i).getGlucoseNumber()/18/list.get(i).getKetoNumber();
            yVals.add(new BarEntry(list.size()-i, gki));
        }

        MojoBarDataSet dataSet = new MojoBarDataSet(yVals, "");

        dataSet.setColors(new int[]{ContextCompat.getColor(getContext(), R.color.level_highest_therapeutic),
                                    ContextCompat.getColor(getContext(), R.color.level_high_therapeutic),
                                    ContextCompat.getColor(getContext(), R.color.level_moderate),
                                    ContextCompat.getColor(getContext(), R.color.level_low),
                                    ContextCompat.getColor(getContext(), R.color.level_not_in_ketosis)});

        dataSet.setDrawValues(true);

        BarData barData = new BarData(dataSet);

        mChart.setData(barData);
        mChart.invalidate();
        mChart.animateY(500);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mViewFragment = inflater.inflate(R.layout.fragment_chart, null);

        mChart = (BarChart) mViewFragment.findViewById(R.id.chart);

        mChart.getDescription().setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawLabels(false);

//        return super.onCreateView(inflater, container, savedInstanceState);
        return mViewFragment;
    }
}
