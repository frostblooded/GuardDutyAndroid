package net.mc21.attendancecheck;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import net.mc21.attendancecheck.internet.HTTP;
import net.mc21.attendancecheck.internet.requests.RouteCreationRequest;
import net.mc21.attendancecheck.internet.requests.interfaces.RouteCreationListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RouteCreationActivity extends AppCompatActivity implements RouteCreationListener {
    private GoogleMapFragment mapFragment;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_creation);
        mapFragment = (GoogleMapFragment) getSupportFragmentManager().findFragmentById(R.id.route_creation_map);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(progressDialog != null)
            progressDialog.dismiss();
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

        if(markers.size() == 0) {
            MainActivity.showToast("There are no markers! Please put some first.", getApplicationContext());
            return;
        }

        progressDialog = ProgressDialog.show(this, getString(R.string.please_wait), "Saving route");
        JSONArray positionsJSON = new JSONArray();
        JSONObject sentData = new JSONObject();

        try {
            for(Marker m: markers) {
                JSONObject jsonObject = new JSONObject();
                LatLng position = m.getPosition();
                jsonObject.put("latitude", position.latitude);
                jsonObject.put("longitude", position.longitude);
                positionsJSON.put(jsonObject);
            }

            sentData.put("positions", positionsJSON);
        } catch (JSONException e) {
            Log.i(MainActivity.TAG, "JSON error: " + e.toString());
            e.printStackTrace();
        }

        new RouteCreationRequest(this, getApplicationContext()).setData(sentData).makeRequest();
    }

    @Override
    public void onRouteCreated(JSONObject response) {
        Log.i(MainActivity.TAG, "Route creation response: " + response.toString());
        MainActivity.showToast("Route created successfully!", getApplicationContext());
        progressDialog.hide();
        finish();
    }

    @Override
    public void onRouteCreationError(VolleyError error) {
        HTTP.handleError(error, getApplicationContext());
        progressDialog.hide();
    }
}
