package connect;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;



import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import vo.LoginVO;


public class PostInsertData extends PostRequest {
    // url 경로 만들기 위한 chk
    int chk;

    // 응답 정보를 넘겨주기 위한 chk
    public int post_res_chk = 0;

    // 로그인 성공 시 user 정보 받기
    public LoginVO vo;

    // 아이디 찾기 시 아이디 받기
    public String findId;

    // JSON 형식 받기
    JSONObject jsonObject = null;


    public PostInsertData(Activity activity, int chk) {
        super(activity);
        this.chk = chk;
    }


    // request
    @Override
    protected void onPreExecute() {
        String serverURLStr = UrlCreate(chk);
        try {
            url = new URL(serverURLStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    // response
    @Override
    protected void onPostExecute(String jsonString) {
        Log.i("test",jsonString);
        if (jsonString == null)
            return;

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

    }


    // 요청 url 생성하기
    public String UrlCreate(int chk){
        String url = "http://192.168.16.85:3001";

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

    public void resultResponse(String result){
        switch (result) {
            // 성공 시(Default)
            case "ok":
                break;

            // 실패 시(Default)
            case "fail":
                Toast.makeText(activity, "없는 정보입니다", Toast.LENGTH_SHORT).show();
                break;

            // 응답이 회원가입 성공이라면
            case "ok_signUp":
                Toast.makeText(activity,"회원가입이 완료되었습니다",Toast.LENGTH_SHORT).show();
                post_res_chk = 3;
                break;

            // 응답이 회원가입 실패라면
            case "fail_signUp":
                Toast.makeText(activity,"회원가입이 정상적으로 되지 않았습니다",Toast.LENGTH_SHORT).show();
                break;

            // 응답이 로그인 성공이라면
            case "ok_login":
                post_res_chk = 1;
                vo = new LoginVO();
                try {
                    vo.setIdx(jsonObject.getJSONObject("user").getInt("id"));
                    vo.setUserId(jsonObject.getJSONObject("user").getString("userId"));
                    vo.setPassword(jsonObject.getJSONObject("user").getString("password"));
                    vo.setName(jsonObject.getJSONObject("user").getString("name"));
                    vo.setEmail(jsonObject.getJSONObject("user").getString("email"));
                    vo.setRole(jsonObject.getJSONObject("user").getString("role"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            // 비밀번호 찾기 실패 시
            case "fail_pwd":
                Toast.makeText(activity, "비밀번호를 잘 못 입력하셨습니다", Toast.LENGTH_SHORT).show();
                break;

            // 아이디 찾기 성공 시
            case "ok_findId":
                post_res_chk = 2;
                try {
                    findId = jsonObject.getString("userId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            // 비밀번호 찾기 성공 시
            case "ok_findPwd":
                //Alert창 띄우기
                AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                dialog.setMessage("작성하신 이메일로 가서 임시 비밀번호를 확인해주세요\n 비밀번호를 변경해주세요");
                dialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                });

                dialog.show();
                break;

            // 그룹 조회 성공 시
            case "ok_group":
                break;
        }
    }


}
