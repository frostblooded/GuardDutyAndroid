package net.mc21.attendancecheck.internet.requests;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import net.mc21.attendancecheck.SPManager;
import net.mc21.attendancecheck.internet.HTTP;
import net.mc21.attendancecheck.internet.requests.interfaces.UpdateSettingsListener;
import net.mc21.attendancecheck.internet.requests.abstracts.AbstractObjectRequest;

import org.json.JSONObject;

public class UpdateSettingsRequest extends AbstractObjectRequest {
    protected UpdateSettingsListener listener;

    public UpdateSettingsRequest(UpdateSettingsListener listener, Context context) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected int getMethod() {
        return Request.Method.GET;
    }

    @Override
    protected String getUrl() {
        String companyId = SPManager.getString(SPManager.SP_COMPANY_ID, context);
        String siteId = SPManager.getString(SPManager.SP_SITE_ID, context);
        String accessToken = SPManager.getString(SPManager.SP_ACCESS_TOKEN, context);
        return HTTP.SERVER_IP + "api/v1/companies/" + companyId + "/sites/" + siteId + "/settings?access_token=" + accessToken;
    }

    @Override
    protected void onSuccess(JSONObject response) {
        listener.onSettingsUpdated(response);
    }

    @Override
    protected void onError(VolleyError error) {
        listener.onSettingsUpdateError(error);
    }
}
