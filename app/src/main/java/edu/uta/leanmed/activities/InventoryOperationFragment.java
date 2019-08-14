package edu.uta.leanmed.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class InventoryOperationFragment extends Fragment {


    public InventoryOperationFragment() {
        // Required empty public constructor
    }


    public static InventoryOperationFragment newInstance(String param1, String param2) {
        InventoryOperationFragment fragment = new InventoryOperationFragment();
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
        View view = inflater.inflate(R.layout.fragment_inventory_operation, container, false);
        this.setAllCardsInLayout(view);
        return view;
    }

    private void setAllCardsInLayout(View view) {
        this.setCardMakeABox(view);
        this.setCardUpdateInventory(view);
    }

    private void setCardUpdateInventory(View view) {
        CardView cardCreateMedProfile = view.findViewById(R.id.cardUpdateInventory);
        cardCreateMedProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new AddInventorySelectMedicineFragment());

            }
        });
    }

    private void setCardMakeABox(View view) {
        CardView cardChooseMed = view.findViewById(R.id.cardMakeABox);
        cardChooseMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new MakeABoxAddBoxNameFragment());
            }
        });
    }

    private void loadFragment(Fragment fragment){

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.forgotPwContent, fragment);
        transaction.addToBackStack("inventoryOperation");
        transaction.commit();
    }

}
