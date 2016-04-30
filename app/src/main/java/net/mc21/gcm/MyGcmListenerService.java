package net.mc21.gcm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import net.mc21.attendancecheck.Call;
import net.mc21.attendancecheck.MainActivity;

public class MyGcmListenerService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        Log.i(MainActivity.TAG, "Received call!");

        Call.startedFromPushNotification = true;

        Intent intent = new Intent("android.intent.category.LAUNCHER");
        intent.putExtra("token", data.getString("token"));
        intent.putExtra("receival_time", data.getString("time"));
        intent.putExtra("submission_interval", Integer.parseInt(data.getString("submission_interval")));
        intent.putExtra("alarm_time", Integer.parseInt(data.getString("alarm_time")));
        intent.putExtra("id", data.getString("id"));

        intent.setClassName("net.mc21.attendancecheck", "net.mc21.attendancecheck.Call");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
