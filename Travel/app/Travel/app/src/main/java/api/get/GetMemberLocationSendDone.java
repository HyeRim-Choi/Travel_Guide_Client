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

/* 멤버들 위치 전송 종료 */

public class GetMemberLocationSendDone extends GetRequest{
    public final int chk;
    String info;
    String jsonString;


    public GetMemberLocationSendDone(Activity activity, String info) {
        super(activity);
        this.chk = API_CHOICE.MEMBER_LOCATION_SEND_DONE;
        this.info = info;

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

    }

    public void resultResponse(String result){
        switch (result) {
            case "ok":
                Toast.makeText(activity,"위치 전송이 종료되었습니다",Toast.LENGTH_SHORT).show();
                break;

            case "fail":
                Toast.makeText(activity,"위치 전송 실패하였습니다",Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
