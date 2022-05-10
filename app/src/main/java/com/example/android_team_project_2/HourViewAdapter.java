package com.example.android_team_project_2;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HourViewAdapter extends BaseAdapter {

    private Context mContext;
    private int mResource;
    private ArrayList<My_date_hour> my_date_hours;
    private Activity mActivity;

    public HourViewAdapter(Context context, int Resource, ArrayList<My_date_hour> hours, Activity activity) {
        mContext = context;
        mResource = Resource;
        my_date_hours = hours;
        mActivity = activity;
    }

    @Override
    public int getCount() {
        return my_date_hours.size();
    }

    @Override
    public Object getItem(int position) {
        return my_date_hours.get(position);
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
        TextView tv_date = convertView.findViewById(R.id.hour_textView);

        Display display = mActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getRealMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        if ((my_date_hours.get(position).hour) > -1)
            tv_date.setText(String.valueOf(my_date_hours.get(position).hour));
        else
            tv_date.setText(" ");

        if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            tv_date.setHeight(width / 25 * 24 / 7);
        } else if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            tv_date.setHeight(height / 25 * 24 / 7);
        }

        return convertView;
    }
}