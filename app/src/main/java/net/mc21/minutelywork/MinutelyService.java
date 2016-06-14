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
import java.util.Calendar;
import java.util.Date;

public class MinutelyService extends Service {
    private final static int SECOND = 1000;
    private final static int MINUTE = 60 * SECOND;
    private final static int SHIFT_START = 6;
    private final static int SHIFT_END = 15;
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

    private boolean isShift() {
        Calendar cal = Calendar.getInstance();
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);

        if(SHIFT_START < SHIFT_END)
            return currentHour >= SHIFT_START && currentHour < SHIFT_END;
        else
            return currentHour < SHIFT_END || currentHour >= SHIFT_START;
    }

    private boolean isLoggedIn() {
        return SPManager.getString(SPManager.SP_WORKER_ID, getApplicationContext()) != null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isRunning) {
            runAsForeground();
            isRunning = true;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startCall();
                    handler.postDelayed(this, SECOND * 5);
                }
            }, SECOND * 5);
        }

        return START_STICKY;
    }

    private void doMinutelyWork() {
        minutesSinceLastCall++;

        if(minutesSinceLastCall >= 15) {
            startCall();
            minutesSinceLastCall = 0;
        }
    }

    private void startCall() {
        // If device has Doze, wake it up before the minutely work,
        // so that all network operations work correctly
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            WakeLockManager.acquire(getApplicationContext());

        if(isShift()) {
            if (isLoggedIn()) {
                CallActivity.makeCall(getApplicationContext());
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            WakeLockManager.release();
    }
}
