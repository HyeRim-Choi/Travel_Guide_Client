package api.background;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import api.API_CHOICE;
import api.callback.AsyncTaskCallBack;
import service.location.GpsTracker;

/* 위치를 백그라운드에서도 전달하는 서버 통신 */

public class BackLocationRequest extends AsyncTask<JSONObject, Void, String> {
    public final int chk;
    Activity activity;
    URL url;
    String info;
    static boolean flag;
    AsyncTaskCallBack callBack;
    double latitude, longitude;

    // 서버로부터 서버 통신 종료 후 받는 JSON 형식의 데이터
    JSONObject jsonObject;

    // 위치 가져오는 class
    GpsTracker gpsTracker;


    public BackLocationRequest(Activity activity, String info, boolean flag, AsyncTaskCallBack callBack) {
        this.activity = activity;
        this.chk = API_CHOICE.LOCATION_SEND;
        this.info = info;
        this.flag = flag;
        Log.i("flag11 : ", ""+flag);
        this.callBack = callBack;

        if(gpsTracker == null){
            gpsTracker = new GpsTracker(activity);
        }
    }


    @Override
    protected void onPreExecute() {
        String serverURLStr = api.UrlCreate.postUrl(chk);
        Log.i("BackLocationRequest", "111");
        try {
            url = new URL(serverURLStr);
            Log.i("test", "url : " + url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("WrongThread")
    @Override
    protected String doInBackground(JSONObject... postDataParams) {

        if(flag == false){
            cancel(true);
        }

        while(flag)
        {
            try{

                if (isCancelled()){
                    Log.i("isCancelled", "come in");
                    break;
                }

                Log.i("BackLocationRequest", "222");
                gpsTracker.getLocation();
                latitude = gpsTracker.getLatitude(); // 위도
                longitude = gpsTracker.getLongitude(); //경도

                // 연결
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(1000000 /* milliseconds */);
                conn.setConnectTimeout(1000000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // 서버에 data 전송
                JSONObject postDataParam = new JSONObject();

                SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String time = format.format(date);

                try {
                    postDataParam.put("userId", info);
                    postDataParam.put("latitude", latitude);
                    postDataParam.put("longitude", longitude);
                    postDataParam.put("date", time.substring(0,10));
                    postDataParam.put("time", time.substring(11));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                String str = getPostDataString(postDataParam);
                Log.e("params", "Post String = " + str);
                writer.write(str);

                writer.flush();
                writer.close();
                os.close();

                String http = Integer.toString(HttpURLConnection.HTTP_OK);
                Log.i("test","http : " + http);

                int responseCode = conn.getResponseCode();
                Log.i("test",""+responseCode);


            }

            catch (Exception e) {
                e.printStackTrace();
            }

            try
            {
                if (isCancelled()){
                    flag = false;
                }

                Log.i("flag", ""+flag);
                Log.i("BackLocationRequest", "333");
                Thread.sleep(30000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }

        return null;

    }

    // 백그라운드 종료 후 돌아오는 곳
    @Override
    protected void onPostExecute(String jsonString) {
        Log.i("response", "res : " + jsonString);
        if (jsonString == null)
            return;

        try {
            Log.i("BackLocationRequest", "444");
            jsonObject = new JSONObject(jsonString);
            String result = jsonObject.getString("approve");
            resultResponse(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void resultResponse(String result){
        switch (result) {
            // 여행객 위치 보내기 종료 시
            case "ok":
                Toast.makeText(activity, "위치 보내기를 종료 했습니다", Toast.LENGTH_SHORT).show();
                break;

            // 여행객 위치 보내기 종료 실패 시
            default :
                Toast.makeText(activity, "위치 보내기를 종료하지 못했습니다", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    // 서버한테 보내는 데이터 형식 만들기
    private String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }

        Log.i("test_res",result.toString());

        return result.toString();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        flag = false;
        callBack.onTaskDone(1);
    }


}
