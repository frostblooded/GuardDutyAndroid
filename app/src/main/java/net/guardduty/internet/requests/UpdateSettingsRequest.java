package net.guardduty.internet.requests;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import net.guardduty.common.InternetHelpers;
import net.guardduty.common.SPHelpers;
import net.guardduty.internet.interfaces.UpdateSettingsListener;
import net.guardduty.internet.abstracts.AbstractObjectRequest;

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
        String companyId = SPHelpers.getString(SPHelpers.SP_COMPANY_ID, context);
        String siteId = SPHelpers.getString(SPHelpers.SP_SITE_ID, context);
        String accessToken = SPHelpers.getString(SPHelpers.SP_ACCESS_TOKEN, context);
        return InternetHelpers.SERVER_IP + "api/v1/companies/" + companyId + "/sites/" + siteId + "/settings?access_token=" + accessToken;
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
