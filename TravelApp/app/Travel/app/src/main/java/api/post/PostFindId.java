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


public class PostFindId extends PostRequest {
    public final int chk;

    // 아이디 찾기 시 아이디 받기
    public String findId;

    AsyncTaskCallBack callBack;
    JSONObject jsonObject;

    // ******
    public int post_res_chk;


    public PostFindId(Activity activity, AsyncTaskCallBack callBack) {
        super(activity);
        this.chk = API_CHOICE.FIND_ID;
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

        callBack.onTaskDone(activity, post_res_chk, findId);

    }



    public void resultResponse(String result){
        switch (result) {
            // 아이디 찾기 실패 시
            case "fail":
                Toast.makeText(activity, "없는 정보입니다", Toast.LENGTH_SHORT).show();
                break;

            // 아이디 찾기 성공 시
            case "ok_findId":
                post_res_chk = 2;
                try {
                    findId = jsonObject.getString("userId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}
