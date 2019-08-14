package edu.uta.leanmed.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.uta.leanmed.adapters.InventoryAdapter;
import edu.uta.leanmed.pojo.Box;
import edu.uta.leanmed.pojo.BoxContent;
import edu.uta.leanmed.pojo.BoxResponse;
import edu.uta.leanmed.pojo.Inventory;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.BoxAPIService;
import edu.uta.leanmed.services.MedicineAPIService;
import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.SharedPreferenceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MakeABoxFinalPreviewFragment extends Fragment {
    public static final String TAG = "MABFinalPreviewFragment";
    private Box box;
    private User user;
    private List<BoxContent> boxContents;
    private BoxAPIService boxAPIService;
    private TextView textBoxId;
    private TextView textZoneName;
    private Button buttonSubmit;
    private RecyclerView recyclerView;

    public static MakeABoxFinalPreviewFragment newInstance(String param1, String param2) {
        MakeABoxFinalPreviewFragment fragment = new MakeABoxFinalPreviewFragment();
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
        View view = inflater.inflate(R.layout.fragment_make_abox_final_preview, container, false);
        this.setBoxAPIService();
        this.getBoxDetails();
        this.setBoxInformationToTextView(view);
        recyclerView=view.findViewById(R.id.recyclerView);
        this.setBoxContentInformation(view);
        this.setSubmitButton(view);
        return view;
    }

    private void setBoxAPIService() {
        this.user = SharedPreferenceService.getSavedObjectFromPreference(
                getActivity().getApplicationContext(), SharedPreferenceService.getUserName());
        this.boxAPIService = RetrofitService.newInstance().create(BoxAPIService.class);
    }


    private void getBoxDetails() {
        this.box = (Box) getArguments().getSerializable("boxObject");
        this.boxContents = box.getBoxContent();
    }

    private void setBoxInformationToTextView(View view) {
        Log.i(TAG, "Setting box basic information");
        this.defineAllBoxTextView(view);
        this.setAllStringsToTextView(view);
    }

    private void setAllStringsToTextView(View view) {
        this.textBoxId.setText(String.format("%s %s",getString(R.string.box_id),String.valueOf(box.getBoxName())));
        this.textZoneName.setText(String.format("%s %s(%s)", getString(R.string.zone), box.getDestinationZone().getZoneName(), box.getDestinationZone().getZoneId()));
    }

    private void defineAllBoxTextView(View view) {
        this.textBoxId = view.findViewById(R.id.textBoxId);
        this.textZoneName = view.findViewById(R.id.textZoneName);
    }

    private void setBoxContentInformation(View view) {
        List<Inventory> inventoryList = this.getInventoryList();
        InventoryAdapter inventoryAdapter = this.getInventoryAdapter(inventoryList, view);
        this.setRecyclerView(inventoryAdapter, view);
    }

    private List<Inventory> getInventoryList() {
        List<Inventory> inventoryList = new ArrayList<Inventory>();
        for (BoxContent tempBoxContent : boxContents) {
            inventoryList.add(tempBoxContent.getInventory());
        }
        return inventoryList;
    }

    private InventoryAdapter getInventoryAdapter(final List<Inventory> inventoryList, View view) {
        Log.i(TAG, "Setting InventoryAdapter");
        final InventoryAdapter inventoryAdapter = new InventoryAdapter(getContext(), inventoryList);
        inventoryAdapter.setOnItemLongClickListener(new InventoryAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(final View view, int position) {
                String medName = String.format("%s(%s)", inventoryList.get(position).getMedicine().getTradeName(), inventoryList.get(position).getMedicine().getGenName());
                BoxContentMenuDialogue boxContentMenuDialogue = new BoxContentMenuDialogue(getContext(), position, medName);
                boxContentMenuDialogue.setDialogListener(new BoxContentMenuDialogue.DialogListener() {
                    @Override
                    public void onSetDeleteBoxContent(int position) {
                        boxContents.remove(position);
                        box.setBoxContent(boxContents);
                        setBoxContentInformation(view);
                    }

                    @Override
                    public void onSetEditBoxContent(int position) {
                        Inventory inventory = boxContents.get(position).getInventory();
                        boxContents.remove(position);
                        box.setBoxContent(boxContents);
                        setBoxContentInformation(view);
                        Fragment fragment = new MakeABoxAddMedicineInfoFragment();
                        ((MakeABoxAddMedicineInfoFragment) fragment).setReturnBack(true);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("boxObject", box);
                        bundle.putSerializable("inventoryObject", inventoryList.get(position));
                        fragment.setArguments(bundle);
                        startNextFragment(fragment);
                    }
                });
                boxContentMenuDialogue.show();
            }
        });
        return inventoryAdapter;
    }

    private void setRecyclerView(InventoryAdapter inventoryAdapter, View view) {
        StaggeredGridLayoutManager mStaggeredLayoutManager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Log.i("RecyclerView", "Setting Adapter to RecyclerView");
        recyclerView.setAdapter(inventoryAdapter);
    }

    private void setSubmitButton(View view) {
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<BoxResponse> boxResponseCall = boxAPIService.addNewBox(user.getEmailId(), user.getToken(), box);
                boxResponseCall.enqueue(new Callback<BoxResponse>() {

                    @Override
                    public void onResponse(Call<BoxResponse> call, Response<BoxResponse> response) {
                        if(response.body() != null){
                            Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                            FragmentManager fm = getFragmentManager();
                            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                                fm.popBackStack();
                            }
                            startNextFragment(new InventoryOperationFragment());
                        }
                        else{
                            Log.i(TAG, response.message());
                            Toast.makeText(getContext(),getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BoxResponse> call, Throwable t) {
                        Toast.makeText(getContext(),getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void startNextFragment(Fragment fragment) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.forgotPwContent, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
    }

}
