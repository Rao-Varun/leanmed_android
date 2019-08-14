package edu.uta.leanmed.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.uta.leanmed.adapters.BoxEditAdapter;
import edu.uta.leanmed.pojo.Box;
import edu.uta.leanmed.pojo.BoxContent;
import edu.uta.leanmed.pojo.BoxResponse;
import edu.uta.leanmed.pojo.Request;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.BoxAPIService;
import edu.uta.leanmed.services.RetrofitService;
import edu.uta.leanmed.services.SharedPreferenceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryBoxEditBoxFragment extends Fragment {
    private Box box;
    private Request request;
    private Button acceptButton;
    private Button cancelButton;
    private BoxEditAdapter boxEditAdapter;
    private RecyclerView recyclerView;
    private BoxAPIService boxAPIService;
    private User user;
    private List<BoxContent> deleteBoxContent = new ArrayList<>();
    private int spinnerVal=0;

    public HistoryBoxEditBoxFragment() {
        // Required empty public constructor
    }


    public static HistoryBoxEditBoxFragment newInstance(String param1, String param2) {
        HistoryBoxEditBoxFragment fragment = new HistoryBoxEditBoxFragment();
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
        View view = inflater.inflate(R.layout.fragment_history_box_edit_box, container, false);
        this.setBoxAPIService();
        this.getFragmentObject();
        this.setBoxAdapter(view);
        this.setRecyclerView(view);
        this.setAcceptButton(view);
        this.setCancelButton(view);
        return view;
    }

    private void setBoxAPIService() {
        this.user = SharedPreferenceService.getSavedObjectFromPreference(
                getActivity().getApplicationContext(), SharedPreferenceService.getUserName());
        this.boxAPIService = RetrofitService.newInstance().create(BoxAPIService.class);
    }

    private void setCancelButton(View view) {
        this.cancelButton = view.findViewById(R.id.buttonCancel);
        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();
            }
        });

    }

    private void setAcceptButton(View view) {
        acceptButton = view.findViewById(R.id.buttonAccept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deleteBoxContent.isEmpty() && spinnerVal == 0)
                    return;
                Log.i("accept bttn", Integer.toString(deleteBoxContent.size()));
                box.setStatus(spinnerVal);
                box.setBoxContent(deleteBoxContent);
                Call<BoxResponse> boxResponseCall = boxAPIService.editBox(user.getEmailId(), user.getToken(), box);
                boxResponseCall.enqueue(new Callback<BoxResponse>() {
                    @Override
                    public void onResponse(Call<BoxResponse> call, Response<BoxResponse> response) {
                        if(response.body()!=null)
                        {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            HistoryBoxChooseBoxFragment fragment = new HistoryBoxChooseBoxFragment();
                            Bundle bundle = new Bundle();
                            fragment.setArguments(bundle);
                            getFragmentManager().popBackStack();
                            startNextFragment(fragment);
                        }
                        else
                            Toast.makeText(getContext(), getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(Call<BoxResponse> call, Throwable t) {
                        Toast.makeText(getContext(), getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG).show();

                    }
                });

            }
        });
    }

    private void startNextFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.forgotPwContent, fragment);
        transaction.commit();
    }

    private void setRecyclerView(View view) {
        recyclerView=view.findViewById(R.id.recyclerViewBox);
        StaggeredGridLayoutManager mStaggeredLayoutManager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(boxEditAdapter);
    }

    private void setBoxAdapter(final View view) {
        final List<Object> objectList = this.getListForAdapter();
        boxEditAdapter = new BoxEditAdapter(getContext(), objectList);
        boxEditAdapter.setOnItemClickListener(new BoxEditAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        boxEditAdapter.setOnItemLongClickListener(new BoxEditAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {


            }
        });

        boxEditAdapter.setBoxDeleteButtonClickListener(new BoxEditAdapter.BoxDeleteButtonClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Box mbox = (Box)objectList.get(position);
                String name = mbox.getBoxName();
                new AlertDialog.Builder(getContext(), R.style.AlertDialog)
                        .setTitle(getString(R.string.delete_box_title))
                        .setMessage(String.format(getString(R.string.delete_box), name))
                        .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Call<BoxResponse> boxResponseCall = boxAPIService.removeBox(user.getEmailId(), user.getToken(), box);
                                boxResponseCall.enqueue(new Callback<BoxResponse>() {
                                    @Override
                                    public void onResponse(Call<BoxResponse> call, Response<BoxResponse> response) {
                                        if(response.body()!=null){
                                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                                            getFragmentManager().popBackStack();
                                            startNextFragment(new HistoryBoxChooseBoxFragment());

                                        }
                                        else
                                            Toast.makeText(getContext(), getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void onFailure(Call<BoxResponse> call, Throwable t) {
                                        Toast.makeText(getContext(), getString(R.string.password_change_unsuccesfull), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        boxEditAdapter.setBoxContentDeleteButtonClickListener(new BoxEditAdapter.BoxContentDeleteButtonClickListener() {
            @Override
            public void onItemClick(View itemView, final int position) {
                BoxContent boxContent = (BoxContent)objectList.get(position);
                String name = String.format("%s(%s)",boxContent.getInventory().getMedicine().getTradeName(),boxContent.getInventory().getMedicine().getGenName());
                new AlertDialog.Builder(getContext(), R.style.AlertDialog)
                        .setTitle(getString(R.string.delete_boxcontent_title))
                        .setMessage(String.format(getString(R.string.delete_boxcontent), name))
                        .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("Delete box", Integer.toString(position));
                                deleteBoxContent.add((BoxContent)objectList.get(position));
                                box.getBoxContent().remove(position-2);
                                setBoxAdapter(view);
                                setRecyclerView(view);
                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

        });

        boxEditAdapter.setSpinnerOnItemSelectedListener(new BoxEditAdapter.SpinnerOnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position) {
                spinnerVal=position;
            }
        });

        boxEditAdapter.setAddBoxContentButtonListener(new BoxEditAdapter.AddBoxContentButtonListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    private List<Object> getListForAdapter() {
        List<Object> objectList = new ArrayList<>();
        objectList.add(box);
        objectList.add(box.getDestinationZone());
        for(BoxContent boxContent : box.getBoxContent())
            objectList.add(boxContent);
        return objectList;
    }

    private void getFragmentObject() {
        box = (Box) getArguments().getSerializable("boxObject");

    }


}
