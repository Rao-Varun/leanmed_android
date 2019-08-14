package edu.uta.leanmed.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.uta.leanmed.activities.R;
import edu.uta.leanmed.pojo.User;

public class RecdonVolunteerArrayAdapter extends ArrayAdapter<User> {
    private List<User> userList;
    private Context context;


    public RecdonVolunteerArrayAdapter(@NonNull Context context,  @NonNull List<User> userList) {
        super(context, 0, userList);
        this.userList = userList;
        this.context = context;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.choose_recdon_volunteer_card,parent,false);
        this.setTextElements(convertView, userList.get(position));
        return convertView;
    }

    private void setTextElements(View view, User user) {
        TextView textUserName = view.findViewById(R.id.textUserName);
        TextView textEmail = view.findViewById(R.id.textEmail);
        TextView textZone = view.findViewById(R.id.textZone);
        textUserName.setText(user.getUserName());
        textEmail.setText(user.getEmailId());
        textZone.setText(String.format("%s(%s)",user.getZone().getZoneName(),user.getZone().getZoneId()));
    }
}
