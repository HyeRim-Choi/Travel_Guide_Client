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


public class PostGroupAdd extends PostRequest {
    public final int chk;

    JSONObject jsonObject;

    AsyncTaskCallBack callBack;

    // *****
    public int post_res_chk;


    public PostGroupAdd(Activity activity, AsyncTaskCallBack callBack) {
        super(activity);
        this.chk = API_CHOICE.GROUP_ADD;
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
            // 그룹 생성 실패 시
            case "fail":
                Toast.makeText(activity, "없는 정보입니다", Toast.LENGTH_SHORT).show();
                break;

            // 그룹 생성 성공 시
            case "ok_groupCreate":
                post_res_chk = 5;
                break;
        }
    }


}
