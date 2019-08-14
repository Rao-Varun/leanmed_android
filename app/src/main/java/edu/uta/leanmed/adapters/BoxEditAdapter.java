package edu.uta.leanmed.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import edu.uta.leanmed.activities.R;
import edu.uta.leanmed.pojo.Box;
import edu.uta.leanmed.pojo.BoxContent;
import edu.uta.leanmed.pojo.Inventory;
import edu.uta.leanmed.pojo.Zone;


public class BoxEditAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Object> inventoryList;
    private BoxEditAdapter.OnItemClickListener mItemClickListener;
    private BoxEditAdapter.OnItemLongClickListener mItemLongClickListener;
    private BoxDeleteButtonClickListener boxDeleteButtonClickListener;
    private BoxContentDeleteButtonClickListener boxContentDeleteButtonClickListener;
    private SpinnerOnItemSelectedListener spinnerOnItemSelectedListener;
    private AddBoxContentButtonListener addBoxContentButtonListener;


    public BoxEditAdapter(Context mContext, List<Object> inventoryList) {
        this.mContext = mContext;
        this.inventoryList = inventoryList;
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
            this.setBoxContentDeleteButton(itemView);
        }

        private void setBoxContentDeleteButton(View view) {
            ImageButton buttonDelete = view.findViewById(R.id.buttonDelete);
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boxContentDeleteButtonClickListener.onItemClick(view, getAdapterPosition());
                }
            });
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

    class BoxViewHolder extends RecyclerView.ViewHolder  {
        private TextView textBoxId, textCreatedBy, textCreatedDate, textStatus, textDestZone;

        public BoxViewHolder(View itemView) {
            super(itemView);
            this.setViewCardLayoutElements(itemView);
        }

        private void setViewCardLayoutElements(View itemView) {
            textBoxId = itemView.findViewById(R.id.textBoxId);
            textCreatedBy = itemView.findViewById(R.id.textCreatedBy);
            textCreatedDate = itemView.findViewById(R.id.textCreatedDate);
            textStatus = itemView.findViewById(R.id.textStatus);
            textDestZone = itemView.findViewById(R.id.textDestZoneId);
            this.setBoxDeleteButton(itemView);
            this.setAddButton(itemView);
            this.setSpinner(itemView);
        }

        private void setAddButton(View itemView) {
            ImageButton addBoxContentButton = itemView.findViewById(R.id.buttonAdd);
            addBoxContentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addBoxContentButtonListener.onItemClick(view, getAdapterPosition());
                }
            });
        }

        private void setSpinner(View itemView) {
            String[] spinnerList = new String[]{"Closed", "Dispatched", "Received", "Canceled"};
            Spinner spinner = itemView.findViewById(R.id.spinnerStatus);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                    android.R.layout.simple_spinner_item, spinnerList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    int status;
                    if(i == 3)
                        status = 6;
                    else
                        status = i +2;
                    spinnerOnItemSelectedListener.onItemSelected(view, status);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        private void setBoxDeleteButton(View view) {
            ImageButton buttonDelete = view.findViewById(R.id.buttonDelete);
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boxDeleteButtonClickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }


    }


    class ZoneViewHolder extends RecyclerView.ViewHolder {
        private TextView textZone, textZoneName, textZoneEmailId, textZoneCountry;

        public ZoneViewHolder(View itemView) {
            super(itemView);
            textZone = itemView.findViewById(R.id.textZoneId);
            textZoneName = itemView.findViewById(R.id.textZoneName);
            textZoneEmailId = itemView.findViewById(R.id.textZoneEmailId);
            textZoneCountry = itemView.findViewById(R.id.textZoneCountry);
        }


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        switch(viewType){
            case 0: viewHolder=this.getBoxViewHolder(parent);
                break;
            case 1: viewHolder=this.getZoneViewHolder(parent);
                break;
            default: viewHolder=this.getInventoryViewHolder(parent);
                break;
        }
        return viewHolder;
    }

    private RecyclerView.ViewHolder getBoxViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_edit_card_layout, parent, false);
        return new BoxEditAdapter.BoxViewHolder(view);
    }



    private RecyclerView.ViewHolder getZoneViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zone_card_layout, parent, false);
        return new BoxEditAdapter.ZoneViewHolder(view);
    }

    private RecyclerView.ViewHolder getInventoryViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.boxcontent_edit_card_layout, parent, false);
        return new BoxEditAdapter.InventoryViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch(position){
            case 0: this.setBoxViewHolder(position, (BoxEditAdapter.BoxViewHolder) holder);
                break;
            case 1: this.setZoneViewHolder(position, (BoxEditAdapter.ZoneViewHolder) holder);
                break;
            default:this.setInventoryViewHolder(position, (BoxEditAdapter.InventoryViewHolder) holder);
                break;
        }
    }

    private void setBoxViewHolder(int position, BoxEditAdapter.BoxViewHolder holder) {
        Box box = (Box)this.inventoryList.get(position);
        holder.textBoxId.setText(String.format("%s %s",this.mContext.getString(R.string.box_id), box.getBoxName()));
        holder.textCreatedBy.setText(String.format("%s %s",this.mContext.getString(R.string.created_by), box.getCreatedUser().getEmailId()));
        holder.textCreatedDate.setText(String.format("%s %s",this.mContext.getString(R.string.created_date), box.getCreationDate()));
        holder.textDestZone.setText(String.format("%s %s(%s)",this.mContext.getString(R.string.dest_zone), box.getDestinationZone().getZoneName(),box.getDestinationZone().getZoneId() ));
        holder.textStatus.setText(String.format("%s %s",this.mContext.getString(R.string.status), mapStatus(box.getStatus())));

    }

    private String mapStatus(int status){
        String statString;
        switch (status){
            case 1:
                statString = "Open";
                break;
            case 2:
                statString = "Closed";
                break;
            case 3:
                statString = "Dispatched";
                break;
            case 4:
                statString = "Received";
                break;
            case 6:
                statString = "Canceled";
                break;
            default:
                statString = "Null";
                break;
        }
        return statString;
    }

    private void setZoneViewHolder(int position, BoxEditAdapter.ZoneViewHolder holder) {
        Zone zone = (Zone)inventoryList.get(position);
        holder.textZone.setText(String.format( "%s %s (%s)",mContext.getString(R.string.zone_name), zone.getZone(),zone.getZoneId()));
        holder.textZoneName.setText(String.format("%s %s", mContext.getString(R.string.zone_name), zone.getZoneName()));
        holder.textZoneCountry.setText(String.format("%s %s", mContext.getString(R.string.zone_country),zone.getZoneCountry()));
        holder.textZoneEmailId.setText(String.format("%s %s", mContext.getString(R.string.zone_email),zone.getZoneEmail()));
    }

    private void setInventoryViewHolder(int position, BoxEditAdapter.InventoryViewHolder holder) {
        BoxContent boxContent = (BoxContent)inventoryList.get(position);
        Inventory inventory = boxContent.getInventory();
        holder.medicineName.setText(inventory.getMedicine().getTradeName() + " (" + inventory.getMedicine().getGenName() + ")");
        holder.medInput.setText(mContext.getString(R.string.med_input) + inventory.getMedicine().getMedicineType());
        holder.dosage.setText(mContext.getString(R.string.dosage) + inventory.getMedicine().getDosage());
        holder.expiry.setText(mContext.getString(R.string.expiry) + inventory.getExpiryDate());
        holder.inventory.setText(mContext.getString(R.string.requested_quantity) + boxContent.getUnits());
        holder.zone.setText(mContext.getString(R.string.available_at) + inventory.getZone().getZoneId());

    }

    public  interface AddBoxContentButtonListener{
        void onItemClick(View view, int position);
    }

    public interface SpinnerOnItemSelectedListener{
        void onItemSelected(View view, int position);
    }

    public interface BoxDeleteButtonClickListener{

        void onItemClick(View view, int position);
    }

    public interface BoxContentDeleteButtonClickListener{

        void onItemClick(View view, int position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(BoxEditAdapter.OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(BoxEditAdapter.OnItemLongClickListener onItemLongClickListener) {
        this.mItemLongClickListener = onItemLongClickListener;
    }

    public void setBoxDeleteButtonClickListener(BoxDeleteButtonClickListener boxDeleteButtonClickListener) {
        this.boxDeleteButtonClickListener = boxDeleteButtonClickListener;
    }

    public void setBoxContentDeleteButtonClickListener(BoxContentDeleteButtonClickListener boxContentDeleteButtonClickListener) {
        this.boxContentDeleteButtonClickListener = boxContentDeleteButtonClickListener;
    }

    public void setSpinnerOnItemSelectedListener(SpinnerOnItemSelectedListener spinnerOnItemSelectedListener) {
        this.spinnerOnItemSelectedListener = spinnerOnItemSelectedListener;
    }

    public void setAddBoxContentButtonListener(AddBoxContentButtonListener addBoxContentButtonListener) {
        this.addBoxContentButtonListener = addBoxContentButtonListener;
    }

    @Override
    public int getItemCount() {
        return inventoryList.size();
    }
}
