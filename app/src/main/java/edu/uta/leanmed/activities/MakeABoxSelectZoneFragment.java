package edu.uta.leanmed.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import edu.uta.leanmed.adapters.ZoneAdapter;
import edu.uta.leanmed.pojo.Box;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.pojo.Zone;
import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.SharedPreferenceService;
import edu.uta.leanmed.services.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MakeABoxSelectZoneFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserService service;
    private User user;
    private ZoneAdapter zoneAdapter;
    private List<Zone> zoneList;
    private Box box;

    public MakeABoxSelectZoneFragment() {
        // Required empty public constructor
    }


    public static MakeABoxSelectZoneFragment newInstance(String param1, String param2) {
        MakeABoxSelectZoneFragment fragment = new MakeABoxSelectZoneFragment();
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
        View view =  inflater.inflate(R.layout.fragment_make_abox_select_zone, container, false);
        this.initServices();
        this.setSearchBar(view);
        this.setDefaultResultForRecyclerView(view);
        return view;
    }

    private void initServices() {
        this.user = SharedPreferenceService.getSavedObjectFromPreference(
                getActivity().getApplicationContext(), SharedPreferenceService.getUserName());
        this.service = RetrofitService.newInstance().create(UserService.class);

    }

    private void setSearchBar(final View view) {
        SearchManager searchManager =(SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =view.findViewById(R.id.autoCompleteSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setView(query,view);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                setView(query,view);
                return false;
            }
        });
        searchView.setQueryHint("Search RecDon Zones");
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setView("",view);
                return false;
            }
        });
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
    }

    public void setView(String query,final View view){
        if(query.isEmpty())
            setDefaultResultForRecyclerView(view);
        else
            setQueryResultForRecyclerView(query, view);

    }

    private void setQueryResultForRecyclerView(String query, final View view) {
        Call<List<Zone>> zoneResponse = this.service.getVenezuelaZones(this.user.getEmailId(), this.user.getToken(),query);
        zoneResponse.enqueue(new Callback<List<Zone>>() {
            @Override
            public void onResponse(Call<List<Zone>> call, Response<List<Zone>> response) {
                if(response.body() != null){
                    zoneList = response.body();
                    setInventoryAdapter(view);
                    setRecyclerView(view);
                }

            }

            @Override
            public void onFailure(Call<List<Zone>> call, Throwable t) {
             }
        });

    }

    private void setDefaultResultForRecyclerView(final View view) {
        Call<List<Zone>> zoneResponse = service.getAllVenezuelaZones(this.user.getEmailId(), this.user.getToken());
        zoneResponse.enqueue(new Callback<List<Zone>>() {
            @Override
            public void onResponse(Call<List<Zone>> call, Response<List<Zone>> response) {
                if(response.body() != null){
                    zoneList = response.body();
                    setInventoryAdapter(view);
                    setRecyclerView(view);
                }

            }

            @Override
            public void onFailure(Call<List<Zone>> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.password_change_unsuccesfull),Toast.LENGTH_LONG).show();
            }
        });
    }


    private void setInventoryAdapter(View view) {
        this.zoneAdapter = new ZoneAdapter(getContext(), this.zoneList);
        ZoneAdapter.OnItemClickListener onItemClickListener=new ZoneAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                getBoxDetails(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("boxObject", box);
                Fragment fragment = new MakeABoxZonePreviewFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.forgotPwContent, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }




        };
        this.zoneAdapter.setOnItemClickListener(onItemClickListener);
        Log.i("Adapter", "Adapter set");

    }

    private void setRecyclerView(View view) {
        recyclerView=view.findViewById(R.id.zone_card_view);
        StaggeredGridLayoutManager mStaggeredLayoutManager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Log.i("RecyclerView", "Setting Adapter to RecyclerView");
        recyclerView.setAdapter(this.zoneAdapter);
    }

    public Box getBoxDetails(int position) {
        this.box = (Box)getArguments().getSerializable("boxObject");
        box.setDestinationZone(zoneList.get(position));
        return box;
    }


}

