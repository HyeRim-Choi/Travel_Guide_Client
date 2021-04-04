package api.get;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import api.API_CHOICE;
import api.callback.AsyncTaskCallBack;

public class GetIdCheck extends GetRequest{
    public final int chk;
    String info;
    String jsonString;
    AsyncTaskCallBack callBack;

    // ******
    public int get_res_chk = 0;

    public GetIdCheck(Activity activity, String info, AsyncTaskCallBack callBack) {
        super(activity);
        this.chk = API_CHOICE.IDCHECK;
        this.info = info;
        this.callBack = callBack;
    }

    // request
    @Override
    protected void onPreExecute() {
        String serverURLStr = api.UrlCreate.getUrl(chk, info);
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
        this.jsonString = jsonString;

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

        callBack.onTaskDone(activity, get_res_chk);
    }

    public void resultResponse(String result){
        switch (result) {
            case "ok_idChk":
                get_res_chk = 3;
                Toast.makeText(activity,"사용가능 한 아이디입니다",Toast.LENGTH_SHORT).show();
                break;
            // 회원가입 아이디 중복체크 시 아이디가 존재한다면
            case "fail_idChk":
                Toast.makeText(activity,"이미 사용중인 아이디입니다",Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
