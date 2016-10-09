package net.guardduty.internet.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONArray;

public interface AcquireSitesListener {
    void onSitesAcquired(JSONArray response);
    void onSitesAcquireError(VolleyError error);
}
