package com.chr.travel.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.chr.travel.GetApiHashKeyActivity;
import com.chr.travel.login.LoginActivity;
import com.chr.travel.R;
import com.chr.travel.mpackage.PackageManagerActivity;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;
import vo.LoginVO;

/* Home */

public class HomeActivity extends AppCompatActivity {

    DrawerLayout drawer_layout;
    LinearLayout drawer;
    Button opendrawer, btn_logout;
    TextView txt_home_actionbar, txt_id;

    // login한 user 정보
    LoginVO vo;

    Spinner spinner_package_menu, spinner_free_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityhome_home);
    }

    // 뒤로 돌아와서 Home에서 작업을 진행할 수 있으니 onResume에다 코드 삽입
    @Override
    protected void onResume() {
        super.onResume();

        vo = LoginVO.getInstance();

        // login 하지 않았다면
        if(vo.getUserId() == null){
            Toast.makeText(HomeActivity.this,"로그인 후 이용해주세요", Toast.LENGTH_LONG).show();
            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
            return;
        }


        drawer_layout = findViewById(R.id.drawer_layout);
        drawer = findViewById(R.id.drawer);
        opendrawer = findViewById(R.id.opendrawer);
        txt_home_actionbar = findViewById(R.id.txt_home_actionbar);
        txt_id = findViewById(R.id.txt_id);
        btn_logout = findViewById(R.id.btn_logout);
        spinner_package_menu = findViewById(R.id.spinner_package_menu);
        spinner_free_menu = findViewById(R.id.spinner_free_menu);


        txt_home_actionbar.setText(vo.getUserId() + "님의 여행");
        txt_id.setText(vo.getUserId() + "님 반가워요");


        // 버튼들 클릭 시
        opendrawer.setOnClickListener(click);
        btn_logout.setOnClickListener(click);

        /* Package Menu */
        ArrayAdapter packageMenu = ArrayAdapter.createFromResource(this, getAuth(vo.getRole()), R.layout.design_homemenuspinner);
        packageMenu.setDropDownViewResource(R.layout.design_homemenuspinner);
        //어댑터에 연결
        spinner_package_menu.setAdapter(packageMenu);

        spinner_package_menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    // 가이드 버튼을 눌렀을 때
                    case 1:
                        if(vo.getRole().equals("manager")){
                            //매니저 액티비티로 이동
                            Intent i = new Intent(HomeActivity.this, PackageManagerActivity.class);
                            startActivity(i);
                        }
                        else{
                            // 여행객 액티비티로 이동
                        }
                        break;

                    case 2:
                        // 공지사항으로 이동
                        break;

                    case 3:
                        // 채팅 창으로 이동
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        /* Free Travel Menu */
        ArrayAdapter freeMenu = ArrayAdapter.createFromResource(this, R.array.freeItem, R.layout.design_homemenuspinner);
        packageMenu.setDropDownViewResource(R.layout.design_homemenuspinner);
        //어댑터에 연결
        spinner_free_menu.setAdapter(freeMenu);

        spinner_free_menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        // 지역별로 이동
                        break;

                    case 2:
                        // 테마별로 이동
                        break;

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });



        /* drawer */
        //손가락 드래그로 메뉴 열고 닫기를 방지(버튼 클릭으로만 서랍을 제어)
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,drawer); // drawer : 지정한 서랍만 수행

        //손가락 드래그로 메뉴 열고 닫기를 가능하게 하기
        //drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, drawer);

        //메뉴가 열리고 닫힘을 감지하는 이벤트 감지자(뒤로 가기로 서랍이 닫힐 수 있도록)
        drawer_layout.setDrawerListener(listener);
    }

    /* drawer */
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


    /* Home Menu array */
    public int getAuth(String auth){
        // 가이드면 packageGuide Array를 return
        if(auth.equals("manager")){
            return R.array.packageGuide;
        }

        // 여행객이면 packageMember Array를 return
        else{
            return R.array.packageMember;
        }
    }



    // 버튼들의 이벤트
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                // 서랍 열기를 눌렀을 때
                case R.id.opendrawer:
                    // 서랍 열림
                    drawer_layout.openDrawer(drawer);
                    break;


                // 로그아웃 버튼을 눌렀을 때
                case R.id.btn_logout:
                    try {
                        AsyncTaskFactory.getApiGetTask(HomeActivity.this, API_CHOICE.LOGOUT, vo.getUserId(), new AsyncTaskCallBack() {
                            @Override
                            public void onTaskDone(Object... params) {
                                if((Integer)params[0] == 1){
                                    Toast.makeText(HomeActivity.this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();
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

            }
        }
    };


}