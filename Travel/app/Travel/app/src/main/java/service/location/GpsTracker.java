package service.location;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.text.Html;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.chr.travel.R;

/* 위치 가져오기 코드 */

public class GpsTracker extends Service implements LocationListener {
    private final Context mContext;
    private Activity activity;

    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    private static final int GPS_UTIL_LOCATION_PERMISSION_REQUEST_CODE = 100;

    Location location;
    protected LocationManager locationManager;

    public GpsTracker(Context context) {
        this.mContext = context;
        this.activity = (Activity) context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            // GPS와 네트워크가 꺼져있다면(설정 화면)
            if (!isGPSEnabled && !isNetworkEnabled) {
                // 코드 작동되는지 확인 후 지우기
                /*AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setMessage("위치 서비스를 사용할 수 없습니다 \n GPS와 네트워크를 켜주세요");

                // 설정 화면으로 이동
                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(myIntent);
                    }
                });

                // 설정 화면으로 이동 거부
                dialog.setNegativeButton("거부", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        return;
                    }
                });
                dialog.show();*/

                // Gps 켜달라는 알림창을 띄움
                makeDialog();
            }

            // GPS와 네트워크가 켜져있다면
            else {
                int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
                int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
                int hasBackLocationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_BACKGROUND_LOCATION);

                // GPS 권한 허가가 안되어있다면
                if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED || hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED || hasBackLocationPermission != PackageManager.PERMISSION_GRANTED) {
                    // 허용을 누르지 않았으면 그 다음 행동 생각해서 코드 추가하기
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, GPS_UTIL_LOCATION_PERMISSION_REQUEST_CODE);
                }


                // 네트워크가 켜져있다면
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                // GPS가 켜져있다면
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }

        catch (Exception e) {
            Log.d("GPSTracker", "" + e.toString());
        }

        return location;
    }

    // 위도 가져오기
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    // 경도 가져오기
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }


    // 위치 가져오기 종료
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GpsTracker.this);
        }
    }


    /* Alert창 */
    private void makeDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        // 가운데로 안오면 글씨를 배경과 같은 색으로 해서 좀 집어넣기
        dialog.setMessage(Html.fromHtml("<font color='#FFFFFF'>위치 서비스를 사용할 수 없습니다 \n GPS와 네트워크를 켜주세요</font>"));

        // 설정 화면으로 이동
        dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent myIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(myIntent);
                dialog.cancel();
            }
        });

        // 설정 화면으로 이동 거부
        dialog.setNegativeButton("거부", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                activity.finish();
            }
        });


        AlertDialog alert = dialog.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
                alert.setIcon(R.drawable.img_alert);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#65acf3")));
            }
        });

        alert.show();
    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}
