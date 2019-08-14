package edu.uta.leanmed.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uta.leanmed.pojo.Request;


public class RequestAcceptResponseChooseBoxFragment extends Fragment {
    private CardView cardExistingBox, cardNewBox;
    private Request request;

    public RequestAcceptResponseChooseBoxFragment() {
        // Required empty public constructor
    }

   public static RequestAcceptResponseChooseBoxFragment newInstance(String param1, String param2) {
        RequestAcceptResponseChooseBoxFragment fragment = new RequestAcceptResponseChooseBoxFragment();
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
        View view = inflater.inflate(R.layout.fragment_request_accept_response_choose_box, container, false);
        this.getRequestObject();
        this.setAllCardInLayout(view);
        return view;
    }

    private void getRequestObject() {
        request = (Request) getArguments().getSerializable("requestObject");

    }

    private void setAllCardInLayout(View view) {
        this.setChooseBoxCard(view);
        this.setCreateNewBox(view);
    }

    private void setChooseBoxCard(View view) {
        cardExistingBox = view.findViewById(R.id.cardExistingBox);
        cardExistingBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestAcceptResponseExistingBoxFragment fragment = new RequestAcceptResponseExistingBoxFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("requestObject", request);
                fragment.setArguments(bundle);
                startNextFragment(fragment);            }
        });
    }

    private void setCreateNewBox(View view) {
        cardNewBox = view.findViewById(R.id.cardNewBox);
        cardNewBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestAcceptResponseCreateBoxFragment fragment = new RequestAcceptResponseCreateBoxFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("requestObject", request);
                fragment.setArguments(bundle);
                startNextFragment(fragment);
            }
        });

    }

    private void startNextFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.forgotPwContent, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
