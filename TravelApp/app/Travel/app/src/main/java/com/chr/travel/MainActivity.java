package com.chr.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import fcm.MyFirebaseMessagingService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}