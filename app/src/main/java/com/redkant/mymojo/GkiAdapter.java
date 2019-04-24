package com.redkant.mymojo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.redkant.mymojo.db.Gki;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.redkant.mymojo.MainActivity.DELETE_GKI_REQUEST;
import static com.redkant.mymojo.MainActivity.EDIT_GKI_REQUEST;

public class GkiAdapter extends RecyclerView.Adapter<GkiAdapter.ViewHolder> {

    private List<Gki> mDataset;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvCreateDate;
        public TextView tvKetoNumber;
        public TextView tvGlucoseNumber;
        public TextView tvGKI;

        public CardView cvGkiRow;

        public ViewHolder(View v) {
            super(v);

            tvCreateDate = v.findViewById(R.id.tvBodyCreateDate);
            tvKetoNumber = v.findViewById(R.id.tvKetoNumber);
            tvGlucoseNumber = v.findViewById(R.id.tvGlucoseNumber);
            tvGKI = v.findViewById(R.id.tvGKI);

            cvGkiRow = v.findViewById(R.id.cvGkiRow);
        }

        public void bind(final Gki item) {
            tvCreateDate.setText(item.getCreateDate());
            tvKetoNumber.setText(Float.toString(item.getKetoNumber()));
            tvGlucoseNumber.setText(Float.toString(item.getGlucoseNumber()));

            float gki = item.getGlucoseNumber()/18/item.getKetoNumber();
            float f = new BigDecimal(gki).setScale(1, RoundingMode.HALF_EVEN).floatValue();
            tvGKI.setText(Float.toString(f));
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GkiAdapter(Context context, List<Gki> myDataset) {
        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gki_recycler_view_row, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final int pos = position;

        holder.bind(mDataset.get(position));

        holder.cvGkiRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(context);
                ad.setTitle(R.string.warning_title);
                ad.setMessage(R.string.warning_delete_row);

                ad.setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, AddEditGkiActivity.class);
                        intent.putExtra("requestCode", DELETE_GKI_REQUEST);
                        intent.putExtra("ID", mDataset.get(pos).getID());

                        ((Activity) context).startActivityForResult(intent, DELETE_GKI_REQUEST);
                    }
                });

                ad.setNegativeButton(R.string.btn_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                // разрешаем нажатие кнопки Back
                ad.setCancelable(true);

                ad.show();

                // дальше отклик не посылаем
                return true;
            }
        });

        holder.cvGkiRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddEditGkiActivity.class);
                intent.putExtra("requestCode", EDIT_GKI_REQUEST);
                intent.putExtra("ID", mDataset.get(pos).getID());

                Date d = null;
                try {
                    d = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(mDataset.get(pos).getCreateDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                intent.putExtra("CREATE_DATE", new SimpleDateFormat("dd.MM.yyyy").format(d));
                intent.putExtra("CREATE_TIME", new SimpleDateFormat("HH:mm").format(d));
                intent.putExtra("KETONE", String.valueOf(mDataset.get(pos).getKetoNumber()));
                intent.putExtra("GLUCOSE", String.valueOf(mDataset.get(pos).getGlucoseNumber()));
                intent.putExtra("NOTE", String.valueOf(mDataset.get(pos).getNote()));

                ((Activity) context).startActivityForResult(intent, EDIT_GKI_REQUEST);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
