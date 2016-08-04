package net.mc21.attendancecheck.internet.requests;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import net.mc21.attendancecheck.common.InternetHelpers;
import net.mc21.attendancecheck.common.SPHelpers;
import net.mc21.attendancecheck.internet.abstracts.AbstractObjectRequest;
import net.mc21.attendancecheck.internet.interfaces.CreateCallListener;

import org.json.JSONObject;

public class CreateCallRequest extends AbstractObjectRequest {
    protected CreateCallListener listener;

    public CreateCallRequest(CreateCallListener listener, Context context) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected int getMethod() {
        return Request.Method.POST;
    }

    @Override
    protected String getUrl() {
        String token = SPHelpers.getString(SPHelpers.SP_ACCESS_TOKEN, context);
        String worker_id = SPHelpers.getString(SPHelpers.SP_WORKER_ID, context);
        return InternetHelpers.SERVER_IP + "api/v1/workers/" + worker_id + "/calls?access_token=" + token;
    }

    @Override
    protected void onSuccess(JSONObject response) {
        listener.onCallCreated(response);
    }

    @Override
    protected void onError(VolleyError error) {
        listener.onCallCreateError(error);
    }
}
