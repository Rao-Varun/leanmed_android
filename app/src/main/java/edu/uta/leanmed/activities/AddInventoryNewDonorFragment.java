package edu.uta.leanmed.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import edu.uta.leanmed.pojo.Donor;
import edu.uta.leanmed.pojo.DonorResponse;
import edu.uta.leanmed.pojo.Inventory;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.MedicineAPIService;
import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.SharedPreferenceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddInventoryNewDonorFragment extends Fragment {
    private EditText etDonorName;
    private EditText etDonorPhone;
    private Button addDonorButton;
    private User user;
    private MedicineAPIService service;
    private Inventory inventory = new Inventory();

    public AddInventoryNewDonorFragment() {
        // Required empty public constructor
    }


    public static AddInventoryNewDonorFragment newInstance(String param1, String param2) {
        AddInventoryNewDonorFragment fragment = new AddInventoryNewDonorFragment();
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

        View view = inflater.inflate(R.layout.fragment_add_inventory_new_donor, container, false);
        this.getInventoryObject();
        this.initServices();
        this.setAllEditTextElement(view);
        this.setAddDonorButton(view);
        return view;
    }

    private void getInventoryObject() {
        this.inventory = (Inventory)getArguments().getSerializable("inventoryObject");
    }

    private void setAddDonorButton(View view) {
        addDonorButton = view.findViewById(R.id.buttonAddNewDonor);
        addDonorButton.setOnClickListener(new View.OnClickListener() {
            private String donorName;
            private String donorPhone;

            @Override
            public void onClick(View view) {
                this.getAllDonorDetails();
                if (this.isDonorDetailsNotValid())
                    return;
                this.submitDonorDetailsToServer();

            }

            private void submitDonorDetailsToServer() {
                Donor donor = this.getDonorObject();
                donor.setDonorName(donorName);
                donor.setDonorPhone(donorPhone);
                Call<DonorResponse> productsCall = service.addNewDonor(user.getEmailId(), user.getToken(), donor);
                productsCall.enqueue(new Callback<DonorResponse>() {
                    @Override
                    public void onResponse(Call<DonorResponse> call, Response<DonorResponse> response) {
                        if (response.body() != null)
                            Toast.makeText(getActivity().getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        if (response.body().getMessage().equals("New Donor added successfully"))
                            startNextFragment(response.body().getDonor());


                    }

                    private void startNextFragment(List<Donor> donorList) {
                        Donor donor = donorList.get(0);
                        inventory.setDonor(donor);
                        inventory.setUser(user);
                        inventory.setZone(user.getZone());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("inventoryObject", inventory);
                        Fragment fragment = new AddInventoryDonorPreviewFragment();
                        fragment.setArguments(bundle);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.forgotPwContent, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();

                    }

                    @Override
                    public void onFailure(Call<DonorResponse> call, Throwable t) {
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG).show();
                        Log.d("onFailure", t.getMessage());
//                hidepDialog();
                    }
                });
            }

            private Donor getDonorObject() {
                Donor donor = new Donor();
                donor.setDonorName(donorName);
                donor.setDonorPhone(donorPhone);
                return donor;
            }

            private boolean isDonorDetailsNotValid() {
                if (donorName.isEmpty()) {
                    etDonorName.setError(getString(R.string.error_field_required));
                    etDonorName.requestFocus();
                    return true;
                }
                if (donorPhone.isEmpty()) {
                    etDonorPhone.setError(getString(R.string.error_field_required));
                    etDonorPhone.requestFocus();
                    return true;
                }
                return false;
            }


            private void getAllDonorDetails() {
                donorName = etDonorName.getText().toString();
                donorPhone = etDonorPhone.getText().toString();
            }
        });
    }

    private void setAllEditTextElement(View view) {
        etDonorName = view.findViewById(R.id.etDonorName);
        etDonorPhone = view.findViewById(R.id.etDonorPhone);
    }

    private void initServices() {
        this.user = SharedPreferenceService.getSavedObjectFromPreference(
                getActivity().getApplicationContext(), SharedPreferenceService.getUserName());
        service = RetrofitService.newInstance().create(MedicineAPIService.class);
    }

}
