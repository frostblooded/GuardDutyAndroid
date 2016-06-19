package net.mc21.attendancecheck.internet.requests;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import net.mc21.attendancecheck.SPManager;
import net.mc21.attendancecheck.internet.HTTP;
import net.mc21.attendancecheck.internet.requests.abstracts.AbstractArrayRequest;
import net.mc21.attendancecheck.internet.requests.interfaces.AcquireSitesListener;

import org.json.JSONArray;

public class AcquireSitesRequest extends AbstractArrayRequest {
    AcquireSitesListener listener;

    public AcquireSitesRequest(AcquireSitesListener listener, Context context) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected int getMethod() {
        return Request.Method.GET;
    }

    @Override
    protected String getUrl() {
        String company_id = SPManager.getString(SPManager.SP_COMPANY_ID, context);
        String token = SPManager.getString(SPManager.SP_ACCESS_TOKEN, context);
        return HTTP.SERVER_IP + "api/v1/companies/" + company_id + "/sites?access_token=" + token;
    }

    @Override
    protected void onSuccess(JSONArray response) {
        listener.onSitesAcquired(response);
    }

    @Override
    protected void onError(VolleyError error) {
        listener.onSitesAcquireError(error);
    }
}