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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

public class Call extends AppCompatActivity {
    private static int DEFAULT_SUBMISSION_INTERVAL = 15 * 60 * 1000;
    private static int DEFAILT_ALARM_TIME = 60 * 1000;
    private static int TICK_INTERVAL = 1000;
    private static int ALLOWED_PUSH_NOTIFICATION_DELAY = 2 * 60 * 1000;

    public static boolean startedFromPushNotification = false;
    public String id;
    public String receival_time;
    public int submission_interval;
    public int alarm_time;
    public String token;
    public int remainingSeconds;
    private PowerManager.WakeLock wakeLock;
    private Ringtone ringtone;
    private CountDownTimer timer;
    private Vibrator vibrator;

    private void exit(){
        startedFromPushNotification = false;
        wakeLock.release();
        ringtone.stop();
        timer.cancel();
        //new SubmissionHandler().execute(this);
        vibrator.cancel();
        finish();
    }

    //Uses deprecated code, but is the only solution I found
    protected void unlockScreen(){
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        final KeyguardManager.KeyguardLock kl = km.newKeyguardLock("MyKeyguardLock");
        kl.disableKeyguard();

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
        wakeLock.acquire();
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
        //                                    + 2 so that it actually starts counting from alarm_time
        timer = new CountDownTimer(alarm_time + 2, TICK_INTERVAL) {
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

    private void initializeVariables(){
        Intent intent = getIntent();

        receival_time = intent.getStringExtra("receival_time");
        token = intent.getStringExtra("token");
        submission_interval = intent.getIntExtra("submission_interval", DEFAULT_SUBMISSION_INTERVAL);
        alarm_time = intent.getIntExtra("alarm_time", DEFAILT_ALARM_TIME);
        id = intent.getStringExtra("id");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(startedFromPushNotification){
            initializeVariables();
            Log.i(MainActivity.TAG, "Handling submission...");
            setContentView(R.layout.activity_call);
            remainingSeconds = alarm_time;
            unlockScreen();
            setVolume();
            startSound();
            startTimer();
            setUpButton();
            startVibration();
        }
    }
}
