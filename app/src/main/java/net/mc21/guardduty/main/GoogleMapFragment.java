package net.mc21.guardduty.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
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

import net.mc21.guardduty.common.GPSHelpers;
import net.mc21.guardduty.common.MiscHelpers;

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

    public boolean isReady() {
        return mMap != null;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!GPSHelpers.GPSIsEnabled(getContext())) {
            GPSHelpers.enableGPS(getContext());
            MiscHelpers.showToast("Please turn on the GPS. Otherwise the map won't work properly.", getActivity());
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
            requestLocationPermissions();
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
                    MiscHelpers.showToast("Permission granted!", getActivity());
                    initMap();
                } else {
                    MiscHelpers.showToast("The map can't find your location! Please enter the map again and allow it to use GPS.", getActivity());
                    getActivity().finish();
                }

                break;
        }
    }

    private void initMap() {
        setLocationEnabled();
        setCameraOnDevicePos();
    }

    private void requestLocationPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), PERMISSION_1))
            MiscHelpers.showToast("GPS is required to get your location. Please allow it.", getActivity());

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
        Location location = GPSHelpers.getLastKnownLocation(getContext());

        if(location == null) {
            Log.i(MainActivity.TAG, "Last known location is null");
            return;
        }

        LatLng devicePos = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(devicePos, START_ZOOM));
    }
}
