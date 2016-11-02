package net.guardduty.calls;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
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
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        JSONObject json = new JSONObject();

        try {
            json.put("time_left", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new SubmitCallRequest(this, getApplicationContext()).setData(json).makeRequest();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCallSubmitted(JSONObject response) {
        Log.i(MainActivity.TAG, "Call retry sent successfully!");
    }

    @Override
    public void onCallSubmitError(VolleyError error) {
        Log.i(MainActivity.TAG, "Call retry failed!");
        GuardDutyHelpers.createRetryCallNotification(getApplicationContext());
    }
}
