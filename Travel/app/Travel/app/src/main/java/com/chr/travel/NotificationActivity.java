package com.chr.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.chr.travel.fragment.MapFragment;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.background.BackLocationRequest;
import api.callback.AsyncTaskCallBack;
import service.LocationService;
import vo.LoginVO;

public class  NotificationActivity extends AppCompatActivity {


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