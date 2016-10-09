package net.guardduty.internet.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface RouteCreationListener {
    void onRouteCreated(JSONObject response);
    void onRouteCreationError(VolleyError error);
}
