package com.redkant.mymojo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.redkant.mymojo.db.Mojo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.redkant.mymojo.MainActivity.DELETE_MOJO_REQUEST;
import static com.redkant.mymojo.MainActivity.EDIT_MOJO_REQUEST;

public class MojoAdapter extends RecyclerView.Adapter<MojoAdapter.ViewHolder> {

    private List<Mojo> mDataset;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvCreateDate;
        public TextView tvKetoNumber;
        public TextView tvSugarNumber;
        public TextView tvGKI;

        public CardView cvMojoRow;

        public ViewHolder(View v) {
            super(v);

            tvCreateDate = v.findViewById(R.id.tvCreateDate);
            tvKetoNumber = v.findViewById(R.id.tvKetoNumber);
            tvSugarNumber = v.findViewById(R.id.tvSugarNumber);
            tvGKI = v.findViewById(R.id.tvGKI);

            cvMojoRow = v.findViewById(R.id.cvMojoRow);
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
        this.context = context;
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
        final int pos = position;

        holder.bind(mDataset.get(position));

        holder.cvMojoRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(context);
                ad.setTitle("Предупреждение");
                ad.setMessage("Вы действительно хотите удалить запись?");

                ad.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, AddEditMojoActivity.class);
                        intent.putExtra("requestCode", DELETE_MOJO_REQUEST);
                        intent.putExtra("ID", mDataset.get(pos).getID());

                        ((Activity) context).startActivityForResult(intent, DELETE_MOJO_REQUEST);
                    }
                });

                ad.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
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

        holder.cvMojoRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddEditMojoActivity.class);
                intent.putExtra("requestCode", EDIT_MOJO_REQUEST);
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
                intent.putExtra("WEIGHT", String.valueOf(mDataset.get(pos).getWeight()));

                ((Activity) context).startActivityForResult(intent, EDIT_MOJO_REQUEST);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
