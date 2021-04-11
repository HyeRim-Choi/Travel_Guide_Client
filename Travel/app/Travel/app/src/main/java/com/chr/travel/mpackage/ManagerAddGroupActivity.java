package com.chr.travel.mpackage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chr.travel.R;
import com.chr.travel.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;

import vo.LoginVO;

/* 그룹 생성 액티비티 */

public class ManagerAddGroupActivity extends AppCompatActivity {

    EditText et_title, et_userId;
    Button btn_addGroup, btn_addId;
    TextView txt_addId;

    String title, id;

    JSONObject postDataParam;

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

        et_title = findViewById(R.id.et_title);
        et_userId = findViewById(R.id.et_userId);
        btn_addGroup = findViewById(R.id.btn_addGroup);
        btn_addId = findViewById(R.id.btn_addId);
        txt_addId = findViewById(R.id.txt_addId);

        btn_addGroup.setOnClickListener(click);
        btn_addId.setOnClickListener(click);
    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // 그룹 추가 버튼 클릭 시
                case R.id.btn_addGroup:
                    postDataParam = new JSONObject();

                    title = et_title.getText().toString();
                    String ids = txt_addId.getText().toString();

                    if (BlankCheck(title, ids)) {
                        try {
                            postDataParam.put("title", title);
                            postDataParam.put("userId", ids);
                            postDataParam.put("manager",vo.getUserId());
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
                        break;
                    }

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

    // 정보 다 입력했는지 확인하는 함수
    public boolean BlankCheck(String title, String ids){

        if(title.isEmpty() || title == null){
            Toast.makeText(ManagerAddGroupActivity.this, "그룹 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(ids.isEmpty() || ids == null){
            Toast.makeText(ManagerAddGroupActivity.this, "초대 할 여행객의 아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    };

}