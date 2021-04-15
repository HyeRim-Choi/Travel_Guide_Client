package api.background;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.chr.travel.mpackage.GroupActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import api.API_CHOICE;
import api.callback.AsyncTaskCallBack;
import service.location.GpsTracker;

/* 위치를 백그라운드에서도 전달하는 서버 통신 */

public class BackLocationRequest extends AsyncTask<JSONObject, Void, String> {
    public final int chk;
    AsyncTaskCallBack callBack;
    Activity activity;
    URL url;
    String info;
    double latitude, longitude;

    // 서버로부터 서버 통신 종료 후 받는 JSON 형식의 데이터
    JSONObject jsonObject;
    // 서버와의 통신 잘 마쳤는지 판단하는 chk
    int post_res_chk = 0;
    // 서비스 시작인지 종료인지 구분하기 위한 chk
    int serviceChk;

    boolean flag = false;

    // 위치 가져오는 class
    GpsTracker gpsTracker;


    public BackLocationRequest(Activity activity, String info, int serviceChk, AsyncTaskCallBack callBack) {
        this.activity = activity;
        this.chk = API_CHOICE.LOCATION_SEND;
        this.callBack = callBack;
        this.info = info;
        this.serviceChk = serviceChk;
    }


    @Override
    protected void onPreExecute() {
        String serverURLStr = api.UrlCreate.postUrl(chk);
        gpsTracker = new GpsTracker(activity);
        Log.i("BackLocationRequest", "111");
        try {
            url = new URL(serverURLStr);
            Log.i("test", "url : " + url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        flag = true;
    }


    @Override
    protected String doInBackground(JSONObject... postDataParams) {

        while(flag)
        {
            try{
                Log.i("BackLocationRequest", "222");
                gpsTracker.getLocation();
                latitude = gpsTracker.getLatitude(); // 위도
                longitude = gpsTracker.getLongitude(); //경도

                // 서버에 data 전송
                JSONObject postDataParam = sendJsonInfo(serviceChk);

                // 연결
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(1000000 /* milliseconds */);
                conn.setConnectTimeout(1000000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

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

                // 가이드가 자유시간 종료를 누르면 500에러를 받아 백그라운드 실행을 종료
                if (responseCode != HttpsURLConnection.HTTP_OK) {

                   BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }
                    in.close();
                    return sb.toString();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            try
            {
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

    // Json 형식 만들기
    private JSONObject sendJsonInfo(int serviceChk){
        JSONObject postDataParam = new JSONObject();

        if(serviceChk == 1){
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
        }

        else{
            try {
                postDataParam.put("userId", "end");
                postDataParam.put("title", info);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return postDataParam;
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

        callBack.onTaskDone(post_res_chk);
    }


    public void resultResponse(String result){
        switch (result) {
            // 여행객 위치 보내기 종료 시
            case "ok":
                post_res_chk = 1;
                Toast.makeText(activity, "위치 보내기를 종료 했습니다", Toast.LENGTH_SHORT).show();
                break;

            // 여행객 위치 보내기 종료 실패 시
            default :
                Toast.makeText(activity, "위치 보내기를 종료하지 못했습니다", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    // 서버 에러보내기 성공 확인 후 delete
    @Override
    protected void onCancelled() {
        super.onCancelled();
        flag = false;
    }

}
