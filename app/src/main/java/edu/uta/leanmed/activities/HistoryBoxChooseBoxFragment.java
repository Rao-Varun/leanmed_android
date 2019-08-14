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
import android.widget.Toast;

import java.util.List;

import edu.uta.leanmed.adapters.BoxAdapter;
import edu.uta.leanmed.pojo.Box;
import edu.uta.leanmed.pojo.BoxResponse;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.BoxAPIService;
import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.SharedPreferenceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryBoxChooseBoxFragment extends Fragment {
    private User user;
    private BoxAPIService boxAPIService;
    private RecyclerView recyclerView;

    public HistoryBoxChooseBoxFragment() {
        // Required empty public constructor
    }


    public static HistoryBoxChooseBoxFragment newInstance(String param1, String param2) {
        HistoryBoxChooseBoxFragment fragment = new HistoryBoxChooseBoxFragment();
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
        View view = inflater.inflate(R.layout.fragment_history_box_choose_box, container, false);
        this.initServices();
        this.getAllBoxs(view);
        return view;
    }

    private void initServices() {
        this.user = SharedPreferenceService.getSavedObjectFromPreference(
                getActivity().getApplicationContext(), SharedPreferenceService.getUserName());
        this.boxAPIService = RetrofitService.newInstance().create(BoxAPIService.class);
    }

    private void getAllBoxs(final View view) {
        Call<BoxResponse> boxResponseCall = boxAPIService.getActiveBoxByZone(user.getEmailId(), user.getToken(), user.getZone().getZoneId());
        boxResponseCall.enqueue(new Callback<BoxResponse>() {
            @Override
            public void onResponse(Call<BoxResponse> call, Response<BoxResponse> response) {
                if(response.body() != null){
                    BoxAdapter boxAdapter = this.setBoxAdapter(response.body().getBox());
                    this.setRecyclerView(boxAdapter);
                }
                else
                    Toast.makeText(getContext(), getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG).show();

            }

            private void setRecyclerView(BoxAdapter boxAdapter) {
                recyclerView=view.findViewById(R.id.recyclerViewBox);
                StaggeredGridLayoutManager mStaggeredLayoutManager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(mStaggeredLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(boxAdapter);
            }

            private BoxAdapter setBoxAdapter(final List<Box> boxList) {
                BoxAdapter boxAdapter = new BoxAdapter(getContext(), boxList);
                boxAdapter.setOnItemClickListener(new BoxAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        HistoryBoxEditBoxFragment fragment = new HistoryBoxEditBoxFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("boxObject", boxList.get(position));
                        fragment.setArguments(bundle);
                        startNextFragment(fragment);
                    }
                });
                return boxAdapter;
            }

            private void startNextFragment(Fragment fragment) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.forgotPwContent, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

            @Override
            public void onFailure(Call<BoxResponse> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG).show();
            }
        });

    }


}
