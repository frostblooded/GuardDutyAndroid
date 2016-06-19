package net.mc21.attendancecheck.internet.requests.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONArray;

public interface AcquireSitesListener {
    void onSitesAcquired(JSONArray response);
    void onSitesAcquireError(VolleyError error);
}
