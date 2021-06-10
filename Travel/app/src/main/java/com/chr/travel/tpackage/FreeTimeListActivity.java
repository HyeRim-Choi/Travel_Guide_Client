package com.chr.travel.tpackage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chr.travel.R;

import java.util.ArrayList;

public class FreeTimeListActivity extends AppCompatActivity {

    TextView txt_actionbar;
    ListView freeTime_listView;

    ArrayAdapter adapter;
    ArrayList<String> freeTimePlace;
    String groupTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_free_time_list);

        txt_actionbar = findViewById(R.id.txt_actionbar);
        freeTime_listView = findViewById(R.id.freeTime_listView);

        freeTimePlace = new ArrayList<>();

        Intent intent = getIntent();
        freeTimePlace = intent.getStringArrayListExtra("freeTimePlace");
        groupTitle = intent.getStringExtra("groupTitle");

        txt_actionbar.setText(groupTitle + "의 자유시간");


        adapter = new ArrayAdapter(FreeTimeListActivity.this, android.R.layout.simple_list_item_1, freeTimePlace) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // ListView custom
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.BLACK);
                tv.setPadding(50, 50, 0, 50);
                return view;
            }
        };

        freeTime_listView.setAdapter(adapter);

        // 장소 클릭 시
        freeTime_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String place = adapter.getItem(i).toString();

                // VisualizationActivity로 이동
                Intent intent = new Intent(FreeTimeListActivity.this, VisualizationActivity.class);
                intent.putExtra("place", place);
                startActivity(intent);
            }
        });
    }
}