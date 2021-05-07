package com.chr.travel.mpackage.plan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.chr.travel.R;

import vo.LoginVO;

/* 공유되어있는 여행 상품 목록 */

public class TripListActivity extends AppCompatActivity {


    Button btn_registerTripSchedule;
    ListView trip_list;

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


        // ****** 서버와 통신해서 여행 상품 목록 보여지도록 하기 *****
        try {

        }

        catch (Exception e){
            e.printStackTrace();
        }



        // title 클릭 시 해당 title의 여행 일정, 소개글이나 정보가 보여지는 액티비티로 이동
        trip_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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