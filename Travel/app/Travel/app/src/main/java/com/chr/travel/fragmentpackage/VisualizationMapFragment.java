package com.chr.travel.fragmentpackage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chr.travel.R;

import net.daum.mf.map.api.MapView;

/* 많이 간 경로를 보여주는 프래그먼트 */

public class VisualizationMapFragment extends Fragment {

    // 지도
    MapView mapView;


    public VisualizationMapFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View v = inflater.inflate(R.layout.fragmentpackage_visualization_map, container, false);

        mapView = new MapView(getActivity());

        // 지도 띄우기
        ViewGroup mapViewContainer = (ViewGroup) v.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);


        // 서버와 통신하여 데이터 가져와서 처리


       return v;
    }
}