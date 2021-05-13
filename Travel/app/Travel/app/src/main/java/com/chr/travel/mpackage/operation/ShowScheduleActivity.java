package com.chr.travel.mpackage.operation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chr.travel.R;
import com.chr.travel.tpackage.FreeTimeListActivity;
import com.chr.travel.tpackage.VisualizationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;

/* 그룹 여행(상품) 일정을 보여주는 액티비티 */

public class ShowScheduleActivity extends AppCompatActivity {

    TextView txt_guide, txt_point, txt_term, txt_information, txt_schedule;
    Button btn_close, btn_freeTime;

    String information, memo, startDate, endDate, title, guideName, guideId;
    ArrayList<String> schedule;

    int day;

    ArrayList<Map> dayScheduleList;
    ArrayList<ArrayList> scheduleList;
    ArrayList<String> freeTimePlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_show_schedule);

        txt_guide = findViewById(R.id.txt_guide);
        txt_point = findViewById(R.id.txt_point);
        txt_term = findViewById(R.id.txt_term);
        txt_information = findViewById(R.id.txt_information);
        txt_schedule = findViewById(R.id.txt_schedule);
        btn_close = findViewById(R.id.btn_close);
        btn_freeTime = findViewById(R.id.btn_freeTime);

        schedule = new ArrayList<>();
        scheduleList = new ArrayList<>();
        freeTimePlace = new ArrayList<>();

        // 인텐트 가져오기
        Intent intent = getIntent();
        // title
        title = intent.getStringExtra("title");
        // information
        information = intent.getStringExtra("information");
        // memo
        memo = intent.getStringExtra("memo");
        // schedule
        schedule = intent.getStringArrayListExtra("schedule");
        // guideName
        guideName = intent.getStringExtra("guideName");
        // guideId
        guideId = intent.getStringExtra("guideId");


        // JSONArray를 여행 일정으로 보여줄 수 있는 상태로 만들기
        for(int i = 0 ; i < schedule.size() ; i++) {
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
                    freeTimePlace.add(name);
                }

                txt_schedule.append(startTime.substring(0,5) + " ~ " + endTime.substring(0,5) + "\n" + name + freeTimeChk + "\n");
            }

            txt_schedule.append("\n");

        }

        txt_guide.setText(guideName + "( id : " + guideId + ")");
        txt_point.setText(memo);
        txt_information.setText(information);
        txt_term.setText( day - 1 + "박 " + day + "일");

        btn_freeTime.setOnClickListener(click);
        btn_close.setOnClickListener(click);

        // 여행 기간 날짜 띄우기
        try {
            AsyncTaskFactory.getApiGetTask(ShowScheduleActivity.this, API_CHOICE.GROUP_TRIP_DATE, title, new AsyncTaskCallBack() {
                @Override
                public void onTaskDone(Object... params) {
                    if((Integer)params[0] == 1){
                        startDate = (String) params[1];
                        endDate = (String) params[2];
                        Log.i("ShowSchedule", " " + startDate + " " + endDate);
                        txt_term.append("\n" + "(" + startDate + " ~ " + endDate + ")");
                    }
                }
            }).execute();
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }

    // 버튼 클릭 이벤트
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){

                // 자유시간 시각화 버튼 클릭 시
                case R.id.btn_freeTime:
                    Intent i = new Intent(ShowScheduleActivity.this, FreeTimeListActivity.class);
                    i.putExtra("freeTimePlace", freeTimePlace);
                    i.putExtra("groupTitle", title);
                    startActivity(i);
                    break;

                // 닫기 버튼 클릭 시
                case R.id.btn_close:

                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();

                    break;

            }
        }
    };
}