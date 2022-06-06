package com.example.android_team_project_2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class ScheduleActivity extends AppCompatActivity implements OnMapReadyCallback {

    //로그캣 사용 설정
    private static final String TAG = "MainActivity";

    EditText mId;
    EditText mMemo;
    EditText mPlace;

    private DBHelper mDbHelper;

    int sHour = 0, sMinute = 0, eHour = 0, eMinute = 0;

    LatLng hansung = new LatLng(37.5822608, 127.0094254);

    //객체 선언
    SupportMapFragment mapFragment;
    GoogleMap map;
    Button btnSearch;
    EditText editText;

    MarkerOptions myMarker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        mId = (EditText)findViewById(R.id.editTitle);
        mMemo = (EditText)findViewById(R.id.memo);
        mPlace = (EditText)findViewById(R.id.editText);

        mDbHelper = new DBHelper(this);

        //권한 설정
        checkDangerousPermissions();

        //객체 초기화
        editText = findViewById(R.id.editText);
        btnSearch = findViewById(R.id.search);

        //지도 프래그먼트 설정
                mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "onMapReady: ");
                map = googleMap;
                map.setMyLocationEnabled(true);
            }
        });
        MapsInitializer.initialize(this);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().length() > 0) {
                    Location location = getLocationFromAddress(getApplicationContext(), editText.getText().toString());

                    assert location != null;
                    showCurrentLocation(location);
                }
            }
        });

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
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    private void showCurrentLocation(Location location) {
        LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
        String msg = "Latitude : " + curPoint.latitude
                + "\nLongitude : " + curPoint.longitude;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        //화면 확대, 숫자가 클수록 확대
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));

        //마커 찍기
        Location targetLocation = new Location("");
        targetLocation.setLatitude(37.4937);
        targetLocation.setLongitude(127.0643);
        showMyMarker(targetLocation);
    }

    private void showMyMarker(Location location) {
        if(myMarker == null) {
            myMarker = new MarkerOptions();
            myMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
            myMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation));
            map.addMarker(myMarker);
        }
    }

    private void checkDangerousPermissions() {
        String[] permissions = {
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (String permission : permissions) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permission);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    private Location getLocationFromAddress(Context context, String address) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        Location resLocation = new Location("");
        try {
            addresses = geocoder.getFromLocationName(address, 5);
            if((addresses == null) || (addresses.size() == 0)) {
                return null;
            }
            Address addressLoc = addresses.get(0);

            resLocation.setLatitude(addressLoc.getLatitude());
            resLocation.setLongitude(addressLoc.getLongitude());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resLocation;
    }

    public void mClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                insertRecord();
                viewAllToTextView();
                viewAllToListView();
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


    private void viewAllToListView() {

        Cursor cursor = mDbHelper.getAllUsersByMethod();

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(),
                R.layout.item, cursor, new String[]{
                UserContract.Users._ID,
                UserContract.Users.KEY_NAME,
                UserContract.Users.KEY_PHONE},
                new int[]{R.id._id, R.id.name, R.id.phone}, 0);

        ListView lv = (ListView)findViewById(R.id.listview);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Adapter adapter = adapterView.getAdapter();

                mId.setText(((Cursor)adapter.getItem(i)).getString(0));
                mMemo.setText(((Cursor)adapter.getItem(i)).getString(1));
                mPhone.setText(((Cursor)adapter.getItem(i)).getString(2));
            }
        });
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    private void updateRecord() {
        TextView _id = (TextView)findViewById(R.id._id);
        TextView name = (TextView)findViewById(R.id.edit_name);
        TextView phone = (TextView)findViewById(R.id.edit_phone);

        mDbHelper.updateUserBySQL(_id.getText().toString(),name.getText().toString(),phone.getText().toString());
        long nOfRows = mDbHelper.updateUserByMethod(_id.getText().toString(),
                name.getText().toString(),
                phone.getText().toString());
        if (nOfRows >0)
            Toast.makeText(this,"Record Updated", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"No Record Updated", Toast.LENGTH_SHORT).show();
    }

    private void deleteRecord() {
        TextView _id = (TextView)findViewById(R.id._id);

        mDbHelper.deleteUserBySQL(_id.getText().toString());
        long nOfRows = mDbHelper.deleteUserByMethod(_id.getText().toString());
        if (nOfRows >0)
            Toast.makeText(this,"Record Deleted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"No Record Deleted", Toast.LENGTH_SHORT).show();
    }

    private void insertRecord() {
        TextView title = (TextView)findViewById(R.id.editTitle);
        TextView memo = (TextView)findViewById(R.id.memo);

        mDbHelper.insertUserBySQL(title.getText().toString(),memo.getText().toString());
        long nOfRows = mDbHelper.insertUserByMethod(title.getText().toString(),memo.getText().toString());
        if (nOfRows >0)
            Toast.makeText(this,nOfRows+" Record Inserted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"No Record Inserted", Toast.LENGTH_SHORT).show();
    }

    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(hansung));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hansung, 15));
    }
}