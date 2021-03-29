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

public class PostNodeObjectConnect {

    // 응답 성공 여부 체크
    public int post_object_res_chk = 1;

    private Context mContext;

    public PostNodeObjectConnect(Context context) {
        this.mContext = context;
    }

    // Node.js와 통신
    // 요청
    public void request(Map<String,Object>map, int chk)  {

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

                    switch (result){
                        case "ok":
                            post_object_res_chk = 0;
                            break;

                        case "fail":
                            post_object_res_chk = 1;
                            break;

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
            // 그룹 추가 시
            case 6:
                url += "/group";
                break;
        }

        return url;
    }
}
