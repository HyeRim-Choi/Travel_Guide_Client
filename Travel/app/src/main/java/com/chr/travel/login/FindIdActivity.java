package com.chr.travel.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chr.travel.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;

/* 아이디 찾기 */

public class FindIdActivity extends AppCompatActivity {

    EditText et_name, et_email;
    Button btn_findId;

    String name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitylogin_find_id);

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        btn_findId = findViewById(R.id.btn_findId);


        // 아이디 찾기 버튼 클릭 시
        btn_findId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = et_name.getText().toString();
                email = et_email.getText().toString();

                // id 찾기 정보 입력 다 했는지 확인
                if (BlankCheck(name, email)) {
                    JSONObject postDataParam = new JSONObject();

                    // node에 전달해 줄 정보 넣기
                    try {
                        postDataParam.put("name", name.trim());
                        postDataParam.put("email", email.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        AsyncTaskFactory.getApiPostTask(FindIdActivity.this, API_CHOICE.FIND_ID, new AsyncTaskCallBack() {
                            @Override
                            public void onTaskDone(Object... params) {
                                // *******Alert창 잘 작동되는지 확인 후 삭제*********
                                if ((Integer) params[0] == 1) {
                                    // id 찾기 성공 시
                                    // 아이디 Alert창 띄우기
                                    /*AlertDialog.Builder dialog = new AlertDialog.Builder(FindIdActivity.this);
                                    dialog.setTitle(name + "님의 아이디");
                                    dialog.setMessage((CharSequence) params[1]);
                                    dialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });

                                    dialog.show();*/

                                    // id 찾기 성공 시
                                    // 아이디 Alert창 띄우기
                                    makeDialog((String) params[1]);
                                }
                            }
                        }).execute(postDataParam);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // id찾기 정보를 다 입력했는지 확인하는 함수
    public boolean BlankCheck(String name, String email) {

        if (name.isEmpty() || name == null) {
            Toast.makeText(FindIdActivity.this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email.isEmpty() || email == null) {
            Toast.makeText(FindIdActivity.this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }


        // 이메일 형식 유효성 검사
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (!pattern.matcher(email).matches()) {
            Toast.makeText(FindIdActivity.this, "잘못된 이메일 형식입니다", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    /* Alert창 */
    private void makeDialog(String findId) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(FindIdActivity.this);
        // 가운데로 안오면 글씨를 배경과 같은 색으로 해서 좀 집어넣기
        dialog.setTitle(Html.fromHtml("<b><font color='#FFFFFF'>" + name + "님의 아이디</font></b>"));
        dialog.setMessage(Html.fromHtml("<font color='#FFFFFF'>" + findId + "</font>"));

        dialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });

        AlertDialog alert = dialog.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
                alert.setIcon(R.drawable.img_alert);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#65acf3")));
            }
        });

        alert.show();
    }
}
