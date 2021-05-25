package com.chr.travel;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PathOverlay;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity/* implements OnMapReadyCallback*/{

    // 지도
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = new MapView(this);

        // 지도 띄우기
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);


        MapPolyline polyline = new MapPolyline();
        polyline.setTag(1000);
        polyline.setLineColor(Color.argb(128, 255, 51, 0)); // Polyline 컬러 지정.

// Polyline 좌표 지정.
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.57386246445046, 126.98178901030693));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.57400497726675,126.98248800934404));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.57396539518232,126.9827689311002));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.5739554996576,126.98290939200922));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.57382685771628,126.98292187742334));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.57380954051489,126.98291875606984));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.57440058216743,126.98479214222259));

// Polyline 지도에 올리기.
        mapView.addPolyline(polyline);

// 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
        int padding = 100; // px
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
    }

   /* private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        naverMapBasicSettings();

    }


    public void naverMapBasicSettings() {
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(final NaverMap naverMap) {

        // 마커띄우가, 색바꾸기, 크기 지정
        Marker marker = new Marker();
        marker.setPosition(new LatLng(37.5670135, 126.9783740));
        marker.setMap(naverMap);
        marker.setIconTintColor(Color.RED);
        marker.setWidth(50);
        marker.setHeight(80);
        marker.setCaptionText("Hello");


        PathOverlay path = new PathOverlay();
        path.setCoords(Arrays.asList(
                new LatLng(37.57152, 126.97714),
                new LatLng(37.56607, 126.98268),
                new LatLng(37.56445, 126.97707),
                new LatLng(37.55855, 126.97822)
        ));
        path.setMap(naverMap);
        //path.setPatternImage(OverlayImage.fromResource(R.drawable.path_pattern));
        path.setPatternInterval(10);

    }*/
}

