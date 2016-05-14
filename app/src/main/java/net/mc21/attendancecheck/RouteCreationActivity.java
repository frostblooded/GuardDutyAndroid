package net.mc21.attendancecheck;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import net.mc21.connections.HTTP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    public void finishRoute(View v) {
        List<Marker> markers = mapFragment.getMarkers();
        JSONArray positionsJSON = new JSONArray();

        for(Marker m: markers) {
            JSONObject jsonObject = new JSONObject();
            LatLng position = m.getPosition();

            try {
                jsonObject.put("latitude", position.latitude);
                jsonObject.put("longitude", position.longitude);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            positionsJSON.put(jsonObject);
        }
    }
}
