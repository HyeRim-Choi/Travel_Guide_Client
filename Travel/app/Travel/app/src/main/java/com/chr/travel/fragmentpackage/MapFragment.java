package com.chr.travel.fragmentpackage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chr.travel.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;


public class MapFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragmentpackage_map, container, false);

        double x = 37.7285214;
        double y = 126.7349748;

        MapView mapView = new MapView(getActivity());

        ViewGroup mapViewContainer = (ViewGroup) v.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);


        /* Marker */
        //MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(37.7285214, 126.73497484);
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("ny");
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(x, y));
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView.addPOIItem(marker);

        /* Overlay */
        /*MapPolyline polyline = new MapPolyline();
        polyline.setTag(1000);
        polyline.setLineColor(Color.argb(200, 255, 51, 0)); // Polyline 컬러 지정.

        // Polyline 좌표 지정.
        // 부산 남포동
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(35.0967055, 129.0282998)); // 부산 자갈치시장
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(35.1006536,129.0304339)); // 부산 용두산 공원
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(35.1012187,129.0301323)); // 부산타워
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(35.101276,129.0235424)); // 부산 깡통시장

        // Polyline 지도에 올리기.
        mapView.addPolyline(polyline);

        MapPolyline polyline2 = new MapPolyline();
        polyline2.setTag(1000);
        polyline2.setLineColor(Color.argb(200, 0, 255, 51)); // Polyline 컬러 지정.

        // Polyline 좌표 지정.
        // 부산 남포동
        polyline2.addPoint(MapPoint.mapPointWithGeoCoord(35.1012922, 129.0303871)); // 부산 투썸
        polyline2.addPoint(MapPoint.mapPointWithGeoCoord(35.0990262,129.0254763)); // 부산 남포문고
        polyline2.addPoint(MapPoint.mapPointWithGeoCoord(35.1012187,129.0301323)); // 부산타워
        polyline2.addPoint(MapPoint.mapPointWithGeoCoord(35.101276,129.0235424)); // 부산 깡통시장

        // Polyline 지도에 올리기.
        mapView.addPolyline(polyline2);

        MapPolyline polyline3 = new MapPolyline();
        polyline3.setTag(1000);
        polyline3.setLineColor(Color.argb(180, 0, 0, 255)); // Polyline 컬러 지정.

        // Polyline 좌표 지정.
        // 부산 남포동
        polyline3.addPoint(MapPoint.mapPointWithGeoCoord(35.1038363,129.0330651)); // 부산 거리
        polyline3.addPoint(MapPoint.mapPointWithGeoCoord(35.0990262,129.0254763)); // 부산 남포문고
        polyline3.addPoint(MapPoint.mapPointWithGeoCoord(35.1012187,129.0301323)); // 부산타워
        polyline3.addPoint(MapPoint.mapPointWithGeoCoord(35.101276,129.0235424)); // 부산 깡통시장

        // Polyline 지도에 올리기.
        mapView.addPolyline(polyline3);

        // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
        int padding = 100; // px
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));*/


        return v;
    }
}