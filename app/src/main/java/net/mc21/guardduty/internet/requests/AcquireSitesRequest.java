package net.mc21.guardduty.internet.requests;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import net.mc21.guardduty.common.InternetHelpers;
import net.mc21.guardduty.common.SPHelpers;
import net.mc21.guardduty.internet.abstracts.AbstractArrayRequest;
import net.mc21.guardduty.internet.interfaces.AcquireSitesListener;

import org.json.JSONArray;

public class AcquireSitesRequest extends AbstractArrayRequest {
    protected AcquireSitesListener listener;

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
        String company_id = SPHelpers.getString(SPHelpers.SP_COMPANY_ID, context);
        String token = SPHelpers.getString(SPHelpers.SP_ACCESS_TOKEN, context);
        return InternetHelpers.SERVER_IP + "api/v1/companies/" + company_id + "/sites?access_token=" + token;
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
