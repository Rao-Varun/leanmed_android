package edu.uta.leanmed.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import edu.uta.leanmed.pojo.Donor;
import edu.uta.leanmed.pojo.Inventory;


public class AddInventoryDonorPreviewFragment extends Fragment {
    private TextView tvDonorName;
    private TextView tvDonorPhone;
    private TextView tvDonationDate;
    private TextView tvError;
    private Button buttonNext;
    private String donationDate;
    private boolean isDonationDateNotSet = true;
    private Inventory inventory;
    private Donor donor;
    private DatePickerDialog datePickerDialog;


    public AddInventoryDonorPreviewFragment(){}

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            donationDate = String.format("%d-%d-%d", year, month + 1, day);
            tvDonationDate.setText(String.format("%d-%d-%d", day, month + 1, year));
            isDonationDateNotSet = false;
        }
    };

    public static AddInventoryDonorPreviewFragment newInstance(String param1, String param2) {
        AddInventoryDonorPreviewFragment fragment = new AddInventoryDonorPreviewFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_inventory_donor_preview, container, false);
        this.getInventoryObject();
        this.setAllTextElements(view);
        this.setDateDialoguePicker(view);
        this.setSubmitButton(view);
        return view;

    }

    private void getInventoryObject() {
        this.inventory = (Inventory) getArguments().getSerializable("inventoryObject");
        this.donor = inventory.getDonor();
    }

    private void setSubmitButton(View view) {
        buttonNext = view.findViewById(R.id.buttonSubmit);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDonationDateNotSet) {
                    tvError.setVisibility(View.VISIBLE);
                    return;
                }
                this.setInventoryDetails();
                this.startNextFragment();
            }

            private void startNextFragment() {
                inventory.setDonationDate(donationDate);
                Bundle bundle = new Bundle();
                bundle.putSerializable("inventoryObject", inventory);
                Fragment fragment = new AddInventoryFinalPreviewFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.forgotPwContent, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

            private void setInventoryDetails() {
                inventory.setDonationDate(donationDate);
            }
        });
    }

    private void setDateDialoguePicker(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        this.datePickerDialog = new DatePickerDialog(getContext(),
                AlertDialog.THEME_DEVICE_DEFAULT_DARK,
                this.dateSetListener, year, month, day);
    }

    private void setAllTextElements(View view) {
        this.declareAllTextVieElements(view);
        this.setAllTextElementsStrings();
    }

    private void declareAllTextVieElements(View view) {
        tvDonorName = view.findViewById(R.id.textDonorName);
        tvDonorPhone = view.findViewById(R.id.textDonorPhone);
        tvDonationDate = view.findViewById(R.id.textDonationDate);
        tvError = view.findViewById(R.id.textError);
        tvDonationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
    }

    private void setAllTextElementsStrings() {
        tvDonorName.setText(String.format("%s  %s", getString(R.string.donor_name), this.donor.getDonorName()));
        tvDonorPhone.setText(String.format("%s  %s", getString(R.string.donor_name), this.donor.getDonorPhone()));
        tvDonationDate.setText("dd-mm-yyyy");
        tvError.setVisibility(View.INVISIBLE);

    }


}
