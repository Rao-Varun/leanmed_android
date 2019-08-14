package edu.uta.leanmed.activities;

import android.app.SearchManager;
import android.content.Context;
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

import java.util.List;

import edu.uta.leanmed.adapters.DonorAdapter;
import edu.uta.leanmed.pojo.Donor;
import edu.uta.leanmed.pojo.DonorResponse;
import edu.uta.leanmed.pojo.Inventory;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.MedicineAPIService;
import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.SharedPreferenceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddInventorySearchDonorFragment extends Fragment {
    private RecyclerView recyclerView;
    private MedicineAPIService service;
    private User user;
    private List<Donor> donorList;
    private DonorAdapter donorAdapter;
    private Inventory inventory;

    public AddInventorySearchDonorFragment() {
        // Required empty public constructor
    }


    public static AddInventorySearchDonorFragment newInstance(String param1, String param2) {
        AddInventorySearchDonorFragment fragment = new AddInventorySearchDonorFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_inventory_search_donor, container, false);
        this.getInventoryObject();
        this.initServices();
        this.setSearchBar(view);
        this.setDefaultResultForRecyclerView(view);
        return view;
    }

    private void getInventoryObject() {
        this.inventory = (Inventory)getArguments().getSerializable("inventoryObject");
    }

    private void initServices() {
        this.user = SharedPreferenceService.getSavedObjectFromPreference(
                getActivity().getApplicationContext(), SharedPreferenceService.getUserName());
        this.service = RetrofitService.newInstance().create(MedicineAPIService.class);

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
        searchView.setQueryHint("Search Donor");
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

    private void setDefaultResultForRecyclerView(final View view) {
        Call<DonorResponse> productsCall = service.getAllDonorsResponse(user.getEmailId(),user.getToken());
        productsCall.enqueue(new Callback<DonorResponse>() {
            @Override
            public void onResponse(Call<DonorResponse> call, Response<DonorResponse> response) {
                DonorResponse donorResponse = response.body();
                donorList=donorResponse.getDonor();
                setInventoryAdapter( view);
                setRecyclerView(view);
            }

            @Override
            public void onFailure(Call<DonorResponse> call, Throwable t) {
                Log.d("onFailure", t.getMessage());
//                hidepDialog();
            }
        });
    }

    private void setQueryResultForRecyclerView(String query,final View view) {
        Call<DonorResponse> productsCall = service.getDonorResponse(user.getEmailId(),user.getToken(),query);
        productsCall.enqueue(new Callback<DonorResponse>() {
            @Override
            public void onResponse(Call<DonorResponse> call, Response<DonorResponse> response) {
                DonorResponse donorResponse = response.body();
                donorList=donorResponse.getDonor();
                setInventoryAdapter(view);
                setRecyclerView(view);


            }

            @Override
            public void onFailure(Call<DonorResponse> call, Throwable t) {
                Log.d("onFailure", t.getMessage());
//                hidepDialog();
            }
        });
    }

    private void setInventoryAdapter(View view) {
        this.donorAdapter = new DonorAdapter(getContext(), donorList);
        DonorAdapter.OnItemClickListener onItemClickListener=new DonorAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                inventory.setDonor(donorList.get(position));
                Bundle bundle = new Bundle();
                bundle.putSerializable("inventoryObject", inventory);
                Fragment fragment = new AddInventoryDonorPreviewFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.forgotPwContent, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };
        this.donorAdapter.setOnItemClickListener(onItemClickListener);
        Log.i("Adapter", "Adapter set");

    }


    private void setRecyclerView(View view) {
        recyclerView=view.findViewById(R.id.donor_card_view);
        StaggeredGridLayoutManager mStaggeredLayoutManager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Log.i("RecyclerView", "Setting Adapter to RecyclerView");
        recyclerView.setAdapter(this.donorAdapter);
    }
}
