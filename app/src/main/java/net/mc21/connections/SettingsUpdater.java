package net.mc21.connections;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.mc21.attendancecheck.SPManager;

import org.json.JSONObject;

public class SettingsUpdater {
    public static void run (final UpdateSettingsListener usl, final Context context) {
        String companyId = SPManager.getString(SPManager.SP_COMPANY_ID, context);
        String siteId = SPManager.getString(SPManager.SP_SITE_ID, context);
        String accessToken = SPManager.getString(SPManager.SP_ACCESS_TOKEN, context);
        String url = HTTP.SERVER_IP + "api/v1/companies/" + companyId + "/sites/" + siteId + "/settings?access_token=" + accessToken;

        HTTP.requestObject(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {usl.onSettingsUpdated(response);}
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {usl.onSettingsUpdateError(error);}
        }, context);
    }
}
