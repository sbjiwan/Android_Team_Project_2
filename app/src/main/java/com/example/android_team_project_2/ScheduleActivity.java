package com.example.android_team_project_2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ScheduleActivity extends AppCompatActivity implements OnMapReadyCallback {

    int sHour = 0, sMinute = 0, eHour = 0, eMinute = 0;

    LatLng hansung = new LatLng(37.5822608, 127.0094254);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        TimePicker timeStart = findViewById(R.id.timeStart);
        TimePicker timeEnd = findViewById(R.id.timeEnd);

        EditText editTitle = findViewById(R.id.editTitle);
        editTitle.setHint(" ");

        timeStart.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int h, int m) {
                sHour = h;
                sMinute = m;
                if(sHour <= 22)
                    timeEnd.setCurrentHour(h + 1);
                else if(sHour == 23)
                    timeEnd.setCurrentHour(0);
                timeEnd.setCurrentMinute(m);
            }
        });

        timeEnd.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int h, int m) {
                eHour = h;
                eMinute = m;

                if(eHour >= 1)
                    timeStart.setCurrentHour(h - 1);
                else if(eHour == 0)
                    timeStart.setCurrentHour(23);
                timeEnd.setCurrentMinute(m);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void mClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                break;
            case R.id.save:

                finish();
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.delete:
                finish();
                break;
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(hansung));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hansung, 15));
    }
}