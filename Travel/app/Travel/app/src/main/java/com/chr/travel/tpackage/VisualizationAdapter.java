package com.chr.travel.tpackage;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.chr.travel.fragmentpackage.VisualizationMapFragment;

/* VisualizationActivity의 ViewPager의 Adapter */

// ** delete

public class VisualizationAdapter extends FragmentStateAdapter {

    private static int NUM_ITEMS=3;

    // TabLayout에 표시 될 제목
    private String tabTitles[] = new String[] { "경로", "나이", "성별" };

    public VisualizationAdapter(FragmentActivity fa) {
        super(fa);
    }

    // 각 페이지를 나타내는 프래그먼트 반환
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            // 많이 간 경로 보여주기
            case 0:
               /* VisualizationMapFragment visualizationMapFragment = new VisualizationMapFragment();
                return visualizationMapFragment;*/

            case 1:
                /*SecondFragment second = new SecondFragment();
                return second;*/

            case 2:
               /* ThirdFragment third = new ThirdFragment();
                return third;*/

            default:
              /* visualizationMapFragment = new VisualizationMapFragment();
               return visualizationMapFragment;*/return  null;

        }
    }

    // 전체 페이지 개수 반환
    @Override
    public int getItemCount() {
        return NUM_ITEMS;
    }

    // TabLayout에 표시 될 제목 전달
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
