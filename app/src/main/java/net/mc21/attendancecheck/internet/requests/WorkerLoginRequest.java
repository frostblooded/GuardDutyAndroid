package net.mc21.attendancecheck.internet.requests;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import net.mc21.attendancecheck.common.InternetHelper;
import net.mc21.attendancecheck.main.SPManager;
import net.mc21.attendancecheck.internet.abstracts.AbstractObjectRequest;
import net.mc21.attendancecheck.internet.interfaces.WorkerLoginListener;

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
        String access_token = SPManager.getString(SPManager.SP_ACCESS_TOKEN, context);
        return InternetHelper.SERVER_IP + "api/v1/workers/" + workerId + "/check_login?access_token=" + access_token;
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
