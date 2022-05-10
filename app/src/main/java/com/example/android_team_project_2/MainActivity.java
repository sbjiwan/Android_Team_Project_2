package com.example.android_team_project_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    int MonthPoint = Integer.MAX_VALUE / 2;
    int year, month, date, dow;
    int WeekPoint = 0, fmonth = 0;
    int monthPage = 0, weekPage = 0;

    public int getMonthPoint() {
        return MonthPoint;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);

        ViewPager2 vpPager = findViewById(R.id.vpPager1);
        FragmentStateAdapter adapter = new MonthPagerAdapter(this);
        vpPager.setAdapter(adapter);

        vpPager.setCurrentItem(MonthPoint, false);

        vpPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                Calendar calendar = Calendar.getInstance();

                MonthPoint = position;
                monthPage = Integer.MAX_VALUE / 2 - position;

                calendar.add(Calendar.MONTH, -monthPage);

                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1;

                setTitle(year + "년 " + month + "월");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_month:
                setContentView(R.layout.activity_month);

                ViewPager2 vpPager1 = findViewById(R.id.vpPager1);
                FragmentStateAdapter adapter1 = new MonthPagerAdapter(this);
                vpPager1.setAdapter(adapter1);

                vpPager1.setCurrentItem(MonthPoint - WeekPoint, false);

                vpPager1.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        Calendar calendar = Calendar.getInstance();

                        MonthPoint = position;

                        monthPage = Integer.MAX_VALUE / 2 - position;

                        calendar.add(Calendar.MONTH, -monthPage);

                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH) + 1;

                        setTitle(year + "년 " + month + "월");
                    }
                });
                return true;

            case R.id.action_week:
                setContentView(R.layout.activity_week);

                ViewPager2 vpPager2 = findViewById(R.id.vpPager2);
                FragmentStateAdapter adapter2 = new WeekPagerAdapter(this);
                vpPager2.setAdapter(adapter2);

                vpPager2.setCurrentItem(Integer.MAX_VALUE / 2, false);

                vpPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        Calendar calendar = Calendar.getInstance();

                        weekPage = Integer.MAX_VALUE / 2 - position;

                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH) + 1;
                        date = calendar.get(Calendar.DATE);
                        fmonth = month;

                        if (Integer.MAX_VALUE / 2 - MonthPoint == 0)
                            calendar.add(Calendar.DATE, -(weekPage * 7));
                        else {
                            calendar.add(Calendar.MONTH, -(Integer.MAX_VALUE / 2 - MonthPoint));
                            fmonth = calendar.get(Calendar.MONTH) + 1;
                            calendar.set(year, month - 1 - (Integer.MAX_VALUE / 2 - MonthPoint), 1 - (weekPage * 7));
                        }

                        dow = calendar.get(Calendar.DAY_OF_WEEK);
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH) + 1;
                        date = calendar.get(Calendar.DAY_OF_MONTH);

                        if (date - dow + 1 <= 0) {
                            setTitle(year + "년 " + (month - 1) + "월 / " + month + "월");
                            WeekPoint = fmonth - month;
                        } else if (date - dow + 7 > calendar.getActualMaximum(Calendar.DATE)) {
                            setTitle(year + "년 " + month + "월 / " + (month + 1) + "월");
                            WeekPoint = fmonth - month + 1;
                        } else {
                            setTitle(year + "년 " + month + "월");
                            WeekPoint = fmonth - month;
                        }
                    }
                });
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}