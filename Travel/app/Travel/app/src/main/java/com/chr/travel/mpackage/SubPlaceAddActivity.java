package com.chr.travel.mpackage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chr.travel.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;
import service.location.GpsTracker;

/* 매니저가 장소를 추가하는 액티비티 */

public class SubPlaceAddActivity extends AppCompatActivity{

    EditText et_place;
    Button btn_search;

    // 주소
    String search;

    // 위도, 경도
    double latitude, longitude;

    // 지도
    MapView mapView;

    // 마커
    MapPOIItem marker;

    // 가이드가 저장한 장소 마커
    MapPOIItem managerSavePlace;

    GpsTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_sub_place_add);

        gpsTracker = new GpsTracker(SubPlaceAddActivity.this);

        mapView = new MapView(this);

        et_place = findViewById(R.id.et_place);
        btn_search = findViewById(R.id.btn_search);

        // 검색 버튼 클릭 시
        btn_search.setOnClickListener(btn_click);

        // 지도 띄우기
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        if(gpsTracker.getLocation() != null){
            showMarker(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        }

        // 지도 Marker EventListener
        mapView.setPOIItemEventListener(marker_click);

        // 지도 EventListener
        mapView.setMapViewEventListener(map_click);

    }

    // 가이드가 저장한 서브 장소들 위치 띄우기
    @Override
    protected void onResume() {
        super.onResume();

        try {
            AsyncTaskFactory.getApiGetTask(SubPlaceAddActivity.this, API_CHOICE.MANAGER_SHOW_PLACE, "", new AsyncTaskCallBack() {
                @Override
                public void onTaskDone(Object... params) {
                    ArrayList<Map> place = (ArrayList) params[0];

                    for(int i=0;i<place.size();i++) {
                        String name = (String) place.get(i).get("name");
                        double latitude = Double.parseDouble((String) place.get(i).get("latitude"));
                        double longitude = Double.parseDouble((String) place.get(i).get("longitude"));


                        // 마커 띄우기
                        managerSavePlace = new MapPOIItem();
                        managerSavePlace.setItemName(name);
                        managerSavePlace.setTag(1);
                        managerSavePlace.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude,longitude));
                        managerSavePlace.setMarkerType(MapPOIItem.MarkerType.YellowPin);
                        mapView.addPOIItem(managerSavePlace);
                    }
                }
            }).execute();
        }

        catch (Exception e){
            e.printStackTrace();
        }


    }

    /* MapViewEventListener */
    MapView.MapViewEventListener map_click = new MapView.MapViewEventListener() {
        @Override
        public void onMapViewInitialized(MapView mapView) {
            Log.i("MapViewClick", "터치1");
        }

        // 지도 클릭 시 반응
        @Override
        public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
            Log.i("MapViewClick", "터치1");
        }

        // 지도 줌, 아웃 시
        @Override
        public void onMapViewZoomLevelChanged(MapView mapView, int i) {
            Log.i("MapViewClick", "터치2");
        }

        // 지도 클릭 시 반응
        @Override
        public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

            // 전에 클릭한 장소에 마커가 있다면 지우기
            if(marker!=null){
                mapView.removePOIItem(marker);
            }

            // 클릭 한 곳 마커 띄우기
            showMarker(mapPoint.getMapPointGeoCoord().latitude, mapPoint.getMapPointGeoCoord().longitude);

        }

        @Override
        public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
            Log.i("MapViewClick", "터치4");
        }

        @Override
        public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
            Log.i("MapViewClick", "터치5");
        }

        // 지도 클릭 시 반응
        @Override
        public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
            Log.i("MapViewClick", "터치6");
        }

        // 지도 클릭 시 반응
        @Override
        public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
            Log.i("MapViewClick", "터치7");
        }

        // 지도 클릭 시 반응
        // 지도 줌, 아웃 시
        @Override
        public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
            Log.i("MapViewClick", "터치8");
        }
    };


    /* POIItemEventListener */
    MapView.POIItemEventListener marker_click = new MapView.POIItemEventListener() {

        // 마커 클릭 시
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
            // 서버에게 search, latitude, longitude 보내기 위해 Alert 창 띄우기
            if(mapView.getPOIItems().length == 5){
                makeDialog();
            }
        }

        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
            Log.i("Place", "touch2");
        }

        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
            Log.i("Place", "touch3");
        }

        @Override
        public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
            Log.i("Place", "touch4");
        }
    };



    // 버튼 클릭 이벤트
    View.OnClickListener btn_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                // 검색 버튼 클릭 시
                case R.id.btn_search:

                    // 전에 검색한 장소에 머커가 있다면 지우기
                    if(marker!=null){
                        mapView.removePOIItem(marker);
                    }

                    search = et_place.getText().toString();

                    // 검색 창이 비어있다면
                    if(search == null || search.isEmpty()){
                        Toast.makeText(SubPlaceAddActivity.this, "장소를 검색해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 장소 검색을해서 위도, 경도 찾기
                    getLocation(search);

                    // 마커 띄우기
                    showMarker(latitude, longitude);

                    et_place.setText(null);

                    break;
            }
        }
    };



    // 주소 이름을 통해 위도 경도 받기
    public void getLocation(String search){
        try {
            Geocoder geocoder = new Geocoder(this, Locale.KOREA);
            List<Address> addresses = geocoder.getFromLocationName(search,1);
            if (addresses.size() >0) {
                Address bestResult = (Address) addresses.get(0);
                latitude =  bestResult.getLatitude();
                longitude =  bestResult.getLongitude();
            }
        } catch (IOException e) {
            Log.e(getClass().toString(),"Failed in using Geocoder.", e);
            return;
        }
    }



    // 해당 주소에 마커 띄우기
    public void showMarker(double latitude, double longitude){
        // 마커 띄우기
        marker = new MapPOIItem();
        marker.setItemName(search);
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude,longitude));
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본 마커
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커 클릭 시
        mapView.addPOIItem(marker);

        // 화면 중앙에 표시 될 위치
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
    }


    /* Alert창 */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void makeDialog() {
        // 장소 이름 지정하는 EditText
        EditText et = new EditText(SubPlaceAddActivity.this);
        et.setTextColor(Color.WHITE);
        et.setHint("     장소 이름 지정해주세요(터치)\n     장소 이름을 지정하지 않으면 '" + search + "' 로 저장됨");
        et.setHintTextColor(Color.WHITE);
        et.setTextCursorDrawable(null);
        et.setTextSize((float) 14.4);
        et.setBackgroundTintList(ColorStateList.valueOf(Color.argb(0,0,0,0)));


        AlertDialog.Builder dialog = new AlertDialog.Builder(SubPlaceAddActivity.this);
        dialog.setTitle(Html.fromHtml("<b><font color='#FFFFFF'>" + search + "</font></b>"));
        dialog.setMessage(Html.fromHtml("<font color='#FFFFFF'>장소를 저장하시겠습니까?</font>"));
        // EditText 추가
        dialog.setView(et);


        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String place = et.getText().toString();

                // 장소 이름 지정하지 않았으면 검색 한 이름으로 장소 저장
                if(place == null || place.isEmpty()){
                    place = search.trim();
                }

                JSONObject postDataParam = new JSONObject();

                try {
                    // node에 전달 할 정보 넣기
                    postDataParam.put("name", place);
                    postDataParam.put("latitude", latitude);
                    postDataParam.put("longitude", longitude);
                }
                catch (JSONException e) {
                    Log.e("PlaceAddActivity", "JSONEXception");
                }

                // 서버 통신
                try {
                    AsyncTaskFactory.getApiPostTask(SubPlaceAddActivity.this, API_CHOICE.MANAGER_ADD_PLACE, null).execute(postDataParam);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        dialog.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
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
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
                alert.setIcon(R.drawable.img_alert);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#65acf3")));
            }
        });

        alert.show();
    }

}