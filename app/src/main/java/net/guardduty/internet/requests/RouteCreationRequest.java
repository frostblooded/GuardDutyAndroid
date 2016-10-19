package net.guardduty.internet.requests;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import net.guardduty.common.InternetHelpers;
import net.guardduty.common.SPHelpers;
import net.guardduty.internet.abstracts.AbstractObjectRequest;
import net.guardduty.internet.interfaces.RouteCreationListener;

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
        String siteId = SPHelpers.getString(SPHelpers.SP_SITE_ID, context);
        String accessToken = SPHelpers.getString(SPHelpers.SP_ACCESS_TOKEN, context);

        return InternetHelpers.SERVER_IP + "api/v1/sites/" + siteId + "/routes?access_token=" + accessToken;
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
