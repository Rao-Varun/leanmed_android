package edu.uta.leanmed.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class RecdonActivity extends AppCompatActivity {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    loadFragment(new RecdonSearchFragment());
                    setTitle(R.string.title_search);
                    return true;
                case R.id.navigation_cart:
                    setTitle(R.string.title_cart);
                    loadFragment(new CartFragment());
                    return true;
                case R.id.navigation_request:
                    setTitle(R.string.title_requests);
                    loadFragment(new RequestFragment());
                    return true;
                case R.id.navigation_history:
                    setTitle(R.string.title_history);
                    loadFragment(new HistoryFragment());
                    return true;
                case R.id.navigation_info:
                    setTitle(R.string.title_info);
                    loadFragment(new InfoFragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recdon);
        setTitle(R.string.title_search);
        loadFragment(new RecdonSearchFragment());
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.forgotPwContent, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
