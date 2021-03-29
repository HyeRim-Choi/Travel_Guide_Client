package com.sample.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import connect.PostNodeConnect;
import vo.LoginVO;

public class LoginActivity extends AppCompatActivity {

    EditText et_id, et_pwd;
    Button btn_login, btn_signUp, btn_findId, btn_findPwd;
    CheckBox check_autoLogin;
    // Node와 통신해주는 객체 생성
    PostNodeConnect post_connect;
    // Node에게 전달해주는 id, pwd
    Map<String,String> map;

    String id, pwd;

    // user 정보를 저장하는 인터페이스 준비
    Gson gson;
    LoginVO vo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        map = new HashMap<>();

        et_id = findViewById(R.id.et_id);
        et_pwd = findViewById(R.id.et_pwd);

        btn_login = findViewById(R.id.btn_login);
        btn_signUp = findViewById(R.id.btn_signUp);
        btn_findId = findViewById(R.id.btn_findId);
        btn_findPwd = findViewById(R.id.btn_findPwd);

        check_autoLogin = (CheckBox)findViewById(R.id.check_autoLogin);

        // 버튼들 클릭 시
        btn_login.setOnClickListener(click);
        btn_signUp.setOnClickListener(click);
        btn_findId.setOnClickListener(click);
        btn_findPwd.setOnClickListener(click);

        // 자동 로그인 체크 시
        check_autoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {
                    // 자동 로그인 체크했을 경우
                    //shared로 id불러오기
                } else {
                    // 자동 로그인 체크 해제했을 경우
                }
            }
        });

    }

    // 버튼 클릭 이벤트
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i;
            switch (v.getId()){
                // 로그인 버튼 클릭 시
                case R.id.btn_login:

                    if(map!=null){
                        map.clear();
                    }

                    id = et_id.getText().toString();
                    pwd = et_pwd.getText().toString();

                    // 아이디, 비밀번호 다 입력했는지 확인
                    if(BlankCheck(id, pwd)){

                        // Node.js에게 값 전달
                        post_connect = new PostNodeConnect(LoginActivity.this);
                        post_connect.request(map, 3);

                        // 1초 정도 딜레이를 준 후 시작(버튼 2번 클릭 방지)
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                // 응답 ok_login 시 로그인 성공
                                if(post_connect.post_res_chk == 4){
                                    // login 한 user 정보 저장
                                    onSaveData();
                                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(i);

                                    post_connect.post_res_chk = 1;
                                    finish();
                                }

                                // 아이디가 존재하지 않을 때(응답이 fail일 때)
                                else if(post_connect.post_res_chk == 1){
                                    Toast.makeText(LoginActivity.this, "없는 아이디입니다", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // 비밀번호가 틀렸을 때
                                else if(post_connect.post_res_chk == 2){
                                    Toast.makeText(LoginActivity.this, "비밀번호를 잘 못 입력하셨습니다", Toast.LENGTH_SHORT).show();
                                    post_connect.post_res_chk = 1;
                                    return;
                                }
                            }
                        }, 1200);
                    }

                    break;

                // 회원가입 버튼 클릭 시
                case R.id.btn_signUp:
                    // 회원가입 액티비티로 이동
                    i = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(i);
                    break;

                // 아이디 찾기 버튼 클릭 시
                case R.id.btn_findId:
                    // 아이디 찾기 액티비티로 이동
                    i = new Intent(LoginActivity.this, FindIdActivity.class);
                    startActivity(i);
                    break;

                // 비밀번호 찾기 버튼 클릭 시
               case R.id.btn_findPwd:
                   // 비밀번호 찾기 액티비티로 이동
                   i = new Intent(LoginActivity.this, FindPwdActivity.class);
                   startActivity(i);
                    break;

            }
        }
    };


    // 로그인 정보를 다 입력했는지 확인하는 함수
    public boolean BlankCheck(String id, String pwd){

        if(id.isEmpty() || id == null){
            Toast.makeText(LoginActivity.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pwd.isEmpty() || pwd == null){
            Toast.makeText(LoginActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        // node에 전달 할 정보 넣기
        map.put("userId", id.trim());
        map.put("password", pwd.trim());


        return true;
    };

    // login 한 user 정보를 저장하는 함수
    public void onSaveData(){
        vo = post_connect.vo;

        if(vo == null){
            Log.i("test","vo 이상");
            return;
        }

        // Gson 인스턴스 생성
        gson = new GsonBuilder().create();
        // JSON 형식으로 변환
        String strUser = gson.toJson(vo, LoginVO.class);
        Log.i("test","loginUser : " + strUser);

        SharedPreferences sp =getSharedPreferences("LOGIN", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        // JSON 형식으로 변환한 객체를 저장
        editor.putString("vo", strUser);
        editor.commit();

    }




}