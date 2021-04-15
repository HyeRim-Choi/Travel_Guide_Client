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

public class MapFragment extends Fragment {

    // 멤버 위치 정보
    ArrayList<Map> travelerLocation;

    // 지도 마커
    MapPOIItem marker;

    public MapFragment(ArrayList<Map> travelerLocation) {
        this.travelerLocation = travelerLocation;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragmentpackage_map, container, false);

        MapView mapView = new MapView(getActivity());

        ViewGroup mapViewContainer = (ViewGroup) v.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        for(int i=0;i<travelerLocation.size();i++) {
            String userId = (String) travelerLocation.get(i).get("userId");
            double latitude = (double) travelerLocation.get(i).get("latitude");
            double longitude = (double) travelerLocation.get(i).get("longitude");

            // 마커 띄우기
            marker = new MapPOIItem();
            marker.setItemName(userId);
            marker.setTag(0);
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude,longitude));
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            mapView.addPOIItem(marker);
        }


        /* Marker */
        // 이걸로 지도 터치 시 마커띄워서 텓스트 띄우기
        //MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(37.7285214, 126.73497484);





        return v;
    }
}