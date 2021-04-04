package com.chr.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;
import vo.LoginVO;

public class  NotificationActivity extends AppCompatActivity {

    Gson gson;

    LoginVO vo;

    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("위치 전송 메세지");
        dialog.setMessage("매니저에게 위치를 전송하시겠습니까?");

        dialog.setNegativeButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // '네' 클릭 시 LocationAccessActivity로 이동
                Intent intent = new Intent(getApplicationContext(), LocationAccessActivity.class);
                startActivityForResult(intent, 0);

                onSearchData();

                //  서버에 아이디, 위치 전송
                JSONObject postDataParam = new JSONObject();

                try {
                    postDataParam.put("userId", vo.getUserId());
                    postDataParam.put("latitude", latitude);
                    postDataParam.put("longitude", longitude);
                    // 날짜 시간 값 전달
                    postDataParam.put("date", longitude);
                    postDataParam.put("time", longitude);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    AsyncTaskFactory.getApiPostTask(NotificationActivity.this, API_CHOICE.LOCATION_SEND, new AsyncTaskCallBack() {
                        @Override
                        public void onTaskDone(Object... params) {
                            if((Integer)params[0] == 1){
                                // 지도에 위치 띄우기

                            }
                        }
                    }).execute(postDataParam);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                finish();
            }
        });

        dialog.setNeutralButton("아니요", null);

        dialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 결과를 반환하는 액티비티가 0 요청코드로 시작된 경우가 아니거나
        // 결과 데이터가 빈 경우라면, 메소드 수행을 바로 반환함.
        if (requestCode != 0 || data == null)
            return;

        latitude = data.getDoubleExtra("latitude", 0);
        longitude = data.getDoubleExtra("longitude", 0);

    }

    // 저장한 user 정보를 불러오는 함수
    public void onSearchData(){
        gson = new Gson();

        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        String strUser = sp.getString("vo","");
        Log.i("test","loginUserSearch : " + strUser);

        vo = gson.fromJson(strUser, LoginVO.class);
    }
}