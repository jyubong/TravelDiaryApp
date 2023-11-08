package com.example.travelapp;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.travelapp.databinding.FragmentHomeBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class HomeFragment extends Fragment {
    SearchView searchView;
    ActionBar actionBar;
    public RecyclerView homeRecyclerView;
    public HomeAdapter homeAdaptor;
    public ArrayList<HomeData> homeDataList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private PreferenceManager pref = new PreferenceManager();
    ActivityResultLauncher<Intent> resultLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 액티비티 소유 탭바
        setHasOptionsMenu(true);
//        pref.clear("main_data", getContext());

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 탭바 제목
        actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("가고 싶은");

        // 리사이클러뷰
        homeRecyclerView = view.findViewById(R.id.homeRecyclerView);
        homeRecyclerView.setHasFixedSize(true);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        homeAdaptor = new HomeAdapter(homeDataList);
        homeRecyclerView.setAdapter(homeAdaptor);

        getSharedPreferencesData();
        getIntentData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        homeAdaptor.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu_main, menu);

//        MenuItem menuItem = menu.findItem(R.id.menu_search);
//        searchView = (SearchView) menuItem.getActionView();
//        searchView.setQueryHint(getResources().getString(R.string.query_hint));
//        searchView.setOnQueryTextListener(queryTextListener);
    }

//    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
//        @Override
//        public boolean onQueryTextSubmit(String query) {
//            searchView.setQuery("", false);
//            searchView.setIconified(true);
//            Toast toast = Toast.makeText(getContext(), query, Toast.LENGTH_SHORT);
//            toast.show();
//            return false;
//        }
//
//        @Override
//        public boolean onQueryTextChange(String newText) {
//            return false;
//        }
//    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) { // 추가로 이동
            Intent intent = new Intent(getContext(), MainAddActivity.class);
            resultLauncher.launch(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getSharedPreferencesData() {
        // 쉐어드 모든 키 벨류 가져오기
        sharedPreferences = this.getActivity().getSharedPreferences("main_data", MODE_PRIVATE);
        Collection<?> col_val = sharedPreferences.getAll().values();
        Iterator<?> it_val = col_val.iterator();
        Collection<?> col_key = sharedPreferences.getAll().keySet();
        Iterator<?> it_key = col_key.iterator();

        while (it_val.hasNext() && it_key.hasNext()) {
            String key = (String) it_key.next();
            String value = (String) it_val.next();
            try {
                JSONObject jsonObject = new JSONObject(value);
                String detail = (String) jsonObject.getString("detail");
                String place = (String) jsonObject.getString("place");
                String memo = (String) jsonObject.getString("memo");
                String category = (String) jsonObject.getString("category");

                homeAdaptor.addItem(new HomeData(key, detail, place, memo, category));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            homeAdaptor.notifyDataSetChanged();
        }
    }

    public void getIntentData() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        String date = intent.getStringExtra("date");
                        String detail = intent.getStringExtra("detail");
                        String place = intent.getStringExtra("place");
                        String memo = intent.getStringExtra("memo");
                        String category = intent.getStringExtra("category");

                        homeAdaptor.addItem(new HomeData(date, detail, place, memo, category));
                        homeAdaptor.notifyDataSetChanged();
                    }
                }
        );
    }

}