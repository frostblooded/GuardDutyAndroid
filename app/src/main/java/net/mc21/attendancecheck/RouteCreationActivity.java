package net.mc21.attendancecheck;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RouteCreationActivity extends FragmentActivity implements OnMapReadyCallback {
    private final static int ACCESS_LOCATION_REQUEST_CODE = 19;
    private final static String PERMISSION_1 = Manifest.permission.ACCESS_FINE_LOCATION;

    private Activity activity;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_route_creation);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSION_1)) {
            MainActivity.showToast("GPS is required to get your location. Please allow it.", getApplicationContext());
        }

        ActivityCompat.requestPermissions(activity, new String[]{PERMISSION_1}, ACCESS_LOCATION_REQUEST_CODE);
    }

    private void setLocationEnabled() {
        boolean hasPermissions = ActivityCompat.checkSelfPermission(this, PERMISSION_1) == PackageManager.PERMISSION_GRANTED;

        if (!hasPermissions) {
            Log.i(MainActivity.TAG, "No permissions for location!");
            return;
        }

        mMap.setMyLocationEnabled(true);
    }

    private void setCameraOnDevicePos() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        boolean hasPermissions = ActivityCompat.checkSelfPermission(this, PERMISSION_1) == PackageManager.PERMISSION_GRANTED;

        if (!hasPermissions) {
            Log.i(MainActivity.TAG, "No permissions for location!");
            return;
        }

        Location location = locationManager.getLastKnownLocation(provider);
        LatLng devicePos = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(devicePos));
    }

    private void initMap() {
        setLocationEnabled();
        setCameraOnDevicePos();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        boolean hasPermissions = ActivityCompat.checkSelfPermission(this, PERMISSION_1) == PackageManager.PERMISSION_GRANTED;

        if (!hasPermissions) {
            requestPermission();
            return;
        }

        initMap();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case ACCESS_LOCATION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MainActivity.showToast("Permission granted!", getApplicationContext());
                    initMap();
                } else {
                    MainActivity.showToast("The map can't find your location! Please enter the map again and allow it to use GPS.", getApplicationContext());
                    finish();
                }

                break;
        }
    }
}
