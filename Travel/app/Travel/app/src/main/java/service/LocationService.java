package service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;


public class LocationService extends Service {


    /*private IBinder mIBinder = new MyBinder();


    public class MyBinder extends Binder {
        // 서비스 객체를 리턴
        public LocationService getService(){
            return LocationService.this;
        }
    }

    public void location(){
        Intent i = new Intent(getApplicationContext(), LocationAccessActivity.class);
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }*/


    @Override
    public void onCreate() {
        Log.e("LOG", "onCreate()");
        super.onCreate();
    }

   /* @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("LOG", "서비스 onBind에서 시작");
        return mIBinder;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("LOG", "onUnbind()");
        return super.onUnbind(intent);
    }*/

    // 서비스 종료 이벤트 함수
    @Override
    public void onDestroy() {
        Log.e("LOG", "onDestroy()");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*@Override
    public void unbindService(ServiceConnection conn) {
        try{
            super.unbindService(conn);
        }
        catch (IllegalArgumentException e){

        }
    }*/

    // 서비스 시작 이벤트 함수
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.e("LOG", "onStartCommand()");
        return START_STICKY;
    }


}
