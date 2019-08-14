package edu.uta.leanmed.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EnterOtpRecoveryFragment extends Fragment {
    private EditText etOtp;
    private Button btnSubmitOtp;
    private UserService service = RetrofitService.newInstance().create(UserService.class);
    private String email;

    public EnterOtpRecoveryFragment() {
        // Required empty public constructor
    }

    public static EnterOtpRecoveryFragment newInstance(String param1, String param2) {
        EnterOtpRecoveryFragment fragment = new EnterOtpRecoveryFragment();
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
        View view =  inflater.inflate(R.layout.fragment_enter_otp_recovery, container, false);
        this.getUserEmail();
        this.setAllLayoutElement(view);
        return view;
    }

    private void getUserEmail() {
        this.email = getArguments().getString("email");
    }

    private void setAllLayoutElement(View view) {
        this.setEditTextElement(view);
        this.setSubmitButtonElement(view);
    }

    private void setSubmitButtonElement(View view) {
        this.btnSubmitOtp = view.findViewById(R.id.btn_submit_opt);
        this.setupBtnListener(view);
    }

    private void setupBtnListener(View view) {

        this.btnSubmitOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otpVal = this.getOtpValueFromUser();
                if (this._isOtpValid(otpVal))
                    this.submitOtpToServer(otpVal);

            }

            private void submitOtpToServer(String otpVal) {
                User user = new User(email, otpVal);
                Call<Boolean> userCall = service.verifyOtp(user);
                userCall.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.body()) {
                            this.startNextFragment();
                        } else {
                            etOtp.setError(getString(R.string.error_invalid_otp));
                        }
                    }

                    private void startNextFragment() {
                        SetPasswordRecoveryFragment fgmt = new SetPasswordRecoveryFragment();
                        Bundle args = new Bundle();
                        args.putString("email", email);
                        fgmt.setArguments(args);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.forgotPwContent, fgmt );
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG).show();
                    }
                });
            }

            private boolean _isOtpValid(String otp) {
                if (otp.isEmpty()) {
                    etOtp.setError(getString(R.string.error_field_required));
                    return false;
                }
                return true;
            }


            private String getOtpValueFromUser() {
                String otp = etOtp.getText().toString();
                return otp;
            }

        });
    }

    private void setEditTextElement(View view) {
        this.etOtp = view.findViewById(R.id.etOpt);

    }


}
