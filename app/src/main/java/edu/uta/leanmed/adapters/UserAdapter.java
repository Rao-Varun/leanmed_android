package edu.uta.leanmed.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uta.leanmed.activities.R;
import edu.uta.leanmed.pojo.Inventory;
import edu.uta.leanmed.pojo.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private Context mContext;
    private List<User> userList;
    private Map<Integer,String> statusMap;
    UserAdapter.OnItemClickListener mItemClickListener;
    public UserAdapter(Context mContext, List<User> userList){
        this.mContext=mContext;
        this.userList=userList;
        statusMap=new HashMap<Integer,String>();
        statusMap.put(1,"Active");
        statusMap.put(2,"Pending");
        statusMap.put(3,"Disabled");
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView userName,userType,zone,email,contact,status;
        private LinearLayout userLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            userLayout=itemView.findViewById(R.id.user_layout);
            userName=itemView.findViewById(R.id.user_name);
            userType=itemView.findViewById(R.id.user_type);
            zone=itemView.findViewById(R.id.zone);
            email=itemView.findViewById(R.id.userEmail);
            contact=itemView.findViewById(R.id.contact);
            status=itemView.findViewById(R.id.status);
            userLayout.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView,getPosition());
            }
        }
    }
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserAdapter.ViewHolder holder, final int position) {
        final User user= userList.get(position);
        StringBuilder builder=new StringBuilder(user.getUserName());
        if(user.getIdentity()!=null) builder.append(" ("+user.getIdentity()+")");
        holder.userName.setText(builder.toString());
        holder.email.setText(mContext.getString(R.string.prompt_email)+": "+user.getEmailId());
        holder.contact.setText(mContext.getString(R.string.prompt_contact)+": "+user.getContacts());
        holder.status.setText(mContext.getString(R.string.status)+statusMap.get(user.getUserStatus()));
        if(user.getType()==1) {
            holder.userType.setText(mContext.getString(R.string.user_type) + mContext.getString(R.string.prompt_getDon));
            holder.zone.setVisibility(View.GONE);
        }
        else
            holder.userType.setText(mContext.getString(R.string.user_type)+mContext.getString(R.string.prompt_recDon));
        if(user.getType()==2)
            holder.zone.setText(mContext.getString(R.string.available_at)+user.getZone().getZoneId());
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(UserAdapter.OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
