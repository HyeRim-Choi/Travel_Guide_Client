package com.sample.locationdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import connect.PostNodeConnect;

public class FindPwdActivity extends AppCompatActivity {

    EditText et_name, et_email, et_id;
    Button btn_findPwd;

    // Node와 통신해주는 객체 생성
    PostNodeConnect post_connect;
    // Node에게 전달해주는 user 정보
    Map<String, String> map;

    String name, email, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd);

        map = new HashMap<>();

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_id = findViewById(R.id.et_id);
        btn_findPwd = findViewById(R.id.btn_findPwd);


        // 비밀번호 찾기 버튼 클릭 시
        btn_findPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = et_name.getText().toString();
                email = et_email.getText().toString();
                id = et_id.getText().toString();

                // 비밀번호 찾기 정보 입력 다 했는지 확인
                if(BlankCheck(name, email, id)){
                    // Node.js에게 값 전달
                    post_connect = new PostNodeConnect(FindPwdActivity.this);
                    post_connect.request(map,5);

                    // 1초 정도 딜레이를 준 후 시작
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // pwd 찾기 성공 시
                            if(post_connect.post_res_chk == 0){
                                //Alert창 띄우기
                                AlertDialog.Builder dialog = new AlertDialog.Builder(FindPwdActivity.this);
                                dialog.setMessage("작성하신 이메일로 가서 임시 비밀번호를 확인해주세요\n 비밀번호를 변경해주세요");
                                dialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });

                                post_connect.post_res_chk = 1;

                                dialog.show();
                            }

                            else{
                                Toast.makeText(FindPwdActivity.this,"없는 정보입니다",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 1000);
                }
            }
        });
    }

    public boolean BlankCheck(String name, String email, String id){

        if(map!=null){
            map.clear();
        }

        if(name.isEmpty() || name == null){
            Log.i("test","name : " + name);
            Toast.makeText(FindPwdActivity.this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(email.isEmpty() || email == null){
            Toast.makeText(FindPwdActivity.this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(id.isEmpty() || id == null){
            Toast.makeText(FindPwdActivity.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 이메일 형식 유효성 검사
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if(!pattern.matcher(email).matches()){
            Toast.makeText(FindPwdActivity.this, "잘못된 이메일 형식입니다", Toast.LENGTH_SHORT).show();
        }

        map.put("name", name.trim());
        map.put("email", email.trim());
        map.put("userId", id.trim());

        return true;
    }
}