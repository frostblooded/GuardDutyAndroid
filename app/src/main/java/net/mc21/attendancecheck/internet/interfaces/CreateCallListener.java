package net.mc21.attendancecheck.internet.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface CreateCallListener {
    void onCallCreated(JSONObject response);
    void onCallCreateError(VolleyError error);
}
