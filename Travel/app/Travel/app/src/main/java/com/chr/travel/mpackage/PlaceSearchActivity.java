package com.chr.travel.mpackage;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.Toast;

import com.chr.travel.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;

/* 관광지 검색 */

/// ******** delete

public class PlaceSearchActivity extends AppCompatActivity {

    EditText et_place;
    Button btn_search, btn_saveRoute;
    TextView txt_addRoute, txt_date;
    // 관광지 검색해서 서버로 부터 받은 데이터
    ListView listView_place;

    ArrayAdapter adapter;

    String date, title;

    ArrayList<Map> placeRoute;
    Map<String, String> map;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_place_search);

        Intent i = getIntent();
        date = i.getStringExtra("date");
        title = i.getStringExtra("title");

        placeRoute = new ArrayList<>();
        map = new HashMap<>();

        et_place = findViewById(R.id.et_place);
        btn_search = findViewById(R.id.btn_search);
        btn_saveRoute = findViewById(R.id.btn_saveRoute);
        txt_addRoute = findViewById(R.id.txt_addRoute);
        listView_place = findViewById(R.id.listView_place);
        txt_date = findViewById(R.id.txt_date);

        txt_date.setText(date);



        btn_search.setOnClickListener(click);
        btn_saveRoute.setOnClickListener(click);

        listView_place.setOnItemClickListener(list_click);
    }

    // ListView 클릭 이벤트
    AdapterView.OnItemClickListener list_click = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String place = adapter.getItem(position).toString();

            makeDialog(place);
        }
    };


    // 버튼 클릭이벤트
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){

                // 검색 클릭 시
                case R.id.btn_search:
                    String place = et_place.getText().toString();

                    // 관광지 검색 창이 비어있다면
                    if(place == null || place.isEmpty()){
                        Toast.makeText(PlaceSearchActivity.this, "관광지를 검색해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 서버 통신하여 관광지 데이터 받기
                    try {
                        AsyncTaskFactory.getApiGetTask(PlaceSearchActivity.this, API_CHOICE.MANAGER_ROUTE_PLACE_SEARCH, place, new AsyncTaskCallBack() {
                            @Override
                            public void onTaskDone(Object... params) {
                                adapter = new ArrayAdapter(PlaceSearchActivity.this, android.R.layout.simple_list_item_1, (List) params[0]) {

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


                // 관광지 저장 클릭 시
                case R.id.btn_saveRoute:

                    String addPlaces = txt_addRoute.getText().toString();

                    // 서버로 등록하고 싶은 관광지 보내기(date, placeRoute, title도)

                    for(int i = 0;i<placeRoute.size();i++){
                        Log.i("placeRoute", "place : " + placeRoute.get(i).get("place") + " freeTimeChk : " + placeRoute.get(i).get("freeTimeChk"));
                    }




                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("routePlaces", addPlaces);
                    resultIntent.putExtra("date", date);
                    setResult(RESULT_OK, resultIntent);
                    finish();

                    break;


            }
        }
    };


    /* Alert창 */
    private void makeDialog(String place) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(PlaceSearchActivity.this);
        dialog.setTitle(Html.fromHtml("<b><font color='#FFFFFF'>" + place + "</font></b>"));
        dialog.setMessage(Html.fromHtml("<font color='#FFFFFF'> 관광지를 경로에 추가하시겠습니까? </font><br>" + "<font color='#000000'> 해당 관광지에 자유시간이 있다면 자유시간 버튼을 눌러주세요 </font>"));

        dialog.setPositiveButton("자유시간", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(txt_addRoute.getText().toString().isEmpty()){
                    txt_addRoute.setText(place);
                }
                else{
                    txt_addRoute.append(" -> "+place);
                }

                map.put("place", place);
                map.put("freeTimeChk", "true");
                placeRoute.add(map);

                Toast.makeText(PlaceSearchActivity.this, "자유시간으로 저장되었습니다", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.setNegativeButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(txt_addRoute.getText().toString().isEmpty()){
                    txt_addRoute.setText(place);
                }
                else{
                    txt_addRoute.append(" -> "+place);
                }

                map.put("place", place);
                map.put("freeTimeChk", "false");
                placeRoute.add(map);

                Toast.makeText(PlaceSearchActivity.this, "저장되었습니다", Toast.LENGTH_SHORT).show();
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
}