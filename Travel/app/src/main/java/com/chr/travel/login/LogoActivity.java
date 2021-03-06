package com.chr.travel.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.chr.travel.R;

/* Logo 페이지 */

public class LogoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitylogin_logo);

        Handler handler = new Handler();

        // 2초 후 Logo 페이지에서 Login 페이지로 이동
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(LogoActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, 2000);
    }
}