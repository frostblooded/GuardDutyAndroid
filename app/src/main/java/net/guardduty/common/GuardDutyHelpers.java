package net.guardduty.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import net.guardduty.R;
import net.guardduty.calls.CallActivity;
import net.guardduty.calls.RetryCallService;

import java.util.Calendar;
import java.util.Random;

public class GuardDutyHelpers {
    private final static int CALL_RETRY_NOTIFICATION_ID = 2;
    private final static String CALL_RETRY_GROUP = "call_retry_group";

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

    public static void createRetryCallNotification(Context context) {
        Intent notificationIntent = new Intent(context, RetryCallService.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(context)
                .setContentText(context.getString(R.string.call_failed))
                .setContentTitle(context.getString(R.string.app_name))
                .setSmallIcon(R.drawable.cast_ic_notification_0)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        // Create a unique notification id. Used so that you can
        // have many notifications of the same type
        int notificationId = (int)System.currentTimeMillis() % Integer.MAX_VALUE;
        notificationManager.notify(notificationId, notification);
    }
}
