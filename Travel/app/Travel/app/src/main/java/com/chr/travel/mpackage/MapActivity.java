package com.chr.travel.mpackage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chr.travel.R;

/* 멤버들의 현재 위치를 가져오는 Map Activity */

public class MapActivity extends AppCompatActivity {

    Button btn_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_map);

        btn_refresh = findViewById(R.id.btn_refresh);


        /*FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frag_map, new MapFragment());
        fragmentTransaction.commit();*/



        btn_refresh.setOnClickListener(click);
    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_refresh:
                    // 새로고침 버튼 클릭 시
                    break;
            }
       }
    };
}