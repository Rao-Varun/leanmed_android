package edu.uta.leanmed.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uta.leanmed.pojo.Inventory;


public class AddInventorySelectDonorFragment extends Fragment {
    private Inventory inventory;

    public AddInventorySelectDonorFragment() {
        // Required empty public constructor
    }


    public static AddInventorySelectDonorFragment newInstance(String param1, String param2) {
        AddInventorySelectDonorFragment fragment = new AddInventorySelectDonorFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_inventory_select_donor, container, false);
        this.getInventoryObject();
        this.setAllCardsInLayout(view);
        return view;
    }

    private void getInventoryObject() {
        this.inventory = (Inventory)getArguments().getSerializable("inventoryObject");
    }

    private void setAllCardsInLayout(View view) {
        this.setCardChooseMed(view);
        this.setCardAddNewMed(view);
    }

    private void setCardAddNewMed(View view) {
        CardView cardCreateMedProfile = view.findViewById(R.id.cardCreateDonor);
        cardCreateMedProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new AddInventoryNewDonorFragment());

            }
        });
    }

    private void setCardChooseMed(View view) {
        CardView cardChooseMed = view.findViewById(R.id.cardChooseDonor);
        cardChooseMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new AddInventorySearchDonorFragment());
            }
        });
    }

    private void loadFragment(Fragment fragment){
        Bundle bundle = new Bundle();
        bundle.putSerializable("inventoryObject", inventory);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.forgotPwContent, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
