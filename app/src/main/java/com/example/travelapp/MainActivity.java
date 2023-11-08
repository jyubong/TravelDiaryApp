package com.example.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.travelapp.databinding.ActivityMainBinding;
import com.example.travelapp.databinding.FragmentHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    LinearLayout home_ly;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 객체 생성
        init();

        // 툴바 설정
        setSupportActionBar(toolbar);

        // 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new TabSelectedListener());

        // 맨 처음 시작할 탭
        bottomNavigationView.setSelectedItemId(R.id.tab_home);

    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        home_ly = findViewById(R.id.home_ly);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
    }

    class TabSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.tab_home) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_ly, new HomeFragment())
                        .commit();
                return true;
            } else if (item.getItemId() == R.id.tab_calendar) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_ly, new CalendarFragment())
                        .commit();
                return true;
            } else if (item.getItemId() == R.id.tab_statistic) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_ly, new StatisticFragment())
                        .commit();
                return true;
            } else if (item.getItemId() == R.id.tab_user) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_ly, new UserFragment())
                        .commit();
                return true;
            }

            return false;
        }
    }


}