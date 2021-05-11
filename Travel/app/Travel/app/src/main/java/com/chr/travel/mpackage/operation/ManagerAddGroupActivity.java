package com.chr.travel.mpackage.operation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chr.travel.R;
import com.chr.travel.login.LoginActivity;
import com.chr.travel.mpackage.plan.TripListActivity;

import org.json.JSONException;
import org.json.JSONObject;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;

import vo.LoginVO;

/* 그룹 생성 액티비티 */

public class ManagerAddGroupActivity extends AppCompatActivity {

    EditText et_title, et_userId;
    Button btn_addGroup, btn_addId, btn_startDate, btn_endDate, btn_registerTrip;
    TextView txt_addId, txt_startDate, txt_endDate, txt_registerTrip;

    String title, id, product;

    JSONObject postDataParam;

    private static final int START_DATE = 1;
    private static final int END_DATE = 2;
    private static final int REGISTER_TRIP = 3;

    // login한 user 정보
    LoginVO vo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_manager_add_group);

        vo = LoginVO.getInstance();

        // login 하지 않았다면
        if(vo.getUserId() == null){
            Toast.makeText(ManagerAddGroupActivity.this,"로그인 후 이용해주세요", Toast.LENGTH_LONG).show();
            Intent i = new Intent(ManagerAddGroupActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        // 인텐트 가져오기
        Intent intent = getIntent();
        // product name
        product = intent.getStringExtra("product");

        et_title = findViewById(R.id.et_title);
        et_userId = findViewById(R.id.et_userId);
        btn_addGroup = findViewById(R.id.btn_addGroup);
        btn_addId = findViewById(R.id.btn_addId);
        btn_startDate = findViewById(R.id.btn_startDate);
        btn_endDate = findViewById(R.id.btn_endDate);
        btn_registerTrip = findViewById(R.id.btn_registerTrip);
        txt_addId = findViewById(R.id.txt_addId);
        txt_startDate = findViewById(R.id.txt_startDate);
        txt_endDate = findViewById(R.id.txt_endDate);
        txt_registerTrip = findViewById(R.id.txt_registerTrip);

        // TripDetailsActivity 에서 운영하기 버튼을 클릭하여 넘어 온 거면 product name 세팅
        if(product != null){
            txt_registerTrip.setText(product);
        }

        btn_addGroup.setOnClickListener(click);
        btn_addId.setOnClickListener(click);
        btn_startDate.setOnClickListener(click);
        btn_endDate.setOnClickListener(click);
        btn_registerTrip.setOnClickListener(click);
    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i;

            switch (v.getId()) {

                // 그룹 추가 버튼 클릭 시
                case R.id.btn_addGroup:
                    postDataParam = new JSONObject();

                    title = et_title.getText().toString();
                    String ids = txt_addId.getText().toString();
                    String startDate = txt_startDate.getText().toString();
                    String endDate = txt_endDate.getText().toString();
                    product = txt_registerTrip.getText().toString();


                    if (BlankCheck(title, ids, startDate, endDate, product)) {
                        try {
                            postDataParam.put("title", title);
                            postDataParam.put("userId", ids);
                            postDataParam.put("manager",vo.getUserId());
                            postDataParam.put("startDate", startDate);
                            postDataParam.put("endDate", endDate);
                            postDataParam.put("product", product);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            AsyncTaskFactory.getApiPostTask(ManagerAddGroupActivity.this, API_CHOICE.GROUP_ADD, new AsyncTaskCallBack() {
                                @Override
                                public void onTaskDone(Object... params) {
                                    // 그룹 생성 성공하면
                                    if((Integer)params[0] == 1){

                                        Intent i = new Intent(ManagerAddGroupActivity.this, PackageManagerActivity.class);
                                        startActivity(i);
                                        finish();

                                    }
                                }
                            }).execute(postDataParam);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    break;



                 // 운영 여행 지정 버튼 클릭 시
                case R.id.btn_registerTrip:
                    // 여행 목록으로 이동
                    i = new Intent(ManagerAddGroupActivity.this, TripListActivity.class);
                   startActivity(i);
                   finish();
                    break;


                    // 출발 날짜 클릭 시
                case R.id.btn_startDate:
                    i = new Intent(ManagerAddGroupActivity.this, TripDatePickerActivity.class);
                    i.putExtra("date", START_DATE);
                    startActivityForResult(i,START_DATE);
                    break;

                // 도착 날짜 클릭 시
                case R.id.btn_endDate:
                    i = new Intent(ManagerAddGroupActivity.this, TripDatePickerActivity.class);
                    i.putExtra("date", END_DATE);
                    startActivityForResult(i,END_DATE);
                    break;

                // 아이디 추가 버튼 클릭 시
                case R.id.btn_addId:
                    postDataParam = new JSONObject();

                    id = et_userId.getText().toString();

                    if(id.isEmpty() || id == null){
                        Toast.makeText(ManagerAddGroupActivity.this, "그룹으로 초대 할 아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 서버를 통해 해당아이디 사용가능한지 확인받기
                    else{
                        try {
                            AsyncTaskFactory.getApiGetTask(ManagerAddGroupActivity.this, API_CHOICE.GROUP_ID_CHECK, id, new AsyncTaskCallBack() {
                                @Override
                                public void onTaskDone(Object... params) {
                                    //  id가 존재한다면
                                    if((Integer)params[0] == 1){
                                        if(txt_addId.getText().toString().isEmpty()){
                                            txt_addId.setText(id);
                                        }
                                        else{
                                            txt_addId.append(","+id);
                                        }
                                    }
                                    et_userId.setText(null);
                                }
                            }).execute();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    break;

            }
        }
    };


    // 출발, 도착날짜를 TripDatePickerActivity에서 선택 후 날짜 가져오기
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null){
            return;
        }

        switch (requestCode){

            // 출발 날짜, 도착 날짜 지정 후 돌아오는 곳
            case START_DATE:
            case END_DATE:

                String year = data.getStringExtra("yy");
                String month = data.getStringExtra("mm");
                String day = data.getStringExtra("dd");


                if(month.length() == 1){
                    month = "0"+month;
                }

                if(day.length() == 1){
                    day = "0"+day;
                }

                if(requestCode == START_DATE){
                    txt_startDate.setText(year + "-" + month + "-" + day);
                }
                else{
                    txt_endDate.setText(year + "-" + month + "-" + day);
                }

                break;


            // ***** 수정
            // 운영 여행 지정 후 돌아오는 곳
            /*case REGISTER_TRIP:
                product = data.getStringExtra("product");
                txt_registerTrip.setText(product);
                break;*/

        }




    }

    // 정보 다 입력했는지 확인하는 함수
    public boolean BlankCheck(String title, String ids, String startDate, String endDate, String product){

        if(title.isEmpty() || title == null){
            Toast.makeText(ManagerAddGroupActivity.this, "그룹 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(ids.isEmpty() || ids == null){
            Toast.makeText(ManagerAddGroupActivity.this, "초대 할 여행객의 아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(startDate.isEmpty() || startDate == null){
            Toast.makeText(ManagerAddGroupActivity.this, "출발 날짜를 지정해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(endDate.isEmpty() || endDate == null){
            Toast.makeText(ManagerAddGroupActivity.this, "도착 날짜를 지정해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(product.isEmpty() || product == null){
            Toast.makeText(ManagerAddGroupActivity.this, "운영 할 여행 상품을 지정해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    };

}