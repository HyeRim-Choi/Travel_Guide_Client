package com.chr.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("알림 메세지 : " + "messageBody");
        dialog.setMessage("messageTitle");
        dialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "알림 선택", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        dialog.show();
    }
}