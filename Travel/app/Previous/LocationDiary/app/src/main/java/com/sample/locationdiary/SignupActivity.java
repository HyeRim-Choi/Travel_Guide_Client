package com.sample.locationdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connect.GetNodeConnect;
import connect.PostNodeConnect;

public class SignupActivity extends AppCompatActivity {

    EditText et_name, et_id, et_pwd, et_checkPwd, et_email;
    Button btn_checkId, btn_signUp;

    boolean check_id = false;
    boolean check_pwd = false;

    String name, id, pwd, checkPwd, email;

    // Node와 통신해주는 객체 생성
    GetNodeConnect get_connect;
    PostNodeConnect post_connect;

    // Node에게 전달해주는 user 정보
    Map<String, String> map;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        map = new HashMap<>();

        et_name = findViewById(R.id.et_name);
        et_id = findViewById(R.id.et_id);
        et_pwd = findViewById(R.id.et_pwd);
        et_checkPwd = findViewById(R.id.et_checkPwd);
        et_email = findViewById(R.id.et_email);
        btn_checkId = findViewById(R.id.btn_checkId);
        btn_signUp = findViewById(R.id.btn_signUp);


        // 버튼들 클릭 시
        btn_checkId.setOnClickListener(click);
        btn_signUp.setOnClickListener(click);


    }

    // 버튼 클릭 이벤트
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                // 아이디 중복 체크 클릭 시
                case R.id.btn_checkId:
                    if(map!=null){
                        map.clear();
                    }

                    id = et_id.getText().toString();

                    if(id.isEmpty() || id == null){
                        Toast.makeText(SignupActivity.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 서버를 통해 해당아이디 사용가능한지 확인받기
                   else{
                       map.put("userId", id.trim());
                       // node로 정보 전달
                       get_connect = new GetNodeConnect(SignupActivity.this);
                       get_connect.request(map, 1);
                    }

                    // 1초 정도 딜레이를 준 후 시작(버튼 2번 클릭 방지)
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if(get_connect.get_res_chk == 0){
                                check_id = true;
                                Toast.makeText(SignupActivity.this, "해당 아이디 사용 가능합니다", Toast.LENGTH_SHORT).show();
                                get_connect.get_res_chk = 1;
                            }

                            else{
                                Toast.makeText(SignupActivity.this, "해당 아이디는 이미 사용중입니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 1200);

                    break;


                // 회원가입 버튼 클릭 시
                case R.id.btn_signUp:
                    if(map!=null){
                        map.clear();
                    }

                    // 회원가입 시 정보들 다 채웠는지 확인
                    if(BlankCheck()){
                        // 아이디 중복 체크했는지 확인
                        if(check_id){
                            // 노드와 연결해서 db에 정보 저장
                            post_connect = new PostNodeConnect(SignupActivity.this);
                            post_connect.request(map, 2);
                        }
                        // 아이디 중복 체크를 하지 않았다면
                        else{
                            Toast.makeText(SignupActivity.this, "아이디 중복체크를 해주세요", Toast.LENGTH_LONG).show();
                            return;
                        }

                        // 1초 정도 딜레이를 준 후 시작(버튼 2번 클릭 방지)
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                // 응답이 OK이면 회원가입 완료
                                if(post_connect.post_res_chk == 0){
                                    Toast.makeText(SignupActivity.this, "회원가입이 완료 되었습니다", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                                    startActivity(i);
                                    post_connect.post_res_chk = 1;
                                    finish();
                                }
                                else{
                                    Toast.makeText(SignupActivity.this, "회원가입이 되지 않았습니다", Toast.LENGTH_LONG).show();
                                }
                            }
                        }, 1000);

                    }

                     break;
            }
        }
    };


    // 회원가입 정보를 다 올바르게 입력했는지 확인하는 함수
    public boolean BlankCheck(){

        name = et_name.getText().toString();
        id = et_id.getText().toString();
        pwd = et_pwd.getText().toString();
        checkPwd = et_checkPwd.getText().toString();
        email = et_email.getText().toString();

        if(name.isEmpty() || name == null){
            Toast.makeText(SignupActivity.this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(email.isEmpty() || email == null){
            Toast.makeText(SignupActivity.this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(id.isEmpty() || id == null){
            Toast.makeText(SignupActivity.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pwd.isEmpty() || pwd == null){
            Toast.makeText(SignupActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(checkPwd.isEmpty() || checkPwd == null){
            Toast.makeText(SignupActivity.this, "비밀번호 확인을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 이메일 형식 유효성 검사
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if(!pattern.matcher(email).matches()){
            Toast.makeText(SignupActivity.this, "잘못된 이메일 형식입니다", Toast.LENGTH_SHORT).show();
            return false;
        }


        if(pwd.equals(checkPwd)){
            check_pwd = true;
        }
        else{
            Toast.makeText(SignupActivity.this, "비밀번호가 다르게 입력되었습니다", Toast.LENGTH_SHORT).show();
            return false;
        }

        // node에 전달 할 정보 넣기
        map.put("name", name.trim());
        map.put("email", email.trim());
        map.put("userId", id.trim());
        map.put("password", pwd.trim());


        return true;
    };



}