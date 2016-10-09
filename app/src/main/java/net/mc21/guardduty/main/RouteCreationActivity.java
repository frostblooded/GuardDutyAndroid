package net.mc21.guardduty.main;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import net.mc21.guardduty.R;
import net.mc21.guardduty.common.GPSHelpers;
import net.mc21.guardduty.common.MiscHelpers;
import net.mc21.guardduty.internet.requests.RouteCreationRequest;
import net.mc21.guardduty.internet.interfaces.RouteCreationListener;

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
        if(!GPSHelpers.GPSIsEnabled(getApplicationContext())) {
            GPSHelpers.enableGPS(getApplicationContext());
            return;
        }

        if(!mapFragment.isReady()) {
            MiscHelpers.showToast("Map is not ready yet. Please wait a moment and try again!", getApplicationContext());
            return;
        }

        mapFragment.createMarker(GPSHelpers.getLastKnownLocation(getApplicationContext()));
    }

    public void finishRoute(View v) {
        List<Marker> markers = mapFragment.getMarkers();

        if(markers.size() == 0) {
            MiscHelpers.showToast("There are no markers! Please put some first.", getApplicationContext());
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
        MiscHelpers.showToast("Route created successfully!", getApplicationContext());
        progressDialog.hide();
        finish();
    }

    @Override
    public void onRouteCreationError(VolleyError error) {
        progressDialog.hide();
    }
}
