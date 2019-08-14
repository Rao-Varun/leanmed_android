package edu.uta.leanmed.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

import edu.uta.leanmed.pojo.Box;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.SharedPreferenceService;


public class MakeABoxAddBoxNameFragment extends Fragment {
    private EditText etBoxId;
    private Button buttonChooseZone;
    public MakeABoxAddBoxNameFragment() {
        // Required empty public constructor
    }


    public static MakeABoxAddBoxNameFragment newInstance(String param1, String param2) {
        MakeABoxAddBoxNameFragment fragment = new MakeABoxAddBoxNameFragment();
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
        View view  = inflater.inflate(R.layout.fragment_make_abox_add_box_name, container, false);
        this.setEditTextElementsInLayout(view);
        this.setButtonElementsInLayout(view);
        return view;
    }

    private void setButtonElementsInLayout(View view) {
        buttonChooseZone = view.findViewById(R.id.buttonChooseZone);
        buttonChooseZone.setOnClickListener(new View.OnClickListener() {
            private String boxId;
            private User user;
            private Box box;

            @Override
            public void onClick(View view) {
                this.getBoxIdString();
                if(this.isBoxIdNotValid()){
                    return;
                }
                this.setBoxObject();
                this.startNextFragment();
            }

            private void setBoxObject() {
                user = SharedPreferenceService.getSavedObjectFromPreference(
                        getActivity().getApplicationContext(), SharedPreferenceService.getUserName());
                box = new Box();
                box.setBoxName(boxId);
                box.setCreatedUser(user);
                box.setStatus(1);
                box.setCreationDate(this.getCreationDate());
            }

            private String getCreationDate() {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);
                return String.format("%d-%d-%d",year,month,day);
            }

            private void startNextFragment() {
                Bundle bundle = new Bundle();
                bundle.putSerializable("boxObject", box);
                Fragment fragment = new MakeABoxSelectZoneFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.forgotPwContent, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }

            private boolean isBoxIdNotValid() {
                if(boxId.isEmpty()){
                    etBoxId.setError(getString(R.string.error_field_required));
                    etBoxId.requestFocus();
                    return true;
                }
                return false;
            }

            private void getBoxIdString() {
                boxId = etBoxId.getText().toString();
            }
        });
    }

    private void setEditTextElementsInLayout(View view) {
        etBoxId = view.findViewById(R.id.etBoxID);

    }


}
