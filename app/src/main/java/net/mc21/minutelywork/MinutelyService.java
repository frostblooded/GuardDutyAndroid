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

import net.mc21.attendancecheck.MainActivity;
import net.mc21.attendancecheck.R;
import net.mc21.attendancecheck.SPManager;
import net.mc21.attendancecheck.WakeLockManager;
import net.mc21.connections.HTTP;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MinutelyService extends Service {
    private final static int SECOND = 1000;
    private final static int MINUTE = 60 * SECOND;
    private final static int NOTIFICATION_ID = 19;
    private static Handler handler = new Handler();

    public static boolean isRunning = false;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void runAsForeground() {
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setContentText("AttendanceCheck is running")
                .setContentTitle("AttendanceCheck")
                .setSmallIcon(R.drawable.cast_ic_notification_0)
                .setContentIntent(pendingIntent).build();
        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isRunning) {
            runAsForeground();
            isRunning = true;
            Log.i(MainActivity.TAG, "Starting service!");

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i(MainActivity.TAG, "Tick");
                    //doMinutelyWork();
                    handler.postDelayed(this, SECOND * 5);
                }
            }, SECOND * 5);
        }

        return START_STICKY;
    }

    private void doMinutelyWork() {
        // If device has Doze, wake it up before the minutely work,
        // so that all network operations work correctly
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            WakeLockManager.acquire(getApplicationContext());

        String access_token = SPManager.getString(SPManager.SP_ACCESS_TOKEN, getApplicationContext());
        String gcmToken = null;

        try{
            gcmToken = SPManager.getGCMToken(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String url = HTTP.SERVER_IP + "api/v1/devices/" + gcmToken + "?access_token=" + access_token;
        HTTP.GET(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(MainActivity.TAG, "Getting worker status check");

                HTTP.GET(url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(MainActivity.TAG, "Getting worker status check");

                        HTTP.GET(url, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(MainActivity.TAG, "Getting worker status check");

                                HTTP.GET(url, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.i(MainActivity.TAG, "Getting worker status check");

                                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                                            WakeLockManager.release();
                                    }
                                }, null, getApplicationContext());
                            }
                        }, null, getApplicationContext());
                    }
                }, null, getApplicationContext());
            }
        }, null, getApplicationContext());
    }
}
