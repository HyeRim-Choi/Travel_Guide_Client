package connect;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.chr.travel.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import callback.AsyncTaskCallBack;


public class GetData extends GetRequest {
    int chk;
    public int get_res_chk = 0;
    String info;
    String jsonString;

    public ArrayList<String> title;
    public ArrayList<String> groupMember;


    AsyncTaskCallBack callBack;

    public GetData(Activity activity, int chk, String info, AsyncTaskCallBack callBack) {
        super(activity);
        this.chk = chk;
        this.info = info;
        this.callBack = callBack;
    }

    // request
    @Override
    protected void onPreExecute() {
        String serverURLStr = UrlCreate(chk);
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

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String result = jsonObject.getString("approve");
            resultResponse(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        callBack.onTaskDone(activity, get_res_chk);

        /*ArrayList<Book> arrayList = getArrayListFromJSONString(jsonString);

        ArrayAdapter adapter = new ArrayAdapter(activity,
                android.R.layout.simple_list_item_1,
                arrayList.toArray());
        ListView txtList = activity.findViewById(R.id.txtList);
        txtList.setAdapter(adapter);
        txtList.setDividerHeight(10);*/
    }

    // 요청 url 생성하기
    public String UrlCreate(int chk){
        String url = "http://192.168.50.85:3001";

        switch (chk){
            // 로그인 중복 체크 시
            case 1:
                url += "/auth/join/"+info;
                break;
            // 그룹 조회 시
            case 7:
                url += "/group/"+info;
                break;
            // 그룹에 멤버 초대 시 아이디 있는 지 체크
            case 8:
                url += "/group/idCheck/"+info;
                break;
            // 그룹 구성원 조회
            case 9:
                url+= "/group/member/" + info;
                break;
            // 로그아웃
            case 10:
                url += "/auth/logout/" + info;
                break;
            // 위치 알림 요청
            case 11:
                url += "/push/alarm/" + info;
                break;
        }

        return url;
    }

    public void resultResponse(String result){
        switch (result) {
            case "ok":
                get_res_chk = 2;
                break;
            // 회원가입 아이디 중복체크 시 아이디가 존재하지 않다면
            case "ok_idChk":
                get_res_chk = 3;
                Toast.makeText(activity,"사용가능 한 아이디입니다",Toast.LENGTH_SHORT).show();
                break;

            // 회원가입 아이디 중복체크 시 아이디가 존재한다면
            case "fail_idChk":
                Toast.makeText(activity,"이미 사용중인 아이디입니다",Toast.LENGTH_SHORT).show();
                break;

            // 그룹에 초대 할 아이디가 존재한다면
            case "ok_groupIdChk":
                get_res_chk = 1;
                break;

            // 그룹에 초대 할 아이디가 존재하지 않다면
            case "fail_groupIdChk":
                Toast.makeText(activity, "해당 아이디는 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                break;

            // 그룹 조회 응답
            case "ok_group":
                Log.i("login", "ok_group받음");
                title = new ArrayList<>();

                try{
                    JSONObject res1 = new JSONObject(jsonString);
                    JSONArray ress1 = res1.getJSONArray("group");
                    for(int i=0;i<ress1.length();i++){
                        JSONObject jObj = (JSONObject)ress1.get(i);
                        Log.i("title", jObj.getString("title"));
                        title.add(jObj.getString("title"));
                    }
                }catch(JSONException e) {
                    e.printStackTrace();
                }

                get_res_chk = 5;
                break;


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

    protected ArrayList<String> getArrayListFromJSONString(String jsonString) {
        ArrayList<String> output = new ArrayList();
        try {

            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                output.add(jsonObject.getJSONObject("groups").getString("title"));
            }
        }
        catch (JSONException e) {
            Log.e(TAG, "Exception in processing JSONString.", e);
            e.printStackTrace();
        }
        return output;
    }

    /*protected ArrayList<Book> getArrayListFromJSONString(String jsonString) {
        ArrayList<Book> output = new ArrayList();
        try {

            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                Book book = new Book(jsonObject.getString("_id"),
                        jsonObject.getString("title"),
                        jsonObject.getString("content"),
                        jsonObject.getString("author"));

                output.add(book);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Exception in processing JSONString.", e);
            e.printStackTrace();
        }
        return output;
    }*/


}
