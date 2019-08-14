package edu.uta.leanmed.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.uta.leanmed.pojo.Donor;
import edu.uta.leanmed.pojo.Inventory;
import edu.uta.leanmed.pojo.InventoryResponse;
import edu.uta.leanmed.pojo.Medicine;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.MedicineAPIService;
import edu.uta.leanmed.services.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddInventoryFinalPreviewFragment extends Fragment {
    private Inventory inventory;
    private Medicine medicine;
    private Donor donor;
    private TextView tvGenName;
    private TextView tvTradeName;
    private TextView tvMedType;
    private TextView tvDosage;
    private TextView tvWeight;
    private TextView tvUnits;
    private TextView tvExpiryDate;
    private Button buttonSubmit;
    private TextView tvDonorName;
    private TextView tvDonorPhone;
    private TextView tvDonationDate;
    private MedicineAPIService service;
    private User user;

    public AddInventoryFinalPreviewFragment() {
        // Required empty public constructor
    }


    public static AddInventoryFinalPreviewFragment newInstance(String param1, String param2) {
        AddInventoryFinalPreviewFragment fragment = new AddInventoryFinalPreviewFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_inventory_final_preview, container, false);
        this.getInventoryObject();
        this.setAllMedicineCardTextViews(view);
        this.setAllDonorCardTextViews(view);
        this.setSubmitButton(view);
        return view;
    }

    private void getInventoryObject() {
        this.inventory = (Inventory) getArguments().getSerializable("inventoryObject");
        this.medicine = inventory.getMedicine();
        this.donor = inventory.getDonor();
        this.user = inventory.getUser();
        this.service = RetrofitService.newInstance().create(MedicineAPIService.class);

    }

    private void setSubmitButton(View view) {
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<InventoryResponse> inventoryResponseCall = service.addNewInventory(user.getEmailId(), user.getToken(), inventory);
                inventoryResponseCall.enqueue(new Callback<InventoryResponse>() {
                    @Override
                    public void onResponse(Call<InventoryResponse> call, Response<InventoryResponse> response) {
                        if(response.body() != null) {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG);
                            this.startNextFragment();
                        }
                        else{
                            Toast.makeText(getContext(), getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG);
                        }
                    }

                    private void startNextFragment() {
                        FragmentManager fm = getFragmentManager();
                        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }
                        Fragment fragment = new InventoryOperationFragment();
                        FragmentTransaction transaction = fm.beginTransaction();
                        transaction.replace(R.id.forgotPwContent, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                    @Override
                    public void onFailure(Call<InventoryResponse> call, Throwable t) {
                        Toast.makeText(getContext(), getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG);
                    }
                });
            }
        });
    }

    private void setAllDonorCardTextViews(View view) {
        this.declareAllDonorCardTextViewElements(view);
        this.setAllDonorCardTextElementsStrings();
    }

    private void declareAllDonorCardTextViewElements(View view) {
        tvDonorName = view.findViewById(R.id.textDonorName);
        tvDonorPhone = view.findViewById(R.id.textDonorPhone);
        tvDonationDate = view.findViewById(R.id.textDonationDate);

    }

    private void setAllDonorCardTextElementsStrings() {
        tvDonorName.setText(String.format("%s  %s", getString(R.string.donor_name), this.donor.getDonorName()));
        tvDonorPhone.setText(String.format("%s  %s", getString(R.string.donor_name), this.donor.getDonorPhone()));
        tvDonationDate.setText(String.format("%s  %s", getString(R.string.donation_date), this.inventory.getDonationDate()));

    }


    private void setAllMedicineCardTextViews(View view) {
        this.declareAllMedicineCardTextElements(view);
        this.setAllMedcineCardTextViewElementsStrings();


    }

    private void setAllMedcineCardTextViewElementsStrings() {
        tvGenName.setText(String.format("%s  %s", getString(R.string.generic_name), medicine.getGenName()));
        tvTradeName.setText(String.format("%s  %s", getString(R.string.trade_name), medicine.getTradeName()));
        tvMedType.setText(String.format("%s  %s", getString(R.string.med_input), medicine.getMedicineType()));
        tvDosage.setText(String.format("%s  %s", getString(R.string.dosage), medicine.getDosage()));
        tvWeight.setText(String.format("%s  %s", getString(R.string.weight), medicine.getWeight()));
        tvUnits.setText(String.format("%s  %s", getString(R.string.unit),  Integer.toString(inventory.getUnits())));
        tvExpiryDate.setText(String.format("%s  %s", getString(R.string.weight), inventory.getExpiryDate()));
    }

    private void declareAllMedicineCardTextElements(View view) {
        tvGenName = view.findViewById(R.id.textGenericName);
        tvTradeName = view.findViewById(R.id.textTradeName);
        tvMedType = view.findViewById(R.id.textMedicineType);
        tvDosage = view.findViewById(R.id.textDosage);
        tvWeight = view.findViewById(R.id.textWeight);
        tvUnits = view.findViewById(R.id.textUnit);
        tvExpiryDate = view.findViewById(R.id.textExpiryDate);

    }


}
