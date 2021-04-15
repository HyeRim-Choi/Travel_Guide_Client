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

/* 해당 그룹 멤버들의 위치를 받아오는 서버통신 */

public class GetMemberReloadLocation extends GetRequest {
    public final int chk;
    String info;
    String jsonString;
    AsyncTaskCallBack callBack;

    // 멤버들의 위치 정보
    ArrayList<Map> travelerLocation;
    Map<String, String> map;


    int get_res_chk = 0;

    public GetMemberReloadLocation(Activity activity, String info, AsyncTaskCallBack callBack) {
        super(activity);
        this.chk = API_CHOICE.MEMBER_LOCATION_RELOAD_SEND;
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

        callBack.onTaskDone(get_res_chk, travelerLocation);
    }



    public void resultResponse(String result){
        switch (result) {
            // 멤버들 위치 조회 성공 시
            case "ok":
                get_res_chk = 1;
                travelerLocation = new ArrayList<>();

                try{
                    JSONObject res1 = new JSONObject(jsonString);
                    JSONArray res2 = res1.getJSONArray("curLoc");
                    for(int i=0;i<res2.length();i++){
                        map = new HashMap<>();
                        JSONObject jObj = (JSONObject)res2.get(i);

                        // 위도
                        String lat = jObj.getString("latitude");
                        map.put("latitude",lat);

                        // 경도
                        String lon = jObj.getString("longitude");
                        map.put("longitude",lon);

                        // 아이디
                        String id = jObj.getString("userId");
                        map.put("userId",id);

                        travelerLocation.add(map);
                    }
                }

                catch(JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "fail":
                Toast.makeText(activity, "멤버들의 위치 정보를 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
