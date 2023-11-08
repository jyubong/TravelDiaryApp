package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelapp.databinding.ActivityRegisterBinding;

import org.w3c.dom.Text;

import java.util.Collection;
import java.util.Iterator;

public class RegisterActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private PreferenceManager pref;
    private ActivityRegisterBinding binding;
    private TextView pwCheckMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 초기화
        pwCheckMsg = binding.passwordCheckMsg;
        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);

        // 아이디 확인
        binding.idCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = binding.idEditText.getText().toString();
                sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
                if (sharedPreferences.getString(id, "none").equals("none")) {
                    showToast("아이디 사용 가능");
                } else {
                    showToast("이미 존재하는 아이디입니다.");
                }
            }
        });

        // 비밀번호 길이
        binding.passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.passwordEditText.getText().toString().length() < 8) {
                    pwCheckMsg.setText("비밀번호를 8자이상 입력해주세요.");
                    pwCheckMsg.setTextColor(Color.RED);
                } else {
                    pwCheckMsg.setText("비밀번호가 8자이상 입니다.");
                    pwCheckMsg.setTextColor(Color.BLUE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 비밀번호 확인
        binding.password2EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pw = binding.passwordEditText.getText().toString();
                String pw2 = binding.password2EditText.getText().toString();
                if (pw.equals(pw2)) {
                    pwCheckMsg.setText("비밀번호가 일치합니다.");
                    pwCheckMsg.setTextColor(Color.BLUE);
                } else {
                    pwCheckMsg.setText("비밀번호가 일치하지 않습니다.");
                    pwCheckMsg.setTextColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 회원가입
        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = binding.idEditText.getText().toString();
                String name = binding.nameEditText.getText().toString();
                String pw = binding.passwordEditText.getText().toString();
                String pw2 = binding.password2EditText.getText().toString();
                Boolean idChecked = false;

                if (sharedPreferences.getString(id, "none").equals("none")) {
                    idChecked = true;
                } else {
                    showToast("이미 존재하는 아이디입니다. 아이디를 다시 확인해주세요.");
                }

                if (name.isEmpty()) {
                    showToast("이름을 입력해주세요.");
                } else if (pw.length() > 7 && pw.equals(pw2) && idChecked) {
                    String save_form = "{\"name\":\""+name+"\",\"id\":\""+id+"\",\"password\":\""+pw+"\"}";
                    pref.setString("user_data", getApplicationContext(), id, save_form);
                    showToast("가입 성공");
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } else {
                    showToast("비밀번호를 확인해주세요.");
                }
            }
        });

        // 취소하기
        binding.registerCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("클릭", "취소 클릭");
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

}