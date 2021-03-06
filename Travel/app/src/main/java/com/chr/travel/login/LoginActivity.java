package com.chr.travel.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chr.travel.R;
import com.chr.travel.home.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;
import service.fcm.FirebaseInstanceIDService;

public class LoginActivity extends AppCompatActivity {

    EditText et_id, et_pwd;
    Button btn_login, btn_signUp, btn_findId, btn_findPwd;
    CheckBox check_autoLogin;

    String id, pwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitylogin_login);

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
                    JSONObject postDataParam = new JSONObject();

                    id = et_id.getText().toString();
                    pwd = et_pwd.getText().toString();

                    // 아이디, 비밀번호 다 입력했는지 확인
                    if(BlankCheck(id, pwd)){
                        // 기기마다 Token 전달해주기
                       FirebaseInstanceIDService fcm = new FirebaseInstanceIDService();
                       fcm.onTokenRefresh();

                        // node에 전달 할 정보 넣기
                        try {
                            postDataParam.put("userId", id.trim());
                            postDataParam.put("password", pwd.trim());
                            postDataParam.put("token",fcm.getToken());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            AsyncTaskFactory.getApiPostTask(LoginActivity.this, API_CHOICE.LOGIN, new AsyncTaskCallBack() {
                                @Override
                                public void onTaskDone(Object... params) {
                                    // 로그인 성공하면
                                    if((Integer)params[0] == 1){
                                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            }).execute(postDataParam);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

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

        return true;
    };


}