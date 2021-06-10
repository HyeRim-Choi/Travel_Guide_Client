package api.post;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import api.API_CHOICE;
import api.callback.AsyncTaskCallBack;
import vo.LoginVO;

/* 로그인을 진행하는 서버 통신 */

public class PostLogin extends PostRequest {
    public final int chk;
    AsyncTaskCallBack callBack;
    JSONObject jsonObject;

    int post_res_chk;


    public PostLogin(Activity activity, AsyncTaskCallBack callBack) {
        super(activity);
        this.chk = API_CHOICE.LOGIN;
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
            // 아이디 존재하지 않다면
            case "fail":
                Toast.makeText(activity, "없는 정보입니다", Toast.LENGTH_SHORT).show();
                break;


            // 응답이 로그인 성공이라면
            case "ok_login":
                // 회원 정보 VO에 담기
                LoginVO vo = LoginVO.getInstance();
                post_res_chk = 1;
                try {
                    vo.setIdx(jsonObject.getJSONObject("user").getInt("id"));
                    vo.setUserId(jsonObject.getJSONObject("user").getString("userId"));
                    vo.setPassword(jsonObject.getJSONObject("user").getString("password"));
                    vo.setName(jsonObject.getJSONObject("user").getString("name"));
                    vo.setEmail(jsonObject.getJSONObject("user").getString("email"));
                    vo.setRole(jsonObject.getJSONObject("user").getString("role"));
                    vo.setBirth(jsonObject.getJSONObject("user").getString("birth"));
                    vo.setGender(jsonObject.getJSONObject("user").getBoolean("gender"));
                    vo.setTel(jsonObject.getJSONObject("user").getString("phoneNum"));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            // 비밀번호 찾기 실패 시
            case "fail_pwd":
                Toast.makeText(activity, "비밀번호를 잘 못 입력하셨습니다", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
