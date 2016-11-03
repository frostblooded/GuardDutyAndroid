package net.guardduty.calls;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.VolleyError;

import net.guardduty.common.NotificationHelpers;
import net.guardduty.common.SPHelpers;
import net.guardduty.internet.interfaces.SubmitCallListener;
import net.guardduty.internet.requests.SubmitCallRequest;
import net.guardduty.main.MainActivity;
import net.guardduty.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CallActivity extends AbstractCallActivity implements SubmitCallListener {
    private int remainingSeconds;

    private void sendResult(){
        JSONObject json = new JSONObject();

        try {
            json.put("time_left", remainingSeconds);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new SubmitCallRequest(this, getApplicationContext()).setData(json).makeRequest();
    }

    @Override
    protected void runExitAction() {
        sendResult();
    }

    @Override
    protected void onTimerTick(long millisUntilFinished) {
        remainingSeconds = (int)(millisUntilFinished / 1000);
        TextView remainingTime = (TextView) findViewById(R.id.call_remaining_time_value);
        remainingTime.setText(String.valueOf(remainingSeconds) + " секунди");
    }

    @Override
    protected void onTimerFinish() {
        remainingSeconds = 0;
        exit();
    }

    @Override
    protected void initialize() {
        Log.i(MainActivity.TAG, "Starting call...");
        setContentView(R.layout.activity_call);
        remainingSeconds = DEFAULT_ALARM_TIME;
    }

    @Override
    public void onCallSubmitted(JSONObject response) {
        Log.i(MainActivity.TAG, "Call sent successfully!");
    }

    @Override
    public void onCallSubmitError(VolleyError error) {
        Log.i(MainActivity.TAG, "Call submission failed!");

        // Get current time in ISO8601
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.UK);
        String formattedDate = sdf.format(new Date());

        Bundle extras = new Bundle();
        extras.putInt("time_left", remainingSeconds);
        extras.putString("worker_id", SPHelpers.getString(SPHelpers.SP_WORKER_ID, getApplicationContext()));
        extras.putString("site_id", SPHelpers.getString(SPHelpers.SP_SITE_ID, getApplicationContext()));
        extras.putString("created_at", formattedDate);

        NotificationHelpers.createRetryCallNotification(getApplicationContext(), extras);
    }
}
