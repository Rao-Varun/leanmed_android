package edu.uta.leanmed.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.uta.leanmed.pojo.Box;
import edu.uta.leanmed.pojo.BoxContent;
import edu.uta.leanmed.pojo.Inventory;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.SharedPreferenceService;
import edu.uta.leanmed.services.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MakeABoxAddMedicineInfoFragment extends Fragment {
    private TextView textUser;
    private TextView textPlus;
    private TextView textMinus;
    private EditText etUnit;
    private Button buttonCancel;
    private Button buttonSubmit;
    private Box box;
    private BoxContent boxContent = new BoxContent();
    private List<User> userList;
    private UserService service;
    private User user;
    private Inventory inventory;
    private Boolean returnBack = false;

    public MakeABoxAddMedicineInfoFragment() {
        // Required empty public constructor
    }

    public void setReturnBack(Boolean returnBack){
        this.returnBack = returnBack;
    }


    public static MakeABoxAddMedicineInfoFragment newInstance(String param1, String param2) {
        MakeABoxAddMedicineInfoFragment fragment = new MakeABoxAddMedicineInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_make_abox_add_medicine_info, container, false);
        this.getBoxValueFromBundle();
        this.initServices();
        this.getUserByZone(view);
        this.setUnitTextView(view);
        this.setButtonLayoutElements(view);
        return view;
    }

    private void initServices() {
        this.user = SharedPreferenceService.getSavedObjectFromPreference(
                getActivity().getApplicationContext(), SharedPreferenceService.getUserName());
        this.service = RetrofitService.newInstance().create(UserService.class);
    }

    private void getUserByZone(final View view) {
        Call<List<User>> userListResponse = this.service.getUserByZone(user.getEmailId(), user.getToken(), box.getDestinationZone().getZoneId());
        userListResponse.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.body() != null){
                    userList = response.body();
                    setUserNameTextView(view);

                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.password_change_unsuccesfull),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getBoxValueFromBundle() {
        this.box = (Box)getArguments().getSerializable("boxObject");
        this.inventory = (Inventory)getArguments().getSerializable("inventoryObject");
    }

    private void setUnitTextView(View view) {
        this.DefineUnitLayoutElements(view);
        this.setOnClickListnersForElements(view);
    }

    private void setOnClickListnersForElements(View view) {
        textMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int unitVal = Integer.parseInt(etUnit.getText().toString());
                if(unitVal>1)
                    etUnit.setText(Integer.toString(unitVal-1));
            }
        });

        textPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int unitVal = Integer.parseInt(etUnit.getText().toString());
                if(unitVal<inventory.getUnits())
                    etUnit.setText(Integer.toString(unitVal+1));
            }
        });
    }

    private void DefineUnitLayoutElements(View view) {
        textPlus = view.findViewById(R.id.textPlus);
        textMinus = view.findViewById(R.id.textMinus);
        etUnit = view.findViewById(R.id.etUnit);
    }

    private void setUserNameTextView(View view) {
        textUser = view.findViewById(R.id.textVolunteer);
        textUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseRecdonVolunteerDialog chooseRecdonVolunteerDialog = new ChooseRecdonVolunteerDialog(getContext(), userList);
                ChooseRecdonVolunteerDialog.DialogListener dialogListener = new ChooseRecdonVolunteerDialog.DialogListener(){

                    @Override
                    public void onItemClick(int position){
                        boxContent.setReceivingUser(userList.get(position));
                        textUser.setText(userList.get(position).getUserName());
                    }
                };
                chooseRecdonVolunteerDialog.setDialogListener(dialogListener);
                chooseRecdonVolunteerDialog.show();
            }
        });

    }

    private void setButtonLayoutElements(final View view) {
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            private Integer unitVal;

            @Override
            public void onClick(View view) {
                this.getUnitValue();
                if(this.isUnitValueNotValid())
                    return;
                this.setBoxContent();
            }

            private void setBoxContent() {
                boxContent.setUnits(unitVal);
                boxContent.setInventory(inventory);
                if(box.getBoxContent()==null)
                    box.setBoxContent(new ArrayList<BoxContent>());
                box.getBoxContent().add(boxContent);
                startNextFragment();
            }

            private boolean isUnitValueNotValid() {
                Integer maxVal = inventory.getUnits();
                if(unitVal>maxVal || unitVal < 1) {
                    etUnit.setError(getString(R.string.error_unit_value));
                    return true;
                }
                return false;
            }

            private void getUnitValue() {
                unitVal = Integer.parseInt(etUnit.getText().toString());
            }
        });

        buttonCancel = view.findViewById(R.id.buttonReject);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNextFragment();
            }
        });
    }

    private void startNextFragment(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("boxObject", box);
        Fragment fragment;
        getFragmentManager().popBackStack();
        if(returnBack){
             fragment = new MakeABoxFinalPreviewFragment();
        }
        else {

            fragment = new MakeABoxChooseMedicineFragment();
        }
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.forgotPwContent, fragment);
        transaction.commit();
    }
}
