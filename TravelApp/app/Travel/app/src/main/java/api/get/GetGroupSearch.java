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


public class GetGroupSearch extends GetRequest {
    public final int chk;
    String info;
    String jsonString;
    AsyncTaskCallBack callBack;
    public ArrayList<String> title;

    // *********
    public int get_res_chk = 0;

    public GetGroupSearch(Activity activity, String info, AsyncTaskCallBack callBack) {
        super(activity);
        this.chk = API_CHOICE.GROUP_SEARCH;
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

        callBack.onTaskDone(activity, get_res_chk, title);

    }



    public void resultResponse(String result){
        switch (result) {
            // 그룹 조회 응답
            case "ok_group":
                title = new ArrayList<>();
                try{
                    JSONObject res1 = new JSONObject(jsonString);
                    JSONArray res2 = res1.getJSONArray("group");
                    for(int i=0;i<res2.length();i++){
                        JSONObject jObj = (JSONObject)res2.get(i);
                        Log.i("title", jObj.getString("title"));
                        title.add(jObj.getString("title"));
                    }
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                get_res_chk = 5;
                break;
        }
    }

}
