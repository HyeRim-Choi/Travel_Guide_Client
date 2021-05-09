package com.chr.travel.mpackage.plan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chr.travel.R;

/* 해당 여행 상품의 세부정보를 보여주는 액티비티 */

public class TripDetailsActivity extends AppCompatActivity {

    TextView txt_title;
    Button btn_operateTrip, btn_close;

    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_trip_details);

        txt_title = findViewById(R.id.txt_title);
        btn_operateTrip = findViewById(R.id.btn_operateTrip);
        btn_close = findViewById(R.id.btn_close);

        // 인텐트 가져오기
        Intent intent = getIntent();
        // title
        title = intent.getStringExtra("title");

        //member = intent.getStringArrayListExtra("members");

        txt_title.setText(title);



        btn_operateTrip.setOnClickListener(click);
        btn_close.setOnClickListener(click);

    }

    // 버튼 클릭 이벤트
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){

                // 여행 운영하기 클릭 시
                case R.id.btn_operateTrip:
                    // title를 가지고 GroupAddActivity로 이동

                    break;

                // 닫기 버튼 클릭 시
                case R.id.btn_close:
                    // TripActivity로 돌아가기(*** TripActivity startForResult를 이용해야함)
                    break;
            }
        }
    };
}