package api.post;

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

/* 해당 자유시간 장소의 시각화 정보 받기 */

public class PostVisualization extends PostRequest {
    public final int chk;
    AsyncTaskCallBack callBack;

    JSONObject jsonObject;
    double latitude, longitude;
    ArrayList<String> subPlace, totalMem, order, avgTime;
    int pos_res_chk = 0;

    public PostVisualization(Activity activity, AsyncTaskCallBack callBack) {
        super(activity);
        this.chk = API_CHOICE.VISUALIZATION;
        this.callBack = callBack;
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
        Log.i("response", "res VisualInfo: " + jsonString);

        if (jsonString == null){
            return;
        }

        try {
            pos_res_chk = 1;

            // 큰 관광지(한성대) 안의 등록해둔 서브 관광지
            subPlace = new ArrayList<>();
            // 많이 간 경로
            totalMem = new ArrayList<>();
            // 구체적 경로
            order = new ArrayList<>();
            // 각 서브 관광지별로 머문 평균 시간
            avgTime = new ArrayList<>();

            jsonObject = new JSONObject(jsonString);


            // 자유시간을 갖는 장소(큰 관광지)의 위도, 경도
            latitude = jsonObject.getDouble("latitude");
            longitude = jsonObject.getDouble("longitude");

            Log.i("PostVisualization", "lat log" + latitude + " " + longitude);


            // 큰 관광지(한성대) 안의 등록해둔 서브 관광지
            JSONArray subPlaceData = jsonObject.getJSONArray("subPlace");
            // 정보가 존재한다면 ]
            if(subPlaceData.length() != 0){
                for(int i = 0 ; i < subPlaceData.length() ; i++){

                    JSONObject jObj = (JSONObject)subPlaceData.get(i);

                    subPlace.add(jObj.toString());

                }
            }


            // 많이 간 경로
            JSONArray totalMemData = jsonObject.getJSONArray("totalMem");

            if(totalMemData.length() != 0){
                for(int i = 0 ; i < totalMemData.length() ; i++){

                    JSONObject jObj = (JSONObject)totalMemData.get(i);

                    totalMem.add(jObj.toString());

                }
            }

            // 구체적 경로
            JSONArray orderData = jsonObject.getJSONArray("order");

            if(orderData.length() != 0){
                for(int i = 0; i < orderData.length(); i++){
                    JSONObject jObj = (JSONObject)orderData.get(i);
                    order.add(jObj.toString());
                }
            }



            // 각 서브 관광지별로 머문 평균 시간
            JSONArray avgTimeData = jsonObject.getJSONArray("avgTime");

            if(avgTimeData.length() != 0){
                for(int i = 0 ; i < avgTimeData.length() ; i++){

                    JSONObject jObj = (JSONObject)avgTimeData.get(i);
                    avgTime.add(jObj.toString());

                }
            }


            Log.i("PostVisualization", "subPlace : " + subPlace);
            Log.i("PostVisualization", "totalMem : " + totalMem);
            Log.i("PostVisualization", "avgTime : " + avgTime);

        }

        catch (JSONException e) {
            e.printStackTrace();
        }

        callBack.onTaskDone(pos_res_chk, latitude, longitude, subPlace, totalMem, avgTime, order);

    }

}
