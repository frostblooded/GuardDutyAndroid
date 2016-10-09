package net.mc21.guardduty.minutelyservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.mc21.guardduty.main.MainActivity;

public class BootServiceStarter extends BroadcastReceiver {
    public BootServiceStarter() {};

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MinutelyService.class);
        context.startService(i);
        Log.i(MainActivity.TAG, "Starting AttendanceCheck service on boot");
    }
}
