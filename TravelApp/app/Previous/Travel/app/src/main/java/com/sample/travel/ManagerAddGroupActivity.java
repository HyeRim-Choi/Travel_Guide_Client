package com.sample.travel;

import androidx.appcompat.app.AppCompatActivity;

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

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import connect.GetNodeConnect;
import connect.PostNodeConnect;
import connect.PostNodeObjectConnect;
import vo.LoginVO;

/* 그룹 생성 액티비티 */

public class ManagerAddGroupActivity extends AppCompatActivity {

    EditText et_title, et_userId;
    Button btn_addGroup, btn_addId;
    TextView txt_addId;

    String title, id;

    boolean check_id = false;

    // Node와 통신해주는 객체 생성
    PostNodeObjectConnect post_object_connect;
    PostNodeConnect post_connect;
    GetNodeConnect get_connect;
    Map<String,String> map;

    // login한 user 정보
    LoginVO vo;

    // login한 user 정보 불러오기
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_add_group);

        map = new HashMap<>();

        // login 한 user 정보 불러오기
        onSearchData();

        // login 하지 않았다면
        if(vo == null){
            Toast.makeText(ManagerAddGroupActivity.this,"로그인 후 이용해주세요", Toast.LENGTH_LONG).show();
            Intent i = new Intent(ManagerAddGroupActivity.this, LoginActivity.class);
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
                    if (map != null) {
                        map.clear();
                    }

                    title = et_title.getText().toString();
                    String ids = txt_addId.getText().toString();
                    Log.i("test","ids : "+ids);

                    if (BlankCheck(title, ids)) {
                        map.put("title", title);
                        map.put("userId", ids);
                        map.put("manager",vo.getUserId());

                        // Node.js에게 값 전달
                        post_connect = new PostNodeConnect(ManagerAddGroupActivity.this);
                        post_connect.request(map, 6);

                        // 1초 정도 딜레이를 준 후 시작
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                if(post_connect.post_res_chk == 0){
                                    Toast.makeText(ManagerAddGroupActivity.this, title + "그룹 생성이 되었습니다", Toast.LENGTH_SHORT).show();
                                    post_connect.post_res_chk = 1;
                                    finish();
                                }
                            }
                        }, 1000);

                        break;
                    }

                // 아이디 추가 버튼 클릭 시
                case R.id.btn_addId:
                    if(map!=null){
                        map.clear();
                    }

                    id = et_userId.getText().toString();

                    if(id.isEmpty() || id == null){
                        Toast.makeText(ManagerAddGroupActivity.this, "그룹으로 초대 할 아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 서버를 통해 해당아이디 사용가능한지 확인받기
                    else{
                        map.put("userId", id.trim());
                        // node로 정보 전달
                        get_connect = new GetNodeConnect(ManagerAddGroupActivity.this);
                        get_connect.request(map, 1);
                    }

                    // 1초 정도 딜레이를 준 후 시작(버튼 2번 클릭 방지)
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // 아이디가 존재하면(로그인 중복 체크 재활용으로 0과 1이 바뀜)
                            if(get_connect.get_res_chk == 1){
                                check_id = true;
                                if(txt_addId.getText().toString().isEmpty()){
                                    txt_addId.setText(id);
                                }
                                else{
                                    txt_addId.append(","+id);
                                }
                            }

                            // 아이디가 존재하지 않으면
                            else{
                                Toast.makeText(ManagerAddGroupActivity.this, "해당 아이디는 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                                get_connect.get_res_chk = 1;
                                return;
                            }
                        }
                    }, 1200);

                    et_userId.setText(null);
                    break;
            }
        }
    };

    public boolean BlankCheck(String title, String ids){

        if(title.isEmpty() || title == null){
            Toast.makeText(ManagerAddGroupActivity.this, "그룹 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(ids.isEmpty() || ids == null){
            Toast.makeText(ManagerAddGroupActivity.this, "초대 할 여행객의 아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
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