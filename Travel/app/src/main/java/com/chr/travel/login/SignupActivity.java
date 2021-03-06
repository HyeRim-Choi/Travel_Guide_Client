package com.chr.travel.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chr.travel.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;


public class SignupActivity extends AppCompatActivity {

    EditText et_name, et_id, et_pwd, et_checkPwd, et_email, et_tel;
    Button btn_checkId, btn_signUp, btn_birth;
    RadioGroup radioGroup_auth, radioGroup_gen;
    RadioButton radio_guide, radio_person, radio_m, radio_w;
    TextView txt_birth;

    boolean check_id = false;
    boolean check_pwd = false;
    boolean check_gen = false;

    String name, id, pwd, checkPwd, email, tel, role, birth;
    boolean gen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitylogin_signup);


        et_name = findViewById(R.id.et_name);
        et_id = findViewById(R.id.et_id);
        et_pwd = findViewById(R.id.et_pwd);
        et_checkPwd = findViewById(R.id.et_checkPwd);
        et_email = findViewById(R.id.et_email);
        et_tel = findViewById(R.id.et_tel);
        btn_checkId = findViewById(R.id.btn_checkId);
        btn_signUp = findViewById(R.id.btn_signUp);
        btn_birth = findViewById(R.id.btn_birth);
        txt_birth = findViewById(R.id.txt_birth);


        // 버튼들 클릭 시
        btn_birth.setOnClickListener(click);
        btn_checkId.setOnClickListener(click);
        btn_signUp.setOnClickListener(click);

        // 라디오 버튼들
        radio_guide = findViewById(R.id.radio_guide);
        radio_person = findViewById(R.id.radio_person);
        radio_m = findViewById(R.id.radio_m);
        radio_w = findViewById(R.id.radio_w);

        // 성별
        radioGroup_gen = findViewById(R.id.radioGroup_gen);
        radioGroup_gen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                check_gen = true;
                if(i == R.id.radio_m){
                    gen = false;
                }
                else if(i == R.id.radio_w){
                    gen = true;
                }
            }
        });



        // 권한
        radioGroup_auth = findViewById(R.id.radioGroup_auth);
        radioGroup_auth.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radio_guide){
                    role = "manager";
                }
                else if(checkedId == R.id.radio_person) {
                    role = "member";
                }
            }
        });


    }

    // 버튼 클릭 이벤트
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                // 생년월일 버튼 클릭 시
                case R.id.btn_birth:
                    Intent birthPicker = new Intent(getApplicationContext(), BirthPickerActivity.class);
                    startActivityForResult(birthPicker,1000);
                    break;

                // 아이디 중복 체크 클릭 시
                case R.id.btn_checkId:
                    id = et_id.getText().toString();

                    if(id.isEmpty() || id == null){
                        Toast.makeText(SignupActivity.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 서버를 통해 해당아이디 사용가능한지 확인받기
                   else{
                       try {
                           // node로 정보 전달
                           AsyncTaskFactory.getApiGetTask(SignupActivity.this, API_CHOICE.IDCHECK, id, new AsyncTaskCallBack() {
                               // 아이디 사용가능하다면
                               @Override
                               public void onTaskDone(Object... params) {
                                   if ((Integer) params[0] == 1) {
                                       check_id = true;
                                   }
                               }
                           }).execute();
                       }
                       catch (Exception e){
                           e.printStackTrace();
                       }
                    }
                    break;


                // 회원가입 버튼 클릭 시
                case R.id.btn_signUp:
                    JSONObject postDataParam = new JSONObject();

                    // 회원가입 시 정보들 다 채웠는지 확인
                    if(BlankCheck()){
                        // 아이디 중복 체크했는지 확인
                        if(check_id){
                            // 보낼 데이터들 저장
                            try {
                                // node에 전달 할 정보 넣기
                                postDataParam.put("name", name.trim());
                                postDataParam.put("gender", gen);
                                postDataParam.put("birth", birth.trim());
                                postDataParam.put("phoneNum", tel.trim());
                                postDataParam.put("email", email.trim());
                                postDataParam.put("userId", id.trim());
                                postDataParam.put("password", pwd.trim());
                                postDataParam.put("role", role.trim());
                            }
                            catch (JSONException e) {
                                Log.e("SignupActivity", "JSONEXception");
                            }

                            try {
                                AsyncTaskFactory.getApiPostTask(SignupActivity.this, API_CHOICE.SIGNUP, new AsyncTaskCallBack() {
                                    @Override
                                    public void onTaskDone(Object... params) {
                                        if((Integer)params[0] == 1){
                                            Toast.makeText(SignupActivity.this, "회원가입이 되었습니다", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(SignupActivity.this, LoginActivity.class);
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

                        // 아이디 중복 체크를 하지 않았다면
                        else{
                            Toast.makeText(SignupActivity.this, "아이디 중복체크를 해주세요", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                     break;
            }
        }
    };


    // 생년월일을 BirthPickerActivity에서 선택 후 생년월일 가져오기
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != 1000 || data == null)
            return;

        String year = data.getStringExtra("yy");
        String month = data.getStringExtra("mm");
        String day = data.getStringExtra("dd");

        if(month.length() == 1){
            month = "0"+month;
        }

        if(day.length() == 1){
            day = "0"+day;
        }

        txt_birth.setText(year.substring(0,4)+month+day);
    }


    // 회원가입 정보를 다 올바르게 입력했는지 확인하는 함수
    public boolean BlankCheck(){

        name = et_name.getText().toString();
        birth = txt_birth.getText().toString();
        id = et_id.getText().toString();
        pwd = et_pwd.getText().toString();
        checkPwd = et_checkPwd.getText().toString();
        email = et_email.getText().toString();
        tel = et_tel.getText().toString();


        if(name.isEmpty() || name == null){
            Toast.makeText(SignupActivity.this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(birth.isEmpty() || birth == null){
            Toast.makeText(SignupActivity.this, "생년월일을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!check_gen){
            Toast.makeText(SignupActivity.this, "성별을 선택해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(tel.isEmpty() || tel == null){
            Toast.makeText(SignupActivity.this, "휴대폰 번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(email.isEmpty() || email == null){
            Toast.makeText(SignupActivity.this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(id.isEmpty() || id == null){
            Toast.makeText(SignupActivity.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pwd.isEmpty() || pwd == null){
            Toast.makeText(SignupActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(checkPwd.isEmpty() || checkPwd == null){
            Toast.makeText(SignupActivity.this, "비밀번호 확인을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(role.isEmpty() || role == null){
            Toast.makeText(SignupActivity.this, "어떤 회원으로 가입하실지 선택해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        //휴대폰번호 유효성 검사
        if(!Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", tel)){
            Toast.makeText(SignupActivity.this,"올바른 핸드폰 번호가 아닙니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 이메일 형식 유효성 검사
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if(!pattern.matcher(email).matches()){
            Toast.makeText(SignupActivity.this, "잘못된 이메일 형식입니다", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 비밀번호 == 비밀번호 확인인지 확인
        if(pwd.equals(checkPwd)){
            check_pwd = true;
        }
        else{
            Toast.makeText(SignupActivity.this, "비밀번호가 다르게 입력되었습니다", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    };



}