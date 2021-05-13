package com.chr.travel.tpackage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chr.travel.R;
import com.chr.travel.login.LoginActivity;
import com.chr.travel.mpackage.operation.GroupActivity;
import com.chr.travel.mpackage.operation.PackageManagerActivity;
import com.chr.travel.mpackage.operation.ShowScheduleActivity;

import java.io.Serializable;
import java.util.List;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;
import vo.LoginVO;

public class PackageTravelerActivity extends AppCompatActivity {

    private static final int PACKAGE_TRAVELER_ACTIVITY_REQUEST_CODE = 9;

    TextView txt_traveler_actionbar;
    ListView traveler_group_listView;
    ArrayAdapter adapter;

    String product;


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
                    AsyncTaskFactory.getApiGetTask(PackageTravelerActivity.this, API_CHOICE.GROUP_REGISTERED_ROUTE_DETAILS, title, new AsyncTaskCallBack() {
                        @Override
                        public void onTaskDone(Object... params) {
                            if((Integer)params[0] == 1){
                                Intent i = new Intent(PackageTravelerActivity.this, ShowScheduleActivity.class);
                                // ShowScheduleActivity에 그룹 이름, 여행일정, 세부정보를 보낸다
                                i.putExtra("title", title);
                                i.putExtra("information", (String) params[1]);
                                i.putExtra("memo", (String) params[2]);
                                i.putExtra("schedule", (Serializable) params[3]);
                                i.putExtra("guideName",(String) params[4]);
                                i.putExtra("guideId",(String) params[5]);
                                startActivityForResult(i, PACKAGE_TRAVELER_ACTIVITY_REQUEST_CODE);
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
}