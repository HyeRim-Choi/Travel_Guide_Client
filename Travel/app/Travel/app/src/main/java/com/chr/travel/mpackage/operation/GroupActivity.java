package com.chr.travel.mpackage.operation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chr.travel.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;
import api.post.PostLocationReq;

/* 매니저 그룹 목록 가져오기 */

public class GroupActivity extends AppCompatActivity {

    private static final int GROUP_ACTIVITY_REQUEST_CODE = 5;

    TextView txt_member;
    Button btn_notify, btn_board, btn_start, btn_end, btn_title, btn_showSchedule;

    String title, product;
    ArrayList<String> member;

    JSONObject postDataParam;

    // 알림 보내기
    PostLocationReq postLocationReq;

    // 자유시간 시작, 종료버튼을 유지하기 위한 저장소
    SharedPreferences pref;
    int freeTimeBtnChk;



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
        //product
        product = intent.getStringExtra("product");

        txt_member = findViewById(R.id.txt_memeber);
        btn_title = findViewById(R.id.btn_title);
        btn_start = findViewById(R.id.btn_start);
        btn_end = findViewById(R.id.btn_end);
        btn_showSchedule = findViewById(R.id.btn_showSchedule);

        btn_title.setText(title);


        for(int i=0;i<member.size();i++){
            txt_member.append(member.get(i) + "\n");
        }


        btn_start.setOnClickListener(click);
        btn_end.setOnClickListener(click);
        btn_title.setOnClickListener(click);
        btn_showSchedule.setOnClickListener(click);
    }

    @Override
    protected void onResume() {
        super.onResume();

        pref = getSharedPreferences("FREETIME",MODE_PRIVATE);

        freeTimeBtnChk = pref.getInt("freeTimeBtnChk", 1);

        Log.i("GroupActivity(onResume)", "freeTimeBtnChk : " + freeTimeBtnChk);

        freeTimeBtn(freeTimeBtnChk);
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("freeTimeBtnChk",freeTimeBtnChk);
        edit.commit();

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
                    freeTimeBtnChk = 0;
                    freeTimeBtn(freeTimeBtnChk);

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
                    freeTimeBtnChk = 1;
                    freeTimeBtn(freeTimeBtnChk);

                    SharedPreferences.Editor edit = pref.edit();
                    edit.clear();
                    edit.commit();

                    postDataParam = new JSONObject();

                    // node에 전달 할 정보 넣기
                    try {
                        postDataParam.put("title", title);
                        postDataParam.put("tourPlace", product);
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


                // 여행 정보 보기 클릭 시
                case R.id.btn_showSchedule:
                    // 일정 보여주는 창으로 이동


                    try {
                        AsyncTaskFactory.getApiGetTask(GroupActivity.this, API_CHOICE.GROUP_REGISTERED_ROUTE_DETAILS, title, new AsyncTaskCallBack() {
                            @Override
                            public void onTaskDone(Object... params) {
                                if((Integer)params[0] == 1){
                                    Intent i = new Intent(GroupActivity.this, ShowScheduleActivity.class);
                                    // ShowScheduleActivity에 그룹 이름, 여행일정, 세부정보를 보낸다
                                    i.putExtra("title", title);
                                    i.putExtra("information", (String) params[1]);
                                    i.putExtra("memo", (String) params[2]);
                                    i.putExtra("schedule", (Serializable) params[3]);
                                    i.putExtra("guideName",(String) params[4]);
                                    i.putExtra("guideId",(String) params[5]);
                                    startActivityForResult(i, GROUP_ACTIVITY_REQUEST_CODE);
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


    public void freeTimeBtn(int chk){
        // 자유시간 시작 버튼 클릭 시
        if(chk == 0){
            btn_start.setVisibility(View.INVISIBLE);
            btn_end.setVisibility(View.VISIBLE);
        }

        // 자유시간 종료 버튼 클릭 시
        else{
            btn_start.setVisibility(View.VISIBLE);
            btn_end.setVisibility(View.INVISIBLE);
        }
    }

    // NotificationActivity과 연결
    public int getFreeTimeBtnChk(){
        return freeTimeBtnChk;
    }



}