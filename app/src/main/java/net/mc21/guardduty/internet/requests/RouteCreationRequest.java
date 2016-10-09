package net.mc21.guardduty.internet.requests;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import net.mc21.guardduty.common.InternetHelpers;
import net.mc21.guardduty.common.SPHelpers;
import net.mc21.guardduty.internet.abstracts.AbstractObjectRequest;
import net.mc21.guardduty.internet.interfaces.RouteCreationListener;

import org.json.JSONObject;

public class RouteCreationRequest extends AbstractObjectRequest {
    protected RouteCreationListener listener;

    public RouteCreationRequest(RouteCreationListener listener, Context context) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected int getMethod() {
        return Request.Method.POST;
    }

    @Override
    protected String getUrl() {
        String companyId = SPHelpers.getString(SPHelpers.SP_COMPANY_ID, context);
        String siteId = SPHelpers.getString(SPHelpers.SP_SITE_ID, context);
        String accessToken = SPHelpers.getString(SPHelpers.SP_ACCESS_TOKEN, context);

        return InternetHelpers.SERVER_IP + "api/v1/companies/" + companyId + "/sites/" + siteId + "/routes?access_token=" + accessToken;
    }

    @Override
    protected void onSuccess(JSONObject response) {
        listener.onRouteCreated(response);
    }

    @Override
    protected void onError(VolleyError error) {
        listener.onRouteCreationError(error);
    }
}
