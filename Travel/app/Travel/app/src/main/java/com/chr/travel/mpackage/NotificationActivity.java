package com.chr.travel.mpackage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import com.chr.travel.R;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;
import vo.LoginVO;

/* 알림 클릭 시 들어오는 Activity */

public class  NotificationActivity extends AppCompatActivity {

    LoginVO vo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_notification);

        vo = LoginVO.getInstance();

        // Alert창 띄우기
        makeDialog();
    }

    /* Alert창 */
    private void makeDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(Html.fromHtml("<b><font color='#FFFFFF'>위치 전송 메세지</font></b>"));
        dialog.setMessage(Html.fromHtml("<font color='#FFFFFF'>매니저에게 위치를 전송하시겠습니까?</font>"));

        dialog.setNegativeButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // 백그라운드로 여행객들 위치 가이드에게 전송하기
                try {
                    AsyncTaskFactory.getApiBackTask(NotificationActivity.this, API_CHOICE.LOCATION_SEND, vo.getUserId(), 1, new AsyncTaskCallBack() {
                        @Override
                        public void onTaskDone(Object... params) {
                            if((Integer)params[0] == 1){
                                Log.i("NotificationActivity", "위치 보내기 종료 성공");
                            }
                        }
                    }).execute();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                dialog.cancel();
                finish();

            }
        });

        dialog.setNeutralButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });

        AlertDialog alert = dialog.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
                alert.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.WHITE);
                alert.setIcon(R.drawable.img_alert);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#65acf3")));
            }
        });

        alert.show();
    }

}