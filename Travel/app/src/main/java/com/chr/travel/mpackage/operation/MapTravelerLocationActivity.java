package com.chr.travel.mpackage.operation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chr.travel.R;
import com.chr.travel.fragmentpackage.MapTravelerLocationFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;

/* 멤버들의 현재 위치를 가져오는 Map Activity */

public class MapTravelerLocationActivity extends AppCompatActivity {

    Button btn_refresh;

    // 서버에게 전달해 줄 title, date
    String info;

    // MapFragment
    MapTravelerLocationFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_map_traveler_location);

        Intent i = getIntent();
        info = i.getStringExtra("title");

        // 날짜 받기
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd");
        Date date = new Date();
        //String time = format.format(date);
        String time = "2021-06-09";

        // info = /:title/:date 이 형식으로 만들기
        info+="/"+ time;

        btn_refresh = findViewById(R.id.btn_refresh);
        btn_refresh.setOnClickListener(click);

    }


    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_refresh:
                    // 새로고침 버튼 클릭 시
                    try {
                        AsyncTaskFactory.getApiGetTask(MapTravelerLocationActivity.this, API_CHOICE.MEMBER_LOCATION_RELOAD_SEND, info, new AsyncTaskCallBack() {
                            @Override
                            public void onTaskDone(Object... params) {
                                // 서버 통신 성공 시 멤버들의 위치를 받아서 지도에 띄우기
                                if((Integer)params[0] == 1){

                                    // MapFragment로 선택된 항목번호 멤버들 위치 정보가 있는 ArrayList 전달
                                    mapFragment = new MapTravelerLocationFragment((ArrayList<Map>) params[1]);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_map, mapFragment).commit();

                                }
                            }
                        }).execute();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
       }
    };
}