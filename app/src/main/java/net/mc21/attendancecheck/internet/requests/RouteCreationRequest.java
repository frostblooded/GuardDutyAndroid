package net.mc21.attendancecheck.internet.requests;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import net.mc21.attendancecheck.common.InternetHelpers;
import net.mc21.attendancecheck.common.SPHelpers;
import net.mc21.attendancecheck.internet.abstracts.AbstractObjectRequest;
import net.mc21.attendancecheck.internet.interfaces.RouteCreationListener;

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
        String company_id = SPHelpers.getString(SPHelpers.SP_COMPANY_ID, context);
        String site_id = SPHelpers.getString(SPHelpers.SP_SITE_ID, context);
        String access_token = SPHelpers.getString(SPHelpers.SP_ACCESS_TOKEN, context);

        return InternetHelpers.SERVER_IP + "api/v1/companies/" + company_id + "/sites/" + site_id + "/routes?access_token=" + access_token;
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
