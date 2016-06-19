package net.mc21.attendancecheck.common;

import android.content.Context;
import android.os.PowerManager;

public class WakeLockManager {
    private static PowerManager.WakeLock wakeLock;

    public static void acquire(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP, "MyWakeLock");
        wakeLock.acquire();
    }

    public static void release() {
        wakeLock.release();
    }
}
