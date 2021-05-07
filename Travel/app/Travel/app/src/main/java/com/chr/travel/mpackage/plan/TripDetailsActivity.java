package com.chr.travel.mpackage.plan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.chr.travel.R;

/* 해당 여행 상품의 세부정보를 보여주는 액티비티 */

public class TripDetailsActivity extends AppCompatActivity {

    Button btn_operateTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_trip_details);

        btn_operateTrip = findViewById(R.id.btn_operateTrip);

        // **** 서버와 통신해서 해당 여행 상품의 일정과 세부정보 가져오기*****


    }
}