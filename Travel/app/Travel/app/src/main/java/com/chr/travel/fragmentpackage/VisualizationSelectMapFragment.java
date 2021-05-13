package com.chr.travel.fragmentpackage;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chr.travel.R;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.Map;


public class VisualizationSelectMapFragment extends Fragment {


    /*private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;*/

    // 지도
    MapView mapView;
    // 서브 관광지(자유시간 장소) 마커
    MapPOIItem subPlaceMarker;
    // 자유시간이 있는 큰 관광지 마커
    MapPOIItem placeMarker;

    // 시각화 될 정보
    Map<String, ArrayList> visualInfo;
    // 자유 시간을 갖는 관광지(큰 관광지)
    String place;
    double latitude, longitude;

    // 각 서브 관광지별로 머문 평균 시간
    ArrayList<Map> avgTime;

    // 지도가 올라가는 레이아웃
    ViewGroup mapViewContainer;

    public VisualizationSelectMapFragment(String place, double latitude, double longitude, Map<String, ArrayList> visualInfo) {
        // subPlace, totalMem, avgTime 정보가 없다면
        if(visualInfo != null){
            Log.i("VisualMapFragment", "subPlace, totalMem, avgTime 정보 들어옴");
            this.visualInfo = visualInfo;
        }

        this.place = place;
        this.latitude = latitude;
        this.longitude = longitude;
    }

   /* public static VisualizationSelectMapFragment newInstance(String param1, String param2) {
        VisualizationSelectMapFragment fragment = new VisualizationSelectMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmentpackage_visualization_map, container, false);


        // 지도 띄우기
        mapViewContainer = (ViewGroup) v.findViewById(R.id.map_view);


        mapView = new MapView(getActivity());
        mapViewContainer.addView(mapView);


        // subPlace, totalMem, avgTime 데이터가 없으면 큰 관광지만 지도에 띄우기
        if(visualInfo == null){
            Log.i("bigPlace : " , "lat : " + latitude + "long : " + longitude);
            // 큰 관광지 위치 띄우기
            showPlaceMarker(latitude, longitude, place);
            return v;
        }

        // 큰 관광지(한성대) 안의 등록해둔 서브 관광지
        ArrayList<Map> subPlace = visualInfo.get("subPlace");
        // 많이 간 경로
        ArrayList<Map> totalMem = visualInfo.get("totalMem");
        // 각 서브 관광지별로 머문 평균 시간
        avgTime = visualInfo.get("avgTime");


        // 큰 관광지 위치 띄우기
        showPlaceMarker(latitude, longitude, place);

        // 서브 관광지 위치(자유시간 장소 종류) 띄우기
        for (int i = 0; i < subPlace.size(); i++){
            double lat = Double.parseDouble((String) subPlace.get(i).get("latitude"));
            double lon = Double.parseDouble((String) subPlace.get(i).get("longitude"));
            String subPlaceName = (String) subPlace.get(i).get("name");
            String subPlaceAvgTime = (String) avgTime.get(i).get("avg");
            showSubPlaceMarker(lat, lon, subPlaceName, subPlaceAvgTime);
        }

        // 많이 간 경로 오버레이해서 띄우기
        showTotalMemOverlay(totalMem);



        return v;
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
    public void showSubPlaceMarker(double latitude, double longitude, String subPlaceName, String subPlaceAvgTime){
        // 마커 띄우기
        subPlaceMarker = new MapPOIItem();
        subPlaceMarker.setItemName(subPlaceName + "\n" + subPlaceAvgTime);
        subPlaceMarker.setTag(0);
        subPlaceMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude,longitude));
        subPlaceMarker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본 마커
        subPlaceMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커 클릭 시
        mapView.addPOIItem(subPlaceMarker);

    }


    // 많이 간 경로 오버레이 띄우기
    public void showTotalMemOverlay(ArrayList<Map> totalMem){
        MapPolyline polyline = new MapPolyline();
        polyline.setTag(1000);
        polyline.setLineColor(Color.argb(128, 255, 51, 0)); // Polyline 컬러 지정.

        // Polyline 좌표 지정.
        for (int i = 0; i < totalMem.size(); i++){
            double lat = Double.parseDouble((String) totalMem.get(i).get("latitude"));
            double lon = Double.parseDouble((String) totalMem.get(i).get("longitude"));
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(lat, lon));
        }

        // Polyline 지도에 올리기.
        mapView.addPolyline(polyline);

        // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
        // ** 확인 후 수정
        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
        int padding = 100; // px
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.i("VisualMapFrag2", "onPause()");

        mapViewContainer.removeView(mapView);
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.i("VisualMapFrag2", "onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("VisualMapFrag2", "onDestroy()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.i("VisualMapFrag2", "onDestroyView()");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        Log.i("VisualMapFrag2", "onDetach()");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i("VisualMapFrag2", "onResume()");
    }
}