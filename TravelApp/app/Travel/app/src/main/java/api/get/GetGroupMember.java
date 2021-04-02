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


public class GetGroupMember extends GetRequest {
    public final int chk;
    String info;
    String jsonString;
    public ArrayList<String> groupMember;
    AsyncTaskCallBack callBack;

    // *******
    public int get_res_chk = 0;

    public GetGroupMember(Activity activity, String info, AsyncTaskCallBack callBack) {
        super(activity);
        this.chk = API_CHOICE.GROUP_MEMBER;
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

        callBack.onTaskDone(activity, get_res_chk, groupMember);

    }

    public void resultResponse(String result){
        switch (result) {

            // 해당 그룹에 존재하는 멤버 받기
            case "ok_mem_receive":
                groupMember = new ArrayList<>();
                try{
                    JSONObject res = new JSONObject(jsonString);
                    JSONArray ress = res.getJSONArray("userMem");
                    for(int i=0;i<ress.length();i++){
                        JSONObject jObj = (JSONObject)ress.get(i);
                        Log.i("userId", jObj.getString("userId"));
                        groupMember.add(jObj.getString("userId"));
                    }
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                get_res_chk = 6;
                break;
        }
    }

}
