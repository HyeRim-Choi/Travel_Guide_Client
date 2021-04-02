package com.chr.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
               // '네' 클릭 시 서버에 아이디, 위치 전송
                JSONObject postDataParam = new JSONObject();

                try {
                    postDataParam.put("userId", vo.getUserId());
                    postDataParam.put("latitude", "");
                    postDataParam.put("longitude", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    AsyncTaskFactory.getApiPostTask(NotificationActivity.this, API_CHOICE.LOCATION_SEND, new AsyncTaskCallBack() {
                        @Override
                        public void onTaskDone(Object... params) {

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

    // 저장한 user 정보를 불러오는 함수
    public void onSearchData(){

        gson = new Gson();

        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        String strUser = sp.getString("vo","");
        Log.i("test","loginUserSearch : " + strUser);

        vo = gson.fromJson(strUser, LoginVO.class);
    }
}