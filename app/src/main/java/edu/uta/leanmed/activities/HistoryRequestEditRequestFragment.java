package edu.uta.leanmed.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import edu.uta.leanmed.pojo.Request;
import edu.uta.leanmed.pojo.RequestResponse;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.RequestAPIService;
import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.SharedPreferenceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryRequestEditRequestFragment extends Fragment {
    private Request request;
    private TextView textCreatedBy, textCreatedDate, textRequestedQuantity;
    private TextView textZoneId, textZoneName, textZoneEmail;
    private TextView textTradeName, textGenericName, textMedicineType, textDosage;
    private TextView textInventory, textExpiry, textBoxId;
    private Button buttonAccept, buttonReject;
    private User user;
    private Spinner spinnerStatus;
    private int spinnerStatusValue = -1;
    private RequestAPIService requestAPIService;


    public HistoryRequestEditRequestFragment() {
        // Required empty public constructor
    }


    public static HistoryRequestEditRequestFragment newInstance(String param1, String param2) {
        HistoryRequestEditRequestFragment fragment = new HistoryRequestEditRequestFragment();
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
        View view =  inflater.inflate(R.layout.fragment_history_request_edit_request, container, false);
        this.initServices();
        this.getRequestObject();
        this.setAllTextElementsInLayout(view);
        this.setButtonElementsInLayout(view);
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

    private void setButtonElementsInLayout(View view) {
        this.setAcceptButton(view);
        this.setRejectButton(view);

    }

    private void setRejectButton(View view) {
        buttonReject = view.findViewById(R.id.buttonReject);
        buttonReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNextFragment(new HistoryRequestChooseRequestFragment());
            }
        });
    }

    private void setAcceptButton(View view) {
        buttonAccept = view.findViewById(R.id.buttonAccept);
        buttonAccept.setEnabled(false);
        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<RequestResponse> requestResponseCall = requestAPIService.changeRequestStatus(user.getEmailId(), user.getToken(), request);
                requestResponseCall.enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if(response.body() != null) {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            startNextFragment(new HistoryRequestChooseRequestFragment());
                        }
                        else
                            Toast.makeText(getContext(), getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<RequestResponse> call, Throwable t) {
                        Toast.makeText(getContext(), getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


    private void setAllTextElementsInLayout(View view) {
        this.setRequestDetails(view);
        this.setZoneDetails(view);
        this.setMedicineDetails(view);
        this.setInventoryDetails(view);
    }

    private void setRequestDetails(View view) {
        this.defineRequestTextViewElements(view);
        this.setStringsForRequest(view);
        this.setSpinnerStatus(view);
    }

    private void setSpinnerStatus(View view) {
        this.spinnerStatus = view.findViewById(R.id.spinnerStatus);
        String[] spinnerList = new String[]{ "Processed", "Dispatched", "Received", "Completed"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerStatus.setAdapter(adapter);
        final int tempSpinnerVal = request.getStatus();
        this.spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position+2 != tempSpinnerVal)
                {
                    request.setStatus(position+2);
                    buttonAccept.setEnabled(true);
                }
                else if(buttonAccept.isEnabled())
                    request.setStatus(tempSpinnerVal);
                    buttonAccept.setEnabled(false);

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void defineRequestTextViewElements(View view) {
        textCreatedBy = view.findViewById(R.id.createdBy);
        textCreatedDate = view.findViewById(R.id.createdDate);
        textRequestedQuantity = view.findViewById(R.id.requestedQuantity);
    }

    private void setStringsForRequest(View view) {
        if(request.getCreatedUser() != null)
            textCreatedBy.setText(String.format("%s %s", getString(R.string.created_by),request.getCreatedUser().getEmailId()));
        textCreatedDate.setText(String.format("%s %s", getString(R.string.created_date),request.getCreatedDate()));
        textRequestedQuantity.setText(String.format("%s %s", getString(R.string.requested_quantity),request.getQuantity()));
    }

    private void setZoneDetails(View view) {
        this.defineZoneTextViewElements(view);
        this.setStringForZone(view);
    }

    private void defineZoneTextViewElements(View view) {
        textZoneId = view.findViewById(R.id.zoneId);
        textZoneName = view.findViewById(R.id.zoneName);
        textZoneEmail = view.findViewById(R.id.zoneEmail);
    }

    private void setStringForZone(View view) {
        textZoneId.setText(String.format("%s %s", getString(R.string.zone_id), request.getZone().getZoneId()));
        textZoneName.setText(String.format("%s %s", getString(R.string.zone_name), request.getZone().getZoneName()));
        textZoneEmail.setText(String.format("%s %s", getString(R.string.zone_email), request.getZone().getZoneEmail()));
    }

    private void setMedicineDetails(View view) {
        this.defineMedicineTextElements(view);
        this.setStringForMedicine(view);
    }

    private void defineMedicineTextElements(View view) {
        textGenericName = view.findViewById(R.id.genName);
        textTradeName = view.findViewById(R.id.tradeName);
        textMedicineType = view.findViewById(R.id.medicineType);
        textDosage = view.findViewById(R.id.dosage);
    }

    private void setStringForMedicine(View view) {
        textTradeName.setText(String.format("%s %s", getString(R.string.trade_name), request.getInventory().getMedicine().getTradeName()));
        textGenericName.setText(String.format("%s %s", getString(R.string.generic_name), request.getInventory().getMedicine().getGenName()));
        textMedicineType.setText(String.format("%s %s", getString(R.string.med_input), request.getInventory().getMedicine().getMedicineType()));
    }

    private void setInventoryDetails(View view) {
        this.defineInventoryTextElements(view);
        this.setStringsForInventory(view);
    }

    private void defineInventoryTextElements(View view) {
        textInventory = view.findViewById(R.id.inventory);
        textExpiry = view.findViewById(R.id.expiryDate);
        textBoxId = view.findViewById(R.id.idBox);
    }

    private void setStringsForInventory(View view) {
        textInventory.setText(String.format("%s %s", getString(R.string.inventory), request.getInventory().getUnits()));
        textExpiry.setText(String.format("%s %s", getString(R.string.expiry), request.getInventory().getExpiryDate()));
        if(request.getInventory().getIdBox() != null)
            textBoxId.setText(String.format("%s %s", getString(R.string.box_id), request.getInventory().getIdBox()));
    }

    private void startNextFragment(Fragment fragment)
    {
        getFragmentManager().popBackStack();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.forgotPwContent, fragment);
        transaction.commit();
    }



}
