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

/* 관광지 경로 지정을 위한 관광지 검색 */

public class GetManagerPlaceRouteSearch extends GetRequest {
    public final int chk;
    String info;
    String jsonString;
    AsyncTaskCallBack callBack;

    // 관광지 데이터 받기
    ArrayList<String> place;
    
    int pos_res_chk;


    public GetManagerPlaceRouteSearch(Activity activity, String info, AsyncTaskCallBack callBack) {
        super(activity);
        this.chk = API_CHOICE.MANAGER_ROUTE_PLACE_SEARCH;
        this.info = info;
        this.callBack = callBack;

        pos_res_chk = 0;
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

        callBack.onTaskDone(pos_res_chk, place);
    }



    public void resultResponse(String result){
        switch (result) {

            case "ok":
                pos_res_chk = 1;

                place = new ArrayList<>();

                try{
                    JSONObject res1 = new JSONObject(jsonString);
                    JSONArray res2 = res1.getJSONArray("places");
                    for(int i=0;i<res2.length();i++){
                        JSONObject jObj = (JSONObject)res2.get(i);

                        // 관광지 이름
                        String name = jObj.getString("name");

                        place.add(name);

                    }
                }

                catch(JSONException e) {
                    e.printStackTrace();
                }

                break;

            case "no_data":
                Toast.makeText(activity, "해당 관광지가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
