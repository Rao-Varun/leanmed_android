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
import edu.uta.leanmed.pojo.Medicine;

public class MedicineAdapter extends RecyclerView.Adapter <MedicineAdapter.ViewHolder>{
    private Context mContext;
    private List<Medicine> medicineList;
    MedicineAdapter.OnItemClickListener mItemClickListener;


    public MedicineAdapter(Context mcontext, List<Medicine> medicineList){
        this.mContext = mcontext;
        this.medicineList = medicineList;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView medicineName, medInput, dosage, inventory, expiry, zone;
        private LinearLayout medicine;

        public ViewHolder(View itemView) {
            super(itemView);
            medicine = itemView.findViewById(R.id.medicine);
            medicineName = itemView.findViewById(R.id.medicine_name);
            medInput = itemView.findViewById(R.id.med_input);
            dosage = itemView.findViewById(R.id.dosage);
            expiry = itemView.findViewById(R.id.expiry);
            zone = itemView.findViewById(R.id.availableAt);
            inventory = itemView.findViewById(R.id.invetory);
            medicine.setOnClickListener(this);
            dosage.setText("");
            expiry.setText("");
            zone.setText("");
            inventory.setText("");
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getAdapterPosition());
            }
        }
    }

    @Override
    public MedicineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_card_layout, parent, false);
        return new MedicineAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MedicineAdapter.ViewHolder holder, final int position) {
        final Medicine medicine = medicineList.get(position);
        holder.medicineName.setText(medicine.getTradeName() + " (" + medicine.getGenName() + ")");
        holder.medInput.setText(mContext.getString(R.string.med_input) + medicine.getMedicineType());
        holder.dosage.setText(mContext.getString(R.string.dosage) + medicine.getDosage());
    }



    public void setOnItemClickListener(MedicineAdapter.OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }
}

