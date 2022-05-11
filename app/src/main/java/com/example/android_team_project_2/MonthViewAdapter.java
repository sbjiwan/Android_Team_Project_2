package com.example.android_team_project_2;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MonthViewAdapter extends BaseAdapter {

    private Context mContext;
    private int mResource;
    private ArrayList<My_date_month> my_date_months;
    private Activity mActivity;
    private int check = 0;

    public MonthViewAdapter(Context context, int Resource, ArrayList<My_date_month> dates, Activity activity) {
        mContext = context;
        mResource = Resource;
        my_date_months = dates;
        mActivity = activity;
    }

    @Override
    public int getCount() {
        return my_date_months.size();
    }

    @Override
    public Object getItem(int position) {
        return my_date_months.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }
        TextView tv_date = convertView.findViewById(R.id.month_textView);

        if (check == 0 && my_date_months.get(position).date == 1)
            check = 1;
        else if (check == 1 && my_date_months.get(position).date == 1)
            check = 0;

        if (position == 0 && my_date_months.get(position).date == 1)
            check = 1;

        tv_date.setText(String.valueOf(my_date_months.get(position).date));

        if (check == 0)
            tv_date.setBackgroundColor(Color.GRAY);

        Display display = mContext.getDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getRealMetrics(metrics);
        int Realheight = metrics.heightPixels;

        Rect rect = new Rect();
        Window window = mActivity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        int contentTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int statusBarHeight = rect.top;

        int bottomBarHeight = 0;
        int resourceIdBottom = mActivity.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceIdBottom > 0)
            bottomBarHeight = mActivity.getResources().getDimensionPixelSize(resourceIdBottom);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int dpi = displayMetrics.densityDpi;
        float density = displayMetrics.density;

        int Th = (int) (25 * density + 0.5);

        if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            tv_date.setHeight((Realheight - statusBarHeight - contentTop - Th - bottomBarHeight) / 6);
        else if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            tv_date.setHeight((Realheight - statusBarHeight - contentTop - Th) / 6);

        return convertView;
    }
}