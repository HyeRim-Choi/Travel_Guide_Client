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
    public Object[] userId;

    // JSON 형식 받기
    JSONObject jsonObject = null;

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
        String url = "http://192.168.231.85:3001";

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
        }

        return url;
    }

    public void resultResponse(String result){
        switch (result) {
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
                // 응답이 어떻게 오는지 확인
                title = getArrayListFromJSONString(jsonString);
                Log.i("test","jsonString : " + jsonString);
                Log.i("test","title : " + title);

                ArrayAdapter adapter = new ArrayAdapter(activity,
                        android.R.layout.simple_list_item_1,
                        title.toArray());
                ListView txtList = activity.findViewById(R.id.manager_group_listView);
                txtList.setAdapter(adapter);
                txtList.setDividerHeight(10);

                get_res_chk = 5;
                break;

             // 해당 그룹에 존재하는 멤버 받기
            case "ok_group_member":
                userId = getArrayListFromJSONString(jsonString).toArray();
                break;

            case "ok_mem_receive":
                Log.i("mem", "comeIn");
                groupMember = new ArrayList<>();
                //Log.i("mem", "" + jsonString.toString());

                try{
                    JSONObject res = new JSONObject(jsonString);
                    JSONObject ress = res.optJSONObject("userMem");
                    for(int i=0;i<ress.length();i++){
                        groupMember.add(ress.getString("userId"));
                    }
                }catch(JSONException e) {
                    e.printStackTrace();
                }

//                try {
//                    Log.i("mem", "" + jsonObject.getString("userMem"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
               /* try {
                    for(int i=0;i<jsonObject.getJSONArray("userMem").length();i++){
                        groupMember.add(jsonObject.getJSONArray("userMem").getString(Integer.parseInt("userId")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

               for(int i=0;i<groupMember.size();i++) {
                   Log.i("mem", "" + groupMember.get(i));
               }
                break;
        }
    }

    protected ArrayList<String> getArrayListFromJSONString(String jsonString) {
        ArrayList<String> output = new ArrayList();
        try {

            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                output.add(jsonObject.getJSONObject("group").getString("title"));
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
