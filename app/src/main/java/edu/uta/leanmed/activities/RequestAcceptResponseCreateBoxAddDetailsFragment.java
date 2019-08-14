package edu.uta.leanmed.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.uta.leanmed.pojo.Box;
import edu.uta.leanmed.pojo.BoxContent;
import edu.uta.leanmed.pojo.BoxResponse;
import edu.uta.leanmed.pojo.Request;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.BoxAPIService;
import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.SharedPreferenceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RequestAcceptResponseCreateBoxAddDetailsFragment extends Fragment {
    private Request request;
    private Box box;
    private Button buttonSubmit, buttonCancel;
    private Spinner spinnerUnits;
    private EditText etComments;
    private User user;
    private BoxAPIService boxAPIService;

    public RequestAcceptResponseCreateBoxAddDetailsFragment() {
        // Required empty public constructor
    }


    public static RequestAcceptResponseCreateBoxAddDetailsFragment newInstance(String param1, String param2) {
        RequestAcceptResponseCreateBoxAddDetailsFragment fragment = new RequestAcceptResponseCreateBoxAddDetailsFragment();
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
        View view = inflater.inflate(R.layout.fragment_request_accept_response_create_box_add_details, container, false);
        this.initServices();
        this.getPreviousObjects();
        this.setAllLayoutElements(view);
        return view;
    }

    private void initServices() {
        this.user = SharedPreferenceService.getSavedObjectFromPreference(
                getActivity().getApplicationContext(), SharedPreferenceService.getUserName());
        this.boxAPIService = RetrofitService.newInstance().create(BoxAPIService.class);
    }


    private void getPreviousObjects() {
        request = (Request) getArguments().getSerializable("requestObject");
        box = (Box) getArguments().getSerializable("boxObject");

    }

    private void setAllLayoutElements(View view) {
        this.setSpinnerElement(view);
        this.setEditTextElement(view);
        this.setAllButtons(view);
    }

    private void setAllButtons(View view) {
        this.setSubmitButton(view);
        this.setCancelButton(view);
    }

    private void setSubmitButton(View view) {
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            private int units;
            private String comments;
            @Override
            public void onClick(View view) {
                this.getAllLayoutValues(view);
                this.getBoxObject();
                this.submitBoxToServer();
            }

            private void getAllLayoutValues(View view) {
                units = Integer.parseInt(spinnerUnits.getSelectedItem().toString());
                comments = etComments.getText().toString();
            }

            private void getBoxObject() {
                request.setComments(comments);
                List<BoxContent> boxContentList = new ArrayList<>();
                boxContentList.add(new BoxContent(request.getInventory(), request, request.getCreatedUser(), units));
                box.setBoxContent(boxContentList);

            }

            private void submitBoxToServer() {
                Call<BoxResponse> boxResponseCall = boxAPIService.addNewBox(user.getEmailId(), user.getToken(), box);
                boxResponseCall.enqueue(new Callback<BoxResponse>() {
                    @Override
                    public void onResponse(Call<BoxResponse> call, Response<BoxResponse> response) {
                        if(response.body() != null)
                        {
                            FragmentManager fm = getFragmentManager();
                            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                                fm.popBackStack();
                            }
                            startNextFragment(new RequestFragment());
                        }
                        else
                            Toast.makeText(getContext(), getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(Call<BoxResponse> call, Throwable t) {
                        Toast.makeText(getContext(), getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

    private void startNextFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.forgotPwContent, fragment);
        transaction.commit();
    }


    private void setCancelButton(View view) {


    }

    private void setEditTextElement(View view) {
        etComments = view.findViewById(R.id.editTextContent);
    }

    private void setSpinnerElement(View view) {
        this.spinnerUnits = view.findViewById(R.id.spinnerUnit);
        String[] spinnerList = this.getSpinnerList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerUnits.setAdapter(adapter);
    }

    private String[] getSpinnerList() {
        int count = request.getQuantity();
        String[] spinnerList = new String[count];
        while(count>0){
            spinnerList[count-1] = Integer.toString(count);
            count -= 1;
        }
        return spinnerList;
    }



}
