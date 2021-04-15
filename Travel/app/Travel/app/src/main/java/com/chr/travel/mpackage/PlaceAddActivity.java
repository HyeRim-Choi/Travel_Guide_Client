package com.chr.travel.mpackage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chr.travel.R;
import com.chr.travel.login.FindIdActivity;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import api.API_CHOICE;
import api.AsyncTaskFactory;

/* 매니저가 장소를 추가하는 액티비티 */

// MapView.MapViewEventListener : 나연이가 클릭 위치 클릭 시 텍스트 입력 받는 그거 해달라고 하면 추가하기

public class PlaceAddActivity extends AppCompatActivity{

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_place_add);

        mapView = new MapView(this);

        et_place = findViewById(R.id.et_place);
        btn_search = findViewById(R.id.btn_search);

        // 검색 버튼 클릭 시
        btn_search.setOnClickListener(btn_click);

        // 지도 띄우기
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        // 지도 Marker EventListener
        mapView.setPOIItemEventListener(marker_click);

        // 지도 EventListener
        //mapView.setMapViewEventListener(this);

    }

    /* POIItemEventListener */
    MapView.POIItemEventListener marker_click = new MapView.POIItemEventListener() {

        // 마커 클릭 시
        @Override
        public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
            // 서버에게 search, latitude, longitude 보내기 위해 Alert 창 띄우기
            makeDialog();
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

                    if(marker!=null){
                        mapView.removePOIItem(marker);
                    }

                    search = et_place.getText().toString();

                    // 검색 창이 비어있다면
                    if(search == null || search.isEmpty()){
                        Toast.makeText(PlaceAddActivity.this, "장소를 검색해주세요", Toast.LENGTH_SHORT).show();
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
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양
        mapView.addPOIItem(marker);

        // 화면 중앙에 표시 될 위치
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
    }

    /* Alert창 */
    private void makeDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(PlaceAddActivity.this);
        // 가운데로 안오면 글씨를 배경과 같은 색으로 해서 좀 집어넣기
        dialog.setTitle(Html.fromHtml("<b><font color='#FFFFFF'>" + search + "</font></b>"));
        dialog.setMessage(Html.fromHtml("<font color='#FFFFFF'>장소를 저장하시겠습니까?</font>"));

        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                JSONObject postDataParam = new JSONObject();

                try {
                    // node에 전달 할 정보 넣기
                    postDataParam.put("name", search.trim());
                    postDataParam.put("latitude", latitude);
                    postDataParam.put("longitude", longitude);
                }
                catch (JSONException e) {
                    Log.e("PlaceAddActivity", "JSONEXception");
                }

                // 서버 통신
                try {
                    AsyncTaskFactory.getApiPostTask(PlaceAddActivity.this, API_CHOICE.MANAGER_ADD_PLACE, null).execute(postDataParam);
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


    /* MapViewEventListener */
   /* @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    // 지도 클릭 시 반응
    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
        // 클릭 한 곳 마커 띄우기
        showMarker(mapPoint.getMapPointGeoCoord().latitude, mapPoint.getMapPointGeoCoord().longitude);

    }

    // 지도 줌, 아웃 시
    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    // 지도 클릭 시 반응
    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    // 지도 클릭 시 반응
    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    // 지도 클릭 시 반응
    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    // 지도 클릭 시 반응
    // 지도 줌, 아웃 시
    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }*/
}