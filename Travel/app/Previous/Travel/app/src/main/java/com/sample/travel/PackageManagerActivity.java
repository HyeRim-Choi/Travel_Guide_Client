package com.sample.travel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import connect.GetNodeConnect;
import vo.LoginVO;

/* 패키지 매니저 권한일때의 액티비티 */

public class PackageManagerActivity extends AppCompatActivity {

    TextView txt_manager_actionbar;
    Button btn_addGroup;
    ListView manager_group_listView;

    // Node와 통신해주는 객체 생성
    GetNodeConnect get_connect;
    // Node에게 전달해주는 user 정보
    Map<String, String> map;

    // login한 user 정보
    LoginVO vo;

    // login한 user 정보 불러오기
    Gson gson;

    // 그룹 정보 업데이트
    SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_manager);

        // login 한 user 정보 불러오기
        onSearchData();

        // login 하지 않았다면
        if(vo == null){
            Toast.makeText(PackageManagerActivity.this,"로그인 후 이용해주세요", Toast.LENGTH_LONG).show();
            Intent i = new Intent(PackageManagerActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        map = new HashMap<>();

        txt_manager_actionbar = findViewById(R.id.txt_manager_actionbar);
        btn_addGroup = findViewById(R.id.btn_addGroup);
        manager_group_listView = findViewById(R.id.manager_group_listView);
        swipe = findViewById(R.id.swipe);


        btn_addGroup.setOnClickListener(click);

        // swipe가 당겨지면
       swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //당겼다가 손을 떼는 순간 호출되는 메서드
                if(map != null){
                    map.clear();
                }

                map.put("userId",""+vo.getUserId());
                // node로 정보 전달
                get_connect = new GetNodeConnect(PackageManagerActivity.this);
                get_connect.request(map, 7);

                // 핸들러 호출(what 번호로 구분)
                handler.sendEmptyMessageDelayed(0,1000);
            }
        });

    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                // 그룹 생성 버튼 클릭 시
                case R.id.btn_addGroup:
                    Intent i = new Intent(PackageManagerActivity.this, ManagerAddGroupActivity.class);
                    startActivity(i);
                    break;
            }
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //약 1초간 로딩을 한다고 가정하여 만든 핸들러
            if(get_connect.get_res_chk == 2){
                Toast.makeText(PackageManagerActivity.this,"그룹 정보 왔다", Toast.LENGTH_LONG).show();
            }

            else if(get_connect.get_res_chk == 1){
                Toast.makeText(PackageManagerActivity.this,"그룹 조회 실패", Toast.LENGTH_LONG).show();
                return;
            }

            //서버통신이 마무리 되면 디스크를 제거
            swipe.setRefreshing(false);
        }
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