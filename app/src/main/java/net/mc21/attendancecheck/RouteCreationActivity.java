package net.mc21.attendancecheck;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RouteCreationActivity extends AppCompatActivity {
    GoogleMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_creation);
        mapFragment = (GoogleMapFragment) getSupportFragmentManager().findFragmentById(R.id.route_creation_map);
    }

    public void createCheckpoint(View v) {
        if(!mapFragment.GPSIsEnabled()) {
            mapFragment.enableGPS();
            return;
        }

        if(!mapFragment.isReady()) {
            MainActivity.showToast("Map is not ready yet. Please wait a moment and try again!", getApplicationContext());
            return;
        }

        mapFragment.createMarker(mapFragment.getLastKnownLocation());
    }
}
