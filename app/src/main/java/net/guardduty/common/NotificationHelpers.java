package net.guardduty.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import net.guardduty.R;
import net.guardduty.calls.RetryCallService;

public class NotificationHelpers {
    private static int uniqueId = 0;

    // Get a unique id. Used to differentiate
    // notifications and pending intents
    public static int getUniqueId() {
        return uniqueId++;
    }

    public static void createRetryCallNotification(Context context, Bundle extras) {
        Intent notificationIntent = new Intent(context, RetryCallService.class);
        notificationIntent.putExtras(extras);

        PendingIntent pendingIntent = PendingIntent.getService(context,
                getUniqueId(),
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
        notificationManager.notify(getUniqueId(), notification);
    }
}
