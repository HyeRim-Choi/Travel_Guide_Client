package com.chr.travel.mpackage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.chr.travel.R;

import java.util.ArrayList;

import adapter.AdapterRegisterTripSchedule;
import adapter.RegisterTripSchedule;

// *********** delete

public class RegisterRouteActivity extends AppCompatActivity implements AdapterRegisterTripSchedule.OnButtonSelectedListener{

    private static final int REGISTER_PLACE_REQUEST_CODE = 0;

    String startDate, endDate, routePlaces, date, title;
    int startDay, endDay;

    Button btn_finish;

    // 날짜 listView
    ListView trip_schedule;
    RegisterTripSchedule registerTripRoute;
    ArrayList<RegisterTripSchedule> data;
    AdapterRegisterTripSchedule adapterRegisterTripSchedule;

    //int cnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_register_route);

        //cnt = 0;

    }

    // 돌아올 때를 대비해서 onResume으로 버튼, 경로 데이터 수정, 추가하기
    @Override
    protected void onResume() {
        super.onResume();

        //Log.i("regiRouter :: ", "" + cnt);

        trip_schedule = findViewById(R.id.trip_schedule);
        btn_finish = findViewById(R.id.btn_finish);

        Intent intent = getIntent();
        startDate = intent.getStringExtra("startDate");
        endDate = intent.getStringExtra("endDate");
        title = intent.getStringExtra("title");


        String date = startDate.substring(0,8);
        startDay = Integer.parseInt(startDate.substring(8));
        endDay = Integer.parseInt(endDate.substring(8));

        data = new ArrayList<>();

        // 날짜를 ListView에 띄우기
        for(int i = 0; i < endDay - startDay + 1 ; i++){
            int dayI = startDay + i;
            String dayS = "";

            if( Integer.toString(dayI).length() == 1){
                dayS = "0" + dayI;
            }

            dayS = date + dayS;

            registerTripRoute = new RegisterTripSchedule();
            registerTripRoute.setDay(dayS);
            registerTripRoute.setBtn("관광지 등록");

            if(dayS.equals(date)){
                registerTripRoute.setBtn("관광지 수정");
                registerTripRoute.setSchedule(routePlaces);
            }

            data.add(registerTripRoute);
        }

        adapterRegisterTripSchedule = new AdapterRegisterTripSchedule(this, R.layout.adapter_package_trip_schedule, data);

        trip_schedule.setAdapter(adapterRegisterTripSchedule);

        // 완료 버튼 클릭 시
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 하나라도 있으면 전달
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

    }

    // 관광지 등록 버튼 클릭 시
    @Override
    public void onButtonSelected(int i) {

        String date = ((RegisterTripSchedule)adapterRegisterTripSchedule.getItem(i)).getDay();

        // 관광지 검색하는 액티비티로 이동
        Intent intent = new Intent(RegisterRouteActivity.this, PlaceSearchActivity.class);
        intent.putExtra("date", date);
        intent.putExtra("title", title);
        startActivityForResult(intent, REGISTER_PLACE_REQUEST_CODE);

    }

    // 관광지 등록 결과 반환
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != REGISTER_PLACE_REQUEST_CODE || data == null){
            return;
        }

        routePlaces = data.getStringExtra("routePlaces");
        date = data.getStringExtra("date");

        //cnt++;

        Log.i("regiRouter", routePlaces);

    }
}