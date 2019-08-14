package edu.uta.leanmed.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HistoryFragment extends Fragment {
    private CardView cardBoxHistory, cardRequestHistory;

    public HistoryFragment() {
    }

    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        this.setAllCardElement(view);
        return view;

    }

    private void setAllCardElement(View view) {
        this.setHistoryBoxCard(view);
        this.setRequestBoxCard(view);
    }

    private void setHistoryBoxCard(View view) {
        cardBoxHistory = view.findViewById(R.id.cardBoxHistory);
        cardBoxHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNextFragment(new HistoryBoxChooseBoxFragment());
            }
        });
    }

    private void setRequestBoxCard(View view) {
        cardRequestHistory = view.findViewById(R.id.cardRequestHistory);
        cardRequestHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNextFragment(new HistoryRequestChooseRequestFragment());
            }
        });
    }

    private void startNextFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.forgotPwContent, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
