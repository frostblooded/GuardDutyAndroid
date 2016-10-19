package net.guardduty.internet.requests;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import net.guardduty.common.InternetHelpers;
import net.guardduty.common.SPHelpers;
import net.guardduty.internet.abstracts.AbstractObjectRequest;
import net.guardduty.internet.interfaces.WorkerLoginListener;

import org.json.JSONObject;

public class WorkerLoginRequest extends AbstractObjectRequest {
    protected WorkerLoginListener listener;
    protected String workerId;

    public WorkerLoginRequest(WorkerLoginListener listener, String workerId, Context context) {
        super(context);
        this.listener = listener;

        // Worker id is gotten from an array from previous response,
        // so it needs to be passed
        this.workerId = workerId;
    }

    @Override
    protected int getMethod() {
        return Request.Method.POST;
    }

    @Override
    protected String getUrl() {
        String accessToken = SPHelpers.getString(SPHelpers.SP_ACCESS_TOKEN, context);
        String siteId = SPHelpers.getString(SPHelpers.SP_SITE_ID, context);
        return InternetHelpers.SERVER_IP + "api/v1/sites/" + siteId + "/workers/" + workerId + "/login?access_token=" + accessToken;
    }

    @Override
    protected void onSuccess(JSONObject response) {
        listener.onWorkerLogin(response);
    }

    @Override
    protected void onError(VolleyError error) {
        listener.onWorkerLoginError(error);
    }
}
