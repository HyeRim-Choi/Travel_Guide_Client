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
import vo.LoginVO;

/* 여행객들에게 알림 보내서 위치 요청하기(/push/alarm) */

public class PostLocationReq extends PostRequest {
    public final int chk;
    AsyncTaskCallBack callBack;



    public PostLocationReq(Activity activity, AsyncTaskCallBack callBack) {
        super(activity);
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


    // response
    @Override
    protected void onPostExecute(String jsonString) {
        callBack.onTaskDone(1);
    }

}
