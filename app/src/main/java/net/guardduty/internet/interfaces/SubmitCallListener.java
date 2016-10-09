package net.guardduty.internet.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface SubmitCallListener {
    void onCallSubmitted(JSONObject response);
    void onCallSubmitError(VolleyError error);
}
