package net.mc21.attendancecheck.internet.requests;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import net.mc21.attendancecheck.common.InternetHelpers;
import net.mc21.attendancecheck.common.SPHelpers;
import net.mc21.attendancecheck.internet.abstracts.AbstractObjectRequest;
import net.mc21.attendancecheck.internet.interfaces.WorkerLoginListener;
import net.mc21.attendancecheck.internet.interfaces.WorkerLogoutListener;

import org.json.JSONObject;

public class WorkerLogoutRequest extends AbstractObjectRequest {
    protected WorkerLogoutListener listener;
    protected String workerId;

    public WorkerLogoutRequest(WorkerLogoutListener listener, Context context) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected int getMethod() {
        return Request.Method.POST;
    }

    @Override
    protected String getUrl() {
        String accessToken = SPHelpers.getString(SPHelpers.SP_ACCESS_TOKEN, context);
        String workerId = SPHelpers.getString(SPHelpers.SP_WORKER_ID, context);
        return InternetHelpers.SERVER_IP + "api/v1/workers/" + workerId + "/logout?access_token=" + accessToken;
    }

    @Override
    protected void onSuccess(JSONObject response) {
        listener.onWorkerLogout(response);
    }

    @Override
    protected void onError(VolleyError error) {
        listener.onWorkerLogoutError(error);
    }
}
