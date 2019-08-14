package edu.uta.leanmed.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import edu.uta.leanmed.pojo.Box;
import edu.uta.leanmed.pojo.BoxContent;
import edu.uta.leanmed.pojo.Request;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.SharedPreferenceService;


public class RequestAcceptResponseCreateBoxFragment extends Fragment {
    private EditText etBoxId;
    private Button buttonCreateBox;
    private Request request;


    public RequestAcceptResponseCreateBoxFragment() {
        // Required empty public constructor
    }


    public static RequestAcceptResponseCreateBoxFragment newInstance(String param1, String param2) {
        RequestAcceptResponseCreateBoxFragment fragment = new RequestAcceptResponseCreateBoxFragment();
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
        View view =  inflater.inflate(R.layout.fragment_request_accept_response_create_box, container, false);
        getRequestObject();
        this.setEditTextElementsInLayout(view);
        this.setButtonElementsInLayout(view);
        return view;
    }

    private void getRequestObject() {
        request = (Request) getArguments().getSerializable("requestObject");
    }


    private void setButtonElementsInLayout(View view) {
        buttonCreateBox = view.findViewById(R.id.buttonCreateBox);
        buttonCreateBox.setOnClickListener(new View.OnClickListener() {
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
                request.setAcceptedUser(new User(user.getEmailId()));
                box = new Box();
                box.setBoxName(boxId);
                box.setCreatedUser(user);
                box.setStatus(1);
                box.setDestinationZone(request.getZone());
                box.setCreationDate(this.getCreationDate());
            }

            private String getCreationDate() {
                Calendar calendar = Calendar.getInstance();
//                int year = calendar.get(Calendar.YEAR);
//                int month = calendar.get(Calendar.MONTH);
//                int day = calendar.get(Calendar.DATE);
//                return String.format("%d-%d-%d",year,month,day);
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                return formatter.format(calendar.getTime());
            }

            private void startNextFragment() {
                Bundle bundle = new Bundle();
                bundle.putSerializable("boxObject", box);
                bundle.putSerializable("requestObject", request);
                Fragment fragment = new RequestAcceptResponseCreateBoxPreviewFragment();
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
