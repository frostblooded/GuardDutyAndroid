package net.guardduty.calls;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.VolleyError;

import net.guardduty.common.GuardDutyHelpers;
import net.guardduty.internet.interfaces.SubmitCallListener;
import net.guardduty.internet.requests.SubmitCallRequest;
import net.guardduty.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class RetryCallService extends Service implements SubmitCallListener {
    protected Bundle extras;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        extras = intent.getExtras();

        if(extras != null) {
            JSONObject json = new JSONObject();

            try {
                json.put("time_left", extras.getInt("time_left"));
                json.put("created_at", extras.getString("created_at"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String workerId = extras.getString("worker_id");
            String siteId = extras.getString("site_id");

            new SubmitCallRequest(this, siteId, workerId, getApplicationContext())
                    .setData(json)
                    .makeRequest();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCallSubmitted(JSONObject response) {
        Log.i(MainActivity.TAG, "Call retry sent successfully!");
    }

    @Override
    public void onCallSubmitError(VolleyError error) {
        Log.i(MainActivity.TAG, "Call retry failed!");
        GuardDutyHelpers.createRetryCallNotification(getApplicationContext(), extras);
    }
}
