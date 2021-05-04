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

/* 가이드가 저장한 서브 장소들 데이터 받기 */

public class GetManagerShowPlace extends GetRequest {
    public final int chk;
    String info;
    String jsonString;
    AsyncTaskCallBack callBack;

    // 가이드가 저장한 서브장소들의 위도, 경도
    ArrayList<Map> place;
    Map<String, String> map;


    public GetManagerShowPlace(Activity activity, String info, AsyncTaskCallBack callBack) {
        super(activity);
        this.chk = API_CHOICE.MANAGER_SHOW_PLACE;
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

        callBack.onTaskDone(place);
    }



    public void resultResponse(String result){
        switch (result) {

            case "ok":
                place = new ArrayList<>();

                try{
                    JSONObject res1 = new JSONObject(jsonString);
                    JSONArray res2 = res1.getJSONArray("subPlaces");
                    for(int i=0;i<res2.length();i++){
                        map = new HashMap<>();
                        JSONObject jObj = (JSONObject)res2.get(i);

                        // 관광지 이름
                        String name = jObj.getString("name");
                        map.put("name",name);

                        // 위도
                        String lat = jObj.getString("latitude");
                        map.put("latitude",lat);

                        // 경도
                        String lon = jObj.getString("longitude");
                        map.put("longitude",lon);


                        place.add(map);
                    }
                }

                catch(JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "fail":
                Toast.makeText(activity, "장소를 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
