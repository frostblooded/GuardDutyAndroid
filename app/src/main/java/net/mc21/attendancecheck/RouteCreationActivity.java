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

import java.util.List;

public class RouteCreationActivity extends FragmentActivity implements OnMapReadyCallback {
    private final static int ACCESS_LOCATION_REQUEST_CODE = 19;
    private final static String PERMISSION_1 = Manifest.permission.ACCESS_FINE_LOCATION;
    private final static String PERMISSION_2 = Manifest.permission.ACCESS_COARSE_LOCATION;

    private final static int START_ZOOM = 15;

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

    private void initMap() {
        setLocationEnabled();
        setCameraOnDevicePos();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        boolean hasPermissions = ActivityCompat.checkSelfPermission(this, PERMISSION_1) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, PERMISSION_2) == PackageManager.PERMISSION_GRANTED;

        if (!hasPermissions) {
            requestPermission();
            return;
        }

        initMap();
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSION_1)) {
            MainActivity.showToast("GPS is required to get your location. Please allow it.", getApplicationContext());
        }

        ActivityCompat.requestPermissions(activity, new String[]{PERMISSION_1, PERMISSION_2}, ACCESS_LOCATION_REQUEST_CODE);
    }

    private void setLocationEnabled() {
        boolean hasPermissions = ActivityCompat.checkSelfPermission(this, PERMISSION_1) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, PERMISSION_2) == PackageManager.PERMISSION_GRANTED;

        if (!hasPermissions) {
            Log.i(MainActivity.TAG, "No permissions for location!");
            return;
        }

        mMap.setMyLocationEnabled(true);
    }

    private Location getLastKnownLocation() {
        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;

        for(String provider: providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);

            if(l == null) {
                continue;
            }

            if(bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }

        return bestLocation;
    }

    private void setCameraOnDevicePos() {
        Location location = getLastKnownLocation();

        if(location == null) {
            Log.i(MainActivity.TAG, "Last known location is null");
            return;
        }

        LatLng devicePos = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(devicePos, START_ZOOM));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case ACCESS_LOCATION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
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
