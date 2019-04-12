package com.redkant.mymojo;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

public class MojoBarDataSet extends BarDataSet {
    public MojoBarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
    }

    @Override
    public int getColor(int index) {
        if (getEntryForIndex(index).getY() <= 1f)
            return mColors.get(0);
        else if (getEntryForIndex(index).getY() < 3f)
            return mColors.get(1);
        else if (getEntryForIndex(index).getY() < 6f)
            return mColors.get(2);
        else if (getEntryForIndex(index).getY() < 9f)
            return mColors.get(3);
        else
            return mColors.get(4);

    }
}
