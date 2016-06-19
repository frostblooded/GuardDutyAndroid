package net.mc21.attendancecheck.internet.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONArray;

public interface AcquireWorkersListener {
    void onWorkersAcquired(JSONArray response);
    void onWorkersAcquireError(VolleyError error);
}
