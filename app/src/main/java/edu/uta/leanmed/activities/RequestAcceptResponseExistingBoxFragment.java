package edu.uta.leanmed.activities;

import android.content.Context;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.uta.leanmed.adapters.BoxAdapter;
import edu.uta.leanmed.pojo.Box;
import edu.uta.leanmed.pojo.BoxResponse;
import edu.uta.leanmed.pojo.Request;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.BoxAPIService;
import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.SharedPreferenceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RequestAcceptResponseExistingBoxFragment extends Fragment {
    private User user;
    private BoxAPIService boxAPIService;
    private List<Box> boxList;
    private RecyclerView recyclerView;
    private BoxAdapter boxAdapter;
    private Request request;

    public RequestAcceptResponseExistingBoxFragment() {
        // Required empty public constructor
    }


    public static RequestAcceptResponseExistingBoxFragment newInstance(String param1, String param2) {
        RequestAcceptResponseExistingBoxFragment fragment = new RequestAcceptResponseExistingBoxFragment();
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
        View view = inflater.inflate(R.layout.fragment_request_accept_response_existing_box, container, false);
        initServices();
        getRequestObject();
        setAllActiveBoxForZone(view);
        return view;
    }

    private void getRequestObject() {
        request = (Request) getArguments().getSerializable("requestObject");
    }

    private void initServices() {
        this.user = SharedPreferenceService.getSavedObjectFromPreference(
                getActivity().getApplicationContext(), SharedPreferenceService.getUserName());
        this.boxAPIService = RetrofitService.newInstance().create(BoxAPIService.class);
    }

    private void setAllActiveBoxForZone(final View view){
        Call<BoxResponse> boxResponseCall = boxAPIService.getBoxByZone(user.getEmailId(), user.getToken(), user.getZone().getZoneId());
        boxResponseCall.enqueue(new Callback<BoxResponse>() {
            @Override
            public void onResponse(Call<BoxResponse> call, Response<BoxResponse> response) {
                if(response.body() != null)
                {
                    boxList = response.body().getBox();

//                    this.test(view);
                    this.setBoxAdapter();
                    this.setRecyclerView(view);
                }
                else {
                    Toast.makeText(getContext(), getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG).show();

                }
            }

            private void test(View itemView) {
                TextView textBoxId, textCreatedBy, textCreatedDate, textStatus, textDestZone;
                textBoxId = itemView.findViewById(R.id.textBoxId);
                textCreatedBy = itemView.findViewById(R.id.textCreatedBy);
                textCreatedDate = itemView.findViewById(R.id.textCreatedDate);
                textStatus = itemView.findViewById(R.id.textStatus);
                textDestZone = itemView.findViewById(R.id.textDestZoneId);
                Box box = boxList.get(1);
                textBoxId.setText(String.format("%s %s",getString(R.string.box_id), box.getBoxName()));
                textCreatedBy.setText(String.format("%s %s",getString(R.string.created_by), box.getCreatedUser()));
                textCreatedDate.setText(String.format("%s %s",getString(R.string.created_date), box.getCreationDate()));
                textDestZone.setText(String.format("%s %s(%s)",getString(R.string.dest_zone), box.getDestinationZone().getZoneName(),box.getDestinationZone().getZoneId() ));
                textStatus.setText(String.format("%s %s",getString(R.string.status),box.getStatus()));
            }

            private void setBoxAdapter() {
                boxAdapter = new BoxAdapter(getContext(), boxList);
                boxAdapter.setOnItemClickListener(new BoxAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        RequestAcceptResponseBoxPreviewFragment fragment = new RequestAcceptResponseBoxPreviewFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("requestObject", request);
                        bundle.putSerializable("boxObject", boxList.get(position));
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
                recyclerView.setAdapter(boxAdapter);
            }

            @Override
            public void onFailure(Call<BoxResponse> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG).show();
            }
        });

    }

}
