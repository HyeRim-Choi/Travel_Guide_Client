package com.chr.travel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import callback.AsyncTaskCallBack;
import connect.GetData;
import connect.PostInsertData;

import vo.LoginVO;

/* 그룹 생성 액티비티 */

public class ManagerAddGroupActivity extends AppCompatActivity {

    EditText et_title, et_userId;
    Button btn_addGroup, btn_addId;
    TextView txt_addId;

    String title, id;

    // Node와 통신해주는 객체 생성
    GetData get_data;

    JSONObject postDataParam;

    // login한 user 정보
    LoginVO vo;

    // login한 user 정보 불러오기
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_add_group);

        // login 한 user 정보 불러오기
        onSearchData();

        // login 하지 않았다면
        if(vo == null){
            Toast.makeText(com.chr.travel.ManagerAddGroupActivity.this,"로그인 후 이용해주세요", Toast.LENGTH_LONG).show();
            Intent i = new Intent(com.chr.travel.ManagerAddGroupActivity.this, com.chr.travel.LoginActivity.class);
            startActivity(i);
            finish();
        }

        et_title = findViewById(R.id.et_title);
        et_userId = findViewById(R.id.et_userId);
        btn_addGroup = findViewById(R.id.btn_addGroup);
        btn_addId = findViewById(R.id.btn_addId);
        txt_addId = findViewById(R.id.txt_addId);

        btn_addGroup.setOnClickListener(click);
        btn_addId.setOnClickListener(click);
    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // 그룹 추가 버튼 클릭 시
                case R.id.btn_addGroup:
                    postDataParam = new JSONObject();

                    title = et_title.getText().toString();
                    String ids = txt_addId.getText().toString();
                    Log.i("test","ids : "+ids);

                    if (BlankCheck(title, ids)) {
                        try {
                            postDataParam.put("title", title);
                            postDataParam.put("userId", ids);
                            postDataParam.put("manager",vo.getUserId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Node.js에게 값 전달
                        new PostInsertData(ManagerAddGroupActivity.this,6, new AsyncTaskCallBack(){

                            @Override
                            public void onTaskDone(Object... params) {
                                if((Integer)params[1] == 5){
                                    Intent i = new Intent(ManagerAddGroupActivity.this, PackageManagerActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }).execute(postDataParam);
                        break;
                    }

                // 아이디 추가 버튼 클릭 시
                case R.id.btn_addId:
                    postDataParam = new JSONObject();

                    id = et_userId.getText().toString();

                    if(id.isEmpty() || id == null){
                        Toast.makeText(com.chr.travel.ManagerAddGroupActivity.this, "그룹으로 초대 할 아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 서버를 통해 해당아이디 사용가능한지 확인받기
                    else{

                        try {
                            postDataParam.put("userId", id.trim());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // node로 정보 전달
                        new GetData(ManagerAddGroupActivity.this, 8, id, new AsyncTaskCallBack() {
                            @Override
                            public void onTaskDone(Object... params) {
                                if((Integer)params[1] == 1){
                                    if(txt_addId.getText().toString().isEmpty()){
                                        txt_addId.setText(id);
                                    }
                                    else{
                                        txt_addId.append(","+id);
                                    }
                                }
                                et_userId.setText(null);
                            }
                        }).execute();

                    }
                    break;
            }
        }
    };

    public boolean BlankCheck(String title, String ids){

        if(title.isEmpty() || title == null){
            Toast.makeText(com.chr.travel.ManagerAddGroupActivity.this, "그룹 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(ids.isEmpty() || ids == null){
            Toast.makeText(com.chr.travel.ManagerAddGroupActivity.this, "초대 할 여행객의 아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    };


    // 저장한 user 정보를 불러오는 함수
    public void onSearchData(){

        gson = new Gson();

        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        String strUser = sp.getString("vo","");
        Log.i("test","loginUserSearch : " + strUser);

        vo = gson.fromJson(strUser, LoginVO.class);
    }
}