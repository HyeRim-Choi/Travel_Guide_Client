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


public class PostLocationReq extends PostRequest {
    public final int chk;

    JSONObject jsonObject;

    AsyncTaskCallBack callBack;

    // ****
    public int post_res_chk;


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

        // 파라미터 바꾸기
        callBack.onTaskDone(activity, post_res_chk);

    }


    public void resultResponse(String result){
        switch (result) {
            // 성공 시
            case "ok":
                post_res_chk = 5;
                break;

            // 실패 시
            case "fail":
                Toast.makeText(activity, "그룹 멤버가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                break;

        }
    }


}
