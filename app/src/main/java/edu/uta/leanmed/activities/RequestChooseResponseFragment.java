package edu.uta.leanmed.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.uta.leanmed.pojo.Request;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.RequestAPIService;
import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.SharedPreferenceService;


public class RequestChooseResponseFragment extends Fragment {
    private Request request;
    private TextView textCreatedBy, textCreatedDate, textRequestedQuantity;
    private TextView textZoneId, textZoneName, textZoneEmail;
    private TextView textTradeName, textGenericName, textMedicineType, textDosage;
    private TextView textInventory, textExpiry, textBoxId;
    private Button buttonAccept, buttonReject;

    public RequestChooseResponseFragment() {
        // Required empty public constructor
    }


    public static RequestChooseResponseFragment newInstance(String param1, String param2) {
        RequestChooseResponseFragment fragment = new RequestChooseResponseFragment();
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
        View view = inflater.inflate(R.layout.fragment_request_choose_response, container, false);
        this.getRequestObject();
        this.setAllTextElementsInLayout(view);
        this.setButtonElementsInLayout(view);
        return view;

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
                startNextFragment(new RequestRejectResponseFragment());
            }
        });
    }

    private void setAcceptButton(View view) {
        buttonAccept = view.findViewById(R.id.buttonAccept);
        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNextFragment(new RequestAcceptResponseChooseBoxFragment());
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
        Bundle bundle = new Bundle();
        bundle.putSerializable("requestObject", request);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.forgotPwContent, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
