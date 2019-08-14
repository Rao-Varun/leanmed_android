package edu.uta.leanmed.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import edu.uta.leanmed.adapters.CartAdapter;
import edu.uta.leanmed.pojo.CartItem;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.MedicineAPIService;
import edu.uta.leanmed.services.SharedPreferenceService;

public class CartFragment extends Fragment {
    private ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;
    private MedicineAPIService service;
    private User user;
    private Button mProceed;
    public CartFragment() {
    }

    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_cart, container, false);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.please_wait_dailog));
        pDialog.setCancelable(false);
        user = SharedPreferenceService.getSavedObjectFromPreference(getContext(),SharedPreferenceService.getUserName());
        cartItems=SharedPreferenceService.getCart(getContext());
        if(cartItems==null) cartItems=new ArrayList<>();
        mProceed=view.findViewById(R.id.proceed);
        mProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceedNextSteps();
            }
        });
        if(cartItems.size()==0) mProceed.setVisibility(View.GONE);
        setView(view);
        return view;
    }

    private void proceedNextSteps(){
        showpDialog();
        hidepDialog();
        SharedPreferenceService.setCart(getContext(),cartItems);
        Intent intent= new Intent(getContext(),CartPatientActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void setView(final View view) {
        showpDialog();
        recyclerView=view.findViewById(R.id.cart_card_view);
        StaggeredGridLayoutManager mStaggeredLayoutManager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        cartAdapter= new CartAdapter(getContext(), cartItems);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cartAdapter);
        cartAdapter.setOnItemClickListener(onItemClickListener);
        hidepDialog();
    }
    CartAdapter.OnItemClickListener onItemClickListener=new CartAdapter.OnItemClickListener(){
        @Override
        public void onItemClick(View view, int position) {
            cartItems.remove(position);
            SharedPreferenceService.setCart(getContext(),cartItems);
            cartAdapter.notifyDataSetChanged();
            /*Intent intent= new Intent(getContext(),InventoryDetailActivity.class);
            intent.putExtra("medicineInventory",inventoryList.get(position));
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
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

    @Override
    public void onResume() {
        super.onResume();
        if(cartItems.size()>0 && (SharedPreferenceService.getCart(getContext())==null || SharedPreferenceService.getCart(getContext()).size()==0))
            getFragmentManager().beginTransaction().replace(R.id.forgotPwContent, CartFragment.newInstance()).commit();
    }
}
