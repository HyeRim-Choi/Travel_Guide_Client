package com.chr.travel.mpackage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

public class PackageManagerActivity extends AppCompatActivity{

    TextView txt_manager_actionbar;
    Button btn_addGroup;
    ListView manager_group_listView;
    ArrayAdapter adapter;


    // login한 user 정보
    LoginVO vo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_package_manager);

    }

    // 뒤로 가기로 이 액티비티로 다시 돌아올 수 있으니 코드를 onResume에 삽입
  @Override
    protected void onResume() {
        super.onResume();

       vo = LoginVO.getInstance();

      // login 하지 않았다면
      if(vo.getUserId() == null){
          Toast.makeText(PackageManagerActivity.this,"로그인 후 이용해주세요", Toast.LENGTH_LONG).show();
          Intent i = new Intent(PackageManagerActivity.this, LoginActivity.class);
          startActivity(i);
          finish();
      }

      txt_manager_actionbar = findViewById(R.id.txt_manager_actionbar);
      btn_addGroup = findViewById(R.id.btn_addGroup);
      manager_group_listView = findViewById(R.id.manager_group_listView);

      btn_addGroup.setOnClickListener(click);


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
                                // ListView custom
                               View view = super.getView(position, convertView, parent);
                               TextView tv = (TextView) view.findViewById(android.R.id.text1);
                               tv.setTextColor(Color.BLACK);
                               tv.setPadding(50, 50, 0, 50);
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


       // title 클릭 시 멤버 정보 받아와서 GroupActivity로 이동
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
                               // GroupActivity에 title, member들 전달
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