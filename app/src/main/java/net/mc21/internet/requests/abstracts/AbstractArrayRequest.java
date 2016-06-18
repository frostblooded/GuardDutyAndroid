package net.mc21.internet.requests.abstracts;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

public abstract class AbstractArrayRequest extends AbstractRequest<JSONArray> {

    protected AbstractArrayRequest(Context context) {
        super(context);
    }

    @Override
    protected Request<JSONArray> getRequest() {
        return new JsonArrayRequest(getMethod(), getUrl(), getData(), getListener(), getErrorListener());
    }
}
