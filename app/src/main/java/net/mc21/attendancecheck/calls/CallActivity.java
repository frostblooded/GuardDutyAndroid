package net.mc21.attendancecheck.calls;

import android.util.Log;
import android.widget.TextView;

import com.android.volley.VolleyError;

import net.mc21.attendancecheck.internet.interfaces.SubmitCallListener;
import net.mc21.attendancecheck.internet.requests.SubmitCallRequest;
import net.mc21.attendancecheck.main.MainActivity;
import net.mc21.attendancecheck.R;

import org.json.JSONException;
import org.json.JSONObject;

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
        Log.i(MainActivity.TAG, "Error while sending call!");
    }
}
