package com.chr.travel.tpackage;

import androidx.appcompat.app.AppCompatActivity;

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

import com.chr.travel.R;
import com.chr.travel.login.LoginActivity;
import com.chr.travel.mpackage.GroupActivity;
import com.chr.travel.mpackage.PackageManagerActivity;

import java.io.Serializable;
import java.util.List;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;
import vo.LoginVO;

public class PackageTravelerActivity extends AppCompatActivity {

    TextView txt_traveler_actionbar;
    ListView traveler_group_listView;
    ArrayAdapter adapter;


    // login한 user 정보
    LoginVO vo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_package_traveler);

        vo = LoginVO.getInstance();
    }

    // 뒤로 가기로 이 액티비티로 다시 돌아올 수 있으니 코드를 onResume에 삽입
    @Override
    protected void onResume() {
        super.onResume();

        vo = LoginVO.getInstance();

        // login 하지 않았다면
        if(vo.getUserId() == null){
            Toast.makeText(PackageTravelerActivity.this,"로그인 후 이용해주세요", Toast.LENGTH_LONG).show();
            Intent i = new Intent(PackageTravelerActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        txt_traveler_actionbar = findViewById(R.id.txt_traveler_actionbar);
        traveler_group_listView = findViewById(R.id.traveler_group_listView);

        txt_traveler_actionbar.setText(vo.getUserId() + "님이 존재하는 그룹");


        try {
            AsyncTaskFactory.getApiGetTask(PackageTravelerActivity.this, API_CHOICE.TRAVELER_GROUP_SEARCH, vo.getUserId(), new AsyncTaskCallBack() {
                @Override
                public void onTaskDone(Object... params) {
                    // 그룹조회 성공 시
                    if((Integer)params[0] == 1){
                        // 공부해서 Adapter class 만들어서 정리하기
                        adapter = new ArrayAdapter(PackageTravelerActivity.this, android.R.layout.simple_list_item_1, (List) params[1]) {

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
                        traveler_group_listView.setAdapter(adapter);
                    }
                }
            }).execute();

        }

        catch (Exception e){
            e.printStackTrace();
        }


        // title 클릭 시
        traveler_group_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String title = adapter.getItem(i).toString();

                try {
                    AsyncTaskFactory.getApiGetTask(PackageTravelerActivity.this, API_CHOICE.GROUP_MEMBER, title, new AsyncTaskCallBack() {
                        @Override
                        public void onTaskDone(Object... params) {
                            Intent i = new Intent(PackageTravelerActivity.this, VisualizationActivity.class);
                            // VisualizationActivity title 전달
                            i.putExtra("title", title);
                            startActivity(i);
                        }
                    }).execute();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}