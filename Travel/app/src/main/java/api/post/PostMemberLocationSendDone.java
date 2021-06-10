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
import api.get.GetRequest;

/* 멤버들 위치 전송 종료 */

public class PostMemberLocationSendDone extends PostRequest {
    public final int chk;

    AsyncTaskCallBack callBack;

    int post_res_chk;


    public PostMemberLocationSendDone(Activity activity, AsyncTaskCallBack callBack) {
        super(activity);
        this.chk = API_CHOICE.MEMBER_LOCATION_SEND_DONE;
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
            JSONObject jsonObject = new JSONObject(jsonString);
            String result = jsonObject.getString("approve");
            resultResponse(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        callBack.onTaskDone(post_res_chk);

    }

    public void resultResponse(String result){
        switch (result) {
            case "ok":
                post_res_chk = 1;
                Toast.makeText(activity,"위치 전송이 종료되었습니다",Toast.LENGTH_SHORT).show();
                break;

            case "fail":
                Toast.makeText(activity,"위치 전송 실패하였습니다",Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
