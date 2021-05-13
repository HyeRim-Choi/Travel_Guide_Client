package com.chr.travel.tpackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.chr.travel.R;
import com.chr.travel.fragmentpackage.VisualizationMapFragment;
import com.chr.travel.fragmentpackage.VisualizationSelectMapFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;

/* 여행객들에게 주는 자유시간 시각화 정보 */

public class VisualizationActivity extends AppCompatActivity {

    String place;
    double latitude, longitude;
    // 큰 관광지(한성대) 안의 등록해둔 서브 관광지
    ArrayList<String> subPlace;
    // 최단 경로
    ArrayList<String> totalMem;
    // 각 서브 관광지별로 머문 평균 시간
    ArrayList<String> avgTime;

    // json 형태로 되어있는 String을 사용할 수 있는 Map구조
    ArrayList<Map> subPlaceMap;
    ArrayList<Map> totalMemMap;
    ArrayList<Map> avgTimeMap;

    Map<String, ArrayList> visualInfo;

    // 나이별, 성별을 검색하기 위한 Spinner
    Spinner spinner_age, spinner_gender;
    Button btn_search;

    String age, gender;

    // 지도를 띄우는 횟수(지도 remove, addView를 위해)
    int fragCnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_visualization);

        spinner_age = findViewById(R.id.spinner_age);
        spinner_gender = findViewById(R.id.spinner_gender);
        btn_search = findViewById(R.id.btn_search);

        // 인텐트 가져오기
        Intent intent = getIntent();
        // place
        place = intent.getStringExtra("place");

        subPlace = new ArrayList<>();
        totalMem = new ArrayList<>();
        avgTime = new ArrayList<>();

        subPlaceMap = new ArrayList<>();
        totalMemMap = new ArrayList<>();
        avgTimeMap = new ArrayList<>();

        visualInfo = new HashMap<>();

        fragCnt = 0;

        // 많이 간 경로(전체)의 데이터를 받기 위해 서버 통신을 하고 프래그먼트 띄우기
        //connectServerGetDataAndSendFragment("all", "all");

        btn_search.setOnClickListener(btn_click);

    }

    @Override
    protected void onResume() {
        super.onResume();

        /* Age Spinner Menu */
        ArrayAdapter ageMenu = ArrayAdapter.createFromResource(this, R.array.age, R.layout.design_home_menu_spinner);
        ageMenu.setDropDownViewResource(R.layout.design_home_menu_spinner);
        //어댑터에 연결
        spinner_age.setAdapter(ageMenu);

        spinner_age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0 || position == 1){
                    age = "all";
                }
                else{
                    age = position + "0";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        /* Gender Spinner Menu */
        ArrayAdapter genderMenu = ArrayAdapter.createFromResource(this, R.array.gender, R.layout.design_home_menu_spinner);
        genderMenu.setDropDownViewResource(R.layout.design_home_menu_spinner);
        //어댑터에 연결
        spinner_gender.setAdapter(genderMenu);

        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                    case 1:
                        // 전체
                        gender = "all";
                        break;

                    case 2:
                        // 남자
                        gender = "0";
                        break;

                    case 3:
                        // 여자
                        gender = "1";
                        break;

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }


    // 버튼 클릭 이벤트
    View.OnClickListener btn_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                // 검색 버튼 클릭 시
                case R.id.btn_search:
                    // 선택한 나이, 성별에 맞는 데이터를 가져오기 위한 서버 통신
                    connectServerGetDataAndSendFragment(age, gender);

                    break;

            }

        }
    };


    // 서버 통신 + 프래그먼트 불러오기 함수
    public void connectServerGetDataAndSendFragment(String age, String gender){
        // 서버와 통신해서 많이 간 경로(전체) 가져오기
        JSONObject postDataParam = new JSONObject();

        final VisualizationMapFragment[] visualizationMapFragment = new VisualizationMapFragment[1];
        final VisualizationSelectMapFragment[] visualizationSelectMapFragment = new VisualizationSelectMapFragment[1];


        // node에 전달 할 정보 넣기
        try {
            postDataParam.put("place", place);
            postDataParam.put("gender", gender);
            postDataParam.put("age", age);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 서버 통신하여 클릭한 장소에 해당하는 시각화 정보를 받아 VisualizationMapFragment를 불러온다
        try {
            AsyncTaskFactory.getApiPostTask(VisualizationActivity.this, API_CHOICE.VISUALIZATION, new AsyncTaskCallBack() {
                @Override
                public void onTaskDone(Object... params) {
                    if((Integer)params[0] == 1){
                        latitude = (double) params[1];
                        longitude = (double) params[2];
                        subPlace = (ArrayList<String>) params[3];
                        totalMem = (ArrayList<String>) params[4];
                        avgTime = (ArrayList<String>) params[5];

                        if(subPlace.size() != 0 && totalMem.size() != 0 && avgTime.size() != 0){
                            // subPlace 이제 사용 가능한 List 형태로 만들어짐
                            for(int i = 0 ; i < subPlace.size() ; i++){
                                subPlaceMap.add(paramMap(subPlace.get(i)));
                            }
                            // totalMem 이제 사용 가능한 List 형태로 만들어짐
                            for(int i = 0 ; i < totalMem.size() ; i++){
                                totalMemMap.add(paramMap(totalMem.get(i)));
                            }
                            // subPlaceMap은 이제 사용 가능한 List 형태로 만들어짐
                            for(int i = 0 ; i < avgTime.size() ; i++){
                                avgTimeMap.add(paramMap(avgTime.get(i)));
                            }

                            // VisualizationFragment에 전달 될 Map
                            visualInfo.put("subPlace", subPlaceMap);
                            visualInfo.put("totalMem", totalMemMap);
                            visualInfo.put("avgTime", avgTimeMap);
                        }

                        else{
                            visualInfo = null;
                        }

                        getSupportFragmentManager().beginTransaction().add(R.id.map_frag_view, new VisualizationMapFragment(place, latitude, longitude, visualInfo)).commit();

                       /* if(fragCnt == 0){
                            fragCnt = 1;

                            visualizationMapFragment[0] = new VisualizationMapFragment(place, latitude, longitude, visualInfo);


                            FragmentManager fragmentManager = getSupportFragmentManager();

                            if(visualizationSelectMapFragment[0] != null){
                                fragmentManager.beginTransaction().remove(visualizationSelectMapFragment[0]).commit();
                                fragmentManager.popBackStack();
                            }

                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.add(R.id.map_frag_view, visualizationMapFragment[0]);
                            fragmentTransaction.commit();


                            // 지도 Fragment 띄우기
                            //getSupportFragmentManager().beginTransaction().add(R.id.map_frag_view, new VisualizationMapFragment(place, latitude, longitude, visualInfo)).commit();
                        }

                        else{
                            fragCnt = 0;

                            visualizationSelectMapFragment[0] = new VisualizationSelectMapFragment(place, latitude, longitude, visualInfo);

                            FragmentManager fragmentManager = getSupportFragmentManager();

                            if(visualizationMapFragment[0] != null){
                                fragmentManager.beginTransaction().remove(visualizationMapFragment[0]).commit();
                                fragmentManager.popBackStack();
                            }

                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.add(R.id.map_frag_view, visualizationSelectMapFragment[0]);
                            fragmentTransaction.commit();



                            // 지도 Fragment 띄우기
                            //getSupportFragmentManager().beginTransaction().add(R.id.map_frag_view, new VisualizationSelectMapFragment(place, latitude, longitude, visualInfo)).commit();
                        }*/




                    }
                }
            }).execute(postDataParam);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }



    // String -> Map 구조로 바꾸는 함수
    public HashMap<String, String> paramMap(Object object){

        HashMap<String, String> hashmap = new HashMap<String, String>();

        try {
            JSONObject json = new JSONObject(String.valueOf(object)); // 받아온 string을 json 으로 변환

            Iterator i = json.keys(); // json key 요소읽어옴

            while(i.hasNext()){

                String k = i.next().toString(); // key 순차적으로 추출

                hashmap.put(k, json.getString(k)); // key, value를 map에 삽입
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }



        return hashmap;

    }

}