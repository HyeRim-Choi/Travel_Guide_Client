package com.chr.travel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.regex.Pattern;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;

/* 비밀번호 찾기 */

public class FindPwdActivity extends AppCompatActivity {

    EditText et_name, et_email, et_id;
    Button btn_findPwd;

    String name, email, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd);


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
                    JSONObject postDataParam = new JSONObject();

                    try {
                        postDataParam.put("name", name.trim());
                        postDataParam.put("email", email.trim());
                        postDataParam.put("userId", id.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        AsyncTaskFactory.getApiPostTask(FindPwdActivity.this, API_CHOICE.FIND_PWD, new AsyncTaskCallBack() {
                            @Override
                            public void onTaskDone(Object... params) {
                                if((Integer)params[0] == 1){
                                    //Alert창 띄우기
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(FindPwdActivity.this);
                                    dialog.setMessage("작성하신 이메일로 가서 임시 비밀번호를 확인해주세요\n 비밀번호를 변경해주세요");
                                    dialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });

                                    dialog.show();
                                }
                            }
                        }).execute(postDataParam);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // 정보 입력 체크
    public boolean BlankCheck(String name, String email, String id){


        if(name.isEmpty() || name == null){
            Log.i("test","name : " + name);
            Toast.makeText(com.chr.travel.FindPwdActivity.this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(email.isEmpty() || email == null){
            Toast.makeText(com.chr.travel.FindPwdActivity.this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(id.isEmpty() || id == null){
            Toast.makeText(com.chr.travel.FindPwdActivity.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 이메일 형식 유효성 검사
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if(!pattern.matcher(email).matches()){
            Toast.makeText(com.chr.travel.FindPwdActivity.this, "잘못된 이메일 형식입니다", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
}