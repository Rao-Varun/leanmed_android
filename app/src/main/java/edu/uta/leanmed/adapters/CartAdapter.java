package edu.uta.leanmed.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import edu.uta.leanmed.activities.R;
import edu.uta.leanmed.pojo.CartItem;
import edu.uta.leanmed.pojo.Inventory;
import edu.uta.leanmed.services.SharedPreferenceService;

/**
 * Created by Vaibhav's Console on 4/22/2019.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    private Context mContext;
    private List<CartItem> cartItems;
    CartAdapter.OnItemClickListener mItemClickListener;
    public CartAdapter(Context mContext, List<CartItem> cartItems){
        this.mContext=mContext;
        this.cartItems=cartItems;
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView medicineName,medInput,dosage,inventory,expiry,zone;
        private Spinner spinner;
        private Button delete;
        public ViewHolder(View itemView) {
            super(itemView);
            delete=itemView.findViewById(R.id.delete);
            medicineName=itemView.findViewById(R.id.medicine_name);
            medInput=itemView.findViewById(R.id.med_input);
            dosage=itemView.findViewById(R.id.dosage);
            expiry=itemView.findViewById(R.id.expiry);
            zone=itemView.findViewById(R.id.availableAt);
            inventory=itemView.findViewById(R.id.invetory);
            spinner=itemView.findViewById(R.id.spinner);
            delete.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView,getPosition());
            }
        }

    }
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout, parent, false);
        return new CartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CartAdapter.ViewHolder holder, final int position) {
        final CartItem cartItem= cartItems.get(position);
        Inventory inventory=cartItem.getInventory();
        holder.medicineName.setText(inventory.getMedicine().getTradeName()+" ("+inventory.getMedicine().getGenName()+")");
        holder.medInput.setText(mContext.getString(R.string.med_input)+inventory.getMedicine().getMedicineType());
        holder.dosage.setText(mContext.getString(R.string.dosage)+inventory.getMedicine().getDosage());
        holder.expiry.setText(mContext.getString(R.string.expiry)+inventory.getExpiryDate());
        holder.inventory.setText(mContext.getString(R.string.inventory)+inventory.getUnits());
        holder.zone.setText(mContext.getString(R.string.available_at)+inventory.getZone().getZoneId());
        holder.spinner.setSelection(cartItem.getCount()-1);
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String itemSelected = adapterView.getItemAtPosition(i).toString();
                cartItem.setCount(Integer.parseInt(itemSelected));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(CartAdapter.OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

}
