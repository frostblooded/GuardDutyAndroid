package net.guardduty.calls;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import net.guardduty.R;
import net.guardduty.common.MiscHelpers;
import net.guardduty.mainactivities.MainActivity;

public abstract class AbstractCallActivity extends AppCompatActivity {
    protected static final int SECOND = 1000;
    protected static final int DEFAULT_ALARM_TIME = 60 * SECOND;
    protected static final int TICK_INTERVAL = SECOND;

    protected Ringtone ringtone;
    protected CountDownTimer timer;
    protected Vibrator vibrator;

    protected abstract void runExitAction();
    protected abstract void onTimerTick(long millisUntilFinish);
    protected abstract void onTimerFinish();
    protected abstract void initialize();

    public static void makeCall(Class<? extends AbstractCallActivity> clazz, Context context) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra("startedByApp", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    protected void exit() {
        ringtone.stop();
        timer.cancel();
        vibrator.cancel();
        runExitAction();
        finish();
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

    private void startTimer(){
        //                                            + 2 so that it actually starts counting from alarm_time
        timer = new CountDownTimer(DEFAULT_ALARM_TIME + 2, TICK_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                onTimerTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                onTimerFinish();
            }
        }.start();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean startedByApp = getIntent().getBooleanExtra("startedByApp", false);

        if(startedByApp) {
            initialize();
            MiscHelpers.unlockScreen(this);
//            MiscHelpers.setVolume(getApplicationContext());
            ringtone = MiscHelpers.startSound(getApplicationContext());
            startTimer();
            setUpButton();
            vibrator = MiscHelpers.startVibration(getApplicationContext());
        } else {
            Log.i(MainActivity.TAG, "Call not started from app. Finishing.");
        }
    }
}
