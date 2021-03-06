package com.example.android_team_project_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.MessageFormat;

public class ScheduleActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mGoogleMap = null;
    private MyDBHelper mDbHelper;
    Intent intent;
    int key, sHour = 0, eHour = 0;
<<<<<<< HEAD
<<<<<<< HEAD
    int Position;
    String type;
    private final LatLng hansung = new LatLng(37.5822608, 127.0094254);
    private final MarkerOptions marker_hansung = new MarkerOptions().position(hansung);
=======
=======
>>>>>>> parent of 85ba184 (WeekSchedule +)
    String type = "";
    private LatLng hansung = new LatLng(37.5822608, 127.0094254);
    private MarkerOptions marker_hansung = new MarkerOptions().position(hansung);
>>>>>>> parent of 85ba184 (WeekSchedule +)
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map); // 구글맵 프래그먼트 생성
        assert mapFragment != null;
        mapFragment.getMapAsync(ScheduleActivity.this);

        intent = getIntent(); // 이전 프래그먼트로부터 데이터를 받아온다
        mDbHelper = new MyDBHelper(this); // 데이터베이스 생성

        TimePicker timeStart = findViewById(R.id.timeStart);
        TimePicker timeEnd = findViewById(R.id.timeEnd);

        EditText editTitle = findViewById(R.id.editTitle);
        EditText editPlace = (EditText) findViewById(R.id.editPlace);
        EditText editMemo = (EditText) findViewById(R.id.editMemo);
        editTitle.setHint(MainActivity.ClickPoint);

<<<<<<< HEAD
<<<<<<< HEAD
        String[] date = String.valueOf(MainActivity.ClickPoint).split("[.]");
        setTitle(date[0] + "년 " + date[1] + "월 " + date[2] + "일");

        intent_save = new Intent(this, MainActivity.class);

        type = intent_load.getStringExtra("type");
        intent_save.putExtra("type", type);

        if(type.equals("month")) {
            Position = intent_load.getIntExtra("Month_Position", Integer.MAX_VALUE / 2);
            intent_save.putExtra("Month_Position", Position);
        }

        else if(type.equals("week")) {
            Position = intent_load.getIntExtra("Week_Position", Integer.MAX_VALUE / 2);
            intent_save.putExtra("Week_Position", Position + 1);
        }

        key = intent_load.getIntExtra("selected", -1);

=======
        type = intent.getStringExtra("type");
        key = intent.getIntExtra("selected", -1);
>>>>>>> parent of 85ba184 (WeekSchedule +)
=======
        type = intent.getStringExtra("type");
        key = intent.getIntExtra("selected", -1);
>>>>>>> parent of 85ba184 (WeekSchedule +)
        if (key != -1) {
            Cursor cursor = mDbHelper.getAllUsersByMethod();

            cursor.moveToPosition(key);

            editTitle.setText(cursor.getString(1));
            editPlace.setText(MessageFormat.format("{0}/{1}", cursor.getString(5), cursor.getString(6)));
            editMemo.setText(cursor.getString(7));

            timeStart.setHour(Integer.parseInt(cursor.getString(3)));
            timeStart.setMinute(0);
            timeEnd.setHour(Integer.parseInt(cursor.getString(4)));
            timeEnd.setMinute(0);
        }

        else {
            int time = intent.getIntExtra("time", 0);
            timeStart.setHour(time);
            sHour = time;
            timeStart.setMinute(0);
            timeEnd.setHour(time + 1);
            eHour = time + 1;
            timeEnd.setMinute(0);
        }

        timeStart.setOnTimeChangedListener((timePicker, h, m) -> {
            sHour = h;
            if (sHour <= 22)
                timeEnd.setHour(h + 1);
            else if (sHour == 23)
                timeEnd.setHour(0);
            timeEnd.setMinute(m);
        });

        timeEnd.setOnTimeChangedListener((timePicker, h, m) -> {
            eHour = h;
            if (eHour >= 1)
                timeStart.setHour(h - 1);
            else if (eHour == 0)
                timeStart.setHour(23);
            timeEnd.setMinute(m);
        });

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> finish()); // 취소 버튼 클릭 시 액티비티를 종료
    }

    @SuppressLint("Range")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) { // 이전 프래그먼트에서 받아온 데이터에 해당하는 데이터베이스에 저장된 주소로 구글맵을 이동
        mGoogleMap = googleMap;
        Cursor cursor = mDbHelper.getAllUsersByMethod();
        if (key == -1) {
            defaultMapReady();
            return;
        }
        cursor.moveToPosition(key);

        if (Double.parseDouble(cursor.getString(6)) == 0.0 && Double.parseDouble(cursor.getString(6)) == 0.0) {
            defaultMapReady();
            return;
        }

        LatLng location = new LatLng(Double.parseDouble(cursor.getString(5)), Double.parseDouble(cursor.getString(6)));

        if (marker != null)
            marker.remove();

        mGoogleMap.addMarker(new MarkerOptions().position(location));
        // move the camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    public void defaultMapReady() {
        if (marker != null)
            marker.remove();

        marker = mGoogleMap.addMarker(marker_hansung);
        // move the camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hansung, 15));
    }

    public void getAddress(View view) { // 주소를 검색하는 함수
        EditText editText = (EditText) findViewById(R.id.editPlace);
        String[] Locate = editText.getText().toString().split("/");

        if (marker != null)
            marker.remove();

        LatLng location = new LatLng(Double.parseDouble(Locate[0]), Double.parseDouble(Locate[1]));

        marker = mGoogleMap.addMarker(new MarkerOptions().position(location));
        // move the camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    public void insertRecord(View view) { // 저장 버튼을 누르면 해당 액티비티에 적힌 데이터를 SQL에 저장
        mDbHelper = new MyDBHelper(this);
        EditText editTitle = (EditText) findViewById(R.id.editTitle);
        EditText editPlace = (EditText) findViewById(R.id.editPlace);
        EditText editMemo = (EditText) findViewById(R.id.editMemo);
        String Title = editTitle.getText().toString();
        String Memo = editMemo.getText().toString();
        String[] Place = editPlace.getText().toString().split("/");

        mDbHelper.insertUserByMethod(Title, MainActivity.ClickPoint, sHour + "", eHour + "", Place[0], Place[1], Memo);
        finish();
        Intent intent_save = new Intent(this, MainActivity.class);
        intent_save.putExtra("type", type);
        startActivity(intent_save);
    }

    public void deleteRecord(View view) { // 삭제 버튼을 누르면 해당 데이터에 적힌 데이터를 SQL에서 삭제
        mDbHelper.delete(MainActivity.ClickPoint, sHour + "", eHour + "");
        finish();
        Intent intent_delete = new Intent(this, MainActivity.class);
        intent_delete.putExtra("type", type);
        startActivity(intent_delete);
    }
}