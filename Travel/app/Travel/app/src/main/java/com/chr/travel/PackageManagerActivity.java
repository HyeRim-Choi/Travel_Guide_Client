package com.chr.travel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;


import java.io.Serializable;
import java.util.List;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;
import vo.LoginVO;

/* 패키지 매니저 권한일때의 액티비티 */

public class PackageManagerActivity extends AppCompatActivity {

    TextView txt_manager_actionbar;
    Button btn_addGroup;
    ListView manager_group_listView;
    ArrayAdapter adapter;



    // login한 user 정보
    LoginVO vo;

    // login한 user 정보 불러오기
    Gson gson;

    // 그룹 정보 업데이트
    //SwipeRefreshLayout swipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_manager);

        Log.i("패키지 매니저", "패키지 매니저 액티비티 들어옴");

        // login 한 user 정보 불러오기
        onSearchData();

        // login 하지 않았다면
        if(vo == null){
            Log.i("login", "login 안됨");
            Toast.makeText(com.chr.travel.PackageManagerActivity.this,"로그인 후 이용해주세요", Toast.LENGTH_LONG).show();
           /* Intent i = new Intent(com.chr.travel.PackageManagerActivity.this, com.chr.travel.LoginActivity.class);
            startActivity(i);
            finish();*/
        }


        txt_manager_actionbar = findViewById(R.id.txt_manager_actionbar);
        btn_addGroup = findViewById(R.id.btn_addGroup);
        manager_group_listView = findViewById(R.id.manager_group_listView);
        //swipe = findViewById(R.id.swipe);


        btn_addGroup.setOnClickListener(click);


        // swipe가 당겨지면 ( swipe안되면 생명주기로 해보기 )
       /*swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                //당겼다가 손을 떼는 순간 호출되는 메서드


                //arrayList = new ArrayList<>();
                //서버통신이 마무리 되면 디스크를 제거
                swipe.setRefreshing(false);


            }
        });*/


    }

    // 매니저의 그룹 조회
  @Override
    protected void onResume() {
        super.onResume();

       onSearchData();

       try {
           AsyncTaskFactory.getApiGetTask(PackageManagerActivity.this, API_CHOICE.GROUP_SEARCH, vo.getUserId(), new AsyncTaskCallBack() {
               @Override
               public void onTaskDone(Object... params) {
                   // 그룹조회 성공 시
                   if((Integer)params[1] == 5){
                       adapter = new ArrayAdapter(PackageManagerActivity.this,
                               android.R.layout.simple_list_item_1,
                               (List) params[2]);
                       manager_group_listView = findViewById(R.id.manager_group_listView);
                       manager_group_listView.setAdapter(adapter);
                       manager_group_listView.setDividerHeight(10);
                   }
               }
           }).execute();

       }

       catch (Exception e){
           e.printStackTrace();
       }


       // title 클릭 시 멤버 정보 받아오기
       manager_group_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               String title = adapter.getItem(i).toString();

               try {
                   AsyncTaskFactory.getApiGetTask(PackageManagerActivity.this, API_CHOICE.GROUP_MEMBER, title, new AsyncTaskCallBack() {
                       @Override
                       public void onTaskDone(Object... params) {
                           if((Integer)params[1] == 6){
                               Intent i = new Intent(PackageManagerActivity.this, GroupActivity.class);
                               i.putExtra("title", title);
                               i.putExtra("members", (Serializable) params[2]);
                               startActivity(i);
                           }
                       }
                   }).execute();
               }
               catch (Exception e){
                   e.printStackTrace();
               }
           }
       });
    }



    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                // 그룹 생성 버튼 클릭 시
                case R.id.btn_addGroup:
                    Intent i = new Intent(com.chr.travel.PackageManagerActivity.this, com.chr.travel.ManagerAddGroupActivity.class);
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