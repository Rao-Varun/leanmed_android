package edu.uta.leanmed.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import edu.uta.leanmed.pojo.Request;
import edu.uta.leanmed.pojo.RequestResponse;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.RequestAPIService;
import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.SharedPreferenceService;


public class RequestRejectResponseFragment extends Fragment {
    private User user;
    private RequestAPIService requestAPIService;
    private Request request;
    private EditText etComment;
    private Button buttonSubmit, buttonCancel;

    public RequestRejectResponseFragment() {
        // Required empty public constructor
    }


    public static RequestRejectResponseFragment newInstance(String param1, String param2) {
        RequestRejectResponseFragment fragment = new RequestRejectResponseFragment();
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
        View view = inflater.inflate(R.layout.fragment_request_reject_response, container, false);
        this.initServices();
        this.getRequestObject();
        this.setCommentBox(view);
        this.setButtonElements(view);
        return view;
    }

    private void initServices() {
        this.user = SharedPreferenceService.getSavedObjectFromPreference(
                getActivity().getApplicationContext(), SharedPreferenceService.getUserName());
        this.requestAPIService = RetrofitService.newInstance().create(RequestAPIService.class);
    }

    private void getRequestObject() {
        request = (Request) getArguments().getSerializable("requestObject");

    }

    private void setButtonElements(View view) {
        this.setSubmitButton(view);
        this.setCancelButton(view);
    }

    private void setCancelButton(View view) {
        buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("requestObject", request);
                Fragment fragment = new RequestChooseResponseFragment();
                fragment.setArguments(bundle);
                startNextFragment(fragment);
            }
        });

    }

    private void setSubmitButton(View view) {
        this.buttonSubmit = view.findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            private String comment;
            @Override
            public void onClick(View view) {
                if(this.isCommentValid()) {
                    this.SubmitRequestObject();
                }
            }

            private void SubmitRequestObject() {
                request.setComments(comment);
                User acceptedUser = new User(user.getEmailId());
                request.setAcceptedUser(acceptedUser);
                Call<RequestResponse> requestResponseCall = requestAPIService.rejectRequest(user.getEmailId(), user.getToken(), request);
                requestResponseCall.enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if(response.body() != null)
                        {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            startNextFragment(new RequestFragment());
                        }
                        else
                            Toast.makeText(getContext(), getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(Call<RequestResponse> call, Throwable t) {
                        Toast.makeText(getContext(), getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG);
                    }
                });

            }

            private Boolean isCommentValid() {
                this.comment = etComment.getText().toString().toString();
                if(this.comment.isEmpty())
                    return false;
                return true;
            }
        });
    }

    private void setCommentBox(View view) {
        etComment = view.findViewById(R.id.editTextComment);
    }

    private void startNextFragment(Fragment fragment) {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.forgotPwContent, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
