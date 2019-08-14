package edu.uta.leanmed.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;
import edu.uta.leanmed.activities.R;
import edu.uta.leanmed.pojo.Inventory;







public class InventoryBoxAdapter extends RecyclerView.Adapter<InventoryBoxAdapter.InventoryViewHolder> {
    private Context mContext;
    private List<Inventory> inventoryList;
    private HashMap<Integer, Integer> hashMap;
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    public InventoryBoxAdapter(Context mContext, List<Inventory> inventoryList, HashMap<Integer, Integer> hashmap) {
        this.mContext = mContext;
        this.inventoryList = inventoryList;
        this.hashMap = hashmap;
    }

    class InventoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView medicineName, medInput, dosage, inventory, expiry, zone;
        private LinearLayout medicine;

        public InventoryViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            medicine = itemView.findViewById(R.id.medicine);
            medicineName = itemView.findViewById(R.id.medicine_name);
            medInput = itemView.findViewById(R.id.med_input);
            dosage = itemView.findViewById(R.id.dosage);
            expiry = itemView.findViewById(R.id.expiry);
            zone = itemView.findViewById(R.id.availableAt);
            inventory = itemView.findViewById(R.id.invetory);
        }


        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getAdapterPosition());
            }
        }


        @Override
        public boolean onLongClick(View view) {
            if(mItemLongClickListener != null) {
                Log.i("Adapter", "Long click worked");
                mItemLongClickListener.onItemLongClick(itemView, getAdapterPosition());
                return true;
            }
            return false;
        }
    }

    @Override
    public InventoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_card_layout, parent, false);
        return new InventoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final InventoryViewHolder holder, final int position) {
        final Inventory inventory = inventoryList.get(position);
        if(hashMap.containsKey(inventory.getInventoryId())) {
            inventory.setUnits(inventory.getUnits() - hashMap.get(inventory.getInventoryId()));
            Log.i("HashMap Val",  String.format("%d: %d", inventory.getInventoryId(), hashMap.get(inventory.getInventoryId())));
        }
        Log.i("Inventory Unit", Integer.toString(inventory.getUnits()));
        Log.i("Inventory List Unit", Integer.toString(inventoryList.get(position).getUnits()));
        holder.medicineName.setText(inventory.getMedicine().getTradeName() + " (" + inventory.getMedicine().getGenName() + ")");
        holder.medInput.setText(mContext.getString(R.string.med_input) + inventory.getMedicine().getMedicineType());
        holder.dosage.setText(mContext.getString(R.string.dosage) + inventory.getMedicine().getDosage());
        holder.expiry.setText(mContext.getString(R.string.expiry) + inventory.getExpiryDate());
        holder.inventory.setText(mContext.getString(R.string.inventory) + inventory.getUnits());
        holder.zone.setText(mContext.getString(R.string.available_at) + inventory.getZone().getZoneId());

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mItemLongClickListener = onItemLongClickListener;
    }



    @Override
    public int getItemCount() {
        return inventoryList.size();
    }
}
