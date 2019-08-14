package edu.uta.leanmed.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.uta.leanmed.pojo.Box;


public class MakeABoxZonePreviewFragment extends Fragment {
    private Box box;
    private TextView textBoxId;
    private TextView textRecdonZone;
    private Button buttonChooseMed;

    public MakeABoxZonePreviewFragment() {
        // Required empty public constructor
    }



    public static MakeABoxZonePreviewFragment newInstance(String param1, String param2) {
        MakeABoxZonePreviewFragment fragment = new MakeABoxZonePreviewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_make_abox_zone_preview, container, false);
        this.getBoxObjectFromPreviousFragment();
        this.setAllTextElements(view);
        this.setSubmitButton(view);
        return view;
    }

    private void setSubmitButton(View view) {
        buttonChooseMed = view.findViewById(R.id.buttonChooseMed);
        buttonChooseMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("boxObject", box);
                Fragment fragment = new MakeABoxChooseMedicineFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.forgotPwContent, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

    }

    private void setAllTextElements(View view) {
        textBoxId = view.findViewById(R.id.textBoxId);
        textBoxId.setText(box.getBoxName());
        textRecdonZone = view.findViewById(R.id.textZone);
        textRecdonZone.setText(box.getDestinationZone().getZoneId());
    }

    private void getBoxObjectFromPreviousFragment() {
        this.box = (Box)getArguments().getSerializable("boxObject");
    }

}
