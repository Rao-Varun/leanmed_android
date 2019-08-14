package edu.uta.leanmed.activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.uta.leanmed.adapters.InventoryAdapter;
import edu.uta.leanmed.pojo.Inventory;
import edu.uta.leanmed.pojo.InventoryResponse;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.MedicineAPIService;
import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.SharedPreferenceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecdonSearchFragment extends Fragment {
    private ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private InventoryAdapter inventoryAdapter;
    private List<Inventory> inventoryList;
    private SearchView searchView;
    private MedicineAPIService service;
    private User user;
    public RecdonSearchFragment(){}

    public static RecdonSearchFragment newInstance() {
        RecdonSearchFragment fragment = new RecdonSearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_search, container, false);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.please_wait_dailog));
        pDialog.setCancelable(false);
        user = SharedPreferenceService.getSavedObjectFromPreference(getActivity().getApplicationContext(),SharedPreferenceService.getUserName());
        service = RetrofitService.newInstance().create(MedicineAPIService.class);
        SearchManager searchManager =(SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView =view.findViewById(R.id.autoCompleteSearch);
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
        setView("",view);
        return view;
    }

    public void setView(String query,final View view){
//        showpDialog();
        Call<InventoryResponse> productsCall = service.getInventoryResponse(user.getEmailId(),user.getToken(),query);
        productsCall.enqueue(new Callback<InventoryResponse>() {
            @Override
            public void onResponse(Call<InventoryResponse> call, Response<InventoryResponse> response) {
                InventoryResponse inventoryResponse = response.body();
                inventoryList=inventoryResponse.getInventory();
                recyclerView=view.findViewById(R.id.medicine_card_view);
                StaggeredGridLayoutManager mStaggeredLayoutManager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
//                hidepDialog();
                inventoryAdapter = new InventoryAdapter(getContext(), inventoryList);
                recyclerView.setLayoutManager(mStaggeredLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(inventoryAdapter);
                inventoryAdapter.setOnItemClickListener(onItemClickListener);
            }

            @Override
            public void onFailure(Call<InventoryResponse> call, Throwable t) {
                Log.d("onFailure", t.getMessage());
                hidepDialog();
            }
        });
    }
    InventoryAdapter.OnItemClickListener onItemClickListener=new InventoryAdapter.OnItemClickListener(){
        @Override
        public void onItemClick(View view, int position) {
            if(user.getType()==2) {
                Intent intent = new Intent(getContext(), InventoryDetailActivity.class);
                intent.putExtra("medicineInventory", inventoryList.get(position));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
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
