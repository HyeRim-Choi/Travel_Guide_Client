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

/* 매니저 그룹 목록 가져오기 */

public class GroupActivity extends AppCompatActivity {

    private static final int FIRST_ACTIVITY_REQUEST_CODE = 0;

    TextView txt_title, txt_member;
    Button btn_notify, btn_board, btn_start, btn_end;

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
        txt_title = findViewById(R.id.txt_title);
        btn_start = findViewById(R.id.btn_start);
        btn_end = findViewById(R.id.btn_end);

        txt_title.setText(title);

        for(int i=0;i<member.size();i++){
            txt_member.append(member.get(i) + "\n");
        }

        btn_start.setOnClickListener(click);
        btn_end.setOnClickListener(click);
    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_start:
                    btn_start.setVisibility(View.INVISIBLE);
                    btn_end.setVisibility(View.VISIBLE);

                    // ************************* ???????????????? ******** 위치 얻는 코드 다시 만들어서 이동하는 액티비티 변경
                    //  가이드 위치 전달
                    // '네' 클릭 시 LocationAccessActivity로 이동
                    Intent intent = new Intent(getApplicationContext(), ManagerLocationSendActivity.class);
                    startActivityForResult(intent, FIRST_ACTIVITY_REQUEST_CODE);
                    break;

                case R.id.btn_end:
                    btn_start.setVisibility(View.VISIBLE);
                    btn_end.setVisibility(View.INVISIBLE);

                    //서비스 종료하기
                    break;
            }
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 결과를 반환하는 액티비티가 FIRST_ACTIVITY_REQUEST_CODE 요청코드로 시작된 경우가 아니거나
        // 결과 데이터가 빈 경우라면, 메소드 수행을 바로 반환
        if (requestCode != FIRST_ACTIVITY_REQUEST_CODE || data == null)
            return;

        //  서버에 아이디, 위치 전송
        JSONObject postDataParam = new JSONObject();

        // node에 전달 할 정보 넣기
        try {
            postDataParam.put("title", title);
            postDataParam.put("latitude", data.getDoubleExtra("latitude", 0));
            postDataParam.put("longitude", data.getDoubleExtra("longitude", 0));
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



    }
}