package edu.uta.leanmed.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import edu.uta.leanmed.adapters.BoxAdapter;
import edu.uta.leanmed.adapters.RequestAdapter;
import edu.uta.leanmed.pojo.Request;
import edu.uta.leanmed.pojo.RequestResponse;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.BoxAPIService;
import edu.uta.leanmed.services.RequestAPIService;
import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.SharedPreferenceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryRequestChooseRequestFragment extends Fragment {
    private List<Request> requestList;
    private User user;
    private RequestAPIService requestAPIService;
    private RecyclerView recyclerView;

    public HistoryRequestChooseRequestFragment() {
        // Required empty public constructor
    }


    public static HistoryRequestChooseRequestFragment newInstance(String param1, String param2) {
        HistoryRequestChooseRequestFragment fragment = new HistoryRequestChooseRequestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_request_choose_request, container, false);
        this.initServices();
        this.getRequestList(view);
        return view;
    }

    private void initServices() {
        this.user = SharedPreferenceService.getSavedObjectFromPreference(
                getActivity().getApplicationContext(), SharedPreferenceService.getUserName());
        this.requestAPIService = RetrofitService.newInstance().create(RequestAPIService.class);
    }

    private void getRequestList(final View view) {
        Call<RequestResponse> requestResponseCall = requestAPIService.getRequestsByAcceptedUser(user.getEmailId(), user.getToken(), user.getEmailId());
        requestResponseCall.enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                if(response.body()!=null){
                    RequestAdapter requestAdapter = this.getrequestAdapter(response.body().getRequests());
                    this.setRecyclerView(requestAdapter);

                }
                else
                    Toast.makeText(getContext(), getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG).show();

            }

            private RequestAdapter getrequestAdapter(final List<Request> requestList) {
                final RequestAdapter requestAdapter = new  RequestAdapter(getContext(), requestList);
                requestAdapter.setOnItemClickListener(new RequestAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        HistoryRequestEditRequestFragment fragment = new HistoryRequestEditRequestFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("requestObject", requestList.get(position));
                        fragment.setArguments(bundle);
                        startNextFragment(fragment);
                    }
                });
                return requestAdapter;
            }

            private void setRecyclerView(RequestAdapter requestAdapter) {
                recyclerView=view.findViewById(R.id.recyclerViewRequest);
                StaggeredGridLayoutManager mStaggeredLayoutManager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(mStaggeredLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(requestAdapter);
            }

            private void startNextFragment(Fragment fragment) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.forgotPwContent, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

            @Override
            public void onFailure(Call<RequestResponse> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG).show();

            }
        });
    }


}
