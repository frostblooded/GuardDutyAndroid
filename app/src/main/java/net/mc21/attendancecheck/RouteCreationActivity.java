package net.mc21.attendancecheck;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import net.mc21.connections.HTTP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RouteCreationActivity extends AppCompatActivity {
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
        progressDialog = ProgressDialog.show(this, getString(R.string.please_wait), "Saving route");
        List<Marker> markers = mapFragment.getMarkers();
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

        String company_id = SPManager.getString(SPManager.SP_COMPANY_ID, getApplicationContext());
        String site_id = SPManager.getString(SPManager.SP_SITE_ID, getApplicationContext());
        String access_token = SPManager.getString(SPManager.SP_ACCESS_TOKEN, getApplicationContext());

        String url = HTTP.SERVER_IP + "api/v1/companies/" + company_id + "/sites/" + site_id + "/routes?access_token=" + access_token;
        HTTP.POST(url, sentData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(MainActivity.TAG, "Route creationg response: " + response.toString());
                MainActivity.showToast("Route created successfully!", getApplicationContext());
                progressDialog.dismiss();
                finish();
            }
        }, progressDialog, getApplicationContext());
    }
}
