package com.chr.travel.mpackage.operation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chr.travel.R;
import com.chr.travel.mpackage.RegisterRouteActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;
import api.post.PostLocationReq;

/* 매니저 그룹 목록 가져오기 */

public class GroupActivity extends AppCompatActivity {

    TextView txt_member;
    Button btn_notify, btn_board, btn_start, btn_end, btn_title, btn_registerRoute, btn_showSchedule;

    String title, startDate, endDate;;
    ArrayList<String> member;

    JSONObject postDataParam;

    // 알림 보내기
    PostLocationReq postLocationReq;

    private static final int REGISTER_ROUTE_REQUEST_CODE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_group);


        // 인텐트 가져오기
        Intent intent = getIntent();
        // title
        title = intent.getStringExtra("title");
        // member
        member = intent.getStringArrayListExtra("members");


        txt_member = findViewById(R.id.txt_memeber);
        btn_title = findViewById(R.id.btn_title);
        btn_start = findViewById(R.id.btn_start);
        btn_end = findViewById(R.id.btn_end);
        btn_registerRoute = findViewById(R.id.btn_registerRoute);
        btn_showSchedule = findViewById(R.id.btn_showSchedule);

        btn_title.setText(title);


        for(int i=0;i<member.size();i++){
            txt_member.append(member.get(i) + "\n");
        }


        btn_start.setOnClickListener(click);
        btn_end.setOnClickListener(click);
        btn_title.setOnClickListener(click);
        btn_registerRoute.setOnClickListener(click);
        btn_showSchedule.setOnClickListener(click);
    }



    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){

                // title 클릭 시 멤버들의 현재 위치를 Map에 띄우기
                case R.id.btn_title:
                    Intent i = new Intent(GroupActivity.this, MapTravelerLocationActivity.class);
                    i.putExtra("title", title);
                    startActivity(i);
                    break;


                // 자유시간 시작 클릭 시
                case R.id.btn_start:
                    btn_start.setVisibility(View.INVISIBLE);
                    btn_end.setVisibility(View.VISIBLE);

                    postDataParam = new JSONObject();

                    // node에 전달 할 정보 넣기
                    try {
                        postDataParam.put("title", title);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    // node로 정보 전달
                    postLocationReq = (PostLocationReq) new PostLocationReq(GroupActivity.this, new AsyncTaskCallBack() {
                        @Override
                        public void onTaskDone(Object... params) {
                            Log.i("GroupActivity", "자유시간 시작");
                        }
                    }).execute(postDataParam);


                    break;

                    // 자유시간 종료 클릭 시
                case R.id.btn_end:
                    btn_start.setVisibility(View.VISIBLE);
                    btn_end.setVisibility(View.INVISIBLE);

                    postDataParam = new JSONObject();

                    // node에 전달 할 정보 넣기
                    try {
                        postDataParam.put("title", title);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // node로 정보 전달
                    postLocationReq = (PostLocationReq) new PostLocationReq(GroupActivity.this, new AsyncTaskCallBack() {
                        @Override
                        public void onTaskDone(Object... params) {
                            Log.i("GroupActivity", "자유시간 종료");


                            try {
                                AsyncTaskFactory.getApiGetTask(GroupActivity.this, API_CHOICE.MEMBER_LOCATION_SEND_DONE, title, null).execute();
                            }

                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }).execute(postDataParam);

                    break;


                // 일정 등록 클릭 시 *** delete ***
               case R.id.btn_registerRoute:

                   try {
                       AsyncTaskFactory.getApiGetTask(GroupActivity.this, API_CHOICE.GROUP_TRIP_DATE, title, new AsyncTaskCallBack() {
                           @Override
                           public void onTaskDone(Object... params) {
                                if((Integer)params[0] == 1){
                                    startDate = (String) params[1];
                                    endDate = (String) params[2];

                                    Intent i = new Intent(GroupActivity.this, RegisterRouteActivity.class);
                                    i.putExtra("startDate", startDate);
                                    i.putExtra("endDate", endDate);
                                    i.putExtra("title", title);
                                    startActivityForResult(i, REGISTER_ROUTE_REQUEST_CODE);
                                }
                           }
                       }).execute();
                   }

                   catch (Exception e){
                       e.printStackTrace();
                   }

                    break;


                // 일정 보기 클릭 시
                case R.id.btn_showSchedule:
                    // 일정 보여주는 창으로 이동
                    // ** 출발 날짜, 도착 날짜 받아서 일정 쭈루룩 보여주기
                    break;
            }
        }
    };


    // 일정 등록 결과 반환
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != REGISTER_ROUTE_REQUEST_CODE){
            return;
        }

        //txt_route에 하나라도 있으면 일정 수정으로 변경

        btn_registerRoute.setText("일정 수정");

    }




}