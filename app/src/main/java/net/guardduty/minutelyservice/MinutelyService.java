package net.guardduty.minutelyservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.android.volley.VolleyError;

import net.guardduty.calls.AbstractCallActivity;
import net.guardduty.calls.CallActivity;
import net.guardduty.common.GuardDutyHelpers;
import net.guardduty.common.MiscHelpers;
import net.guardduty.common.NotificationHelpers;
import net.guardduty.common.SPHelpers;
import net.guardduty.mainactivities.MainActivity;
import net.guardduty.R;
import net.guardduty.common.WakeLockManager;
import net.guardduty.calls.LoginRemindActivity;
import net.guardduty.internet.requests.UpdateSettingsRequest;
import net.guardduty.internet.interfaces.UpdateSettingsListener;

import org.json.JSONObject;

public class MinutelyService extends Service implements UpdateSettingsListener {
    private final static int SECOND = 1000;
    private final static int MINUTE = 60 * SECOND;
    private final static int HANDLER_DELAY = SECOND;

    private final static String ACTION_STOP_SERVICE = "STOP_SERVICE";

    private static Handler handler = new Handler();
    private static Runnable runnable;
    public static boolean isRunning = false;

    private int minutesSinceLastCall = 0;

    private void runAsForeground() {
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                                                                NotificationHelpers.getUniqueId(),
                                                                notificationIntent,
                                                                0);

        Intent stopServiceIntent = new Intent(getApplicationContext(), MinutelyService.class);
        stopServiceIntent.setAction(ACTION_STOP_SERVICE);

        PendingIntent stopServicePendingIntent = PendingIntent.getService(getApplicationContext(),
                                                                          NotificationHelpers.getUniqueId(),
                                                                          stopServiceIntent,
                                                                          0);

        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setContentText(getString(R.string.app_name) + " " + getString(R.string.is_running))
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .addAction(R.drawable.cast_ic_expanded_controller_stop, "Stop service", stopServicePendingIntent)
                .setContentIntent(pendingIntent).build();

        startForeground(NotificationHelpers.getUniqueId(), notification);
    }

    private void stopService() {
        Log.i(MainActivity.TAG, "Stopping minutely service.");
        stopSelf();
    }

    private void doMinutelyWork() {
        String callIntervalString = SPHelpers.getString(SPHelpers.SP_CALL_INTERVAL, getApplicationContext());

        // If settings have been set
        if(callIntervalString != null) {
            int callInterval = Integer.valueOf(callIntervalString);
            minutesSinceLastCall++;
            Log.i(MainActivity.TAG, String.valueOf(callInterval - minutesSinceLastCall) + " minutes left to call");

            if (minutesSinceLastCall % callInterval == 0) {
                new UpdateSettingsRequest(this, getApplicationContext()).makeRequest();
                minutesSinceLastCall = 0;
            }
        } else {
            Log.i(MainActivity.TAG, "Settings not set");
            MiscHelpers.showToast("App's settings not set!", getApplicationContext());
        }
    }

    private void startCall() {
        // If device has Doze, wake it up before the minutely work,
        // so that all network operations work correctly
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            WakeLockManager.acquire(getApplicationContext());

        if(GuardDutyHelpers.isShift(getApplicationContext())) {
            if (GuardDutyHelpers.isLoggedIn(getApplicationContext()))
                AbstractCallActivity.makeCall(CallActivity.class, getApplicationContext());
            else
                AbstractCallActivity.makeCall(LoginRemindActivity.class, getApplicationContext());
        }
        else {
            Log.i(MainActivity.TAG, "Interval passed, but it isn't shift yet");
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
        if(intent != null && ACTION_STOP_SERVICE.equals(intent.getAction()))
            stopService();

        if(!isRunning) {
            Log.i(MainActivity.TAG, "Starting service");
            runAsForeground();
            isRunning = true;

            runnable = new Runnable() {
                @Override
                public void run() {
                    doMinutelyWork();
                    handler.postDelayed(this, HANDLER_DELAY);
                }
            };

            handler.postDelayed(runnable, HANDLER_DELAY);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
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
