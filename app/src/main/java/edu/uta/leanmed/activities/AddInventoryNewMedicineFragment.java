package edu.uta.leanmed.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import edu.uta.leanmed.pojo.Inventory;
import edu.uta.leanmed.pojo.Medicine;
import edu.uta.leanmed.pojo.MedicineResponse;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.MedicineAPIService;
import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.SharedPreferenceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddInventoryNewMedicineFragment extends Fragment {
    private EditText etGenericName;
    private EditText etTradeName;
    private EditText etMedType;
    private EditText etDosage;
    private EditText etWeight;
    private Button buttonAddNewMed;
    private User user;
    private MedicineAPIService service ;
    private Inventory inventory = new Inventory();

    public AddInventoryNewMedicineFragment() {
        // Required empty public constructor
    }


    public static AddInventoryNewMedicineFragment newInstance(String param1, String param2) {
        AddInventoryNewMedicineFragment fragment = new AddInventoryNewMedicineFragment();
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
        View view =  inflater.inflate(R.layout.fragment_add_inventory_new_medicine, container, false);
        this.initServices();
        this.setEditTextsInLayout(view);
        this.setAddMedicineButtonInLayout(view);
        return view;

    }

    private void setAddMedicineButtonInLayout(final View view) {
        buttonAddNewMed = view.findViewById(R.id.buttonAddNewMed);
        buttonAddNewMed.setOnClickListener(new View.OnClickListener() {
            private String genName;
            private String tradeName;
            private String medType;
            private String dosage;
            private String weight;

            @Override
            public void onClick(View view) {
                this.getNewMedDetails();
                if(this.isAllFieldsNotValid())
                    return;
                this.submitMedDetailsToServer();
            }

            private void submitMedDetailsToServer() {
                Medicine medicine = this.getMedicineObject();
                Call<MedicineResponse> medicineResponseCall = service.addNewMedicine(user.getEmailId(), user.getToken(), medicine);
                medicineResponseCall.enqueue(new Callback<MedicineResponse>(){
                    @Override
                    public void onResponse(Call<MedicineResponse> call, Response<MedicineResponse> response) {
                    if(response.body() != null) {
                        Toast.makeText(getActivity().getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        if(!response.body().getMessage().equals("Medicine exist in DB already")
                        && response.body().getMessage().equals("Medicine added successfully"));
                          this.startNextFragement(response.body().getMedicine());
                    }
                    else{
                        Toast.makeText(getActivity().getApplicationContext(),R.string.password_change_unsuccesfull , Toast.LENGTH_LONG).show();

                    }


                    }

                    private void startNextFragement(List<Medicine> medicineList) {
                        Medicine medicine = medicineList.get(0);
                        Bundle bundle = new Bundle();
                        inventory.setMedicine(medicine);
                        inventory.setUser(user);
                        inventory.setZone(user.getZone());
                        bundle.putSerializable("inventoryObject", inventory);
                        Fragment fragment = new AddInventoryMedicinePreviewFragment();
                        fragment.setArguments(bundle);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.forgotPwContent, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();

                    }

                    @Override
                    public void onFailure(Call<MedicineResponse> call, Throwable t) {
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG).show();

                    }
                });
            }

            private Medicine getMedicineObject() {
                Medicine medicine = new Medicine();
                medicine.setGenName(genName);
                medicine.setTradeName(tradeName);
                medicine.setMedicineType(medType);
                medicine.setDosage(dosage);
                medicine.setWeight(weight);
                return medicine;
            }

            private boolean isAllFieldsNotValid() {
                if(genName.isEmpty())
                {
                    etGenericName.setError(getString(R.string.error_field_required));
                    etGenericName.requestFocus();
                    return true;
                }
                if(tradeName.isEmpty())
                {
                    etTradeName.setError(getString(R.string.error_field_required));
                    etTradeName.requestFocus();
                    return true;
                }
                if(medType.isEmpty())
                {
                    etMedType.setError(getString(R.string.error_field_required));
                    etMedType.requestFocus();
                    return true;
                }
                if(dosage.isEmpty())
                {
                    etDosage.setError(getString(R.string.error_field_required));
                    etDosage.requestFocus();
                    return true;
                }
                return false;

            }

            private void getNewMedDetails() {
                genName = etGenericName.getText().toString();
                tradeName = etTradeName.getText().toString();
                medType = etMedType.getText().toString();
                dosage = etDosage.getText().toString();
                weight = etWeight.getText().toString();
            }
        });
    }

    private void setEditTextsInLayout(View view) {
        etGenericName = view.findViewById(R.id.etGenericName);
        etTradeName = view.findViewById(R.id.etTradeName);
        etMedType = view.findViewById(R.id.etMedInput);
        etDosage = view.findViewById(R.id.etDosage);
        etWeight = view.findViewById(R.id.etWeight);
    }

    private void initServices() {
        this.user = SharedPreferenceService.getSavedObjectFromPreference(
                getActivity().getApplicationContext(), SharedPreferenceService.getUserName());
        service = RetrofitService.newInstance().create(MedicineAPIService.class);

    }


}
