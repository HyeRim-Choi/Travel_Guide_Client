package com.chr.travel.mpackage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.chr.travel.R;
import com.chr.travel.login.LoginActivity;

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

    // 그룹 정보 업데이트
    //SwipeRefreshLayout swipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_package_manager);

        vo = LoginVO.getInstance();

        // login 하지 않았다면
        if(vo == null){
            Log.i("login", "login 안됨");
            Toast.makeText(PackageManagerActivity.this,"로그인 후 이용해주세요", Toast.LENGTH_LONG).show();
           Intent i = new Intent(PackageManagerActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
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

      vo = LoginVO.getInstance();

       try {
           AsyncTaskFactory.getApiGetTask(PackageManagerActivity.this, API_CHOICE.GROUP_SEARCH, vo.getUserId(), new AsyncTaskCallBack() {
               @Override
               public void onTaskDone(Object... params) {
                   // 그룹조회 성공 시
                   if((Integer)params[0] == 1){
                       // 공부해서 Adapter class 만들어서 정리하기
                       adapter = new ArrayAdapter(PackageManagerActivity.this, android.R.layout.simple_list_item_1, (List) params[1]) {

                           @Override
                           public View getView(int position, View convertView, ViewGroup parent) {

                               View view = super.getView(position, convertView, parent);
                               TextView tv = (TextView) view.findViewById(android.R.id.text1);
                               tv.setTextColor(Color.BLACK);
                               tv.setPadding(20, 40, 0, 40);
                               return view;
                           }
                       };
                       manager_group_listView.setAdapter(adapter);
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
                           if((Integer)params[0] == 1){
                               Intent i = new Intent(PackageManagerActivity.this, GroupActivity.class);
                               i.putExtra("title", title);
                               i.putExtra("members", (Serializable) params[1]);
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
                    Intent i = new Intent(PackageManagerActivity.this, ManagerAddGroupActivity.class);
                    startActivity(i);
                    break;
            }
        }
    };


}