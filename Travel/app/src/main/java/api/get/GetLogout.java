package api.get;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import api.API_CHOICE;
import api.callback.AsyncTaskCallBack;
import vo.LoginVO;

/* 로그아웃을 하는 서버통신 */

public class GetLogout extends GetRequest {
    public final int chk;
    String info;
    String jsonString;
    AsyncTaskCallBack callBack;

    int get_res_chk = 0;

    public GetLogout(Activity activity, String info, AsyncTaskCallBack callBack) {
        super(activity);
        this.chk = API_CHOICE.LOGOUT;
        this.info = info;
        this.callBack = callBack;
    }

    // request
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

        callBack.onTaskDone(get_res_chk);
    }


    public void resultResponse(String result){
        switch (result) {
            case "ok":
                get_res_chk = 1;
                // 로그아웃이 되었다면 VO를 null로 만들기
                LoginVO vo = LoginVO.getInstance();
                vo.setPassword(null);
                vo.setUserId(null);
                vo.setIdx(0);
                vo.setBirth(null);
                vo.setRole(null);
                vo.setEmail(null);
                vo.setName(null);
                vo.setTel(null);
                break;
        }
    }


}
