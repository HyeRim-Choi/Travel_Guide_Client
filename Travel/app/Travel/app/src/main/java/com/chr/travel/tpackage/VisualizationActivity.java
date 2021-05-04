package com.chr.travel.tpackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.chr.travel.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/* 여행객들에게 주는 자유시간 시각화 정보 */

public class VisualizationActivity extends AppCompatActivity {

    Button btn_title;

    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypackage_visualization);

        btn_title = findViewById(R.id.btn_title);

        // 인텐트 가져오기
        Intent intent = getIntent();
        // title
        title = intent.getStringExtra("title");

        btn_title.setText(title);


        /* ViewPager */
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        FragmentStateAdapter adapter = new VisualizationAdapter(this);
        viewPager.setAdapter(adapter);


        /* TabLayout */
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);

        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(TabLayout.Tab tab, int position) {
                        // Tab에 표시 될 제목을 Adapter에서 받아서 처리
                        tab.setText(((VisualizationAdapter)adapter).getPageTitle(position));
                    }
                }
        ).attach();


        // 많이 간 경로를 첫 화면에 표시되도록 설정정
       viewPager.setCurrentItem(0);
    }
}