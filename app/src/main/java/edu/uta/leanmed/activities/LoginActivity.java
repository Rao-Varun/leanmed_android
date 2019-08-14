package edu.uta.leanmed.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.pojo.UserResponse;
import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.SharedPreferenceService;
import edu.uta.leanmed.services.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity{
    private EditText mEmailView,mPasswordView;
    private View mProgressView;
    private UserService service;
    private TextView mForgot,mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        if(SharedPreferenceService.getUserName().length() != 0){
            User user = SharedPreferenceService.getSavedObjectFromPreference(LoginActivity.this, SharedPreferenceService.getUserName());
            nextActivity(user);
            finish();
        }
        else {
            Intent intent=getIntent();
            if(intent.getExtras()!=null)
                Snackbar.make(findViewById(android.R.id.content),intent.getStringExtra("message"),Snackbar.LENGTH_LONG).show();
            mEmailView = findViewById(R.id.etpassword1);
            mPasswordView = findViewById(R.id.password);
            mForgot=findViewById(R.id.forgotPass);
            mRegister=findViewById(R.id.register);
            mRegister.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                    startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
            });
            mForgot.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(LoginActivity.this,ForgotActivity.class);
                    startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
            });
            Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
            service = RetrofitService.newInstance().create(UserService.class);
            mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });
            mProgressView = findViewById(R.id.login_progress);
            createDummyRegField();
        }
    }
    public void createDummyRegField(){
        mEmailView.setText("varunrao.2894@gmail.com");
        mPasswordView.setText("1234567890");
    }
    private void attemptLogin() {
        mEmailView.setError(null);
        mPasswordView.setError(null);
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            User user=new User(email,password);
            Call<UserResponse> userCall = service.loginUser(user);
            userCall.enqueue(new Callback<UserResponse>(){
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    showProgress(false);
                    UserResponse user =response.body();
                    if(user !=null) {
                        if(user.getStatus()==200 && user.getUser().getUserStatus()==1) {
                            user.getUser().setToken(user.getToken());
                            SharedPreferenceService.saveObjectToSharedPreference(LoginActivity.this,user.getUser().getEmailId(), user.getUser());
                            SharedPreferenceService.setLocale(getBaseContext(), user.getUser().getLanguagePref()==1?"en":"es");
                            SharedPreferenceService.loadLocale(getBaseContext());
                            showProgress(false);
                            nextActivity(user.getUser());
                            finish();
                        }
                        else{
                            showProgress(false);
                            mPasswordView.setError(user.getMessage());
                            mPasswordView.requestFocus();
                        }
                    }
                    else{
                        showProgress(false);
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    mPasswordView.setError(t.getLocalizedMessage());
                    mPasswordView.requestFocus();
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void nextActivity(User user){
        Intent intent=null;
        if(user.getType()==2)   intent=new Intent(LoginActivity.this,RecdonActivity.class);
        else if(user.getType()==1) intent=new Intent(LoginActivity.this,GetdonActivity.class);
        else intent=new Intent(LoginActivity.this,AdminActivity.class);
        intent.putExtra("user", user);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}

