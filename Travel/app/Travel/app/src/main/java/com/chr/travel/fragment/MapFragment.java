package com.chr.travel.fragment;

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
        View v =  inflater.inflate(R.layout.fragment_map, container, false);

        double x = 37.7285214;
        double y = 126.7349748;

        MapView mapView = new MapView(getActivity());

        ViewGroup mapViewContainer = (ViewGroup) v.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        //MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(37.7285214, 126.73497484);

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("ny");
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(x, y));
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);

        //지도 보여주기 코드
        /*Intent implicit_intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("geo:" + x + "," + y));

        if (implicit_intent != null){
            startActivity(implicit_intent);
        }*/

        return v;
    }
}