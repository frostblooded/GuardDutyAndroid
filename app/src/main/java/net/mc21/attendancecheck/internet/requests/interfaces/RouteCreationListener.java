package net.mc21.attendancecheck.internet.requests.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface RouteCreationListener {
    void onRouteCreated(JSONObject response);
    void onRouteCreationError(VolleyError error);
}
