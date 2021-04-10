package com.chr.travel.mpackage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.chr.travel.R;
import com.chr.travel.mpackage.LocationAccessActivity;

public class  NotificationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_notification);



        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("위치 전송 메세지");
        dialog.setMessage("매니저에게 위치를 전송하시겠습니까?");

        dialog.setNegativeButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // '네' 클릭 시 LocationAccessActivity로 이동
                Intent intent = new Intent(getApplicationContext(), LocationAccessActivity.class);
                startActivity(intent);
                finish();

            }
        });

        dialog.setNeutralButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        dialog.show();
    }

}