package net.mc21.attendancecheck.internet.requests;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import net.mc21.attendancecheck.main.SPManager;
import net.mc21.attendancecheck.internet.HTTP;
import net.mc21.attendancecheck.internet.requests.abstracts.AbstractObjectRequest;
import net.mc21.attendancecheck.internet.requests.interfaces.RouteCreationListener;

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
        String company_id = SPManager.getString(SPManager.SP_COMPANY_ID, context);
        String site_id = SPManager.getString(SPManager.SP_SITE_ID, context);
        String access_token = SPManager.getString(SPManager.SP_ACCESS_TOKEN, context);

        return HTTP.SERVER_IP + "api/v1/companies/" + company_id + "/sites/" + site_id + "/routes?access_token=" + access_token;
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
