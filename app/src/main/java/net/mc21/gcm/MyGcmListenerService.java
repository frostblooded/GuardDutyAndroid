package net.mc21.gcm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import net.mc21.attendancecheck.CallActivity;
import net.mc21.attendancecheck.MainActivity;

public class MyGcmListenerService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        Log.i(MainActivity.TAG, "Received call!");
        CallActivity.makeCall(getApplicationContext());
    }
}
