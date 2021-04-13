package api.background;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

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

    boolean flag = false;

    // 위치 가져오는 class
    GpsTracker gpsTracker;


    public BackLocationRequest(Activity activity, String info, AsyncTaskCallBack callBack) {
        this.activity = activity;
        this.chk = API_CHOICE.LOCATION_SEND;
        this.callBack = callBack;
        this.info = info;
        gpsTracker = new GpsTracker(activity);
    }


    @Override
    protected void onPreExecute() {
        String serverURLStr = api.UrlCreate.postUrl(chk);
        // 여기에 넣어야 하나?
        //gpsTracker = new GpsTracker(activity);
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
                gpsTracker.getLocation();
                double latitude = gpsTracker.getLatitude(); // 위도
                double longitude = gpsTracker.getLongitude(); //경도

                SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String time = format.format(date);
                Log.i("LocationAccessActivity", time);

                //  서버에 아이디, 위치 전송
                JSONObject postDataParam = new JSONObject();

                try {
                    postDataParam.put("userId", info);
                    postDataParam.put("latitude", latitude);
                    postDataParam.put("longitude", longitude);
                    postDataParam.put("date", time.substring(0,10));
                    postDataParam.put("time", time.substring(11));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

                if (responseCode == HttpsURLConnection.HTTP_OK) {

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
                else {
                    return new String("Server Error : " + responseCode);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            try
            {
                Thread.sleep(30000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }

        return null;

    }

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

    // post에서 flag = false로 바꿀까? pre -> back -> post 순서가 맞나?

    @Override
    protected void onCancelled() {
        super.onCancelled();
        flag = false;
    }

}
