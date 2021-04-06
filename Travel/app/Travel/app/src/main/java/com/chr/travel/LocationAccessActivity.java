package com.chr.travel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import api.API_CHOICE;
import api.AsyncTaskFactory;
import api.callback.AsyncTaskCallBack;
import vo.LoginVO;

public class LocationAccessActivity extends AppCompatActivity {
    private static final String TAG = LocationAccessActivity.class.getSimpleName();

    private static final int GPS_UTIL_LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final int GPS_UTIL_LOCATION_RESOLUTION_REQUEST_CODE = 101;

    public static final int DEFAULT_LOCATION_REQUEST_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;
    public static final long DEFAULT_LOCATION_REQUEST_INTERVAL = 300000; // 300000
    public static final long DEFAULT_LOCATION_REQUEST_FAST_INTERVAL = 200000; // 200000

    //현재 위치를 가져오는 객체
    private FusedLocationProviderClient fusedLocationProviderClient;
    //위치 설정하는 객체
    private LocationRequest locationRequest;
    //위도, 경도
    double longitude, latitude;

    LoginVO vo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_access);

        vo = LoginVO.getInstance();

        checkLocationPermission();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("LocationAccessActivity", "----Pause-----");
        checkLocationPermission();
    }

    // 위치 정보를 접근할 수 있는 권한을 확인
    private void checkLocationPermission() {
        int accessForeLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int accessBackLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        if (accessForeLocation == PackageManager.PERMISSION_GRANTED && accessBackLocation == PackageManager.PERMISSION_GRANTED) {
            checkLocationSetting();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GPS_UTIL_LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    // 위치 정보에 접근할 수 있는 권한 설정
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == GPS_UTIL_LOCATION_PERMISSION_REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                // 여러개의 권한 확인
                if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[i])) {
                    // 위치 정보 접근 권한을 허락했다면
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        checkLocationSetting();
                    } else { // 위치 정보 권한을 거부했다면
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
                        builder.setTitle("위치 권한이 꺼져있습니다.");
                        builder.setMessage("[권한] 설정에서 위치 권한을 허용해야 합니다.");
                        builder.setPositiveButton("설정으로 가기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent); // 설정화면으로 이동
                            }
                        }).setNegativeButton("종료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        androidx.appcompat.app.AlertDialog alert = builder.create();
                        alert.show();
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPS_UTIL_LOCATION_RESOLUTION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) { // 위치 서비스를 활성화했다면
                checkLocationSetting();
            } else { // 위치 서비스 활성화하지 않았다면
                finish(); // 앱 종료
            }
        }
    }

    // 안드로이드 기기에 위치 서비스가 활성화 되어있는지 확인
    private void checkLocationSetting() {
        locationRequest = LocationRequest.create();
        // 어느 수준으로 위치 정보가 필요하다라고 locationRequest를 설정
        locationRequest.setPriority(DEFAULT_LOCATION_REQUEST_PRIORITY);
        locationRequest.setInterval(DEFAULT_LOCATION_REQUEST_INTERVAL);
        locationRequest.setFastestInterval(DEFAULT_LOCATION_REQUEST_FAST_INTERVAL);
        // 안드로이드에서 내가 설정한 위치 정보 수준을 가져올 수 있는지 확인
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest).setAlwaysShow(true);
        settingsClient.checkLocationSettings(builder.build())
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) { // OK일 때
                        // 위치 서비스에 접근할 수 있는 API를 이용해서 정보를 받을 것이다(구글에서 제공하는 API)
                        // 현재 위치를 가져 올 수있는 위치 서비스
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(LocationAccessActivity.this);
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    }
                })
                .addOnFailureListener(LocationAccessActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) { // Fail일 때(보통 안드로이드 기기에 위치 서비스가 활성화되어있지 않았을 때)
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED: // 우리가 설정을 통해서 문제 해결이 가능한 경우
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(LocationAccessActivity.this, GPS_UTIL_LOCATION_RESOLUTION_REQUEST_CODE);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.w(TAG, "unable to start resolution for result due to " + sie.getLocalizedMessage());
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE: // 우리가 설정을 통해서 문제 해결이 불가능한 경우
                                String errorMessage = "location settings are inadequate, and cannot be fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                        }
                    }
                });
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            longitude = locationResult.getLastLocation().getLongitude();
            latitude = locationResult.getLastLocation().getLatitude();
            //현재 리스너를 제거(리스너를 제거하지 않으면 설정한 시간마다 onLocationResult가 호출되어 위치 정보가 전달된다)
            //fusedLocationProviderClient.removeLocationUpdates(locationCallback);

            // delete
            Log.i("test", "lat : " + (int)(latitude*100)/100.0);
            Log.i("test", "long : " + (int)(longitude*100)/100.0);


            // 30초마다 서버로 위치 정보 전달
            SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String time = format.format(date);
            Log.i("LocationAccessActivity", time);

            //  서버에 아이디, 위치 전송
            JSONObject postDataParam = new JSONObject();

            try {
                postDataParam.put("userId", vo.getUserId());
                postDataParam.put("latitude", (int)(latitude*100)/100.0);
                postDataParam.put("longitude", (int)(longitude*100)/100.0);
                postDataParam.put("date", time.substring(0,10));
                postDataParam.put("time", time.substring(11));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                AsyncTaskFactory.getApiPostTask(LocationAccessActivity.this, API_CHOICE.LOCATION_SEND, new AsyncTaskCallBack() {
                    @Override
                    public void onTaskDone(Object... params) {
                        if((Integer)params[0] == 1){
                            Log.i("NotificationActivity", "위치 보내기 성공");
                        }
                    }
                }).execute(postDataParam);
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
            super.onLocationAvailability(locationAvailability);
            Log.i(TAG, "onLocationAvailability - " + locationAvailability);
        }
    };


}