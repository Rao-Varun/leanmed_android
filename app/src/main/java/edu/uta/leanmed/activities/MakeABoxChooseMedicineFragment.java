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
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import edu.uta.leanmed.adapters.InventoryAdapter;
import edu.uta.leanmed.adapters.InventoryBoxAdapter;
import edu.uta.leanmed.pojo.Box;
import edu.uta.leanmed.pojo.BoxContent;
import edu.uta.leanmed.pojo.Inventory;
import edu.uta.leanmed.pojo.InventoryResponse;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.MedicineAPIService;
import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.SharedPreferenceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MakeABoxChooseMedicineFragment extends Fragment {
    private HashMap<Integer, Integer> hashmap = new  HashMap<Integer, Integer>();
    private RecyclerView recyclerView;
    private InventoryBoxAdapter inventoryBoxAdapter;
    private List<Inventory> inventoryList;
    private Box box;
    private User user;
    private MedicineAPIService service;
    private Button buttonSubmit;


    public MakeABoxChooseMedicineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_make_abox_choose_medicine, container, false);
        this.getBoxObject();
        this.initServices();
        this.setSearchBar(view);
        this.setDefaultResultForRecyclerView(view);
        this.setSubmitButton(view);
        return view;
    }

    private void setSubmitButton(View view) {
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("boxObject", box);
                Fragment fragment = new MakeABoxFinalPreviewFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.forgotPwContent, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void getBoxObject() {
        this.box = (Box)getArguments().getSerializable("boxObject");
        this.InventoryCount();
    }

    private void InventoryCount() {
        if( this.box.getBoxContent() == null) {
            Log.i("InventoryCount", "null");
            return;
        }
        for(BoxContent boxContent : this.box.getBoxContent()){
            Log.i("InventoryCount",String.format("%d %d",boxContent.getInventory().getInventoryId(),boxContent.getUnits()));
            if(hashmap.containsKey(boxContent.getInventory().getInventoryId())){
                hashmap.put(boxContent.getInventory().getInventoryId(), hashmap.get(boxContent.getInventory().getInventoryId())+boxContent.getUnits());
            }
            hashmap.put(boxContent.getInventory().getInventoryId(), boxContent.getUnits());
        }
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
        searchView.setQueryHint("Search Inventory");
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
        Call<InventoryResponse> inventoryResponseCall = service.getInventoryResponse(user.getEmailId(), user.getToken(), query);
        inventoryResponseCall.enqueue(new Callback<InventoryResponse>() {
            @Override
            public void onResponse(Call<InventoryResponse> call, Response<InventoryResponse> response) {
                if(response.body()!=null){
                    if(response.body().getStatus() == 200 && response.body().getInventory()!=null){
                        inventoryList = response.body().getInventory();
                        setInventoryBoxAdapter(view);
                        setRecyclerView(view);
                    }
                }
            }

            @Override
            public void onFailure(Call<InventoryResponse> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.password_change_unsuccesfull),Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setDefaultResultForRecyclerView(final View view) {
        Call<InventoryResponse> inventoryResponseCall = service.getAllInventoryResponse(user.getEmailId(), user.getToken());
        inventoryResponseCall.enqueue(new Callback<InventoryResponse>() {
            @Override
            public void onResponse(Call<InventoryResponse> call, Response<InventoryResponse> response) {
                if(response.body()!=null){
                    if(response.body().getStatus() == 200 && response.body().getInventory()!=null){
                        inventoryList = response.body().getInventory();
                        setInventoryBoxAdapter(view);
                        setRecyclerView(view);
                    }
                }
            }

            @Override
            public void onFailure(Call<InventoryResponse> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.password_change_unsuccesfull),Toast.LENGTH_LONG).show();

            }
        });
    }

    private void setRecyclerView(View view) {
        recyclerView=view.findViewById(R.id.zone_card_view);
        StaggeredGridLayoutManager mStaggeredLayoutManager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Log.i("RecyclerView", "Setting Adapter to RecyclerView");
        recyclerView.setAdapter(this.inventoryBoxAdapter);
    }

    private void setInventoryBoxAdapter(View view) {
        this.inventoryBoxAdapter = new InventoryBoxAdapter(getContext(), this.inventoryList, this.hashmap);
        InventoryBoxAdapter.OnItemClickListener onItemClickListener=new InventoryBoxAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                 Bundle bundle = new Bundle();
                bundle.putSerializable("boxObject", box);
                bundle.putSerializable("inventoryObject", inventoryList.get(position));
                Fragment fragment = new MakeABoxAddMedicineInfoFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.forgotPwContent, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

        };

        InventoryBoxAdapter.OnItemLongClickListener onItemLongClickListener = new InventoryBoxAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Log.i("Adapter", "Long Click");
                MedicineInfoDialogue medicineInfoDialogue = new MedicineInfoDialogue(getContext(), inventoryList.get(position));
                medicineInfoDialogue.show();
            }
        };
        this.inventoryBoxAdapter.setOnItemClickListener(onItemClickListener);
        this.inventoryBoxAdapter.setOnItemLongClickListener(onItemLongClickListener);
        Log.i("Adapter", "Adapter set");

    }

}
