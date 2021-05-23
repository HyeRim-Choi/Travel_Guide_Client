package com.chr.travel.tpackage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.chr.travel.R;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import adapter.CustomCalloutBalloonAdapter;
import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;
import vo.VisualizationInfo;

/* 여행객들에게 주는 자유시간 시각화 정보 */

public class VisualizationActivity extends AppCompatActivity{

    String place;
    double latitude, longitude;

    // 큰 관광지(한성대) 안의 등록해둔 서브 관광지
    ArrayList<String> subPlace;
    // 경로
    ArrayList<String> totalMem;
    // 각 서브 관광지별로 머문 평균 시간
    ArrayList<String> avgTime;

    // json 형태로 되어있는 String을 사용할 수 있는 Map구조
    ArrayList<Map> subPlaceMap;
    ArrayList<Map> totalMemMap;
    ArrayList<Map> avgTimeMap;


    // 나이별, 성별을 검색하기 위한 Spinner
    Spinner spinner_age, spinner_gender;
    Button btn_search;

    String age, gender;

    // map
    RelativeLayout relativeLayout;
    MapView mapView;
    // 서브 관광지(자유시간 장소) 마커
    MapPOIItem subPlaceMarker;
    // 자유시간이 있는 큰 관광지 마커
    MapPOIItem placeMarker;
    // Polyline
    MapPolyline polyline;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_visualization);

        spinner_age = findViewById(R.id.spinner_age);
        spinner_gender = findViewById(R.id.spinner_gender);
        btn_search = findViewById(R.id.btn_search);
        relativeLayout = findViewById(R.id.map_view);

        // 인텐트 가져오기
        Intent intent = getIntent();
        // place
        place = intent.getStringExtra("place");

        ViewGroup mapViewContainer = relativeLayout;
        mapView = new MapView(VisualizationActivity.this);
        mapViewContainer.addView(mapView);


        // 많이 간 경로(전체)의 데이터를 받기 위해 서버 통신해서 정보 보여주기
        connectServerGetData("all", "all");

        // 커스텀 말풍선 등록
        mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter(getLayoutInflater()));


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
                    connectServerGetData(age, gender);
                    break;

            }

        }
    };


    // 서버 통신 + 프래그먼트 불러오기 함수
    public void connectServerGetData(String age, String gender){
        // 서버와 통신해서 많이 간 경로(전체) 가져오기
        JSONObject postDataParam = new JSONObject();

        // 변수 초기화
        subPlaceMap = new ArrayList<>();
        totalMemMap = new ArrayList<>();
        avgTimeMap = new ArrayList<>();

        subPlace = new ArrayList<>();
        totalMem = new ArrayList<>();
        avgTime = new ArrayList<>();


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


                        if(subPlace.size() != 0){
                            // subPlace 이제 사용 가능한 List 형태로 만들어짐
                            for(int i = 0 ; i < subPlace.size() ; i++){
                                subPlaceMap.add(paramMap(subPlace.get(i)));
                            }
                        }

                        if(totalMem.size() != 0){
                            // totalMem 이제 사용 가능한 List 형태로 만들어짐
                            for(int i = 0 ; i < totalMem.size() ; i++){
                                totalMemMap.add(paramMap(totalMem.get(i)));
                            }
                        }

                        if(avgTime.size() != 0){
                            // subPlaceMap은 이제 사용 가능한 List 형태로 만들어짐
                            for(int i = 0 ; i < avgTime.size() ; i++){
                                avgTimeMap.add(paramMap(avgTime.get(i)));
                            }
                        }

                        // 마커 지우기
                        if(subPlaceMarker != null){
                            mapView.removePOIItem(subPlaceMarker);
                        }

                        // polyline 지우기
                        if(polyline != null){
                            mapView.removeAllPolylines();
                        }

                        Log.i("Visualization", "subPlaceMap : " + subPlaceMap);
                        Log.i("Visualization", "totalMemMap : " + totalMemMap);
                        Log.i("Visualization", "avgTimeMap : " + avgTimeMap);

                        // 마커, 오버레이 띄우기
                        showData(place, latitude, longitude, subPlaceMap, totalMemMap, avgTimeMap);

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



    // 마커, 오버레이 나타내기
    public void showData(String place, double latitude, double longitude, ArrayList<Map> subPlaceMap, ArrayList<Map> totalMemMap, ArrayList<Map> avgTimeMap){

        // 큰 관광지(한성대) 안의 등록해둔 서브 관광지
        ArrayList<Map> subPlace = new ArrayList<>();
        // 많이 간 경로
        ArrayList<Map> totalMem = new ArrayList<>();
        // 각 서브 관광지별로 머문 평균 시간
        ArrayList<Map> avgTime = new ArrayList<>();

        // Visualization Instance
        ArrayList<VisualizationInfo> info = new ArrayList<>();

        // 큰 관광지 위치 띄우기
        showPlaceMarker(latitude, longitude, place);

        if(subPlaceMap.size() != 0){
            for(int i = 0; i < subPlaceMap.size(); i++){
                subPlace.add(subPlaceMap.get(i));
            }

        }

        if(totalMemMap.size() != 0){
            for(int i = 0; i < totalMemMap.size(); i++){
                totalMem.add(totalMemMap.get(i));
            }
        }

        if(avgTimeMap.size() != 0){
            for(int i = 0; i < avgTimeMap.size(); i++){
                avgTime.add(avgTimeMap.get(i));
            }
        }

        // totalMem 순으로 저장하기
        for (int i = 0; i < totalMem.size(); i++){
            VisualizationInfo vo = new VisualizationInfo();

            String name = (String) totalMem.get(i).get("name");

            vo.setName(name);
            vo.setLatitude(Double.parseDouble((String) totalMem.get(i).get("latitude")));
            vo.setLongitude(Double.parseDouble((String) totalMem.get(i).get("longitude")));
            vo.setRank(i + 1);


            for(int j = 0; j < subPlace.size(); j++){
                if(name.equals(subPlace.get(j).get("name"))){
                    subPlace.remove(j);
                }
            }


            for(int j = 0; j < avgTime.size(); j++){
                if(name.equals(avgTime.get(j).get("name"))){
                    vo.setAvg((String) avgTime.get(j).get("avg"));
                    avgTime.remove(j);
                }
            }

            info.add(vo);
        }

        // totalMem 에 존재하지 않은 subPlace 저장하기
        for(int i = 0; i < subPlace.size(); i++){
            VisualizationInfo vo = new VisualizationInfo();

            String name = (String) subPlace.get(i).get("name");

            vo.setName(name);
            vo.setLatitude(Double.parseDouble((String) subPlace.get(i).get("latitude")));
            vo.setLongitude(Double.parseDouble((String) subPlace.get(i).get("longitude")));
            vo.setRank(0);

            for(int j = 0; j < avgTime.size(); j++){
                if(name.equals(avgTime.get(j).get("name"))){
                    vo.setAvg((String) avgTime.get(j).get("avg"));
                }
            }

            info.add(vo);
        }

        // 마커 띄우기
        showSubPlaceMarker(info);


        if(totalMem.size() != 0){
            showTotalMemOverlay(totalMem);
        }

    }


    // 큰 관광지에 마커 띄우기
    public void showPlaceMarker(double latitude, double longitude, String placeName){

        // 마커 띄우기
        placeMarker = new MapPOIItem();
        placeMarker.setItemName(placeName);
        placeMarker.setTag(0);
        placeMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude,longitude));
        placeMarker.setMarkerType(MapPOIItem.MarkerType.YellowPin); // 기본 마커
        placeMarker.setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin); // 마커 클릭 시
        mapView.addPOIItem(placeMarker);

        // 화면 중앙에 표시 될 위치
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);

    }

    // 서브 관광지에 마커 띄우기
    public void showSubPlaceMarker(ArrayList<VisualizationInfo> info){

        for(int i = 0; i < info.size(); i++){

            int rank = info.get(i).getRank();

            // 마커 띄우기
            subPlaceMarker = new MapPOIItem();
            if(rank == 0){
                subPlaceMarker.setItemName( "" + "\n" + info.get(i).getName() + "\n" + info.get(i).getAvg());
            }
            else{
                subPlaceMarker.setItemName( rank + "\n" + info.get(i).getName() + "\n" + info.get(i).getAvg());
            }
            subPlaceMarker.setTag(1);
            subPlaceMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(info.get(i).getLatitude(),info.get(i).getLongitude()));
            subPlaceMarker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본 마커
            subPlaceMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커 클릭 시
            mapView.addPOIItem(subPlaceMarker);

        }

    }


    // 많이 간 경로 오버레이 띄우기
    public void showTotalMemOverlay(ArrayList<Map> totalMem){

        polyline = new MapPolyline();
        polyline.setTag(1000);
        polyline.setLineColor(Color.argb(200, 101, 172, 243)); // Polyline 컬러 지정.

        // Polyline 좌표 지정.
        for (int i = 0; i < totalMem.size(); i++){
            double lat = Double.parseDouble((String) totalMem.get(i).get("latitude"));
            double lon = Double.parseDouble((String) totalMem.get(i).get("longitude"));
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(lat, lon));
        }

        // Polyline 지도에 올리기.
        mapView.addPolyline(polyline);

        // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
        int padding = 100;
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
    }

}