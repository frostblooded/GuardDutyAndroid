package net.mc21.attendancecheck.minutelyservice;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.android.volley.VolleyError;

import net.mc21.attendancecheck.calls.AbstractCallActivity;
import net.mc21.attendancecheck.calls.CallActivity;
import net.mc21.attendancecheck.common.MiscHelpers;
import net.mc21.attendancecheck.common.InternetHelpers;
import net.mc21.attendancecheck.common.SPHelpers;
import net.mc21.attendancecheck.main.MainActivity;
import net.mc21.attendancecheck.R;
import net.mc21.attendancecheck.common.WakeLockManager;
import net.mc21.attendancecheck.calls.LoginRemindActivity;
import net.mc21.attendancecheck.internet.requests.UpdateSettingsRequest;
import net.mc21.attendancecheck.internet.interfaces.UpdateSettingsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MinutelyService extends Service implements UpdateSettingsListener {
    private final static int SECOND = 1000;
    private final static int MINUTE = 60 * SECOND;
    private final static int NOTIFICATION_ID = 19;

    private static Handler handler = new Handler();
    public static boolean isRunning = false;

    private int minutesSinceLastCall = 0;

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
        int shiftStart = Integer.parseInt(SPHelpers.getString(SPHelpers.SP_SHIFT_START, getApplicationContext()));
        int shiftEnd = Integer.parseInt(SPHelpers.getString(SPHelpers.SP_SHIFT_END, getApplicationContext()));

        Calendar cal = Calendar.getInstance();
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);

        if(shiftStart < shiftEnd)
            return currentHour >= shiftStart && currentHour < shiftEnd;
        else
            return currentHour < shiftEnd || currentHour >= shiftStart;
    }

    private boolean isLoggedIn() {
        return SPHelpers.getString(SPHelpers.SP_WORKER_ID, getApplicationContext()) != null;
    }

    private void doMinutelyWork() {
        String callIntervalString = SPHelpers.getString(SPHelpers.SP_CALL_INTERVAL, getApplicationContext());

        // If settings have been set
        if(callIntervalString != null) {
            int callInterval = Integer.valueOf(callIntervalString);
            minutesSinceLastCall++;

            if (minutesSinceLastCall % callInterval == 0) {
                new UpdateSettingsRequest(this, getApplicationContext()).makeRequest();
                minutesSinceLastCall = 0;
            }
        } else {
            //Log.i(MainActivity.TAG, "Settings not set");
            //MiscHelpers.showToast("AttendanceCheck settings not set!", getApplicationContext());
        }
    }

    private void startCall() {
        // If device has Doze, wake it up before the minutely work,
        // so that all network operations work correctly
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            WakeLockManager.acquire(getApplicationContext());

        if(isShift()) {
            if (isLoggedIn())
                AbstractCallActivity.makeCall(CallActivity.class, getApplicationContext());
            else
                AbstractCallActivity.makeCall(LoginRemindActivity.class, getApplicationContext());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            WakeLockManager.release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
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

    @Override
    public void onSettingsUpdated(JSONObject response) {
        MiscHelpers.saveSettings(response, getApplicationContext());
        startCall();
    }

    @Override
    public void onSettingsUpdateError(VolleyError error) {
        startCall();
    }
}
