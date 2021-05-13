package com.chr.travel.mpackage.plan;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.chr.travel.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.AdapterRegisterTripSchedule;
import adapter.RegisterTripSchedule;
import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.post.PostManagerRegisterRoute;

/* 여행 상품을 등록하는 액티비티 */

public class RegisterTripScheduleActivity extends AppCompatActivity implements AdapterRegisterTripSchedule.OnButtonSelectedListener{
    private static final int REGISTER_SCHEDULE_REQUEST_CODE = 1;

    EditText et_title, et_night, et_day, et_introduce, et_information;
    Button btn_confirm, btn_register;

    ListView trip_schedule;
    RegisterTripSchedule registerTripSchedule;
    ArrayList<RegisterTripSchedule> data;
    AdapterRegisterTripSchedule adapterRegisterTripSchedule;

    int night, day;
    String title, introduce, information;
    ArrayList daySchedule;
    ArrayList<String> list;

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_register_trip_schedule);

        daySchedule = new ArrayList();
    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.i("daySchedule", "Re");

        et_title = findViewById(R.id.et_title);
        et_night = findViewById(R.id.et_night);
        et_day = findViewById(R.id.et_day);
        et_introduce = findViewById(R.id.et_introduce);
        et_information = findViewById(R.id.et_information);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_register = findViewById(R.id.btn_register);
        trip_schedule = findViewById(R.id.trip_schedule);


        btn_confirm.setOnClickListener(click);
        btn_register.setOnClickListener(click);

        makeListView(day);

    }


    // 버튼 클릭 이벤트
    View.OnClickListener click = new View.OnClickListener() {
        @SuppressLint("LongLogTag")
        @Override
        public void onClick(View v) {
          switch (v.getId()){

              // 확인 버튼 클릭 시
              case R.id.btn_confirm:
                  String str1 = et_night.getText().toString();
                  String str2 = et_day.getText().toString();

                  if(str1.isEmpty() || str1 == null){
                      Toast.makeText(RegisterTripScheduleActivity.this, "박 수를 입력해 주세요", Toast.LENGTH_SHORT).show();
                      return;
                  }

                  if(str2.isEmpty() || str2 == null){
                      Toast.makeText(RegisterTripScheduleActivity.this, "일 수를 입력해 주세요", Toast.LENGTH_SHORT).show();
                      return;
                  }

                  night = Integer.parseInt(str1);
                  day = Integer.parseInt(str2);

                  // 1일차, 2일차 목록 띄우기
                  makeListView(day);

                  // 확인 버튼 안보이게 하기
                  //btn_confirm.setVisibility(View.INVISIBLE);

                  break;


              // 등록 버튼 클릭 시
              case R.id.btn_register:

                  // 빈 칸을 다 채웠다면
                  if(BlankCheck()){

                      // 저장소 비우기
                      SharedPreferences pref = getSharedPreferences("SCHEDULE", MODE_PRIVATE);
                      SharedPreferences.Editor editor = pref.edit();
                      editor.clear();
                      editor.commit();


                      JSONObject postDataParam = new JSONObject();

                      // 보낼 데이터들 저장
                      try {
                          // node에 전달 할 정보 넣기
                          postDataParam.put("title", title);
                          postDataParam.put("schedule", daySchedule);
                          postDataParam.put("introduce", introduce);
                          postDataParam.put("memo", information);

                      }
                      catch (JSONException e) {
                          Log.e("RegisterTripScheduleActivity", "JSONEXception");
                      }

                      // 서버 통신
                      try {
                          AsyncTaskFactory.getApiPostTask(RegisterTripScheduleActivity.this, API_CHOICE.MANAGER_REGISTER_ROUTE, null).execute(postDataParam);
                      }

                      catch (Exception e){
                          e.printStackTrace();
                      }
                  }
                  break;
          }
        }
    };


    // 확인 버튼 클릭 시 일 수 계산하여 1일차, 2일차 목록 띄우기
    public void makeListView(int day){

        String schedule = "";

        Log.i("dayScheduleList", String.valueOf(list));
        Log.i("makeList", "come in");

        data = new ArrayList<>();

        for(int i = 1; i <= day ; i++){
            registerTripSchedule = new RegisterTripSchedule();

            registerTripSchedule.setDay(i + "일차");

            // 해당하는 일차에 일정을 등록을 했다면 일정 등록 버튼을 수정 버튼으로 바꾸기 위해 저장소 불러오기
            try {
                pref = getSharedPreferences("SCHEDULE", MODE_PRIVATE);
                schedule = pref.getString("day" + i, "");
            }
            catch (Exception e){
                e.printStackTrace();
            }


            // 일정 등록이 되었으면 버튼이 '일정 수정'로 바꿔지도록
            if(schedule == null || schedule.isEmpty()){
                registerTripSchedule.setBtn("일정 등록");
            }
            else{
                registerTripSchedule.setBtn("일정 수정");
            }

            data.add(registerTripSchedule);
        }

        adapterRegisterTripSchedule = new AdapterRegisterTripSchedule(this, R.layout.adapter_package_trip_schedule, data);

        trip_schedule.setAdapter(adapterRegisterTripSchedule);

        // ListView가 ScrollView안에 속하게 되면 이중 스크롤 현상이 발생되어 높이를 강제로 할당해야한다
        listViewHeightSet(adapterRegisterTripSchedule, trip_schedule);
    }

    // ListView 높이 강제 할당
    private void listViewHeightSet(Adapter listAdapter, ListView listView){
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    // 일정 등록 버튼 클릭 시
    @Override
    public void onButtonSelected(int i) {

        String day = ((RegisterTripSchedule)adapterRegisterTripSchedule.getItem(i)).getDay();

        // 시간, 장소 등록하는 액티비티로 이동
        Intent intent = new Intent(RegisterTripScheduleActivity.this, RegisterTripDetailScheduleActivity.class);
        intent.putExtra("day", day);
        RegisterTripScheduleActivity.this.startActivityForResult(intent, REGISTER_SCHEDULE_REQUEST_CODE);
    }


    // 여행 등록 결과
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        list = new ArrayList<>();

        if (requestCode != REGISTER_SCHEDULE_REQUEST_CODE || data == null){
            return;
        }

        list = (ArrayList<String>) data.getSerializableExtra("daySchedule");
        Log.i("dayScheduleList", String.valueOf(list));

        // ** 나중 수정사항) list를 JSON형식으로 바꿔서 json.put("~", list)를 다시 string으로 바꿔서 array에 저장해서 보낸다

        daySchedule.add(list);

        Log.i("dayScheduleMap", String.valueOf(daySchedule));

    }

    // 정보 다 입력했는지 확인하는 함수
   public boolean BlankCheck(){

        title = et_title.getText().toString();
        introduce = et_introduce.getText().toString();
        information = et_information.getText().toString();

        if(title.isEmpty() || title == null){
            Toast.makeText(RegisterTripScheduleActivity.this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(daySchedule == null){
            Toast.makeText(RegisterTripScheduleActivity.this, "일정을 등록해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(introduce.isEmpty() || introduce == null){
            Toast.makeText(RegisterTripScheduleActivity.this, "소개 글을 작성해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(information.isEmpty() || information == null){
            Toast.makeText(RegisterTripScheduleActivity.this, "메모를 작성해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    };
}