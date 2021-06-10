package com.chr.travel.mpackage.plan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chr.travel.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* 일정을 등록하는 창*/

public class RegisterTripDetailScheduleActivity extends AppCompatActivity{
    private static final int REGISTER_TIME_PLACE_REQUEST_CODE = 2;

    TextView txt_day, txt_schedule;
    Button btn_add, btn_save;

    String day, start, end, place, schedule;
    boolean freeTimeChk;
    ArrayList<String> daySchedule;
    JSONObject postDataParam;

    // 일정 등록을 하고 수정 버튼을 눌렀을 시 여행 일정을 불러오기 위해 저장하는 곳
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_register_trip_detail_schedule);

        Intent intent = getIntent();
        day = intent.getStringExtra("day");

        Log.i("dayScheduleDetailsDay", day);

        txt_day = findViewById(R.id.txt_day);
        txt_schedule = findViewById(R.id.txt_schedule);
        btn_add = findViewById(R.id.btn_add);
        btn_save = findViewById(R.id.btn_save);

        txt_day.setText(day);

        pref = getSharedPreferences("SCHEDULE", MODE_PRIVATE);

        // 수정 버튼을 클릭하고 들어오면 등록했던 일정이 보여지도록 세팅팅
        schedule = pref.getString("day" + String.valueOf(day.charAt(0)), "");
        txt_schedule.setText(schedule);

        daySchedule = new ArrayList();

        btn_add.setOnClickListener(click);
        btn_save.setOnClickListener(click);
    }




    // 버튼 클릭 이벤트
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){

                // 추가 버튼 클릭 시
                case R.id.btn_add:
                    Intent intent = new Intent(RegisterTripDetailScheduleActivity.this, RegisterTimePlaceActivity.class);
                    startActivityForResult(intent, REGISTER_TIME_PLACE_REQUEST_CODE);
                    break;

                // 저장 버튼 클릭 시
                case R.id.btn_save:
                    // 저장소에 일정 담기
                    schedule = txt_schedule.getText().toString();
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("day" + String.valueOf(day.charAt(0)), schedule);
                    edit.commit();

                    // 일정 시작시간, 일정 종료시간, 장소, 자유시간 체크 들고 이동
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("daySchedule", daySchedule);
                    setResult(RESULT_OK, resultIntent);
                    finish();

                    Toast.makeText(RegisterTripDetailScheduleActivity.this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };


    // 여행 일정 추가 결과
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        postDataParam = new JSONObject();

        String freeTime = "";


        if (requestCode != REGISTER_TIME_PLACE_REQUEST_CODE || data == null){
            return;
        }

        start = data.getStringExtra("start");
        end = data.getStringExtra("end");
        place = data.getStringExtra("place");
        freeTimeChk = data.getBooleanExtra("freeTimeChk", false);

       try {
            postDataParam.put("startTime", start);
            postDataParam.put("endTime", end);
            postDataParam.put("name", place);
            postDataParam.put("freeTimeChk", freeTimeChk);
            postDataParam.put("day", String.valueOf(day.charAt(0)));
        }

       catch (JSONException e) {
            e.printStackTrace();
        }

        daySchedule.add(postDataParam.toString());

        if(freeTimeChk){
            freeTime = "(자유시간)";
        }

        Log.i("dayScheduleDetails", String.valueOf(daySchedule));

        txt_schedule.append(start + " ~ " + end + "\n" + place + freeTime + "\n\n");

    }

}