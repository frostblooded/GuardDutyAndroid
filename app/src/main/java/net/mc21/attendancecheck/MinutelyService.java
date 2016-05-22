package net.mc21.attendancecheck;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.Response;

import net.mc21.connections.HTTP;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MinutelyService extends Service {
    private final static int SECOND = 1000;
    private final static int MINUTE = 60 * SECOND;
    private static Handler handler = new Handler();

    public static boolean isRunning = false;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isRunning) {
            isRunning = true;
            Log.i(MainActivity.TAG, "Starting service!");

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i(MainActivity.TAG, "Tick");
                    doMinutelyWork();
                    handler.postDelayed(this, MINUTE);
                }
            }, MINUTE);
        }

        return START_STICKY;
    }

    private void doMinutelyWork() {
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
