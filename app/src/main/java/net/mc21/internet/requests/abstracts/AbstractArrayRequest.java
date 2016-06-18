package net.mc21.internet.requests.abstracts;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

public abstract class AbstractArrayRequest extends AbstractRequest<JSONArray> {

    protected AbstractArrayRequest(Context context) {
        super(context);
    }

    @Override
    protected Request<JSONArray> getRequest() {
        return new JsonArrayRequest(getMethod(), getUrl(), getData(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        onSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onError(error);
                    }
                }
        );
    }
}
