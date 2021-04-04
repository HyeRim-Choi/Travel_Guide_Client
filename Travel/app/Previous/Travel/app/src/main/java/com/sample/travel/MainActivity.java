package com.sample.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static double latitudePrev;
    static double longitudePrev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // GpsSettingActivity에서 얻어 온 위도, 경도 값
        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("latitude", 0);
        double longitude = intent.getDoubleExtra("longitude", 0);

        TextView location = findViewById(R.id.location);
        location.setText("위도=" + latitude + ", 경도=" + longitude);

        // 같은 위치인지 확인하기 위해 소수점 둘째자리까지 남기고 자르기
        double latitudeDeci = (int)(latitude*100)/100.0;
        double longitudeDeci = (int)(longitude*100)/100.0;

        // 같은 위치이면 Alert창 띄워서 DB에 저장하기
        if(latitudeDeci == latitudePrev && longitudeDeci == longitudePrev){
            location.setText("위도=" + latitude + ", 경도=" + longitude + "\n" + "위도=" + latitudePrev + ", 경도=" + longitudePrev + "/n 같은 위치입니다");

            // 현재 위치의 위도, 경도 값을 주소로 변경
            Geocoder g = new Geocoder(this);
            List<Address> address = null;
            try {
                address = g.getFromLocation(latitude,longitude,10);
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("test","입출력 오류");
            }

            if(address!=null){
                if(address.size()==0){
                    Log.i("test","주소찾기 오류");
                }else{
                    // address.get(0).getAddressLine(0) -> 주소
                    Log.i("test",address.get(0).getAddressLine(0));
                    location.setText("위도=" + latitude + ", 경도=" + longitude + "\n" + "위도=" + latitudePrev + ", 경도=" + longitudePrev + "\n" + "주소 : " + address.get(0).getAddressLine(0));
                }
            }


        }
        else{
            location.setText("위도=" + latitude + ", 경도=" + longitude + "\n" + "위도=" + latitudePrev + ", 경도=" + longitudePrev + "/n 다른 위치입니다");
        }

        latitudePrev = latitudeDeci;
        longitudePrev = longitudeDeci;


    }
}
