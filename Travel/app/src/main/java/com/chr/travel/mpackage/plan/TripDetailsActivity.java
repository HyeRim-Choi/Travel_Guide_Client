package com.chr.travel.mpackage.plan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chr.travel.R;
import com.chr.travel.mpackage.operation.ManagerAddGroupActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* 해당 여행 상품의 세부정보를 보여주는 액티비티 */

public class TripDetailsActivity extends AppCompatActivity {

    TextView txt_title, txt_information, txt_point, txt_term, txt_schedule;
    Button btn_operateTrip, btn_close;

    String title, information, memo;
    ArrayList<String> schedule;

    int day;

    ArrayList<Map> dayScheduleList;
    ArrayList<ArrayList> scheduleList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_trip_details);

        txt_title = findViewById(R.id.txt_title);
        txt_point = findViewById(R.id.txt_point);
        txt_information = findViewById(R.id.txt_information);
        txt_term = findViewById(R.id.txt_term);
        txt_schedule = findViewById(R.id.txt_schedule);
        btn_operateTrip = findViewById(R.id.btn_operateTrip);
        btn_close = findViewById(R.id.btn_close);

        schedule = new ArrayList<>();
        scheduleList = new ArrayList<>();


        // 인텐트 가져오기
        Intent intent = getIntent();
        // title
        title = intent.getStringExtra("title");
        // introduce
        information = intent.getStringExtra("information");
        // memo
        memo = intent.getStringExtra("memo");
        // schedule
        schedule = intent.getStringArrayListExtra("schedule");

        Log.i("result1", "json : " + schedule);

        // JSONArray를 여행 일정으로 보여줄 수 있는 상태로 만들기
        for(int i=0;i<schedule.size();i++) {
            try {

                JSONArray daySchedule = new JSONArray(schedule.get(i));

                dayScheduleList = new ArrayList<>();

                Log.i("result2", "json : " + daySchedule);

                for(int j = 0; j < daySchedule.length() ; j++){
                    JSONObject timeSchedule = (JSONObject)daySchedule.get(j);

                    Log.i("result3", "json : " + timeSchedule);

                    Map timeScheduleMap = new HashMap();
                    timeScheduleMap.put("name", timeSchedule.get("name"));
                    timeScheduleMap.put("startTime", timeSchedule.get("startTime"));
                    timeScheduleMap.put("endTime", timeSchedule.get("endTime"));
                    timeScheduleMap.put("freeTime", timeSchedule.getInt("freeTime"));

                    if(day < timeSchedule.getInt("day")){
                        day = timeSchedule.getInt("day");
                    }

                    dayScheduleList.add(timeScheduleMap);

                }

                scheduleList.add(dayScheduleList);

            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }


        // JSONArray를 여행 일정으로 보여주기
        for(int i = 0;i < scheduleList.size(); i++){

            txt_schedule.append(Html.fromHtml("<b><font color='#000000'>" + (i + 1) + "일차</font></b><br>"));

            for(int j = 0; j < scheduleList.get(i).size() ; j++){

                String freeTimeChk = "";

                Map timeScheduleMap = (Map) scheduleList.get(i).get(j);
                String name = (String) timeScheduleMap.get("name");
                String startTime = (String) timeScheduleMap.get("startTime");
                String endTime = (String) timeScheduleMap.get("endTime");
                int freeTime = (int) timeScheduleMap.get("freeTime");

                if(freeTime == 1){
                    freeTimeChk = "(자유시간)";
                }

                txt_schedule.append(startTime.substring(0,5) + " ~ " + endTime.substring(0,5) + "\n" + name + freeTimeChk + "\n");
            }

            txt_schedule.append("\n");

        }


        txt_point.setText(memo);
        txt_information.setText(information);
        txt_term.setText( day - 1 + "박 " + day + "일");
        txt_title.setText(title);


        btn_operateTrip.setOnClickListener(click);
        btn_close.setOnClickListener(click);

    }

    // 버튼 클릭 이벤트
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){

                // 여행 운영하기 클릭 시
                case R.id.btn_operateTrip:
                    // 여행 상품 이름을(title) 가지고 TripListActivity 이동
                    Intent i = new Intent(TripDetailsActivity.this, ManagerAddGroupActivity.class);
                    i.putExtra("product", title);
                    startActivity(i);
                    finish();

                    break;

                // 닫기 버튼 클릭 시
                case R.id.btn_close:
                    // 여행 상품 목록 창으로 돌아가기
                    finish();
                    break;
            }
        }
    };
}