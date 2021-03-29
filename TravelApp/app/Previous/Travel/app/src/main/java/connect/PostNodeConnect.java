package connect;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import vo.LoginVO;

public class PostNodeConnect {

    // 응답 성공 여부 체크
    public int post_res_chk = 1;

    // 아이디 찾기 시 아이디 받기
    public String findId;

    // 로그인 성공 시 user 정보 받기
    public LoginVO vo;

    private Context mContext;

    public PostNodeConnect(Context context) {
        this.mContext = context;
    }

    // Node.js와 통신
    // 요청
    public void request(Map<String,String>map, int chk)  {

        //JSON형식으로 데이터 통신을 진행합니다!
        JSONObject testjson = new JSONObject();

        for(String key : map.keySet()){
            try {
                // node로 보내는 데이터들 넣기
                testjson.put(key, map.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //url 요청주소
        String url = UrlCreate(chk);
        Log.i("test","url : "+ url);

        //전송
        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,testjson, new Response.Listener<JSONObject>() {

            //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
            @Override
            public void onResponse(JSONObject response) {
                // 응답
                try {
                    Log.i("test","데이터 받기 성공");

                    //받은 json형식의 응답을 받아
                    JSONObject jsonObject = new JSONObject(response.toString());

                    Log.i("test","json res : "+jsonObject);

                    //key값에 따라 value값을 쪼개 받아옵니다.
                    String result= jsonObject.getString("approve");

                    // 응답이 OK라면
                   /* if(result.equals("ok")){
                        post_res_chk = 0;
                    }

                    // 응답이 fail이라면
                    else if(result.equals("fail")){
                        post_res_chk = 1;
                    }

                    // 로그인 시 비밀번호가 틀렸다고 응답이 오면
                    else if(result.equals("fail_pwd")){
                        post_res_chk = 2;
                    }

                    else if(result.equals("ok_id")){
                        post_res_chk = 3;
                        findId = jsonObject.getString("userId");
                    }*/

                    switch (result){
                        // 응답이 OK라면
                        case "ok":
                            post_res_chk = 0;
                            break;

                        // 응답이 fail이라면
                        case "fail":
                            post_res_chk = 1;
                            break;

                        // 로그인 시 비밀번호가 틀렸다고 응답이 오면
                        case "fail_pwd":
                            post_res_chk = 2;
                            break;

                        // id 찾기 시 id를 찾는 걸 성공했다면
                        case "ok_id":
                            post_res_chk = 3;
                            findId = jsonObject.getString("userId");
                            break;

                        // login 성공 시 user 정보 받기
                        case "ok_login":
                            post_res_chk = 4;
                            vo = new LoginVO();
                            vo.setIdx(jsonObject.getJSONObject("user").getInt("id"));
                            vo.setUserId(jsonObject.getJSONObject("user").getString("userId"));
                            vo.setPassword(jsonObject.getJSONObject("user").getString("password"));
                            vo.setName(jsonObject.getJSONObject("user").getString("name"));
                            vo.setEmail(jsonObject.getJSONObject("user").getString("email"));
                            vo.setRole(jsonObject.getJSONObject("user").getString("role"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        //jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });


        requestQueue.add(jsonObjectRequest);

    }

    // 요청 url 생성하기
    public String UrlCreate(int chk){
        String url = "";

        switch (chk){
            // 회원가입 시
            case 2:
                url += "/auth/join";
                break;
            // 로그인 시
            case 3:
                url += "/auth/login";
                break;
            // 아이디 찾기 시
            case 4:
                url += "/auth/findId";
                break;
            // 비밀번호 찾기 시
            case 5:
                url += "/auth/findPw";
                break;
            // 그룹 추가 시
            case 6:
                url += "/group";
                break;
        }

        return url;
    }
}
