package net.guardduty.internet.requests;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import net.guardduty.common.InternetHelpers;
import net.guardduty.common.SPHelpers;
import net.guardduty.internet.abstracts.AbstractObjectRequest;
import net.guardduty.internet.interfaces.SubmitCallListener;

import org.json.JSONObject;

public class SubmitCallRequest extends AbstractObjectRequest {
    protected SubmitCallListener listener;
    protected String workerId;
    protected String siteId;

    public SubmitCallRequest(SubmitCallListener listener, String siteId, String workerId, Context context) {
        super(context);
        this.listener = listener;
        this.siteId = siteId;
        this.workerId = workerId;
    }

    public SubmitCallRequest(SubmitCallListener listener, Context context) {
        this(listener, null, null, context);
    }

    public String getSiteId() {
        if(siteId != null)
            return siteId;

        return SPHelpers.getString(SPHelpers.SP_SITE_ID, context);
    }

    public String getWorkerId() {
        if(workerId != null)
            return workerId;

        return SPHelpers.getString(SPHelpers.SP_WORKER_ID, context);
    }

    @Override
    protected int getMethod() {
        return Request.Method.POST;
    }

    @Override
    protected String getUrl() {
        String token = SPHelpers.getString(SPHelpers.SP_ACCESS_TOKEN, context);
        return InternetHelpers.SERVER_IP + "api/v1/sites/" + getSiteId() +
                "/workers/" + getWorkerId() + "/calls?access_token=" + token;
    }

    @Override
    protected void onSuccess(JSONObject response) {
        listener.onCallSubmitted(response);
    }

    @Override
    protected void onError(VolleyError error) {
        listener.onCallSubmitError(error);
    }
}
