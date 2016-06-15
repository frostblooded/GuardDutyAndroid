package net.mc21.attendancecheck;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class GoogleMapFragment extends SupportMapFragment implements OnMapReadyCallback {
    private final static int ACCESS_LOCATION_REQUEST_CODE = 19;
    private final static String PERMISSION_1 = Manifest.permission.ACCESS_FINE_LOCATION;
    private final static String PERMISSION_2 = Manifest.permission.ACCESS_COARSE_LOCATION;

    private final static int START_ZOOM = 15;
    private GoogleMap mMap;
    private List<Marker> markers;

    public void createMarker(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Marker m = mMap.addMarker(new MarkerOptions().position(latLng));
        markers.add(m);
    }

    public List<Marker> getMarkers() {
        return markers;
    }

    public void enableGPS() {
        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(i);
    }

    public boolean GPSIsEnabled() {
        LocationManager manager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean isReady() {
        return mMap != null;
    }

    public Location getLastKnownLocation() {
        LocationManager mLocationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;

        for(String provider: providers) {
            // It is okay if the bottom line is underlined... depending on how you use it
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

    @Override
    public void onResume() {
        super.onResume();

        if(!GPSIsEnabled()) {
            enableGPS();
            MainActivity.showToast("Please turn on the GPS. Otherwise the map won't work properly.", getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        markers = new ArrayList<>();
        getMapAsync(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        boolean hasPermissions = ActivityCompat.checkSelfPermission(getActivity(), PERMISSION_1) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), PERMISSION_2) == PackageManager.PERMISSION_GRANTED;

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
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    MainActivity.showToast("Permission granted!", getActivity());
                    initMap();
                } else {
                    MainActivity.showToast("The map can't find your location! Please enter the map again and allow it to use GPS.", getActivity());
                    getActivity().finish();
                }

                break;
        }
    }

    private void initMap() {
        setLocationEnabled();
        setCameraOnDevicePos();
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), PERMISSION_1)) {
            MainActivity.showToast("GPS is required to get your location. Please allow it.", getActivity());
        }

        ActivityCompat.requestPermissions(getActivity(), new String[]{PERMISSION_1, PERMISSION_2}, ACCESS_LOCATION_REQUEST_CODE);
    }

    private void setLocationEnabled() {
        boolean hasPermissions = ActivityCompat.checkSelfPermission(getActivity(), PERMISSION_1) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), PERMISSION_2) == PackageManager.PERMISSION_GRANTED;

        if (!hasPermissions) {
            Log.i(MainActivity.TAG, "No permissions for location!");
            return;
        }

        mMap.setMyLocationEnabled(true);
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
}
