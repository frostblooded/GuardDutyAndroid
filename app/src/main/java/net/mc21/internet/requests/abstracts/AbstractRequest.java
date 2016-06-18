package net.mc21.internet.requests.abstracts;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

public abstract class AbstractRequest<T> {
    private final static int REQUEST_TIMEOUT = 5000;

    protected static RequestQueue requestQueue;

    protected abstract Request<T> getRequest();
    protected abstract int getMethod();
    protected abstract String getUrl();
    protected abstract T getData();
    protected abstract void onSuccess(T response);
    protected abstract void onError(VolleyError error);

    protected Context context;

    protected AbstractRequest(Context context) {
        this.context = context;
    }

    protected Response.Listener getListener() {
        return new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                onSuccess(response);
            }
        };
    }

    protected Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onError(error);
            }
        };
    }

    public void makeRequest() {
        Request<T> request = getRequest();

        // Set request timeout
        request.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        if(requestQueue == null)
            requestQueue = Volley.newRequestQueue(context);

        requestQueue.add(request);
    }
}