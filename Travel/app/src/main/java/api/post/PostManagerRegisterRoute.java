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

/* 가이드 여행 상품 등록 */

public class PostManagerRegisterRoute extends PostRequest {
    public final int chk;


    public PostManagerRegisterRoute(Activity activity, AsyncTaskCallBack callBack) {
        super(activity);
        this.chk = API_CHOICE.MANAGER_REGISTER_ROUTE;
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

    }


    public void resultResponse(String result){
        switch (result) {

            case "ok":
                Toast.makeText(activity,"등록되었습니다",Toast.LENGTH_SHORT).show();
                activity.finish();
                break;


            case "fail":
                Toast.makeText(activity,"등록되지 못했습니다",Toast.LENGTH_SHORT).show();
                break;

        }
    }


}
