package api.background;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.chr.travel.LocationAccessActivity;

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

public class BackLocationRequest extends AsyncTask<JSONObject, Void, String> {
    public final int chk;
    AsyncTaskCallBack callBack;
    URL url;

    boolean flag = false;


    public BackLocationRequest(AsyncTaskCallBack callBack) {
        this.chk = API_CHOICE.LOCATION_SEND;
        this.callBack = callBack;
    }


    @Override
    protected void onPreExecute() {
        String serverURLStr = api.UrlCreate.postUrl(chk);
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
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            try
            {
                Thread.sleep(30000);//Your Interval after which you want to refresh the screen
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

    @Override
    protected void onCancelled() {
        super.onCancelled();
        flag = false;
    }

}
