package edu.uta.leanmed.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.pojo.Zone;
import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity{
    private EditText mName, mContact, mEmail, mPassword, mConfirmPass,mUserAddress, mCity, mState, mCountry;
    private RadioGroup mType,mZone,mLanguagePref;
    private RadioButton rType,rLanguagePref;
    private Button mBtnZone,mBtnRegister;
    private View mProgressView,mView;
    private UserService service;
    private TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        mName=findViewById(R.id.name);
        mEmail = findViewById(R.id.etpassword1);
        mContact=findViewById(R.id.contact);
        mPassword = findViewById(R.id.password);
        mConfirmPass=findViewById(R.id.confirmPassword);
        mUserAddress=findViewById(R.id.address);
        mCity = findViewById(R.id.city);
        mState=findViewById(R.id.state);
        mCountry = findViewById(R.id.country);
        mType=findViewById(R.id.type);
        mBtnZone=findViewById(R.id.selectZone);
        mBtnRegister = findViewById(R.id.btnRegister);
        mLanguagePref=findViewById(R.id.langPref);
        service = RetrofitService.newInstance().create(UserService.class);
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(RegisterActivity.this);
        mView=getLayoutInflater().inflate(R.layout.dailog_zone, null);
        ImageView close=mView.findViewById(R.id.dailog_close);
        Button selectZone=mView.findViewById(R.id.buttonSelect);
        mZone=mView.findViewById(R.id.add_zone);
        mBuilder.setView(mView);
        final AlertDialog dailog=mBuilder.create();
        mType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id=radioGroup.getCheckedRadioButtonId();
                rType=findViewById(id);
                if(rType.getText().toString().equalsIgnoreCase(getString(R.string.prompt_recDon))){
                    mBtnZone.setVisibility(View.VISIBLE);
                }
                else{
                    mBtnZone.setText(getString(R.string.select_zone));
                    mBtnZone.setVisibility(View.GONE);
                }
            }
        });
        mLanguagePref.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id=radioGroup.getCheckedRadioButtonId();
                rLanguagePref=findViewById(id);
            }
        });
        mBtnZone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dailog.show();
            }
        });
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dailog.dismiss();
            }
        });
        mBtnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        getZones(dailog);

        selectZone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int c=mZone.getCheckedRadioButtonId();
                RadioButton radioButton=mView.findViewById(c);
                mBtnZone.setText("Zone: "+radioButton.getText().toString());
                dailog.dismiss();
            }
        });
        mProgressView = findViewById(R.id.login_progress);
        login=findViewById(R.id.login);
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
//        createDummyRegField();
    }
    public void createDummyRegField(){
        mName.setText("Vaibhav Naik");
        mEmail.setText("vaibhavsnaik09@gmail.com");
        mContact.setText("+16824149593");
        mPassword.setText("password");
        mConfirmPass.setText("password");
        mUserAddress.setText("419 Summit Ave");
        mCity.setText("Arlington");
        mState.setText("TX");
        mCountry.setText("United States");
    }

    private void attemptLogin() {
        mName.setError(null);
        mEmail.setError(null);
        mContact.setError(null);
        mPassword.setError(null);
        mConfirmPass.setError(null);
        mUserAddress.setError(null);
        mCity.setError(null);
        mState.setError(null);
        mCountry.setError(null);
        String name=mName.getText().toString();
        String email = mEmail.getText().toString();
        String contact=mContact.getText().toString();
        String password = mPassword.getText().toString();
        String confirmPass=mConfirmPass.getText().toString();
        String address=mUserAddress.getText().toString();
        String city=mCity.getText().toString();
        String state=mState.getText().toString();
        String country=mCountry.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        } else if(!TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPass) && !password.equalsIgnoreCase(confirmPass)) {
            mConfirmPass.setError(getString(R.string.error_invalid_confirm_password));
            focusView = mConfirmPass;
            cancel = true;
        } else if(TextUtils.isEmpty(confirmPass)){
            mConfirmPass.setError(getString(R.string.error_field_required));
            focusView = mConfirmPass;
            cancel = true;
        }else if(TextUtils.isEmpty(password)){
            mPassword.setError(getString(R.string.error_field_required));
            focusView = mPassword;
            cancel = true;
        }
        if (TextUtils.isEmpty(contact)) {
            mContact.setError(getString(R.string.error_field_required));
            focusView = mContact;
            cancel = true;
        } else if (!isContactValid(contact)) {
            mContact.setError(getString(R.string.error_invalid_contact));
            focusView = mContact;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.error_field_required));
            focusView = mEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEmail;
            cancel = true;
        }
        if(TextUtils.isEmpty(name)){
            mName.setError(getString(R.string.error_field_required));
            focusView=mName;
            cancel=true;
        }
        if(TextUtils.isEmpty(name)){
            mName.setError(getString(R.string.error_field_required));
            focusView=mName;
            cancel=true;
        }
        if(TextUtils.isEmpty(address)){
            mUserAddress.setError(getString(R.string.error_field_required));
            focusView=mUserAddress;
            cancel=true;
        }
        if(TextUtils.isEmpty(city)){
            mCity.setError(getString(R.string.error_field_required));
            focusView=mCity;
            cancel=true;
        }
        if(TextUtils.isEmpty(state)){
            mState.setError(getString(R.string.error_field_required));
            focusView=mState;
            cancel=true;
        }
        if(TextUtils.isEmpty(country)){
            mCountry.setError(getString(R.string.error_field_required));
            focusView=mCountry;
            cancel=true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            if(rType==null) rType=findViewById(mType.getCheckedRadioButtonId());
            if(rLanguagePref==null) rLanguagePref=findViewById(mLanguagePref.getCheckedRadioButtonId());
            User user =new User(name,email,contact,address,city,state,country,password,rType.getText().toString().equals(getString(R.string.prompt_getDon))?1:2,"zone",2,rLanguagePref.getText().toString().equals(getString(R.string.english))?1:2);
            if(user.getType()==2){
                int c=mZone.getCheckedRadioButtonId();
                RadioButton radioButton=mView.findViewById(c);
                user.setZone(new Zone(radioButton.getText().toString()));
                mBtnZone.setText("Zone: "+radioButton.getText().toString());
            }
            user.setUserStatus(2);
            Call<String> userCall = service.addUser(user);
            userCall.enqueue(new Callback<String>(){
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    showProgress(false);
                    String status=response.body();
                    Log.d("status",status);
                    if(status.contains("successful")) {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.putExtra("message",getString(R.string.register_status));
                        startActivity(intent);
                        finish();
                    }else
                        Snackbar.make(findViewById(android.R.id.content),getString(R.string.register_failed_status)+" "+status,Snackbar.LENGTH_LONG).show();
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    showProgress(false);
                    Toast.makeText(RegisterActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    private void getZones(final AlertDialog dailog){
        Call<List<Zone>> call=service.getZones();
        call.enqueue(new Callback<List<Zone>>() {
            @Override
            public void onResponse(Call<List<Zone>> call, Response<List<Zone>> response) {
                List<Zone> zones=response.body();
                for(int i=0;i<zones.size();i++){
                    RadioButton button  = new RadioButton(dailog.getContext());
                    button.setText(zones.get(i).getZoneName()+" ("+zones.get(i).getZoneId()+")");
                    button.setId(123123+i);
                    mZone.addView(button);
                }
            }

            @Override
            public void onFailure(Call<List<Zone>> call, Throwable t) {
                Log.d("error",t.getMessage());
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }
    private boolean isContactValid(String contact) {
        return contact.length()>9 && contact.length() < 14;
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
}

