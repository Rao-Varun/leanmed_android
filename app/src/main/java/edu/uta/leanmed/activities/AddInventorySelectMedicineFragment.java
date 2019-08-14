package edu.uta.leanmed.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AddInventorySelectMedicineFragment extends Fragment {

    public AddInventorySelectMedicineFragment() {
        // Required empty public constructor
    }


    public static AddInventorySelectMedicineFragment newInstance(String param1, String param2) {
        AddInventorySelectMedicineFragment fragment = new AddInventorySelectMedicineFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_inventory_select_medicine, container, false);
        this.setAllCardsInLayout(view);
        return view;
    }



    private void setAllCardsInLayout(View view) {
        this.setCardChooseMed(view);
        this.setCardAddNewMed(view);
    }

    private void setCardAddNewMed(View view) {
        CardView cardCreateMedProfile = view.findViewById(R.id.cardCreateMed);
        cardCreateMedProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new AddInventoryNewMedicineFragment());

            }
        });
    }

    private void setCardChooseMed(View view) {
        CardView cardChooseMed = view.findViewById(R.id.cardChooseMed);
        cardChooseMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new AddInventorySearchMedicineFragment());
            }
        });
    }

    private void loadFragment(Fragment fragment){

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.forgotPwContent, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
