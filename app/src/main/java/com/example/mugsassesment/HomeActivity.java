package com.example.mugsassesment;

import static androidx.navigation.Navigation.findNavController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    boolean off = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Paper.init(getApplicationContext());
        Objects.requireNonNull(getSupportActionBar()).hide();
        setIds();
        setListeners();



    }

    private void setListeners() {

     TextView btn = findViewById(R.id.logout);
     btn.setOnClickListener(view -> {



         Paper.book().destroy();
         Intent intent = new Intent(HomeActivity.this,Login_mainActivity.class);
         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         startActivity(intent);

     });

    }

    private void setIds() {
         bottomNavigationView = findViewById(R.id.navigation_bar);

        final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        assert navHostFragment != null;
        final NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView,navController);



    }
    @Override
    public void onBackPressed() {

        Toast.makeText(HomeActivity.this, "Press again to exit", Toast.LENGTH_SHORT).show();

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {


            @Override
            public void run() {
                off = false;
            }
        };

        timer.schedule(timerTask, 1000);
        if (off) {

            super.onBackPressed();

        } else {

            off = true;
        }

    }

}