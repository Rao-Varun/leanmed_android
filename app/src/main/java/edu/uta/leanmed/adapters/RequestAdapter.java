package edu.uta.leanmed.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import edu.uta.leanmed.activities.R;
import edu.uta.leanmed.pojo.CartItem;
import edu.uta.leanmed.pojo.Inventory;
import edu.uta.leanmed.pojo.Request;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder>{
    private Context mContext;
    private List<Request> requests;
    RequestAdapter.OnItemClickListener mItemClickListener;
    public RequestAdapter(Context mContext, List<Request> requests){
        this.mContext=mContext;
        this.requests=requests;
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView medicineName,medInput,inventory,createdDate,createdUser,zone,requestedCount;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            medicineName=itemView.findViewById(R.id.medicine_name);
            medInput=itemView.findViewById(R.id.med_input);
            zone=itemView.findViewById(R.id.availableAt);
            inventory=itemView.findViewById(R.id.invetory);
            requestedCount=itemView.findViewById(R.id.userRequest);
            createdDate=itemView.findViewById(R.id.createdDate);
            createdUser=itemView.findViewById(R.id.createdBy);
        }
        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView,getAdapterPosition());
            }
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.request_layout, parent, false);
        return new RequestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RequestAdapter.ViewHolder holder, final int position) {
        final Request request= requests.get(position);
        Inventory inventory=request.getInventory();
        holder.medicineName.setText(inventory.getMedicine().getTradeName()+" ("+inventory.getMedicine().getGenName()+")");
        holder.medInput.setText(mContext.getString(R.string.med_input)+inventory.getMedicine().getMedicineType());
        holder.zone.setText(mContext.getString(R.string.available_at)+request.getZone().getZoneId());
        holder.inventory.setText(mContext.getString(R.string.inventory)+inventory.getUnits());
        holder.requestedCount.setText(mContext.getString(R.string.requested_quantity)+request.getQuantity());
        holder.createdDate.setText(mContext.getString(R.string.created_date)+request.getCreatedDate());
        if(request.getCreatedUser() != null)
            holder.createdUser.setText(mContext.getString(R.string.created_by)+request.getCreatedUser().getUserName());
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(RequestAdapter.OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }
}
