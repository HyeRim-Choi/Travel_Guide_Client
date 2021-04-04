package com.chr.travel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.gson.Gson;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;
import vo.LoginVO;

/* Home */

public class HomeActivity extends AppCompatActivity {

    DrawerLayout drawer_layout;
    LinearLayout drawer;
    Button opendrawer, btn_package, btn_free, btn_area, btn_logout;
    TextView txt_home_actionbar, txt_id;

    // login한 user 정보
    LoginVO vo;

    // login한 user 정보 불러오기
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // user 정보 불러오기
        onSearchData();

        // login 하지 않았다면
        if(vo == null){
            Toast.makeText(com.chr.travel.HomeActivity.this,"로그인 후 이용해주세요", Toast.LENGTH_LONG).show();
            Intent i = new Intent(com.chr.travel.HomeActivity.this, com.chr.travel.LoginActivity.class);
            startActivity(i);
            finish();
        }

        drawer_layout = findViewById(R.id.drawer_layout);
        drawer = findViewById(R.id.drawer);
        opendrawer = findViewById(R.id.opendrawer);
        txt_home_actionbar = findViewById(R.id.txt_home_actionbar);
        txt_id = findViewById(R.id.txt_id);
        btn_package = findViewById(R.id.btn_package);
        btn_free = findViewById(R.id.btn_free);
        btn_area = findViewById(R.id.btn_area);
        btn_logout = findViewById(R.id.btn_logout);


        txt_home_actionbar.setText(vo.getUserId() + "님의 일상");
        txt_id.setText(vo.getUserId() + "님 반가워요");


        // 버튼들 클릭 시
        opendrawer.setOnClickListener(click);
        btn_package.setOnClickListener(click);
        btn_area.setOnClickListener(click);
        btn_logout.setOnClickListener(click);


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
                // 서랍 열기를 눌렀을 때
                case R.id.opendrawer:
                    // 서랍 열림
                    drawer_layout.openDrawer(drawer);
                    break;

                // 패키지 버튼을 눌렀을 때
                case R.id.btn_package:
                    // login 한 user 권한이 매니저인 경우
                    if(vo.getRole().equals("manager")){
                        // 매니저 액티비티로 이동
                        i = new Intent(com.chr.travel.HomeActivity.this, com.chr.travel.PackageManagerActivity.class);
                        startActivity(i);
                    }
                    break;

                // 로그아웃 버튼을 눌렀을 때
                case R.id.btn_logout:
                    try {
                        AsyncTaskFactory.getApiGetTask(HomeActivity.this, API_CHOICE.LOGOUT, vo.getUserId(), new AsyncTaskCallBack() {
                            @Override
                            public void onTaskDone(Object... params) {
                                if((Integer)params[1] == 2){
                                    Toast.makeText(HomeActivity.this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();
                                    vo = null;
                                    Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }).execute();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;

                case R.id.btn_area:
                    // login 한 user 권한이 매니저인 경우
                    if(vo.getRole().equals("manager")){
                        // 매니저 액티비티로 이동
                        i = new Intent(com.chr.travel.HomeActivity.this, GetApiHashKeyActivity.class);
                        startActivity(i);
                    }
                    break;
            }
        }
    };

    // 저장한 user 정보를 불러오는 함수
    public void onSearchData(){

        gson = new Gson();

        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        String strUser = sp.getString("vo","");
        Log.i("test","loginUserSearch : " + strUser);

        vo = gson.fromJson(strUser, LoginVO.class);
    }

}