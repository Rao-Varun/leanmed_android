package edu.uta.leanmed.adapters;
import android.view.LayoutInflater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import edu.uta.leanmed.activities.R;
import edu.uta.leanmed.pojo.Box;
import edu.uta.leanmed.services.BoxAPIService;

public class BoxAdapter extends RecyclerView.Adapter<BoxAdapter.ViewHolder> {
    private List<Box> boxList;
    private Context context;
    private BoxAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public BoxAdapter(Context context, List<Box> boxList){
        this.context = context;
        this.boxList = boxList;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView textBoxId, textCreatedBy, textCreatedDate, textStatus, textDestZone;

        public ViewHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            this.setViewCardLayoutElements(itemView);

        }

        private void setViewCardLayoutElements(View itemView) {
            textBoxId = itemView.findViewById(R.id.textBoxId);
            textCreatedBy = itemView.findViewById(R.id.textCreatedBy);
            textCreatedDate = itemView.findViewById(R.id.textCreatedDate);
            textStatus = itemView.findViewById(R.id.textStatus);
            textDestZone = itemView.findViewById(R.id.textDestZoneId);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    @Override
    public BoxAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_card_layout, parent, false);
        return new BoxAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Box box = this.boxList.get(position);
        holder.textBoxId.setText(String.format("%s %s",this.context.getString(R.string.box_id), box.getBoxName()));
        holder.textCreatedBy.setText(String.format("%s %s",this.context.getString(R.string.created_by), box.getCreatedUser().getEmailId()));
        holder.textCreatedDate.setText(String.format("%s %s",this.context.getString(R.string.created_date), box.getCreationDate()));
        holder.textDestZone.setText(String.format("%s %s(%s)",this.context.getString(R.string.dest_zone), box.getDestinationZone().getZoneName(),box.getDestinationZone().getZoneId() ));
        holder.textStatus.setText(String.format("%s %s",this.context.getString(R.string.status), mapStatus(box.getStatus())));

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


    @Override
    public int getItemCount() {
        return boxList.size();
    }
}
