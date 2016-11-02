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

    public static void createRetryCallNotification(Context context,Bundle extras) {
        // Create a unique id. Used so that you can
        // have many notifications of the same type
        int uniqueId = (int)System.currentTimeMillis();

        Intent notificationIntent = new Intent(context, RetryCallService.class);
        notificationIntent.putExtras(extras);

        PendingIntent pendingIntent = PendingIntent.getService(context,
                                                               uniqueId,
                                                               notificationIntent,
                                                               PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context)
                .setContentText(context.getString(R.string.call_failed))
                .setContentTitle(context.getString(R.string.app_name))
                .setSmallIcon(R.drawable.cast_ic_notification_0)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(uniqueId, notification);
    }
}
