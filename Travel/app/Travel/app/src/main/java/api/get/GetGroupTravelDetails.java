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

/* 그룹에 해당하는 여행 상품의 세부정보와 가이드 정보를 받는 서버 통신 */


public class GetGroupTravelDetails extends GetRequest {
    public final int chk;
    String info;
    String jsonString;
    // 해당 그룹에 존재하는 멤버들을 저장하는 ArrayList
    public ArrayList<String> schedule;
    String introduce, memo;
    AsyncTaskCallBack callBack;

    JSONObject jsonObject;
    int get_res_chk = 0;

    public GetGroupTravelDetails(Activity activity, String info, AsyncTaskCallBack callBack) {
        super(activity);
        this.chk = API_CHOICE.GROUP_REGISTERED_ROUTE_DETAILS;
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

        try {
            jsonObject = new JSONObject(jsonString);
            String result = jsonObject.getString("approve");
            resultResponse(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        callBack.onTaskDone(get_res_chk, introduce, memo, schedule);

    }

    public void resultResponse(String result){
        switch (result) {

            case "ok":
                get_res_chk = 1;
                schedule = new ArrayList<>();

                try{
                    JSONObject product = jsonObject.getJSONObject("product");
                    introduce = product.getString("introduce");
                    memo = product.getString("memo");

                    JSONArray route = jsonObject.getJSONArray("route");

                    for(int i = 0 ; i < route.length() ; i++){

                        JSONArray jObj = (JSONArray)route.get(i);

                        schedule.add(jObj.toString());

                    }

                    Log.i("GetTravelDealsDetails", "schedule : " + schedule);
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

}
