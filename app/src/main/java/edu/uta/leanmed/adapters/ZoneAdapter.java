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
import edu.uta.leanmed.pojo.Zone;

public class ZoneAdapter extends RecyclerView.Adapter<ZoneAdapter.ViewHolder>{
    private Context mContext;
    private List<Zone> zoneList;
    private ZoneAdapter.OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textZone, textZoneName, textZoneEmailId, textZoneCountry;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textZone = itemView.findViewById(R.id.textZoneId);
            textZoneName = itemView.findViewById(R.id.textZoneName);
            textZoneEmailId = itemView.findViewById(R.id.textZoneEmailId);
            textZoneCountry = itemView.findViewById(R.id.textZoneCountry);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getAdapterPosition());
            }
        }
    }

    public ZoneAdapter(Context mContext, List<Zone> zoneList){
        this.mContext = mContext;
        this.zoneList = zoneList;
    }

    @Override
    public ZoneAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zone_card_layout, parent, false);
        return new ZoneAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ZoneAdapter.ViewHolder holder, int position) {
        Zone zone = zoneList.get(position);
        holder.textZone.setText(String.format( "%s (%s)",zone.getZone(),zone.getZoneId()));
        holder.textZoneName.setText(String.format("%s %s", mContext.getString(R.string.zone_name), zone.getZoneName()));
        holder.textZoneCountry.setText(String.format("%s %s", mContext.getString(R.string.zone_country),zone.getZoneCountry()));
        holder.textZoneEmailId.setText(String.format("%s %s", mContext.getString(R.string.zone_email),zone.getZoneEmail()));
    }

    @Override
    public int getItemCount() {
        return zoneList.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener;
    }
}
