package com.example.travelapp;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelapp.MainActivity;
import com.example.travelapp.R;
import com.example.travelapp.databinding.ActivityLoginBinding;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private SharedPreferences sharedPreferences;
    private PreferenceManager pref;
    private CheckBox autoCheckBox;
    private String id;
    private String pw;
    private Boolean isChecked;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 초기화
        usernameEditText = binding.username;
        passwordEditText = binding.password;
//        autoCheckBox = binding.autoCheckBox;
        pref = new PreferenceManager();

        // 데이터 가져오기
        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

//        if (sharedPreferences.getBoolean("checked", false)) {
//            startActivity(new);
//        }

        // event handler
        usernameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });

//        // 체크박스 체크
//        if (autoCheckBox.isChecked()) {
//            editor.putBoolean("checked", true);
//        }

        // 로그인 버튼
        binding.login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (sharedPreferences.getString(usernameEditText.getText().toString(), "none").equals("none")) {
                    showToast("존재하지 않는 아이디입니다.");
                } else {
                    String value = pref.getString("user_data", getApplicationContext(), usernameEditText.getText().toString());
                    try {
                        JSONObject jsonObject = new JSONObject(value);
                        String pwData = (String) jsonObject.getString("password");

                        if (pwData.equals(passwordEditText.getText().toString())) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("id", usernameEditText.getText().toString());
                            showToast("로그인 성공");
                            startActivity(intent);
                        } else {
                            showToast("일치하지 않는 비밀번호입니다.");
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        // 회원가입 버튼
        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}