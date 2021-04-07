package api.callback;

import android.util.Log;

import com.chr.travel.LocationAccessActivity;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import api.background.BackLocationRequest;
import vo.LoginVO;

public class LocationCallback extends com.google.android.gms.location.LocationCallback {

    double longitude, latitude;

    LoginVO vo;

    @Override
    public void onLocationResult(LocationResult locationResult) {
        super.onLocationResult(locationResult);

        vo = LoginVO.getInstance();

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
            postDataParam.put("latitude", latitude);
            postDataParam.put("longitude", longitude);
            postDataParam.put("date", time.substring(0,10));
            postDataParam.put("time", time.substring(11));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            new BackLocationRequest(new AsyncTaskCallBack() {
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
        Log.i("LocationCallback", "onLocationAvailability - " + locationAvailability);
    }
}
