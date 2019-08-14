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

import java.util.List;

import edu.uta.leanmed.adapters.MedicineAdapter;
import edu.uta.leanmed.pojo.Inventory;
import edu.uta.leanmed.pojo.Medicine;
import edu.uta.leanmed.pojo.MedicineResponse;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.MedicineAPIService;
import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.SharedPreferenceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddInventorySearchMedicineFragment extends Fragment {
    private RecyclerView recyclerView;
    private MedicineAPIService service;
    private User user;
    private List<Medicine> medicineList;
    private MedicineAdapter medicineAdapter;
    private Inventory inventory = new Inventory();
    // TODO: Rename and change types of parameters

    public AddInventorySearchMedicineFragment() {
        // Required empty public constructor
    }

    public static AddInventorySearchMedicineFragment newInstance(String param1, String param2) {
        AddInventorySearchMedicineFragment fragment = new AddInventorySearchMedicineFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_inventory_search_medicine, container, false);
        this.initServices();
        this.setSearchBar(view);
        this.setDefaultResultForRecyclerView(view);
        return view;
    }

    private void setDefaultResultForRecyclerView(final View view) {
        Call<MedicineResponse> productsCall = service.getAllMedicinesResponse(user.getEmailId(),user.getToken());
        productsCall.enqueue(new Callback<MedicineResponse>() {
            @Override
            public void onResponse(Call<MedicineResponse> call, Response<MedicineResponse> response) {
                MedicineResponse medicineResponse = response.body();
                medicineList=medicineResponse.getMedicine();
                setInventoryAdapter();
                setRecyclerView(view);

            }

            @Override
            public void onFailure(Call<MedicineResponse> call, Throwable t) {
                Log.d("onFailure", t.getMessage());
//                hidepDialog();
            }
        });
    }


    private void setRecyclerView(View view) {
        recyclerView=view.findViewById(R.id.medicine_card_view);
        StaggeredGridLayoutManager mStaggeredLayoutManager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(medicineAdapter);
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
        searchView.setQueryHint("Search Medicine");
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
//        setRecyclerView(view);
        if(query.isEmpty())
            setDefaultResultForRecyclerView(view);
        else
            setQueryResultForRecyclerView(query, view);

    }

    private void setQueryResultForRecyclerView(String query,final View view) {
        Call<MedicineResponse> productsCall = service.getMedicineResponse(user.getEmailId(),user.getToken(),query);
        productsCall.enqueue(new Callback<MedicineResponse>() {
            @Override
            public void onResponse(Call<MedicineResponse> call, Response<MedicineResponse> response) {
                MedicineResponse medicineResponse = response.body();
                medicineList=medicineResponse.getMedicine();
                setInventoryAdapter();
                setRecyclerView(view);


            }



            @Override
            public void onFailure(Call<MedicineResponse> call, Throwable t) {
                Log.d("onFailure", t.getMessage());
//                hidepDialog();
            }
        });
    }

    private void setInventoryAdapter() {
        medicineAdapter = new MedicineAdapter(getContext(), medicineList);
        MedicineAdapter.OnItemClickListener onItemClickListener=new MedicineAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                inventory.setMedicine(medicineList.get(position));
                inventory.setUser(user);
                inventory.setZone(user.getZone());
                Bundle bundle = new Bundle();
                bundle.putSerializable("inventoryObject", inventory);
                Fragment fragment = new AddInventoryMedicinePreviewFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.forgotPwContent, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };
        medicineAdapter.setOnItemClickListener(onItemClickListener);

    }

    private void initServices() {
        this.user = SharedPreferenceService.getSavedObjectFromPreference(
                getActivity().getApplicationContext(), SharedPreferenceService.getUserName());
        service = RetrofitService.newInstance().create(MedicineAPIService.class);

    }

}
