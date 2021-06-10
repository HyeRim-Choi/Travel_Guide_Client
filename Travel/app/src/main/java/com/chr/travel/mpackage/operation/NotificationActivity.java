package com.chr.travel.mpackage.operation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import com.chr.travel.R;

import org.json.JSONObject;

import api.background.BackLocationRequest;
import api.callback.AsyncTaskCallBack;
import vo.LoginVO;

/* 알림 클릭 시 들어오는 Activity */

public class  NotificationActivity extends AppCompatActivity {

    LoginVO vo;

    // 자유시간 시작인지 종료인지 알기위한 chk
    int freeTimeChk;

    // 기록하는 인터페이스 준비
    SharedPreferences chkPref;

    BackLocationRequest backLocationRequestStart;
    BackLocationRequest backLocationRequestEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_notification);

        vo = LoginVO.getInstance();

        //저장을 위한 SharedPreferences 객체를 생성
        chkPref = getSharedPreferences("CHK",MODE_PRIVATE);

        // 알림이 꼬였을 때
        /*SharedPreferences.Editor edit = chkPref.edit();
        edit.clear();
        edit.commit();*/

        //로드
        freeTimeChk = chkPref.getInt("freeTimeChk", 0);

        Log.i("NotificationActivity", "freeTimeBtnChk : " + freeTimeChk);

        // Alert창 띄우기
        makeDialog(freeTimeChk);
    }


    /* Alert창 */
    private void makeDialog(int freeTimeChk) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        // 위치 전송 요청 알림
       if(freeTimeChk == 0){
           dialog.setTitle(Html.fromHtml("<b><font color='#FFFFFF'>위치 전송 메세지</font></b>"));
           dialog.setMessage(Html.fromHtml("<font color='#FFFFFF'>매니저에게 위치를 전송하시겠습니까?</font>"));

           freeTimeChk++;

           //앱이 일시정지 되었을 때 현재 cnt값을 저장
           SharedPreferences.Editor edit = chkPref.edit();
           //Map 구조
           edit.putInt("freeTimeChk",freeTimeChk);
           edit.commit(); // commit을 하지 않으면 값이 저장되지 않는다

           dialog.setNegativeButton("네", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {

                   backLocationRequestStart = (BackLocationRequest) new BackLocationRequest(NotificationActivity.this, vo.getUserId(), true, new AsyncTaskCallBack() {
                       @Override
                       public void onTaskDone(Object... params) {
                           Log.i("NotificationStart : ", "위치 전송 종료");
                       }
                   }).execute();


                   dialog.cancel();
                   finish();

               }
           });
       }

       // 위치 전송 종료 알림
       else{
           dialog.setTitle(Html.fromHtml("<b><font color='#FFFFFF'>자유시간 종료 메세지</font></b>"));
           dialog.setMessage(Html.fromHtml("<font color='#FFFFFF'>매니저에게 위치 전송을 멈추겠습니까?</font>"));

           SharedPreferences.Editor edit = chkPref.edit();
           edit.clear();
           edit.commit();

           dialog.setNegativeButton("네", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {

                   backLocationRequestEnd = (BackLocationRequest) new BackLocationRequest(NotificationActivity.this, vo.getUserId(), false, new AsyncTaskCallBack() {
                       @Override
                       public void onTaskDone(Object... params) {
                           Log.i("NotificationEnd : ", "위치 전송 종료");
                       }
                   }).execute();


                   dialog.cancel();
                   finish();

               }
           });
       }

        dialog.setNeutralButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // 알림이 꼬였을 시
                /*SharedPreferences.Editor edit = chkPref.edit();
                edit.clear();
                edit.commit();*/

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