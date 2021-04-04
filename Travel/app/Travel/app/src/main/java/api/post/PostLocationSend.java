package api.post;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import api.API_CHOICE;
import api.callback.AsyncTaskCallBack;


public class PostLocationSend extends PostRequest {
    public final int chk;


    AsyncTaskCallBack callBack;
    JSONObject jsonObject;

    // ******
    public int post_res_chk;


    public PostLocationSend(Activity activity, AsyncTaskCallBack callBack) {
        super(activity);
        this.chk = API_CHOICE.LOCATION_SEND;
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



    // response
    @Override
    protected void onPostExecute(String jsonString) {
        Log.i("response", "res : " + jsonString);
        if (jsonString == null)
            return;


        try {
            jsonObject = new JSONObject(jsonString);
            String result = jsonObject.getString("approve");
            resultResponse(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        callBack.onTaskDone(activity, post_res_chk);

    }



    public void resultResponse(String result){
        switch (result) {
            case "ok":
                post_res_chk = 1;
                break;

            case "fail":
                Toast.makeText(activity, "위치 전달 실패", Toast.LENGTH_SHORT).show();
                break;


        }
    }

}
