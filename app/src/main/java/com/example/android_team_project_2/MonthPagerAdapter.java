package com.example.android_team_project_2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

class MonthPagerAdapter extends FragmentStateAdapter {
    private static final int NUM_ITEMS = Integer.MAX_VALUE;

    public MonthPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    // 각 페이지를 나타내는 프래그먼트 반환
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new MonthFragment(position);
    }

    // 전체 페이지 개수 반환
    @Override
    public int getItemCount() {
        return NUM_ITEMS;
    }
}
