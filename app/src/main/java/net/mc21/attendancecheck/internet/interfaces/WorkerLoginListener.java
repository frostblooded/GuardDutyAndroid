package net.mc21.attendancecheck.internet.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface WorkerLoginListener {
    void onWorkerLogin(JSONObject response);
    void onWorkerLoginError(VolleyError error);
}
