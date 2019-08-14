package edu.uta.leanmed.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import edu.uta.leanmed.activities.R;
import edu.uta.leanmed.pojo.Donor;
import edu.uta.leanmed.pojo.Inventory;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.ViewHolder> {
    private Context mContext;
    private List<Donor> donorList;
    private DonorAdapter.OnItemClickListener mItemClickListener;

    public DonorAdapter(Context mContext, List<Donor> donorList){
        this.mContext = mContext;
        this.donorList = donorList;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView donorName, donorPhone;
        private LinearLayout donor;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            donor = itemView.findViewById(R.id.layoutDonor);
            donorName = itemView.findViewById(R.id.textDonorName);
            donorPhone = itemView.findViewById(R.id.textDonorPhone);

        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getAdapterPosition());
            }
        }
    }

    @Override
    public DonorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donor_card_layout, parent, false);
        return new DonorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DonorAdapter.ViewHolder holder, int position) {
        final Donor donor = donorList.get(position);
        holder.donorName.setText(donor.getDonorName());
        holder.donorPhone.setText(donor.getDonorPhone());
    }


    public void setOnItemClickListener(DonorAdapter.OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return donorList.size();
    }
}
