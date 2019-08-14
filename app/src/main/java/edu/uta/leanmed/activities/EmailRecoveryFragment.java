package edu.uta.leanmed.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class EmailRecoveryFragment extends Fragment {
    private EditText email;
    private Button btnRecover;
    private TextView returnLogin;
    private UserService service;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER




    public EmailRecoveryFragment() {
        // Required empty public constructor
    }


    public static EmailRecoveryFragment newInstance() {
        EmailRecoveryFragment fragment = new EmailRecoveryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }


    public void startSetPasswordRecoveryFragment(String email){
        EnterOtpRecoveryFragment fgmt = new EnterOtpRecoveryFragment();
        Bundle args = new Bundle();
        args.putString("email", email);
        fgmt.setArguments(args);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.forgotPwContent, fgmt );
        transaction.addToBackStack(null);
        transaction.commit();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_email_recovery, container, false);
        returnLogin=view.findViewById(R.id.login);
        btnRecover=view.findViewById(R.id.btn_recover_password);
        email=view.findViewById(R.id.etpassword1);
        returnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity().getApplicationContext() ,LoginActivity.class);
                startActivity(intent);

            }
        });
        service = RetrofitService.newInstance().create(UserService.class);
        btnRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email.setError(null);
                final String emailString = email.getText().toString();
                if (TextUtils.isEmpty(emailString)){
                    email.setError(getString(R.string.error_empty_email));
                    email.requestFocus();
                }else if (!isEmailValid(emailString)) {
                    email.setError(getString(R.string.error_invalid_email));
                    email.requestFocus();
                }
                else{
                    Call<Boolean> userCall = service.forgotPass(emailString);
                    userCall.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if(response.body()) {
                                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.forgot_pass_message), Toast.LENGTH_LONG).show();
                                startSetPasswordRecoveryFragment(emailString);
                            }
                            else{
                                email.setError(getString(R.string.error_invalid_email));
                                email.requestFocus();
                            }
                        }
                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            email.setError(getString(R.string.error_invalid_email));
                            email.requestFocus();
                        }
                    });
                }

            }
        });
        return view;
    }


}
