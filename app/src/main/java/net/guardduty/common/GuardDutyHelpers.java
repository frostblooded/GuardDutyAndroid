package net.guardduty.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import net.guardduty.R;
import net.guardduty.calls.CallActivity;
import net.guardduty.calls.RetryCallService;
import net.guardduty.main.MainActivity;

import java.util.Calendar;
import java.util.Random;

public class GuardDutyHelpers {
    public static boolean isShift(Context context) {
        int shiftStart = Integer.parseInt(SPHelpers.getString(SPHelpers.SP_SHIFT_START, context));
        int shiftEnd = Integer.parseInt(SPHelpers.getString(SPHelpers.SP_SHIFT_END, context));

        Calendar cal = Calendar.getInstance();
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);

        if(shiftStart < shiftEnd)
            return currentHour >= shiftStart && currentHour < shiftEnd;
        else
            return currentHour < shiftEnd || currentHour >= shiftStart;
    }

    public static boolean isLoggedIn(Context context) {
        return SPHelpers.getString(SPHelpers.SP_WORKER_ID, context) != null;
    }
}
