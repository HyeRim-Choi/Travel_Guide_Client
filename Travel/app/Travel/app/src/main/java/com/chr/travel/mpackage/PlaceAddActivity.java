package com.chr.travel.mpackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chr.travel.R;
import com.chr.travel.fragmentpackage.MapFragment;

import net.daum.mf.map.api.MapView;
import net.daum.mf.map.api.MapPoint;

public class PlaceAddActivity extends AppCompatActivity {

    EditText et_place;
    Button btn_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_place_add);

        et_place = findViewById(R.id.et_place);
        btn_search = findViewById(R.id.btn_search);

        MapView mapView = new MapView(this);

        RelativeLayout mapViewContainer = (RelativeLayout) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);




    }
}