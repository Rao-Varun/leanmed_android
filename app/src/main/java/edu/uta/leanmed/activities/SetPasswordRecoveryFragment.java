package edu.uta.leanmed.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class SetPasswordRecoveryFragment extends Fragment {
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button buttonSetPassword;
    private UserService service = RetrofitService.newInstance().create(UserService.class);
    private String email;

    public SetPasswordRecoveryFragment() {
        // Required empty public constructor
    }


    public static SetPasswordRecoveryFragment newInstance(String param1, String param2) {
        SetPasswordRecoveryFragment fragment = new SetPasswordRecoveryFragment();
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
        View view = inflater.inflate(R.layout.fragment_set_password_recovery, container, false);
        email = getArguments().getString("email");
        this.setLayoutElements(view);
        return view;
    }

    private void setLayoutElements(View view) {
        this.setEditTextsForPasswords(view);
        this.setSubmitPasswordButton(view);
    }


    private void setSubmitPasswordButton(View view) {
        this.buttonSetPassword = view.findViewById(R.id.btn_submit_password);
        this.setPasswordButtonListener(view);
    }

    private void setPasswordButtonListener(View view) {
        buttonSetPassword.setOnClickListener(new View.OnClickListener() {
            String password;

            @Override
            public void onClick(View view) {
                if (this._isNewPasswordValid())
                    this.submitPasswordToServer(view);
            }

            private void submitPasswordToServer(View view) {
                User user = new User(email, password);
                Call<Boolean> userCall = service.setPass(user);
                userCall.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.body()) {
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.password_change_succesfull), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG).show();
                    }
                });
            }

            private boolean _isNewPasswordValid() {
                String passwordValue = etPassword.getText().toString();
                String confirmPasswordValue = etConfirmPassword.getText().toString();
                if (this._isPasswordFieldEmpty(passwordValue, confirmPasswordValue))
                    return false;
                if (this._isPasswordNotMatching(passwordValue, confirmPasswordValue))
                    return false;
                if (this._isPasswordSizeSmall(passwordValue))
                    return false;
                this.password = passwordValue;
                return true;
            }

            private boolean _isPasswordSizeSmall(String passwordValue) {
                if (passwordValue.length() < 6) {
                    etPassword.setError(getString(R.string.error_invalid_password));
                    return true;
                }
                return false;
            }

            private boolean _isPasswordNotMatching(String passwordValue, String confirmPasswordValue) {
                if (!passwordValue.equals(confirmPasswordValue)) {
                    etConfirmPassword.setError(getString(R.string.error_invalid_confirm_password));
                    return true;
                }
                return false;

            }

            private boolean _isPasswordFieldEmpty(String passwordValue, String confirmPasswordValue) {
                if (passwordValue.isEmpty()) {
                    etPassword.setError(getString(R.string.error_field_required));
                    return true;
                }
                if (confirmPasswordValue.isEmpty()) {
                    etConfirmPassword.setError(getString(R.string.error_field_required));
                    return true;
                }
                return false;
            }
        });
    }

    private void setEditTextsForPasswords(View view) {
        this.etPassword = view.findViewById(R.id.etpassword1);
        this.etConfirmPassword = view.findViewById(R.id.etPassword2);
    }


}




