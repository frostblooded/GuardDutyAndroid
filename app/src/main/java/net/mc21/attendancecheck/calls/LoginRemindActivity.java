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

import net.mc21.attendancecheck.common.MiscHelpers;
import net.mc21.attendancecheck.main.MainActivity;
import net.mc21.attendancecheck.R;
import net.mc21.attendancecheck.main.WorkerLoginActivity;

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
        MiscHelpers.unlockScreen(this);
        MiscHelpers.setVolume(getApplicationContext());
        ringtone = MiscHelpers.startSound(getApplicationContext());
        startTimer();
        setUpButton();
        vibrator = MiscHelpers.startVibration(getApplicationContext());
    }
}
