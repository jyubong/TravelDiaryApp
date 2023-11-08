package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CalendarDetailActivity extends AppCompatActivity {

    private TextView placeText;
    private TextView categoryText;
    private TextView detailText;
    private TextView withText;
    private TextView whenText;
    private TextView memoText;
    private Button modifyButton;
    private Button deleteButton;
    private Toolbar toolbar;
    private PreferenceManager pref;

    // 구글맵 관련 객체
    GoogleMap map;
    SupportMapFragment mapFragment;
    private Marker currentMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_detail);

        // 생성자
        init();

        Intent intent = getIntent();
        String key = intent.getStringExtra("key");

        // 데이터 받기
        getData(key);

        // 툴바
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 수정 버튼
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getApplicationContext(), CalendarModifyActivity.class);
                newIntent.putExtra("key", key);
                startActivity(newIntent);
            }
        });


        // 삭제 버튼
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("알림");
                builder.setMessage("정말 삭제 하시겠습니까?");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pref.newRemoveKey(view.getContext(), key);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.create().show();
            }
        });


        // 권한 설정
        checkDangerousPermissions();

        //지도 프래그먼트 설정
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.cd_mapFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                Location location = getLocationFromAddress(getApplicationContext(), placeText.getText().toString());

                // 초기 위치
                setDefaultLocation(location);

                if (androidx.core.app.ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && androidx.core.app.ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                map.setMyLocationEnabled(true);
            }
        });
        MapsInitializer.initialize(this);

    }

    private void init() {
        placeText = findViewById(R.id.cd_placeTextView);
        categoryText = findViewById(R.id.cd_categoryTextView);
        detailText = findViewById(R.id.cd_detailTextView);
        withText = findViewById(R.id.cd_withTextView);
        whenText = findViewById(R.id.cd_whenTextView);
        memoText = findViewById(R.id.cd_memoTextView);
        modifyButton = findViewById(R.id.cd_modifyButton);
        deleteButton = findViewById(R.id.cd_deleteButton);
        toolbar = findViewById(R.id.cd_toolbar);

        pref = new PreferenceManager();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData(String key) {
        String value = pref.getNewString(getApplication(), key);
        try {
            JSONObject jsonObject = new JSONObject(value);
            String place = (String) jsonObject.getString("place");
            String category = (String) jsonObject.getString("category");
            String detail = (String) jsonObject.getString("detail");
            String with = (String) jsonObject.getString("withText");
            String year = (String) jsonObject.getString("yearText");
            String month = (String) jsonObject.getString("monthText");
            String day = (String) jsonObject.getString("dayText");
            String memo = (String) jsonObject.getString("memo");

            placeText.setText(place);
            categoryText.setText(category);
            detailText.setText(detail);
            withText.setText(with);
            whenText.setText(year + "/" + month + "/" + day);
            memoText.setText(memo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 위치 확인
    private Location getLocationFromAddress(Context context, String address) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        Location resLocation = new Location("");
        try {
            addresses = geocoder.getFromLocationName(address, 5);
            if((addresses == null) || (addresses.size() == 0)) {
                return null;
            }
            Address addressLoc = addresses.get(0);

            resLocation.setLatitude(addressLoc.getLatitude());
            resLocation.setLongitude(addressLoc.getLongitude());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resLocation;
    }

    // 초기 위치
    public void setDefaultLocation(Location location) {
        //디폴트 위치
        LatLng DEFAULT_LOCATION;
        if (location == null) {
            DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        } else {
            DEFAULT_LOCATION = new LatLng(location.getLatitude(), location.getLongitude());
        }

        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 여부 확인하세요";

        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = map.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        map.moveCamera(cameraUpdate);

    }

    //------------------권한 설정 시작------------------------
    private void checkDangerousPermissions() {
        String[] permissions = {
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    //------------------권한 설정 끝------------------------

}