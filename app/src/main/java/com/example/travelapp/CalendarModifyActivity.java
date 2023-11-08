package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarModifyActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private Button modifyButton;

    // 구글맵 관련 객체
    GoogleMap map;
    SupportMapFragment mapFragment;
    private Button mapButton;
    MarkerOptions myMarker;
    private Marker currentMarker = null;


    //데이터
    PreferenceManager pref;
    private EditText placeEditText;
    private EditText detailEditText;
    private EditText memoEditText;
    private EditText withEditText;
    private EditText whenEditText;
    private ImageView whenImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_modify);

        // 객체 생성
        init();

        // 툴바 설정
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 데이터 받기
        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        getData(key);

        // 스피너
        adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 달력 이미지 클릭
        whenImage.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    whenEditText.setText(year + "/" + (month+1) + "/" + dayOfMonth);
                }
            }, year, month, day);
            dateDialog.show();
        });

        // 권한 설정
        checkDangerousPermissions();

        //지도 프래그먼트 설정
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.cm_mapFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                Location location = getLocationFromAddress(getApplicationContext(), placeEditText.getText().toString());

                // 초기 위치
                setDefaultLocation(location);

                if (androidx.core.app.ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && androidx.core.app.ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                map.setMyLocationEnabled(true);
            }
        });
        MapsInitializer.initialize(this);

        // 지도 버튼
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(placeEditText.getText().toString().length() > 0) {
                    Location location = getLocationFromAddress(getApplicationContext(), placeEditText.getText().toString());

                    showCurrentLocation(location);
                }
            }
        });

        // 수정 버튼
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editDetail = detailEditText.getText().toString();
                String editPlace = placeEditText.getText().toString();
                String editMemo = memoEditText.getText().toString();
                String textCategory = spinner.getSelectedItem().toString();
                String withText = withEditText.getText().toString();

                String dateText = whenEditText.getText().toString();
                String yearText = dateText.split("/")[0];
                String monthText = dateText.split("/")[1];
                String dayText = dateText.split("/")[2];

                //Json 형식으로 저장
                String save_form = "{\"detail\":\"" + editDetail + "\",\"place\":\"" + editPlace + "\",\"memo\":\"" + editMemo + "\",\"category\":\"" + textCategory + "\",\"withText\":\"" + withText + "\",\"yearText\":\"" + yearText + "\",\"monthText\":\"" + monthText + "\",\"dayText\":\"" + dayText + "\"}";

                //값 수정하여 저장
                pref.setNewString(getApplication(),key,save_form);

                // key값이 겹치지 않도록 현재 시간으로 부여
                long now = System.currentTimeMillis();
                Date mDate = new Date(now);
                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String getTime = simpleDate.format(mDate).toString();

                pref.setNewString(getApplicationContext(), getTime, save_form);
                pref.newRemoveKey(getApplicationContext(), key);

                Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(newIntent);
            }
        });

    }

    //toolbar의 back키 눌렀을 때 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        toolbar = findViewById(R.id.cm_toolbar);
        spinner = findViewById(R.id.cm_categorySpinner);
        placeEditText = findViewById(R.id.cm_placeTextView);
        mapButton = findViewById(R.id.cm_map_button);
        modifyButton = findViewById(R.id.cm_modifyButton);
        detailEditText = findViewById(R.id.cm_detailTextView);
        memoEditText = findViewById(R.id.cm_memoTextView);
        whenEditText = findViewById(R.id.cm_whenTextView);
        withEditText = findViewById(R.id.cm_withTextView);
        whenImage = findViewById(R.id.cm_whenImageView);

        sharedPreferences = getSharedPreferences("complete_data", MODE_PRIVATE);
        pref = new PreferenceManager();
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

            placeEditText.setText(place);
            detailEditText.setText(detail);
            spinner.setSelection(getIndex(spinner, category));
            withEditText.setText(with);
            whenEditText.setText(year + "/" + month + "/" + day);
            memoEditText.setText(memo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // spinner 초기값 인덱스
    private int getIndex(Spinner spinner, String item) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(item)) {
                return i;
            }
        }
        return 0;
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

    private void showCurrentLocation(Location location) {
        LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());

        //화면 확대, 숫자가 클수록 확대
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));

        //마커 찍기
        Location targetLocation = new Location("");
        targetLocation.setLatitude(location.getLatitude());
        targetLocation.setLongitude(location.getLongitude());
        showMyMarker(targetLocation);
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

    private void showMyMarker(Location location) {
        if(myMarker == null) {
            myMarker = new MarkerOptions();
            myMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
            myMarker.title(placeEditText.getText().toString());
            myMarker.snippet("여기 저장⭐️");
            myMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            map.addMarker(myMarker);
        }
    }

}