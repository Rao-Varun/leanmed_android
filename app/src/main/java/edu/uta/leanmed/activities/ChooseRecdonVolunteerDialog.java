package edu.uta.leanmed.activities;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import edu.uta.leanmed.adapters.InventoryBoxAdapter;
import edu.uta.leanmed.adapters.RecdonVolunteerArrayAdapter;
import edu.uta.leanmed.pojo.User;

public class ChooseRecdonVolunteerDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private List<User> userList;
    private DialogListener dialogListener;

    public interface DialogListener{

        public void onItemClick(int position);

    }

    public ChooseRecdonVolunteerDialog(@NonNull Context context, List<User> listUser) {
        super(context);
       this.context = context;
       this.userList = listUser;
    }

    private RecdonVolunteerArrayAdapter ArrayAdapter() {
        RecdonVolunteerArrayAdapter recdonVolunteerArrayAdapter = new RecdonVolunteerArrayAdapter(getContext(), userList);
        return recdonVolunteerArrayAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_recdon_volunteer_dialogue);
        RecdonVolunteerArrayAdapter recdonVolunteerArrayAdapter = this.ArrayAdapter();
        this.setupListView(recdonVolunteerArrayAdapter);
    }

    private void setupListView(RecdonVolunteerArrayAdapter recdonVolunteerArrayAdapter) {
        ListView listView = findViewById(R.id.listUser);
        listView.setAdapter(recdonVolunteerArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                dialogListener.onItemClick(position);
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }
}
