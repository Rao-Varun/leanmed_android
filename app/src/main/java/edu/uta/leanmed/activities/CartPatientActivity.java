package edu.uta.leanmed.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import edu.uta.leanmed.pojo.CartItem;
import edu.uta.leanmed.pojo.Order;
import edu.uta.leanmed.pojo.OrderResponse;
import edu.uta.leanmed.pojo.Patient;
import edu.uta.leanmed.pojo.Request;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.OrderAPIService;
import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.SharedPreferenceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartPatientActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    private EditText mFName,mLName,mEmail,mContact,mAddress,mCity,mState,mCountry,mBirthday;
    private Button mPrescription,mCancel,mSubmit;
    private List<CartItem> cartItems;
    private User user;
    private OrderAPIService orderAPIService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_patient);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.please_wait_dailog));
        pDialog.setCancelable(false);
        cartItems=SharedPreferenceService.getCart(getBaseContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        orderAPIService= RetrofitService.newInstance().create(OrderAPIService.class);
        user=SharedPreferenceService.getSavedObjectFromPreference(getBaseContext(),SharedPreferenceService.getUserName());
        mFName=findViewById(R.id.fname);
        mLName=findViewById(R.id.lname);
        mEmail=findViewById(R.id.etpassword1);
        mContact=findViewById(R.id.contact);
        mAddress=findViewById(R.id.address);
        mCity=findViewById(R.id.city);
        mState=findViewById(R.id.state);
        mCountry=findViewById(R.id.country);
        mBirthday=findViewById(R.id.birthday);
        mPrescription=findViewById(R.id.uploadPrescription);
        mCancel=findViewById(R.id.discard);
        mSubmit=findViewById(R.id.submit);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(CartPatientActivity.this)
                        .setTitle(getString(R.string.discard_title))
                        .setMessage(getString(R.string.discard_detail))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                getBaseContext().getSharedPreferences("cart", 0).edit().clear().commit();
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
                mBirthday.setText(sdf.format(myCalendar.getTime()));
            }
        };
        mBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CartPatientActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeOrder();
            }
        });
        createDummyData();
    }

    private void createDummyData(){
        mFName.setText("Vikrant");
        mLName.setText("Shinde");
        mEmail.setText("vikrant.shinde1994@gmail.com");
        mContact.setText("3134136033");
        mAddress.setText("419 Summit Ave");
        mCity.setText("Arlington");
        mState.setText("TX");
        mCountry.setText("United States");
        mBirthday.setText("04/17/19");
    }

    public void placeOrder(){
        mFName.setError(null);
        mLName.setError(null);
        mEmail.setError(null);
        mContact.setError(null);
        mAddress.setError(null);
        mCity.setError(null);
        mState.setError(null);
        mCountry.setError(null);
        mBirthday.setError(null);
        String fName=mFName.getText().toString();
        String lName=mLName.getText().toString();
        String email = mEmail.getText().toString();
        String contact=mContact.getText().toString();
        String address=mAddress.getText().toString();
        String city=mCity.getText().toString();
        String state=mState.getText().toString();
        String country=mCountry.getText().toString();
        String birthday=mBirthday.getText().toString();
        boolean cancel = false;
        View focusView = null;
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
        if(TextUtils.isEmpty(fName)){
            mFName.setError(getString(R.string.error_field_required));
            focusView=mFName;
            cancel=true;
        }
        if(TextUtils.isEmpty(lName)){
            mLName.setError(getString(R.string.error_field_required));
            focusView=mLName;
            cancel=true;
        }
        if(TextUtils.isEmpty(address)){
            mAddress.setError(getString(R.string.error_field_required));
            focusView=mAddress;
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
        if(TextUtils.isEmpty(birthday)){
            mBirthday.setError(getString(R.string.error_field_required));
            focusView=mBirthday;
            cancel=true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            showpDialog();
            Patient patient=new Patient(fName,lName,birthday,contact,address,email,city,state,country);
            Order order=new Order();
            order.setCreatedUser(user);
            order.setPatient(patient);
            List<Request> requests=new ArrayList<>();
            for (CartItem cartItem:cartItems)
                requests.add(new Request(cartItem.getInventory(),cartItem.getRequestedZone(),cartItem.getCount()));
            order.setRequests(requests);
            Call<OrderResponse> orderCall = orderAPIService.placeOrder(user.getEmailId(),user.getToken(),order);
            orderCall.enqueue(new Callback<OrderResponse>() {
                @Override
                public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                    SharedPreferenceService.setCart(getBaseContext(),new ArrayList<CartItem>());
                    hidepDialog();
                    finish();
                }

                @Override
                public void onFailure(Call<OrderResponse> call, Throwable t) {
                    hidepDialog();
                }
            });
        }
    }
    private boolean isContactValid(String contact) {
        return contact.length()>9 && contact.length() < 14;
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
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
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
