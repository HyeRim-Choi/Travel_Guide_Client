package com.chr.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ManagerGetLocationActivity2 extends AppCompatActivity {

    Button btn_get_location;

    private static final int FIRST_ACTIVITY_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_get_location);

        btn_get_location = findViewById(R.id.btn_get_location);

        btn_get_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MemberSendLocationActivity2.class);

                intent.putExtra("dataFromFirstActivity", "location");

                // ThirdActivity의 수행 결과를 얻기 위해서는 startActivity() 메소드 대신에 startActivityForResult() 메소드를 사용한다
                startActivityForResult(intent, FIRST_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 결과를 반환하는 액티비티가 FIRST_ACTIVITY_REQUEST_CODE 요청코드로 시작된 경우가 아니거나
        // 결과 데이터가 빈 경우라면, 메소드 수행을 바로 반환함.
        if (requestCode != FIRST_ACTIVITY_REQUEST_CODE || data == null)
            return;

        double latitude = data.getDoubleExtra("latitude",0);
        double longitude = data.getDoubleExtra("longitude",0);

        Log.i("test", "latitude : " + latitude + ", longitude : " + longitude);

        //지도 보여주기 코드
        Intent implicit_intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("geo:" + latitude + "," + longitude));

        if (implicit_intent != null){
            startActivity(implicit_intent);
        }

    }
}