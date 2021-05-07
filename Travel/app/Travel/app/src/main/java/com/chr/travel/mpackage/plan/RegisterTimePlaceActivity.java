package com.chr.travel.mpackage.plan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.chr.travel.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;

/* 시작 시간, 종료 시간. 장소를 지정하는 액티비티 */

public class RegisterTimePlaceActivity extends AppCompatActivity {

    EditText et_place;
    TextView txt_start, txt_end;
    Button btn_search, btn_save, btn_start, btn_end;

    // 관광지 검색해서 서버로 부터 받은 데이터
    ListView listView_place;
    ArrayAdapter adapter;

    String start, end, place = "";
    boolean freeTimeChk;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_register_time_place);


        txt_start = findViewById(R.id.txt_start);
        txt_end = findViewById(R.id.txt_end);
        et_place = findViewById(R.id.et_place);
        btn_save = findViewById(R.id.btn_save);
        btn_search = findViewById(R.id.btn_search);
        btn_start = findViewById(R.id.btn_start);
        btn_end = findViewById(R.id.btn_end);
        listView_place = findViewById(R.id.listView_place);

        btn_search.setOnClickListener(click);
        btn_save.setOnClickListener(click);
        btn_start.setOnClickListener(click);
        btn_end.setOnClickListener(click);

        listView_place.setOnItemClickListener(list_click);
    }

    // ListView 클릭 이벤트
    AdapterView.OnItemClickListener list_click = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String spot = adapter.getItem(position).toString();

            makeDialogPlace(spot);
        }
    };


    // 버튼 클릭 이벤트
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){

                // 시작 시간 클릭 시
                case R.id.btn_start:

                    // TimePickerDialog
                    timePickerDialog(1);

                    break;

                // 종료 시간 클릭 시
                case R.id.btn_end:

                    // TimePickerDialog
                    timePickerDialog(2);

                    break;


                // 검색 버튼 클릭 시
                case R.id.btn_search:

                    String place = et_place.getText().toString();

                    // 관광지 검색 창이 비어있다면
                    if(place == null || place.isEmpty()){
                        Toast.makeText(RegisterTimePlaceActivity.this, "관광지를 검색해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 서버 통신하여 관광지 데이터 받기
                    try {
                        AsyncTaskFactory.getApiGetTask(RegisterTimePlaceActivity.this, API_CHOICE.MANAGER_ROUTE_PLACE_SEARCH, place, new AsyncTaskCallBack() {
                            @Override
                            public void onTaskDone(Object... params) {
                                adapter = new ArrayAdapter(RegisterTimePlaceActivity.this, android.R.layout.simple_list_item_1, (List) params[0]) {

                                    @Override
                                    public View getView(int position, View convertView, ViewGroup parent) {
                                        // ListView custom
                                        View view = super.getView(position, convertView, parent);
                                        TextView tv = (TextView) view.findViewById(android.R.id.text1);
                                        tv.setTextColor(Color.BLACK);
                                        tv.setPadding(50, 50, 0, 50);
                                        return view;
                                    }
                                };

                                listView_place.setAdapter(adapter);
                            }
                        }).execute();
                    }

                    catch (Exception e){
                        e.printStackTrace();
                    }

                    break;


                 // 저장 버튼 클릭 시
                case R.id.btn_save:

                    makeDialogSave();

                    break;

            }
        }
    };


    // TimePickerDialog
    private void timePickerDialog(int chk){

        Calendar cal = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(RegisterTimePlaceActivity.this,
                android.R.style.Theme_Holo_Light_Dialog,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                if(chk == 1){
                    txt_start.setText(hourOfDay + "시 " + minute + "분");
                }

                else{
                    txt_end.setText(hourOfDay + "시 " + minute + "분");
                }

            }
        },cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);


        timePickerDialog.show();
        
    }



    /* ListView click 했을 때의 Alert창 */
    private void makeDialogPlace(String spot) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterTimePlaceActivity.this);
        dialog.setTitle(Html.fromHtml("<b><font color='#FFFFFF'>" + spot + "</font></b>"));
        dialog.setMessage(Html.fromHtml("<font color='#FFFFFF'> 관광지를 경로에 추가하시겠습니까? </font><br>" + "<font color='#000000'> 해당 관광지에 자유시간이 있다면 자유시간 버튼을 눌러주세요 </font>"));

        dialog.setPositiveButton("자유시간", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                place = spot;
                freeTimeChk = true;

                Toast.makeText(RegisterTimePlaceActivity.this, "자유시간으로 저장되었습니다", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.setNegativeButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                place = spot;
                freeTimeChk = false;

                Toast.makeText(RegisterTimePlaceActivity.this, "저장되었습니다", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.setNeutralButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = dialog.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                alert.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.WHITE);
                alert.setIcon(R.drawable.img_alert);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#65acf3")));
            }
        });

        alert.show();
    }



    /* 저장 버튼 눌렀을 때의 Alert창 */
    private void makeDialogSave() {

        start = txt_start.getText().toString();
        end = txt_end.getText().toString();

        String freeTime = "";

        AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterTimePlaceActivity.this);

        // 일정 시작 시간을 지정하지 않았다면
        if(start == null || start.isEmpty()){
            Toast.makeText(RegisterTimePlaceActivity.this, "일정 시작 시간을 지정해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        // 일정 종료 시간을 지정하지 않았다면
        if(end == null || end.isEmpty()){
            Toast.makeText(RegisterTimePlaceActivity.this, "일정 종료 시간을 지정해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        // 관광지를 지정하지 않았다면
        if(place == null || place.isEmpty()){
            Toast.makeText(RegisterTimePlaceActivity.this, "장소를 지정해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        if(freeTimeChk){
            freeTime = "(자유시간)";
        }


        dialog.setTitle(Html.fromHtml("<b><font color='#FFFFFF'> 일정 추가 </font></b>"));
        dialog.setMessage(Html.fromHtml("<font color='#FFFFFF'> 시간 :  </font>" + start + "<font color='#FFFFFF'> ~ </font>" + end +
                                               "<br><font color='#FFFFFF'> 장소 : </font>" + place + freeTime +
                                               "<br><font color='#FFFFFF'> 추가 하시겠습니까? </font>"));

        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // 시작 시간, 종료 시간, 관광지 장소, 자유 시간 가지고 다시 돌아가기
                Intent resultIntent = new Intent();

                resultIntent.putExtra("start", start);
                resultIntent.putExtra("end", end);
                resultIntent.putExtra("place", place);
                resultIntent.putExtra("freeTimeChk", freeTimeChk);

                setResult(RESULT_OK, resultIntent);
                finish();

                Toast.makeText(RegisterTimePlaceActivity.this, "저장되었습니다", Toast.LENGTH_SHORT).show();
            }
        });



        dialog.setNeutralButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = dialog.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
                alert.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.WHITE);
                alert.setIcon(R.drawable.img_alert);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#65acf3")));
            }
        });

        alert.show();
    }
}