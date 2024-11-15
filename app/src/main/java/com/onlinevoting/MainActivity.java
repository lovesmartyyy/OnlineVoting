package com.onlinevoting;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
HomeFragment homeFragment = new HomeFragment();
ResultFragment resultFragment= new ResultFragment();
ElectedFragment electedFragment= new ElectedFragment();
ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        getSupportFragmentManager().beginTransaction().add(R.id.FrameLayout,homeFragment).commit();


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, homeFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.result) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, resultFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.elected) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, electedFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, profileFragment).commit();
                    return true;
                }
                return false;
            }
        });


    }
}