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

/* 회원가입 서버 통신 */

public class PostSignUp extends PostRequest {
    public final int chk;
    AsyncTaskCallBack callBack;

    int post_res_chk;


    public PostSignUp(Activity activity, AsyncTaskCallBack callBack) {
        super(activity);
        this.chk = API_CHOICE.SIGNUP;
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

        JSONObject jsonObject;
        try {
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
            // 응답이 회원가입 성공이라면
            case "ok_signUp":
                post_res_chk = 1;
                Toast.makeText(activity,"회원가입이 완료되었습니다",Toast.LENGTH_SHORT).show();
                break;

            // 응답이 회원가입 실패라면
            case "fail_signUp":
                Toast.makeText(activity,"회원가입이 정상적으로 되지 않았습니다",Toast.LENGTH_SHORT).show();
                break;

        }
    }


}
