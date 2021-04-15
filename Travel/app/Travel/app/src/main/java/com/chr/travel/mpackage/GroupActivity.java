package com.chr.travel.mpackage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chr.travel.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;
import service.location.GpsTracker;

/* 매니저 그룹 목록 가져오기 */

public class GroupActivity extends AppCompatActivity {

    TextView txt_member;
    Button btn_notify, btn_board, btn_start, btn_end, btn_title;

    String title;
    ArrayList<String> member;


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

        btn_title.setText(title);

        for(int i=0;i<member.size();i++){
            txt_member.append(member.get(i) + "\n");
        }

        btn_start.setOnClickListener(click);
        btn_end.setOnClickListener(click);
        btn_title.setOnClickListener(click);
    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){

                // title 클릭 시 멤버들의 현재 위치를 Map에 띄우기
                case R.id.btn_title:
                    Intent i = new Intent(GroupActivity.this, MapActivity.class);
                    i.putExtra("title", title);
                    startActivity(i);
                    break;


                // 자유시간 시작 클릭 시
                case R.id.btn_start:
                    btn_start.setVisibility(View.INVISIBLE);
                    btn_end.setVisibility(View.VISIBLE);

                    // 서버에 가이드 위치 전송
                    JSONObject postDataParam = new JSONObject();

                    // node에 전달 할 정보 넣기
                    try {
                        postDataParam.put("title", title);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // node로 정보 전달
                    try {
                        AsyncTaskFactory.getApiPostTask(GroupActivity.this, API_CHOICE.LOCATION_REQ, new AsyncTaskCallBack() {
                            @Override
                            public void onTaskDone(Object... params) {
                                if((Integer)params[0] == 1){
                                    Log.i("GroupActivity", "알림 받음");
                                }
                            }
                        }).execute(postDataParam);
                    }

                    catch (Exception e){
                        e.printStackTrace();
                    }

                    break;

                    // 자유시간 종료 클릭 시
                case R.id.btn_end:
                    btn_start.setVisibility(View.VISIBLE);
                    btn_end.setVisibility(View.INVISIBLE);

                    // 여행객들 위치 받기 종료
                    try {
                        AsyncTaskFactory.getApiBackTask(GroupActivity.this, API_CHOICE.LOCATION_SEND, title , 2, new AsyncTaskCallBack() {
                            @Override
                            public void onTaskDone(Object... params) {
                                if((Integer)params[0] == 1){
                                    Log.i("GroupActivity", "위치 보내기 종료 성공");
                                    finish();
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