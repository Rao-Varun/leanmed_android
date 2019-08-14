package edu.uta.leanmed.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.uta.leanmed.adapters.CartAdapter;
import edu.uta.leanmed.adapters.RequestAdapter;
import edu.uta.leanmed.pojo.CartItem;
import edu.uta.leanmed.pojo.InventoryResponse;
import edu.uta.leanmed.pojo.Request;
import edu.uta.leanmed.pojo.RequestResponse;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.MedicineAPIService;
import edu.uta.leanmed.services.OrderAPIService;
import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.SharedPreferenceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestFragment extends Fragment {
    private ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private RequestAdapter requestAdapter;
    private List<Request> requests;
    private OrderAPIService orderAPIService;
    private User user;
    private Request request;
    public RequestFragment() {
    }

    public static RequestFragment newInstance() {
        RequestFragment fragment = new RequestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_request, container, false);
//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage(getString(R.string.please_wait_dailog));
//        pDialog.setCancelable(false);
        orderAPIService= RetrofitService.newInstance().create(OrderAPIService.class);
        user=SharedPreferenceService.getSavedObjectFromPreference(getContext(),SharedPreferenceService.getUserName());
        setView(view);
        return view;
    }

    public void setView(final View view) {
//        showpDialog();
        Call<RequestResponse> requestCall = orderAPIService.getRequests(user.getEmailId(),user.getToken(),user.getZone().getZoneId());
        requestCall.enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    RequestResponse requestResponse= response.body();
                    requests=requestResponse.getRequests();
                    Log.d("requests",requests.toString());
                    requestAdapter=new RequestAdapter(getContext(),requests);
                    requestAdapter.setOnItemClickListener(onItemClickListener);
                    setRecyclerView(view);
                    recyclerView.setAdapter(requestAdapter);
//                    hidepDialog();
                }

                @Override
                public void onFailure(Call<RequestResponse> call, Throwable t) {
                    Log.d("requests",t.getLocalizedMessage());
//                    hidepDialog();
                }
            });
    }

    private void setRecyclerView(View view){
        StaggeredGridLayoutManager mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView=view.findViewById(R.id.request_card_view);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
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

    RequestAdapter.OnItemClickListener onItemClickListener=new RequestAdapter.OnItemClickListener(){
        @Override
        public void onItemClick(View view, int position) {
            request = requests.get(position);
            startNextFragment(new RequestChooseResponseFragment());
        }
    };
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
