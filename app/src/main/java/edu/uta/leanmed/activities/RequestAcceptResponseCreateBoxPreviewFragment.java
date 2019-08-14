package edu.uta.leanmed.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import edu.uta.leanmed.adapters.BoxPreviewAdapter;
import edu.uta.leanmed.pojo.Box;
import edu.uta.leanmed.pojo.BoxContent;
import edu.uta.leanmed.pojo.Request;


public class RequestAcceptResponseCreateBoxPreviewFragment extends RequestAcceptResponseBoxPreviewFragment {
    private Box box;
    private Request request;
    private Button acceptButton;
    private Button cancelButton;
    private BoxPreviewAdapter boxPreviewAdapter;
    private RecyclerView recyclerView;


    public RequestAcceptResponseCreateBoxPreviewFragment() {
        // Required empty public constructor
    }

    public static RequestAcceptResponseCreateBoxPreviewFragment newInstance(String param1, String param2) {
        RequestAcceptResponseCreateBoxPreviewFragment fragment = new RequestAcceptResponseCreateBoxPreviewFragment();
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
        View view =  inflater.inflate(R.layout.fragment_request_accept_response_create_box_preview, container, false);
        this.getFragmentObject();
        this.setBoxAdapter();
        this.setRecyclerView(view);
        this.setAcceptButton(view);
        this.setCancelButton(view);
        return view;

    }


    private void setCancelButton(View view) {
        this.cancelButton = view.findViewById(R.id.buttonCancel);
        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();
            }
        });

    }

    private void setAcceptButton(View view) {
        acceptButton = view.findViewById(R.id.buttonAccept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestAcceptResponseCreateBoxAddDetailsFragment fragment = new RequestAcceptResponseCreateBoxAddDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("boxObject", box);
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

    private void setRecyclerView(View view) {
        recyclerView=view.findViewById(R.id.recyclerViewBox);
        StaggeredGridLayoutManager mStaggeredLayoutManager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(boxPreviewAdapter);
    }

    private void setBoxAdapter() {
        List<Object> objectList = this.getListForAdapter();
        boxPreviewAdapter = new BoxPreviewAdapter(getContext(), objectList);
        boxPreviewAdapter.setOnItemClickListener(new BoxPreviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        boxPreviewAdapter.setOnItemLongClickListener(new BoxPreviewAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    private List<Object> getListForAdapter() {
        List<Object> objectList = new ArrayList<>();
        objectList.add(box);
        objectList.add(box.getDestinationZone());
//        for(BoxContent boxContent : box.getBoxContent())
//            objectList.add(boxContent);
        return objectList;
    }

    private void getFragmentObject() {
        request = (Request) getArguments().getSerializable("requestObject");
        box = (Box) getArguments().getSerializable("boxObject");

    }



}
