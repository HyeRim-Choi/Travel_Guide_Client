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
import java.util.HashMap;
import java.util.Map;

import api.API_CHOICE;
import api.callback.AsyncTaskCallBack;

/* 그룹 여행 시작, 마지막 날짜 받기 */

public class GetGroupTripDate extends GetRequest {
    public final int chk;
    String info;
    String jsonString;
    AsyncTaskCallBack callBack;

    String startDate, endDate;

    int get_res_chk;


    public GetGroupTripDate(Activity activity, String info, AsyncTaskCallBack callBack) {
        super(activity);
        this.chk = API_CHOICE.GROUP_TRIP_DATE;
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

        callBack.onTaskDone(get_res_chk, startDate, endDate);
    }



    public void resultResponse(String result){
        switch (result) {

            case "ok":
                get_res_chk = 1;

                try{
                    JSONObject res1 = new JSONObject(jsonString);
                    JSONArray res2 = res1.getJSONArray("date");
                    for(int i=0;i<res2.length();i++){

                        JSONObject jObj = (JSONObject)res2.get(i);

                        // 출발 날짜
                        startDate = jObj.getString("startDate");

                        // 도착 날짜
                        endDate = jObj.getString("endDate");

                    }
                }

                catch(JSONException e) {
                    e.printStackTrace();
                }
                break;


            case "fail_nogroup":
                get_res_chk = 0;

                Toast.makeText(activity, "날짜를 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
