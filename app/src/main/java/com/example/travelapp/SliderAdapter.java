package com.example.travelapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.travelapp.databinding.ItemSlideBinding;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private static final String TAG = "SliderAdapter";
    private Context context;
    private List<Integer> sliderItems;
    private ViewPager2 viewPager2;

    public SliderAdapter(Context context, ViewPager2 viewPager2, List<Integer> sliderImage) {
        this.context = context;
        this.viewPager2 = viewPager2;
        this.sliderItems = sliderImage;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(ItemSlideBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.bind(sliderItems.get(position));
        if (position == sliderItems.size() - 2) {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {

        private ItemSlideBinding binding;

        public SliderViewHolder(ItemSlideBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Integer sliderItem) {
            try {
                Glide.with(context).load(sliderItem).into(binding.imageSlider);
            } catch (Exception e) {
                Log.d(TAG, "ERROR: " + e.getMessage());
            }
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();
        }
    };

}
