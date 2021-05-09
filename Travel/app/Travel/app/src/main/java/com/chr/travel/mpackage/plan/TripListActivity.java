package com.chr.travel.mpackage.plan;

import androidx.appcompat.app.AppCompatActivity;

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

import com.chr.travel.R;
import com.chr.travel.mpackage.operation.GroupActivity;
import com.chr.travel.mpackage.operation.PackageManagerActivity;

import java.io.Serializable;
import java.util.List;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;
import vo.LoginVO;

/* 공유되어있는 여행 상품 목록 */

public class TripListActivity extends AppCompatActivity {


    Button btn_registerTripSchedule;
    ListView trip_list;
    ArrayAdapter adapter;

    // login한 user 정보
    LoginVO vo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_trip_list);
    }

    @Override
    protected void onResume() {
        super.onResume();

        vo = LoginVO.getInstance();

        btn_registerTripSchedule = findViewById(R.id.btn_registerTripSchedule);
        trip_list = findViewById(R.id.trip_list);


        // 여행 상품 목록 보여지도록 하기
        try {
            AsyncTaskFactory.getApiGetTask(TripListActivity.this, API_CHOICE.MANAGER_REGISTERED_ROUTE_TITLE, "", new AsyncTaskCallBack() {
                @Override
                public void onTaskDone(Object... params) {
                    if((Integer) params[0] == 1){
                        adapter = new ArrayAdapter(TripListActivity.this, android.R.layout.simple_list_item_1, (List) params[1]) {

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

                        trip_list.setAdapter(adapter);
                    }
                }
            }).execute();
        }

        catch (Exception e){
            e.printStackTrace();
        }



        // title 클릭 시 해당 title의 여행 일정, 소개글이나 정보가 보여지는 액티비티로 이동
        trip_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 목록 중 하나 클릭하면 TripDetailsActivity로 이동
                String title = adapter.getItem(position).toString();

                try {
                    AsyncTaskFactory.getApiGetTask(TripListActivity.this, 77, title, new AsyncTaskCallBack() {
                        @Override
                        public void onTaskDone(Object... params) {
                            if((Integer)params[0] == 1){
                                Intent i = new Intent(TripListActivity.this, TripDetailsActivity.class);
                                // TripDetailsActivity에 title, 여행일정, 세부정보를 보낸다
                                i.putExtra("title", title);
                                //i.putExtra("members", (Serializable) params[1]);
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


        btn_registerTripSchedule.setOnClickListener(click);

    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                // 여행 일정 등록하기 버튼 클릭 시
                case R.id.btn_registerTripSchedule:
                    // 여행 일정을 등록하는 Activity로 이동
                    Intent i = new Intent(TripListActivity.this, RegisterTripScheduleActivity.class);
                    startActivity(i);
                    break;
            }
        }
    };
}