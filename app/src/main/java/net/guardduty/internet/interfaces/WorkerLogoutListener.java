package net.guardduty.internet.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface WorkerLogoutListener {
    void onWorkerLogout(JSONObject response);
    void onWorkerLogoutError(VolleyError error);
}
