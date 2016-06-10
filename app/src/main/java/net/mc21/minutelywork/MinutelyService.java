package net.mc21.minutelywork;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Response;

import net.mc21.attendancecheck.CallActivity;
import net.mc21.attendancecheck.MainActivity;
import net.mc21.attendancecheck.R;
import net.mc21.attendancecheck.SPManager;
import net.mc21.attendancecheck.WakeLockManager;
import net.mc21.connections.HTTP;
import net.mc21.gcm.MyGcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MinutelyService extends Service {
    private final static int SECOND = 1000;
    private final static int MINUTE = 60 * SECOND;
    private final static int NOTIFICATION_ID = 19;

    private static Handler handler = new Handler();
    public static boolean isRunning = false;

    private int minutesSinceLastCall = 0;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void runAsForeground() {
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setContentText(getString(R.string.app_name) + " " + getString(R.string.is_running))
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.drawable.cast_ic_notification_0)
                .setContentIntent(pendingIntent).build();
        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isRunning) {
            runAsForeground();
            isRunning = true;
            CallActivity.makeCall(getApplicationContext());

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    makeCall();
                    handler.postDelayed(this, SECOND * 5);
                }
            }, SECOND * 5);
        }

        return START_STICKY;
    }

    private void doMinutelyWork() {
        minutesSinceLastCall++;
        Log.i(MainActivity.TAG, "Minutes since last call: " + minutesSinceLastCall);

        if(minutesSinceLastCall >= 15) {
            makeCall();
            minutesSinceLastCall = 0;
        }
    }

    private void makeCall() {
        // If device has Doze, wake it up before the minutely work,
        // so that all network operations work correctly
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        //    WakeLockManager.acquire(getApplicationContext());

        CallActivity.makeCall(getApplicationContext());

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        //    WakeLockManager.release();
    }
}
