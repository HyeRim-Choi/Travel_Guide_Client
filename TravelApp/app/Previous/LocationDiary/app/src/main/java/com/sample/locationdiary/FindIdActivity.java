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
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connect.GetNodeConnect;
import connect.PostNodeConnect;

public class FindIdActivity extends AppCompatActivity {

    EditText et_name, et_email;
    Button btn_findId;

    // Node와 통신해주는 객체 생성
    PostNodeConnect post_connect;
    // Node에게 전달해주는 user 정보
    Map<String, String> map;

    String name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        map = new HashMap<>();

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        btn_findId = findViewById(R.id.btn_findId);



        // 아이디 찾기 버튼 클릭 시
        btn_findId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = et_name.getText().toString();
                email = et_email.getText().toString();

                // id 찾기 정보 입력 다 했는지 확인
                if(BlankCheck(name, email)){
                    // Node.js에게 값 전달
                    post_connect = new PostNodeConnect(FindIdActivity.this);
                    post_connect.request(map,4);

                    // 1초 정도 딜레이를 준 후 시작
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // id 찾기 성공 시
                            if(post_connect.post_res_chk == 3){
                               // 아이디 Alert창 띄우기
                                AlertDialog.Builder dialog = new AlertDialog.Builder(FindIdActivity.this);
                                dialog.setTitle(name + "님의 아이디");
                                dialog.setMessage(post_connect.findId);
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
                                Toast.makeText(FindIdActivity.this,"없는 정보입니다",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 1000);

                }
            }
        });
    }

    // id찾기 정보를 다 입력했는지 확인하는 함수
    public boolean BlankCheck(String name, String email){

        if(map!=null){
            map.clear();
        }

        if(name.isEmpty() || name == null){
            Toast.makeText(FindIdActivity.this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(email.isEmpty() || email == null){
            Toast.makeText(FindIdActivity.this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }


        // 이메일 형식 유효성 검사
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if(!pattern.matcher(email).matches()){
            Toast.makeText(FindIdActivity.this, "잘못된 이메일 형식입니다", Toast.LENGTH_SHORT).show();
            return false;
        }

        // node에 전달해 줄 정보 넣기
        map.put("name", name.trim());
        map.put("email", email.trim());

        return true;
    }
}