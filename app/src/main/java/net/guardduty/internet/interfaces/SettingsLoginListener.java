package net.guardduty.internet.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface SettingsLoginListener {
    void onSettingsLogin(JSONObject response);
    void onSettingsLoginError(VolleyError error);
}
