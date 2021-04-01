package com.chr.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import vo.LoginVO;

import org.json.JSONException;
import org.json.JSONObject;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class SocketClientActivity extends AppCompatActivity {

    Socket mSocket;
    String title;
    ArrayList<String> users;

    //Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_client);

        //onSearchData();

        try {
            mSocket = IO.socket("http://10.0.0.0:3001");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        title = intent.getStringExtra("title");

        mSocket.connect();

        //mSocket.on(Socket.EVENT_CONNECT, onConnect);
        //mSocket.on("newUser", onNewUser);

    }

    // 저장한 user 정보를 불러오는 함수
    /*public void onSearchData(){

        gson = new Gson();

        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        String strUser = sp.getString("vo","");
        Log.i("test","loginUserSearch : " + strUser);

        vo = gson.fromJson(strUser, LoginVO.class);
    }*/
}