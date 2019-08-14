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
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import edu.uta.leanmed.pojo.Inventory;
import edu.uta.leanmed.pojo.Medicine;


public class AddInventoryMedicinePreviewFragment extends Fragment {
    private TextView tvGenName;
    private TextView tvTradeName;
    private TextView tvMedType;
    private TextView tvDosage;
    private TextView tvWeight;
    private EditText etUnit;
    private TextView tvExpiryDate;
    private TextView tvError;
    private Button buttonNext;
    private String expiryDate;
    private Boolean isExpiryDateNotSet = true;
    private DatePickerDialog datePickerDialog;
    private Inventory inventory;
    private Medicine  medicine;

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            expiryDate = String.format("%d-%d-%d",year,month+1,day);
            tvExpiryDate.setText(String.format("%d-%d-%d",day,month+1,year));
            isExpiryDateNotSet = false;
        }
    };


    public AddInventoryMedicinePreviewFragment() {
        // Required empty public constructor
    }


    public static AddInventoryMedicinePreviewFragment newInstance(String param1, String param2) {
        AddInventoryMedicinePreviewFragment fragment = new AddInventoryMedicinePreviewFragment();
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
        View view =  inflater.inflate(R.layout.fragment_add_inventory_medicine_preview, container, false);
        this.getInventoryObject();
        this.setAllTextViewElements(view);
        this.setDateDialoguePickler();
        this.setNextButton(view);
        return view;

    }

    private void getInventoryObject() {
        this.inventory = (Inventory)getArguments().getSerializable("inventoryObject");
        this.medicine = inventory.getMedicine();
    }

    private void setNextButton(View view) {
        buttonNext = view.findViewById(R.id.buttonSubmit);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            private String unit;
            @Override
            public void onClick(View view) {
                this.getUnitValue();
                if(this.isExpiryDateSet() && this.isUnitValid()) {
                    tvError.setVisibility(View.VISIBLE);
                    return;
                }
                this.setInventoryDetails();
                this.startNextFragment();
            }

            private Boolean isExpiryDateSet(){
                if(isExpiryDateNotSet){
                    tvExpiryDate.setError(getString(R.string.error_field_required));
                    return true;
                }
                return false;
            }


            private Boolean isUnitValid() {
                if (unit.isEmpty()) {
                    etUnit.setError(getString(R.string.error_field_required));
                    return true;
                }
                return false;
            }

            private void getUnitValue() {
                unit = etUnit.getText().toString();
            }

            private void startNextFragment() {
                Bundle bundle = new Bundle();
                bundle.putSerializable("inventoryObject", inventory);
                Fragment fragment = new AddInventorySelectDonorFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.forgotPwContent, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }

            private void setInventoryDetails() {
                inventory.setExpiryDate(expiryDate);
                inventory.setUnits(Integer.parseInt(unit));
            }
        });
    }

    private void setDateDialoguePickler() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
//        tvExpiryDate.setText(String.format("%d-%d-%d",day,month+1,year));
        this.datePickerDialog = new DatePickerDialog(getContext(),
                AlertDialog.THEME_DEVICE_DEFAULT_DARK,
                this.dateSetListener, year, month, day);
//        this.datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void setAllTextViewElements(View view) {
        this.declareAllTextElements(view);
        this.setAllTextViewElementsStrings();


    }

    private void setAllTextViewElementsStrings() {
        tvError.setVisibility(View.INVISIBLE);
        tvGenName.setText(String.format("%s  %s",getString(R.string.generic_name), medicine.getGenName()));
        tvTradeName.setText(String.format("%s  %s",getString(R.string.trade_name), medicine.getTradeName()));
        tvMedType.setText(String.format("%s  %s",getString(R.string.med_input), medicine.getMedicineType()));
        tvDosage.setText(String.format("%s  %s",getString(R.string.dosage), medicine.getDosage()));
        tvWeight.setText(String.format("%s  %s",getString(R.string.weight), medicine.getWeight()));
        tvExpiryDate.setText("dd-mm-yyyy");
    }

    private void declareAllTextElements(View view) {
        tvGenName = view.findViewById(R.id.textGenericName);
        tvTradeName = view.findViewById(R.id.textTradeName);
        tvMedType = view.findViewById(R.id.textMedicineType);
        tvDosage = view.findViewById(R.id.textDosage);
        tvWeight = view.findViewById(R.id.textWeight);
        tvExpiryDate = view.findViewById(R.id.textExpiryDate);
        tvError = view.findViewById(R.id.textError);
        etUnit = view.findViewById(R.id.etUnit);
        tvExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
    }


}
