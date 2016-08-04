package net.mc21.attendancecheck.common;

import android.content.Context;

import java.util.Calendar;

public class AttendanceCheckHelpers {
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
