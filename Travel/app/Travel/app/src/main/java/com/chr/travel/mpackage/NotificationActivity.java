package com.chr.travel.mpackage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;

import com.chr.travel.R;
import com.chr.travel.login.FindIdActivity;
import com.chr.travel.mpackage.LocationAccessActivity;

/* 알림 클릭 시 들어오는 Activity */

public class  NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_notification);

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

                // ********* ????????? 위치 얻는 코드 변경 후 이동하는 액티비티 변경 *********
                // '네' 클릭 시 LocationAccessActivity로 이동
                Intent intent = new Intent(getApplicationContext(), LocationAccessActivity.class);
                startActivity(intent);
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
                alert.setIcon(R.drawable.img_alert);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#65acf3")));
            }
        });

        alert.show();
    }

}