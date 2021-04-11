package api.post;

import android.app.Activity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;


import api.API_CHOICE;
import api.callback.AsyncTaskCallBack;

// change

public class PostLocationSend extends PostRequest {
    public final int chk;

    AsyncTaskCallBack callBack;
    JSONObject jsonObject;
    String jsonString;
    int post_res_chk;

    /*ArrayList<String> userId;
    ArrayList<Double> latitude;
    ArrayList<Double> longitude;
    */

    public PostLocationSend(Activity activity, AsyncTaskCallBack callBack) {
        super(activity);
        this.chk = API_CHOICE.LOCATION_REQ;
        this.callBack = callBack;
    }


    // request
    @Override
    protected void onPreExecute() {
        String serverURLStr = api.UrlCreate.postUrl(chk);
        try {
            URL url = new URL(serverURLStr);
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

        callBack.onTaskDone(post_res_chk);

    }



    public void resultResponse(String result){
        switch (result) {
            case "ok":
                post_res_chk = 1;
                /*userId = new ArrayList<>();
                latitude = new ArrayList<>();
                longitude = new ArrayList<>();

                try{
                    JSONObject res1 = new JSONObject(jsonString);
                    // 전달되는 이름 확인
                    JSONArray res2 = res1.getJSONArray("location");
                    for(int i=0;i<res2.length();i++){
                        JSONObject jObj = (JSONObject)res2.get(i);
                        // ArrayList로 아이디, 경도, 위도 각각 받기
                        userId.add(jObj.getString("userId"));
                        latitude.add(jObj.getDouble("latitude"));
                        longitude.add(jObj.getDouble("longitude"));

                        Log.i("id", jObj.getString("userId"));
                        Log.i("latitude", jObj.getString("latitude"));
                        Log.i("longitude", jObj.getString("longitude"));
                    }
                }catch(JSONException e) {
                    e.printStackTrace();
                }*/
                break;

            case "fail":
                break;


        }
    }

}
