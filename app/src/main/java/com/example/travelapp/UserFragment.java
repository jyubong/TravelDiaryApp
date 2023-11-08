package com.example.travelapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

public class UserFragment extends Fragment {
    ActionBar actionBar;
    private TextView username;
    private PreferenceManager pref;
    private Button nameChangeButton;
    private Button logoutButton;
    private String id;
    private String pw;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = new PreferenceManager();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("설정");

        init(view);

        // 이름 설정
//        getUserData();

        nameChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                LayoutInflater inflater = LayoutInflater.from(view.getContext());
                View dialogView = inflater.inflate(R.layout.name_change_dialog, null);
                builder.setView(dialogView);

                EditText nameChangeEditText = dialogView.findViewById(R.id.nameChangeEditText);

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String changedName = nameChangeEditText.getText().toString();
                        username.setText(changedName);
//
//                        pref.removeKey("user_data", getContext(), id);
//
//                        String save_form = "{\"name\":\""+changedName+"\",\"id\":\""+id+"\",\"password\":\""+pw+"\"}";
//                        pref.setString("user_data", getContext(), id, save_form);
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.create().show();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

        return view;
    }

    private void init(View view) {
        username = view.findViewById(R.id.userNameText);
        nameChangeButton = view.findViewById(R.id.userChangeButton);
        logoutButton = view.findViewById(R.id.userLogout);
    }

    private void getUserData() {
        Intent intent = getActivity().getIntent();
        String key = intent.getStringExtra("id");

        String value = pref.getString("user_data", getContext(), key);
        try {
            JSONObject jsonObject = new JSONObject(value);
            String nameData = (String) jsonObject.getString("name");
            Log.d("이름", nameData);
            id = (String) jsonObject.getString("id");
            pw = (String) jsonObject.getString("password");

            username.setText(nameData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}