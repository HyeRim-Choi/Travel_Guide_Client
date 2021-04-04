package com.sample.locationdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    DrawerLayout drawer_layout;
    LinearLayout drawer;
    Button opendrawer, createDiary;
    TextView txt_home_actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Login 액티비티에서 전달받은 id 불러오기
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras(); // bundle 꺼내기
        String id = bundle.getString("id");

        drawer_layout = findViewById(R.id.drawer_layout);
        drawer = findViewById(R.id.drawer);
        opendrawer = findViewById(R.id.opendrawer);
        createDiary = findViewById(R.id.createDiary);
        txt_home_actionbar = findViewById(R.id.txt_home_actionbar);

        txt_home_actionbar.setText(id +"님의 일상");


        // 버튼들 클릭 시
        opendrawer.setOnClickListener(click);
        createDiary.setOnClickListener(click);


        //손가락 드래그로 메뉴 열고 닫기를 방지(버튼 클릭으로만 서랍을 제어)
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,drawer); // drawer : 지정한 서랍만 수행

        //손가락 드래그로 메뉴 열고 닫기를 가능하게 하기
        //drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, drawer);

        //메뉴가 열리고 닫힘을 감지하는 이벤트 감지자(뒤로 가기로 서랍이 닫힐 수 있도록)
        drawer_layout.setDrawerListener(listener);
    }


    //뒤로 가기로 서랍이 닫힐 수 있도록
    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            //메뉴가 열리는 중일 때
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
            //메뉴가 완전히 열려있을 때
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, drawer);
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
            //메뉴가 완전히 닫혀있을 때
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,drawer);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            //서랍의 현재 상태
        }
    };

    //뒤로 가기로 서랍 닫기
    @Override
    public void onBackPressed() {
        drawer_layout.closeDrawers();
    }



    // 버튼들의 이벤트
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i;

            switch (v.getId()){
                case R.id.opendrawer:
                    drawer_layout.openDrawer(drawer);
                    break;

                case R.id.createDiary:
                    i = new Intent(HomeActivity.this, DiaryCreateActivity.class);
                    startActivity(i);
                    break;
            }
        }
    };

}