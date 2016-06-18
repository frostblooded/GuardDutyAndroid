package net.mc21.calls;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
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

import net.mc21.attendancecheck.MainActivity;
import net.mc21.attendancecheck.R;
import net.mc21.attendancecheck.WorkerLoginActivity;

public class LoginRemindActivity extends AppCompatActivity {
    private static final int SECOND = 1000;
    private static final int DEFAULT_ALARM_TIME = 60 * SECOND;
    private static final int TICK_INTERVAL = SECOND;

    private Ringtone ringtone;
    private CountDownTimer timer;
    private Vibrator vibrator;

    public static void makeRemind(Context context) {
        Intent intent = new Intent(context, LoginRemindActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void exit(){
        ringtone.stop();
        timer.cancel();
        vibrator.cancel();

        Intent i = new Intent(getApplicationContext(), WorkerLoginActivity.class);
        startActivity(i);

        finish();
    }

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
        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);

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
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                exit();
            }
        }.start();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i(MainActivity.TAG, "Starting login remind...");
        setContentView(R.layout.activity_login_remind);
        unlockScreen();
        setVolume();
        startSound();
        startTimer();
        setUpButton();
        startVibration();
    }
}
