package net.mc21.connections.abstracts;

import android.content.Context;

import com.android.volley.VolleyError;

public abstract class AbstractRequest<T> {
    protected abstract int getMethod();
    protected abstract String getUrl();
    protected abstract T getData();
    protected abstract void onSuccess(T response);
    protected abstract void onError(VolleyError error);

    protected Context context;

    protected AbstractRequest(Context context) {
        this.context = context;
    }
}
