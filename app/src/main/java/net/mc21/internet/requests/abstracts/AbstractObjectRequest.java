package net.mc21.internet.requests.abstracts;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.mc21.internet.HTTP;

import org.json.JSONObject;

public abstract class AbstractObjectRequest extends AbstractRequest<JSONObject> {
    protected AbstractObjectRequest(Context context) {
        super(context);
    }

    public void run() {
        HTTP.requestObject(getMethod(), getUrl(), getData(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {onSuccess(response);};
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {onError(error);}
        }, context);
    }
}
