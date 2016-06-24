package net.mc21.attendancecheck.calls;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

import net.mc21.attendancecheck.common.MiscHelpers;
import net.mc21.attendancecheck.main.MainActivity;
import net.mc21.attendancecheck.R;

public class CallActivity extends AbstractCallActivity {
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
}
