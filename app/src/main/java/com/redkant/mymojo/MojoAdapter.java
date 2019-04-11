package com.redkant.mymojo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.redkant.mymojo.db.Mojo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class MojoAdapter extends RecyclerView.Adapter<MojoAdapter.ViewHolder> {
    private List<Mojo> mDataset;

    public class ViewHolder extends RecyclerView.ViewHolder  {
        // each data item is just a string in this case
        public TextView tvCreateDate;
        public TextView tvKetoNumber;
        public TextView tvSugarNumber;
        public TextView tvGKI;

        public ViewHolder(View v) {
            super(v);
            tvCreateDate = v.findViewById(R.id.tvCreateDate);
            tvKetoNumber = v.findViewById(R.id.tvKetoNumber);
            tvSugarNumber = v.findViewById(R.id.tvSugarNumber);
            tvGKI = v.findViewById(R.id.tvGKI);
        }

        public void bind(final Mojo item) {
            tvCreateDate.setText(item.getCreateDate());
            tvKetoNumber.setText(Float.toString(item.getKetoNumber()));
            tvSugarNumber.setText(Float.toString(item.getGlucoseNumber()));

            float gki = item.getGlucoseNumber()/18/item.getKetoNumber();
            float f = new BigDecimal(gki).setScale(1, RoundingMode.HALF_EVEN).floatValue();
            tvGKI.setText(Float.toString(f));
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MojoAdapter(Context context, List<Mojo> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mojo_recycler_view_row, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bind(mDataset.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
