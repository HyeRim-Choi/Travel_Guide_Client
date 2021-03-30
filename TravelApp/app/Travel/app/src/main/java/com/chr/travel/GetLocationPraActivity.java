package com.chr.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import callback.AsyncTaskCallBack;
import connect.GetData;

public class GetLocationPraActivity extends AppCompatActivity {

    Button btn_location;

    GetData get_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location_pra);

        btn_location = findViewById(R.id.btn_location);

        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_data = (GetData) new GetData(GetLocationPraActivity.this, 9, "seoul", new AsyncTaskCallBack() {
                    @Override
                    public void onTaskDone(Object... params) {

                    }
                }).execute();
            }
        });
    }
}