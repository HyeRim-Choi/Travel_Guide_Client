package com.chr.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;

/* 매니저 그룹 목록 가져오기 */

public class GroupActivity extends AppCompatActivity {

    TextView txt_title, txt_member;
    Button btn_notify, btn_board, btn_location;

    String title;
    ArrayList<String> member;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        // 인텐트 가져오기
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        member = intent.getStringArrayListExtra("members");

        txt_member = findViewById(R.id.txt_memeber);
        txt_title = findViewById(R.id.txt_title);
        btn_location = findViewById(R.id.btn_location);

        txt_title.setText(title);

        for(int i=0;i<member.size();i++){
            txt_member.append(member.get(i) + "\n");
        }

        btn_location.setOnClickListener(click);
    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_location:
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
                                if((Integer)params[1] == 5){
                                    Log.i("GroupActivity", "알림 받음");
                                }
                            }
                        }).execute(postDataParam);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}