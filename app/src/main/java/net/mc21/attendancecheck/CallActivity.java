package net.mc21.attendancecheck;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;

import net.mc21.connections.HTTP;

import org.json.JSONException;
import org.json.JSONObject;

public class CallActivity extends AppCompatActivity {
    private static int DEFAULT_ALARM_TIME = 60 * 1000;
    private static int TICK_INTERVAL = 1000;

    public static boolean startedFromService = false;
    public int remainingSeconds;
    private Ringtone ringtone;
    private CountDownTimer timer;
    private Vibrator vibrator;

    public static void makeCall(Context context) {
        CallActivity.startedFromService = true;
        Intent intent = new Intent(context, CallActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void exit(){
        startedFromService = false;
        //sendResult();
        ringtone.stop();
        timer.cancel();
        vibrator.cancel();
        finish();
    }

    private void sendResult(){
        /*String access_token = SPManager.getString(SPManager.SP_ACCESS_TOKEN, getApplicationContext());
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

    //Uses deprecated code, but is the only solution I found
    protected void unlockScreen() {
        Window w = getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        w.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        w.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }


    private void startSound() {
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if(alert == null){
            // alert is null, using backup
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            if(alert == null){
                // backup is null, using second backup
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
        }
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), alert);

        if (ringtone != null) {
            ringtone.play();
        }
    }

    private void setUpButton(){
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(1000); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in

        Button button = (Button) findViewById(R.id.call_respond_button);
        button.setAnimation(animation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
    }

    private void startVibration(){
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = { 200, 800 };
        vibrator.vibrate(pattern, 0);
    }

    private void setVolume(){
        final AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_RING, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
    }

    private void startTimer(){
        //                                            + 2 so that it actually starts counting from alarm_time
        timer = new CountDownTimer(DEFAULT_ALARM_TIME + 2, TICK_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingSeconds = (int)(millisUntilFinished / 1000);
                TextView remainingTime = (TextView) findViewById(R.id.call_remaining_time_value);
                remainingTime.setText(String.valueOf(remainingSeconds) + " секунди");
            }

            @Override
            public void onFinish() {
                remainingSeconds = 0;
                exit();
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(MainActivity.TAG, "Got submission...");

        if(startedFromService){
            Log.i(MainActivity.TAG, "Handling submission...");
            setContentView(R.layout.activity_call);
            remainingSeconds = DEFAULT_ALARM_TIME;
            unlockScreen();
            setVolume();
            startSound();
            startTimer();
            setUpButton();
            startVibration();
        }
    }
}
