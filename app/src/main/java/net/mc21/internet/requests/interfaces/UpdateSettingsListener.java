package net.mc21.internet.requests.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface UpdateSettingsListener {
    void onSettingsUpdated(JSONObject response);
    void onSettingsUpdateError(VolleyError error);
}
