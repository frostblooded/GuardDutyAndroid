package net.guardduty.calls;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.VolleyError;

import net.guardduty.internet.interfaces.SubmitCallListener;
import net.guardduty.internet.requests.SubmitCallRequest;
import net.guardduty.main.MainActivity;
import net.guardduty.R;

import org.json.JSONException;
import org.json.JSONObject;

public class CallActivity extends AbstractCallActivity implements SubmitCallListener {
    private int remainingSeconds;
    private final static int CALL_RETRY_NOTIFICATION_ID = 2;

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
        Log.i(MainActivity.TAG, "Error while sending call!");

        Intent notificationIntent = new Intent(getApplicationContext(), CallActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setContentText(getString(R.string.call_failed))
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.drawable.cast_ic_notification_0)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(CALL_RETRY_NOTIFICATION_ID, notification);
    }
}
