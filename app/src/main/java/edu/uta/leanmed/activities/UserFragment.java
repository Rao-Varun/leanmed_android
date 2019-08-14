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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

import edu.uta.leanmed.adapters.UserAdapter;
import edu.uta.leanmed.pojo.User;

import edu.uta.leanmed.pojo.UsersResponse;

import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.SharedPreferenceService;
import edu.uta.leanmed.services.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {
    private ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private SearchView searchView;
    private UserService service;
    private RadioGroup radioGroup;
    private RadioButton selectedBtn;
    private User user;
    private int selectedFilter;

    public UserFragment() {
    }
    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_user, container, false);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.please_wait_dailog));
        pDialog.setCancelable(false);
        user = SharedPreferenceService.getSavedObjectFromPreference(getActivity().getApplicationContext(),SharedPreferenceService.getUserName());
        service = RetrofitService.newInstance().create(UserService.class);
        SearchManager searchManager =(SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView =view.findViewById(R.id.autoCompleteSearch);
        radioGroup=view.findViewById(R.id.userFilter);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                setView("",view);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setView(query,view);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setQueryHint(getString(R.string.search_user));
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
        showpDialog();
        Call<UsersResponse> userCall =null;
        if("".equalsIgnoreCase(query)) {
            selectedBtn=view.findViewById(radioGroup.getCheckedRadioButtonId());
            if(selectedBtn.getText().toString().equalsIgnoreCase(getString(R.string.active)))
                selectedFilter=1;
            else if(selectedBtn.getText().toString().equalsIgnoreCase(getString(R.string.pending)))
                selectedFilter=2;
            else
                selectedFilter=3;
                userCall= service.getUserByStatus(user.getEmailId(), user.getToken(), selectedFilter);
        }else
            userCall=service.getUserByName(user.getEmailId(),user.getToken(),query);
        userCall.enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                UsersResponse inventoryResponse = response.body();
                userList=inventoryResponse.getUsers();
                recyclerView=view.findViewById(R.id.user_card_view);
                StaggeredGridLayoutManager mStaggeredLayoutManager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                hidepDialog();
                userAdapter= new UserAdapter(getContext(), userList);
                recyclerView.setLayoutManager(mStaggeredLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(userAdapter);
                userAdapter.setOnItemClickListener(onItemClickListener);
            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {
                Log.d("onFailure", t.getMessage());
                hidepDialog();
            }
        });
    }
    UserAdapter.OnItemClickListener onItemClickListener=new UserAdapter.OnItemClickListener(){
        @Override
        public void onItemClick(View view, int position) {
            if(user.getType()==3) {
                Intent intent = new Intent(getContext(), UserDetailActivity.class);
                intent.putExtra("userDetail", userList.get(position));
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
