package net.mc21.attendancecheck.internet.requests.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONArray;

public interface AcquireWorkersListener {
    void onWorkersAcquired(JSONArray response);
    void onWorkersAcquireError(VolleyError error);
}
