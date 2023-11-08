package com.example.travelapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class StatisticFragment extends Fragment {
    ActionBar actionBar;
    PieChart pieChart;
    SharedPreferences sharedPreferences;

    // 카테고리 바구니
    ArrayList<String> eatList;
    ArrayList<String> caffeList;
    ArrayList<String> exhibitionList;
    ArrayList<String> festivalList;
    ArrayList<String> showList;
    ArrayList<String> domesticTravelList;
    ArrayList<String> overseasTravelList;
    ArrayList<String> exList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);

        actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("통계");

        init();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getCategoryData();

        setPieChart(getView());
    }

    private void init() {
        eatList = new ArrayList<>();
        caffeList = new ArrayList<>();
        exhibitionList = new ArrayList<>();
        festivalList = new ArrayList<>();
        showList = new ArrayList<>();
        domesticTravelList = new ArrayList<>();
        overseasTravelList = new ArrayList<>();
        exList = new ArrayList<>();
    }

    private void setPieChart(View view) {
        pieChart = (PieChart) view.findViewById(R.id.pieChart);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<>();

        yValues.add(new PieEntry(eatList.size(), "맛집"));
        yValues.add(new PieEntry(caffeList.size(), "카페"));
        yValues.add(new PieEntry(festivalList.size(), "축제"));
        yValues.add(new PieEntry(exhibitionList.size(), "전시회"));
        yValues.add(new PieEntry(showList.size(), "공연"));
        yValues.add(new PieEntry(domesticTravelList.size(), "국내 여행"));
        yValues.add(new PieEntry(overseasTravelList.size(), "해외 여행"));
        yValues.add(new PieEntry(exList.size(), "기타"));

        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues,"카테고리");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        //pieChart.invalidate(); // 회전 및 터치 효과 사라짐
        //pieChart.setTouchEnabled(false);

        pieChart.setData(data);
    }

    private void getCategoryData() {
        sharedPreferences = this.getActivity().getSharedPreferences("complete_data", MODE_PRIVATE);
        Collection<?> col_val = sharedPreferences.getAll().values();
        Iterator<?> it_val = col_val.iterator();
        Collection<?> col_key = sharedPreferences.getAll().keySet();
        Iterator<?> it_key = col_key.iterator();

        while (it_val.hasNext() && it_key.hasNext()) {
            String key = (String) it_key.next();
            String value = (String) it_val.next();
            try {
                JSONObject jsonObject = new JSONObject(value);
                String category = (String) jsonObject.getString("category");

                switch (category) {
                    case "맛집":
                        eatList.add(category);
                        break;
                    case "카페":
                        caffeList.add(category);
                        break;
                    case "축제":
                        festivalList.add(category);
                        break;
                    case "전시회":
                        exhibitionList.add(category);
                        break;
                    case "공연":
                        showList.add(category);
                        break;
                    case "국내 여행":
                        domesticTravelList.add(category);
                        break;
                    case "해외 여행":
                        overseasTravelList.add(category);
                        break;
                    default:
                        exList.add(category);
                        break;
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}