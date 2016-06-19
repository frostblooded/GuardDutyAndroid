package net.mc21.attendancecheck.internet.requests;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import net.mc21.attendancecheck.internet.HTTP;
import net.mc21.attendancecheck.internet.requests.abstracts.AbstractObjectRequest;
import net.mc21.attendancecheck.internet.requests.interfaces.SettingsLoginListener;

import org.json.JSONObject;

public class SettingsLoginRequest extends AbstractObjectRequest {
    protected SettingsLoginListener listener;

    public SettingsLoginRequest(SettingsLoginListener listener, Context context) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected int getMethod() {
        return Request.Method.POST;
    }

    @Override
    protected String getUrl() {
        return HTTP.SERVER_IP + "api/v1/access_tokens";
    }

    @Override
    protected void onSuccess(JSONObject response) {
        listener.onSettingsLogin(response);
    }

    @Override
    protected void onError(VolleyError error) {
        listener.onSettingsLoginError(error);
    }
}
