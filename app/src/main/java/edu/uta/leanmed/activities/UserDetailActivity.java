package edu.uta.leanmed.activities;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.SharedPreferenceService;
import edu.uta.leanmed.services.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    private User user,userDetail;
    private TextView mUserName,mUserType,mEmail,mContact,mStatus,zoneId,zoneName,zoneEmail;
    private CardView zone;
    private Button submit,deny;
    private Map<Integer,String> statusMap;
    private UserService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.please_wait_dailog));
        pDialog.setCancelable(false);
        statusMap=new HashMap<Integer,String>();
        statusMap.put(1,"Active");
        statusMap.put(2,"Pending");
        statusMap.put(3,"Disabled");
        user= SharedPreferenceService.getSavedObjectFromPreference(getBaseContext(),SharedPreferenceService.getUserName());
        userDetail=(User)getIntent().getSerializableExtra("userDetail");
        service = RetrofitService.newInstance().create(UserService.class);
        getSupportActionBar().setTitle(userDetail.getUserName());
        mUserName=findViewById(R.id.user_name);
        mUserType=findViewById(R.id.user_type);
        mEmail=findViewById(R.id.userEmail);
        mContact=findViewById(R.id.contact);
        mStatus=findViewById(R.id.status);
        zoneId=findViewById(R.id.zoneId);
        zoneName=findViewById(R.id.zoneName);
        zoneEmail=findViewById(R.id.zoneEmail);
        submit=findViewById(R.id.submit);
        deny=findViewById(R.id.deny);
        zone=findViewById(R.id.zone_card_view);
        mUserName.setText(getString(R.string.user_name)+" "+userDetail.getUserName());
        if(userDetail.getType()==1) {
            mUserType.setText(getString(R.string.user_type)+" "+getString(R.string.prompt_getDon));
            zone.setVisibility(View.GONE);
        }
        else
            mUserType.setText(getString(R.string.user_type)+" "+getString(R.string.prompt_recDon));
        mEmail.setText(getString(R.string.prompt_email) +": "+ userDetail.getEmailId());
        mContact.setText(getString(R.string.prompt_contact)+": "+userDetail.getContacts());
        mStatus.setText(getString(R.string.status)+" "+statusMap.get(userDetail.getUserStatus()));
        zoneId.setText(getString(R.string.zone_id)+" "+userDetail.getZone().getZoneId());
        zoneName.setText(getString(R.string.zone_name)+" "+userDetail.getZone().getZoneName());
        zoneEmail.setText(getString(R.string.zone_email)+" "+userDetail.getZone().getZoneEmail());
        switch (userDetail.getUserStatus()){
            case 1:
                submit.setText(getString(R.string.disable));
                deny.setVisibility(View.GONE);
                break;
            case 2:
                submit.setText(getString(R.string.approve));
                deny.setVisibility(View.VISIBLE);
                break;
            case 3:
                submit.setText(getString(R.string.active));
                deny.setVisibility(View.GONE);
                break;
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (userDetail.getUserStatus()){
                    case 1:
                        updateStatus(3);
                        break;
                    case 2:
                        updateStatus(1);
                        break;
                    case 3:
                        updateStatus(1);
                        break;
                }
            }
        });
        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatus(3);
            }
        });
    }

    public void updateStatus(final int status){
        User currentUser=new User();
        currentUser.setEmailId(userDetail.getEmailId());
        currentUser.setUserStatus(status);
        Call<Boolean> userCall = service.updateUserStatus(user.getEmailId(), user.getToken(),currentUser);
        userCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(status==3)
                    Snackbar.make(findViewById(android.R.id.content),getString(R.string.user_denied),Snackbar.LENGTH_LONG).show();
                else {
                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.user_updated), Snackbar.LENGTH_LONG).show();
                }
                mStatus.setText(getString(R.string.status)+" "+statusMap.get(status));
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Snackbar.make(findViewById(android.R.id.content),t.getLocalizedMessage(),Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
