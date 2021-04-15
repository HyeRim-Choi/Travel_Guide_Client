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

import java.util.ArrayList;
import java.util.Map;

/* Map을 띄워주는 Fragment */

public class MapTravelerLocationFragment extends Fragment {

    // 멤버 위치 정보
    ArrayList<Map> travelerLocation;

    // 지도 마커
    MapPOIItem marker;

    // 지도
    MapView mapView;

    // userId의 수
    int cnt = 0;
    // latitude의 합
    double sumIat = 0.0;
    // longitude의 합
    double sumLong = 0.0;

    public MapTravelerLocationFragment(ArrayList<Map> travelerLocation) {
        this.travelerLocation = travelerLocation;
        cnt = travelerLocation.size();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragmentpackage_map_traveler_location, container, false);

        mapView = new MapView(getActivity());

        // 지도 띄우기
        ViewGroup mapViewContainer = (ViewGroup) v.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        for(int i=0;i<travelerLocation.size();i++) {
            String userId = (String) travelerLocation.get(i).get("userId");
            double latitude = Double.parseDouble((String) travelerLocation.get(i).get("latitude"));
            double longitude = Double.parseDouble((String) travelerLocation.get(i).get("longitude"));

            sumIat+=latitude;
            sumLong+=longitude;

            // 마커 띄우기
            marker = new MapPOIItem();
            marker.setItemName(userId);
            marker.setTag(0);
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude,longitude));
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            mapView.addPOIItem(marker);
        }

        // 화면 중앙에 표시 될 위치
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(sumIat/cnt, sumLong/cnt), true);

        return v;
    }
}