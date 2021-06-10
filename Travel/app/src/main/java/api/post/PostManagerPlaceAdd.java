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

/* 자유시간 장소를 추가하는 서버 통신 */

public class PostManagerPlaceAdd extends PostRequest {
    public final int chk;

    JSONObject jsonObject;


    public PostManagerPlaceAdd(Activity activity, AsyncTaskCallBack callBack) {
        super(activity);
        this.chk = API_CHOICE.MANAGER_ADD_PLACE;
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

    }


    public void resultResponse(String result){
        switch (result) {
            // 장소 저장 성공 시
            case "ok_save_sub":
                Toast.makeText(activity, "장소 저장이 되었습니다", Toast.LENGTH_SHORT).show();
                break;

            // 장소 저장 실패 시
            case "fail":
                Toast.makeText(activity, "장소 저장에 실패하였습니다", Toast.LENGTH_SHORT).show();
                break;

        }
    }


}
