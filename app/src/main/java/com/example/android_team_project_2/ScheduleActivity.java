package com.example.android_team_project_2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ScheduleActivity extends AppCompatActivity implements OnMapReadyCallback {

    private int sHour = 0, sMinute = 0, eHour = 0, eMinute = 0;
    private MyDBHelper myDBHelper;
    private int search_check = 0;
    private GoogleMap map;
    private CompoundButton animateToggle;
    private CompoundButton customDurationToggle;
    private SeekBar customDurationBar;
    private LatLng latLng;
    private LatLng hansung = new LatLng(37.5822608, 127.0094254);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        myDBHelper = new MyDBHelper(this);

        TimePicker timeStart = findViewById(R.id.timeStart);
        TimePicker timeEnd = findViewById(R.id.timeEnd);

        EditText editTitle = findViewById(R.id.editTitle);
        EditText editPlace = (EditText) findViewById(R.id.editPlace);
        EditText editMemo = (EditText) findViewById(R.id.editMemo);
        editTitle.setHint(MainActivity.ClickPoint);

        Intent intent = getIntent();
        if(intent.getIntExtra("selected", -1) != -1) {
            Cursor cursor = myDBHelper.getAllUsersByMethod();

            cursor.moveToPosition(intent.getIntExtra("selected", -1));

            editTitle.setText(cursor.getString(1));
            editPlace.setText(cursor.getString(5) + "/" + cursor.getString(6));
            editMemo.setText(cursor.getString(7));

            timeStart.setCurrentHour(Integer.parseInt(cursor.getString(3)));
            timeStart.setCurrentMinute(0);
            sHour = Integer.parseInt(cursor.getString(3));
            timeEnd.setCurrentHour(Integer.parseInt(cursor.getString(4)));
            timeEnd.setCurrentMinute(0);
            eHour = Integer.parseInt(cursor.getString(4));

            String[] Place = editPlace.getText().toString().split("/");

            if(Double.parseDouble(Place[0]) != 0.0 && Double.parseDouble(Place[1]) != 0.0) {

                CameraPosition search =
                        new CameraPosition.Builder().target(new LatLng(Double.parseDouble(Place[0]), Double.parseDouble(Place[1])))
                                .zoom(15.5f)
                                .bearing(300)
                                .tilt(50)
                                .build();

                changeCamera(CameraUpdateFactory.newCameraPosition(search));
            }
        }

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
        myDBHelper = new MyDBHelper(this);
        EditText editTitle = (EditText) findViewById(R.id.editTitle);
        EditText editPlace = (EditText) findViewById(R.id.editPlace);
        EditText editMemo = (EditText) findViewById(R.id.editMemo);
        String Title = editTitle.getText().toString();
        String Memo = editMemo.getText().toString();
        String[] Place = editPlace.getText().toString().split("/");

        switch (view.getId()) {
            case R.id.save:
                myDBHelper.insertUserByMethod(Title, MainActivity.ClickPoint, sHour+"", eHour+"",Place[0], Place[1], Memo);
                finish();
                Intent intentsave = new Intent(this, MainActivity.class);
                startActivity(intentsave);
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.delete:
                myDBHelper.delete(MainActivity.ClickPoint, sHour+"", eHour+"");
                finish();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    private boolean checkReady() {
        if (map == null) {
            Toast.makeText(this, "map not ready", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void onGoToSearch(View view) {
        if (!checkReady()) {
            return;
        }
        myDBHelper = new MyDBHelper(this);

        EditText editPlace = (EditText) findViewById(R.id.editPlace);
        String[] Place = editPlace.getText().toString().split("/");

        if(Double.parseDouble(Place[0]) == 0.0 && Double.parseDouble(Place[1]) == 0.0)
            return;

        CameraPosition search =
                new CameraPosition.Builder().target(new LatLng(Double.parseDouble(Place[0]), Double.parseDouble(Place[1])))
                        .zoom(15.5f)
                        .bearing(300)
                        .tilt(50)
                        .build();

        changeCamera(CameraUpdateFactory.newCameraPosition(search));
    }

    private void changeCamera(CameraUpdate update) {
        changeCamera(update, null);
    }

    private void changeCamera(CameraUpdate update, GoogleMap.CancelableCallback callback) {
        if (animateToggle.isChecked()) {
            if (customDurationToggle.isChecked()) {
                int duration = customDurationBar.getProgress();
                // The duration must be strictly positive so we make it at least 1.
                map.animateCamera(update, Math.max(duration, 1), callback);
            } else {
                map.animateCamera(update, callback);
            }
        } else {
            map.moveCamera(update);
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.addMarker(new MarkerOptions().position(hansung));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(hansung, 15));
    }
}