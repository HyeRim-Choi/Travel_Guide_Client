package api.get;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import api.API_CHOICE;
import api.callback.AsyncTaskCallBack;

/* 여행객 자신이 속한 그룹 title 받기 */

public class GetTravelerGroupSearch extends GetRequest {
    public final int chk;
    String info;
    String jsonString;
    AsyncTaskCallBack callBack;
    // 여행객의 그룹들 title 저장
    public ArrayList<String> title;

    int get_res_chk = 0;

    public GetTravelerGroupSearch(Activity activity, String info, AsyncTaskCallBack callBack) {
        super(activity);
        this.chk = API_CHOICE.TRAVELER_GROUP_SEARCH;
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

        callBack.onTaskDone(get_res_chk, title);

    }



    public void resultResponse(String result){
        switch (result) {
            // 그룹 조회 응답
            case "ok":
                get_res_chk = 1;
                title = new ArrayList<>();
                try{
                    JSONObject res1 = new JSONObject(jsonString);
                    JSONArray res2 = res1.getJSONArray("group");
                    for(int i=0;i<res2.length();i++){
                        title.add((String) res2.get(i));
                    }
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                break;

                // 그룹 조회 실패 시
            case "fail_nogroup":
                Log.i("GetTravelerGroupSearch", "여행객 그룹 조회 실패");
                break;
        }
    }

}
