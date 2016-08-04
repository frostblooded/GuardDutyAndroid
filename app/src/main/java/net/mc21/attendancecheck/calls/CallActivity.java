package net.mc21.attendancecheck.calls;

import android.util.Log;
import android.widget.TextView;

import com.android.volley.VolleyError;

import net.mc21.attendancecheck.internet.interfaces.SubmitCallListener;
import net.mc21.attendancecheck.main.MainActivity;
import net.mc21.attendancecheck.R;

import org.json.JSONException;
import org.json.JSONObject;

public class CallActivity extends AbstractCallActivity implements SubmitCallListener {
    private int remainingSeconds;

    private void sendResult(){
        /*String access_token = SPHelpers.getString(SPHelpers.SP_ACCESS_TOKEN, getApplicationContext());
        String url = HTTP.SERVER_IP + "api/v1/calls/" + call_id + "?access_token=" + access_token;
        JSONObject json = new JSONObject();

        try {
            json.put("call_token", call_token);
            json.put("time_left", remainingSeconds);
            json.put("call_id", call_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HTTP.PUT(url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(MainActivity.TAG, "Token responding result: " + response.toString());
            }
        }, null, this);*/

        JSONObject json = new JSONObject();

        try {
            json.put("time_left", remainingSeconds);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void runExitAction() {
        //sendResult();
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
        Log.i(MainActivity.TAG, "Call create error");
    }
}
