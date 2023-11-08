package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.travelapp.databinding.ActivityStartBinding;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity {

    private ActivityStartBinding binding;
    private Handler sliderHandler = new Handler();
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        List<Integer> sliderItems = new ArrayList<>();

        setItemList(sliderItems);

        binding.vpImageSlider.setAdapter(new SliderAdapter(this, binding.vpImageSlider, sliderItems));

        // 페이지 수동 변경
        binding.vpImageSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 2000);
            }
        });

        // 버튼
        startButton = binding.startButton;
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            binding.vpImageSlider.setCurrentItem(binding.vpImageSlider.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setItemList(List<Integer> sliderItems) {
        sliderItems.add(R.drawable.start_first);
        sliderItems.add(R.drawable.start_second);
        sliderItems.add(R.drawable.start_third);
        sliderItems.add(R.drawable.start_forth);
        sliderItems.add(R.drawable.start_feeths);
    }

}