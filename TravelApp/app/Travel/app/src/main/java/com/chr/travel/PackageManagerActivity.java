package com.chr.travel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

import callback.AsyncTaskCallBack;
import connect.GetData;
import vo.LoginVO;

/* 패키지 매니저 권한일때의 액티비티 */

public class PackageManagerActivity extends AppCompatActivity {

    TextView txt_manager_actionbar;
    Button btn_addGroup;
    ListView manager_group_listView;


    // Node와 통신해주는 객체 생성
    GetData get_data;

    // login한 user 정보
    LoginVO vo;

    // login한 user 정보 불러오기
    Gson gson;

    // 그룹 정보 업데이트
    SwipeRefreshLayout swipe;

    ArrayList<String> title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_manager);

        // login 한 user 정보 불러오기
        onSearchData();

        // login 하지 않았다면
        if(vo == null){
            Toast.makeText(com.chr.travel.PackageManagerActivity.this,"로그인 후 이용해주세요", Toast.LENGTH_LONG).show();
            Intent i = new Intent(com.chr.travel.PackageManagerActivity.this, com.chr.travel.LoginActivity.class);
            startActivity(i);
            finish();
        }


        txt_manager_actionbar = findViewById(R.id.txt_manager_actionbar);
        btn_addGroup = findViewById(R.id.btn_addGroup);
        manager_group_listView = findViewById(R.id.manager_group_listView);
        swipe = findViewById(R.id.swipe);


        btn_addGroup.setOnClickListener(click);

        // ListView 클릭 시
        if(manager_group_listView!=null){
            manager_group_listView.setOnItemClickListener(list_click);
        }


        // swipe가 당겨지면 ( swipe안되면 생명주기로 해보기 )
       swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                //당겼다가 손을 떼는 순간 호출되는 메서드
                Log.i("eee" , "zdfsa");
                JSONObject postDataParam = new JSONObject();

                try {
                    postDataParam.put("userId",""+vo.getUserId());
                    Log.i("eee" , "id : " + vo.getUserId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // node로 정보 전달
                        get_data = new GetData(PackageManagerActivity.this, 7, vo.getUserId(), new AsyncTaskCallBack() {
                            @Override
                            public void onTaskDone(Object... params) {
                                Log.i("eee" , "zdfsa");
                            }
                        });

                    }
                },3000);


                //arrayList = new ArrayList<>();
                //서버통신이 마무리 되면 디스크를 제거
                swipe.setRefreshing(false);


            }
        });


    }

    // title 전달
    AdapterView.OnItemClickListener list_click = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            get_data = new GetData(PackageManagerActivity.this, 9, title.get(i), new AsyncTaskCallBack() {
                @Override
                public void onTaskDone(Object... params) {

                }
            });
        }
    };


    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                // 그룹 생성 버튼 클릭 시
                case R.id.btn_addGroup:
                    //ManagerAddGroupActivity로 고치기
                    Intent i = new Intent(com.chr.travel.PackageManagerActivity.this, com.chr.travel.GetLocationPraActivity.class);
                    startActivity(i);
                    break;
            }
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