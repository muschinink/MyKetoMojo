package com.redkant.mymojo;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.redkant.mymojo.db.Body;
import java.util.List;

public class BodyAdapter extends RecyclerView.Adapter<BodyAdapter.ViewHolder> {

    private List<Body> mDataset;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvBodyCreateDate;
        public TextView tvBodyWeight;
        public TextView tvBodyArmHighLeft;
        public TextView tvBodyArmHighRight;
        public TextView tvBodyArmLowLeft;
        public TextView tvBodyArmLowRight;
        public TextView tvBodyChest;
        public TextView tvBodyChestUnder;
        public TextView tvBodyWaist;
        public TextView tvBodyHipsHigh;
        public TextView tvBodyHips;
        public TextView tvBodyThighLeft;
        public TextView tvBodyThighRight;
        public TextView tvBodyCalfLeft;
        public TextView tvBodyCalfRight;

        public CardView cvBodyRow;

        public ViewHolder(View v) {
            super(v);

            tvBodyCreateDate = v.findViewById(R.id.tvBodyCreateDate);
            tvBodyWeight = v.findViewById(R.id.tvBodyWeight);
            tvBodyArmHighLeft = v.findViewById(R.id.tvBodyArmHighLeft);
            tvBodyArmHighRight = v.findViewById(R.id.tvBodyArmHighRight);
            tvBodyArmLowLeft = v.findViewById(R.id.tvBodyArmLowLeft);
            tvBodyArmLowRight = v.findViewById(R.id.tvBodyArmLowRight);
            tvBodyChest = v.findViewById(R.id.tvBodyChest);
            tvBodyChestUnder = v.findViewById(R.id.tvBodyChestUnder);
            tvBodyWaist = v.findViewById(R.id.tvBodyWaist);
            tvBodyHipsHigh = v.findViewById(R.id.tvBodyHipsHigh);
            tvBodyHips = v.findViewById(R.id.tvBodyHips);
            tvBodyThighLeft = v.findViewById(R.id.tvBodyThighLeft);
            tvBodyThighRight = v.findViewById(R.id.tvBodyThighRight);
            tvBodyCalfLeft = v.findViewById(R.id.tvBodyCalfLeft);
            tvBodyCalfRight = v.findViewById(R.id.tvBodyCalfRight);

            cvBodyRow = v.findViewById(R.id.cvBodyRow);
        }

        public void bind(final Body item) {
            tvBodyCreateDate.setText(item.getCreateDate());

            tvBodyWeight.setText(String.valueOf(item.getWeight()));
            tvBodyArmHighLeft.setText(String.valueOf(item.getArmHighLeft()));
            tvBodyArmHighRight.setText(String.valueOf(item.getArmHighRight()));
            tvBodyArmLowLeft.setText(String.valueOf(item.getArmLowLeft()));
            tvBodyArmLowRight.setText(String.valueOf(item.getArmLowRight()));
            tvBodyChest.setText(String.valueOf(item.getChest()));
            tvBodyChestUnder.setText(String.valueOf(item.getChestUnder()));
            tvBodyWaist.setText(String.valueOf(item.getWaist()));
            tvBodyHipsHigh.setText(String.valueOf(item.getHipsHigh()));
            tvBodyHips.setText(String.valueOf(item.getHips()));
            tvBodyThighLeft.setText(String.valueOf(item.getThighLeft()));
            tvBodyThighRight.setText(String.valueOf(item.getThighRight()));
            tvBodyCalfLeft.setText(String.valueOf(item.getCalfLeft()));
            tvBodyCalfRight.setText(String.valueOf(item.getCalfRight()));
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BodyAdapter(Context context, List<Body> myDataset) {
        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.body_recycler_view_row, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final int pos = position;

        holder.bind(mDataset.get(position));

        holder.cvBodyRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(context);
                ad.setTitle(R.string.warning_title);
                ad.setMessage(R.string.warning_delete_row);

                ad.setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
/*
                        Intent intent = new Intent(context, AddEditBodyActivity.class);
                        intent.putExtra("requestCode", DELETE_BODY_REQUEST);
                        intent.putExtra("ID", mDataset.get(pos).getID());

                        ((Activity) context).startActivityForResult(intent, DELETE_BODY_REQUEST);
*/
                    }
                });

                ad.setNegativeButton(R.string.btn_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                // СЂР°Р·СЂРµС€Р°РµРј РЅР°Р¶Р°С‚РёРµ РєРЅРѕРїРєРё Back
                ad.setCancelable(true);

                ad.show();

                // РґР°Р»СЊС€Рµ РѕС‚РєР»РёРє РЅРµ РїРѕСЃС‹Р»Р°РµРј
                return true;
            }
        });

        holder.cvBodyRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                Intent intent = new Intent(context, AddEditBodyActivity.class);
                intent.putExtra("requestCode", EDIT_BODY_REQUEST);
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
*/
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
