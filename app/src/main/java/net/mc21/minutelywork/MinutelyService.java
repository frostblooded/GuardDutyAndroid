package net.mc21.minutelywork;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.mc21.calls.CallActivity;
import net.mc21.attendancecheck.MainActivity;
import net.mc21.attendancecheck.R;
import net.mc21.attendancecheck.SPManager;
import net.mc21.attendancecheck.WakeLockManager;
import net.mc21.calls.LoginRemindActivity;
import net.mc21.connections.HTTP;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MinutelyService extends Service {
    private final static int SECOND = 1000;
    private final static int MINUTE = 60 * SECOND;
    private final static int NOTIFICATION_ID = 19;

    private final static int MINUTES_BETWEEN_CALLS = 15;

    private static Handler handler = new Handler();
    public static boolean isRunning = false;

    private int minutesSinceLastCall = 0;
    private int shiftStart;
    private int shiftEnd;

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

        if(shiftStart < shiftEnd)
            return currentHour >= shiftStart && currentHour < shiftEnd;
        else
            return currentHour < shiftEnd || currentHour >= shiftStart;
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
                    doMinutelyWork();
                    handler.postDelayed(this, SECOND);
                }
            }, SECOND);
        }

        return START_STICKY;
    }

    private void doMinutelyWork() {
        minutesSinceLastCall++;

        if(minutesSinceLastCall >= MINUTES_BETWEEN_CALLS) {
            updateSettings();
            minutesSinceLastCall = 0;
        }
    }

    private int getHour(String timeString) {
        // Most important part of function
        // It handles PM and AM and gives hour of day(6 PM becomes 18)
        SimpleDateFormat sdf = new SimpleDateFormat("K:mm aa");
        Date date = null;

        try {
            date = sdf.parse(timeString);
        } catch (ParseException e) {
            Log.i(MainActivity.TAG, "Date parse error: " + e.toString());
            e.printStackTrace();
        }

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    private void updateSettings() {
        String siteId = SPManager.getString(SPManager.SP_SITE_ID, getApplicationContext());

        // If company has logged in
        if(siteId != null) {
            String accessToken = SPManager.getString(SPManager.SP_ACCESS_TOKEN, getApplicationContext());
            String companyId = SPManager.getString(SPManager.SP_COMPANY_ID, getApplicationContext());
            String url = HTTP.SERVER_IP + "api/v1/companies/" + companyId + "/sites/" + siteId + "/settings?access_token=" + accessToken;

            HTTP.requestObject(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        shiftStart = getHour(response.getString("shift_start"));
                        shiftEnd = getHour(response.getString("shift_end"));
                    } catch (JSONException e) {
                        Log.i(MainActivity.TAG, "JSON parse error: " + e.toString());
                        e.printStackTrace();
                    }

                    startCall();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    HTTP.handleError(error, getApplicationContext());
                    startCall();
                }
            }, getApplicationContext());
        } else {
            Log.i(MainActivity.TAG, "Settings not configured!");
            MainActivity.showToast("AttendanceCheck settings not configured!", getApplicationContext());
        }
    }

    private void startCall() {
        // If device has Doze, wake it up before the minutely work,
        // so that all network operations work correctly
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            WakeLockManager.acquire(getApplicationContext());

        if(isShift()) {
            if (isLoggedIn())
                CallActivity.makeCall(getApplicationContext());
            else
                LoginRemindActivity.makeRemind(getApplicationContext());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            WakeLockManager.release();
    }
}
