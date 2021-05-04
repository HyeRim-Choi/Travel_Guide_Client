package api.post;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import api.API_CHOICE;
import api.callback.AsyncTaskCallBack;
import vo.LoginVO;

/* 여행객들에게 알림 보내서 위치 요청하기(/push/alarm) */

public class PostLocationReq extends AsyncTask<JSONObject, Void, String> {
    public final int chk;
    AsyncTaskCallBack callBack;
    Activity activity;
    URL url;



    public PostLocationReq(Activity activity, AsyncTaskCallBack callBack) {
        this.activity = activity;
        this.chk = API_CHOICE.LOCATION_REQ;
        this.callBack = callBack;
    }

    // request
    @Override
    protected void onPreExecute() {
        String serverURLStr = api.UrlCreate.postUrl(chk);
        try {
            url = new URL(serverURLStr);
            Log.i("test", "url : " + url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(JSONObject... postDataParams){
        try {
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
            String str = getPostDataString(postDataParams[0]);
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

        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    protected void onPostExecute(String s) {
        callBack.onTaskDone(1);
    }


}
