package com.chr.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
                Toast.makeText(getApplicationContext(), "ㅇ클릭",Toast.LENGTH_SHORT).show();
                get_data = (GetData) new GetData(GetLocationPraActivity.this, 11, "seoul", new AsyncTaskCallBack() {
                    @Override
                    public void onTaskDone(Object... params) {

                    }
                }).execute();
            }
        });
    }
}