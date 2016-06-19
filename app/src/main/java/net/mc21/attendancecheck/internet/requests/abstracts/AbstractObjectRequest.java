package net.mc21.attendancecheck.internet.requests.abstracts;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public abstract class AbstractObjectRequest extends AbstractRequest<JSONObject> {
    protected AbstractObjectRequest(Context context) {
        super(context);
    }

    @Override
    protected Request<JSONObject> getRequest() {
        return new JsonObjectRequest(getMethod(), getUrl(), data, getListener(), getErrorListener());
    }
}
