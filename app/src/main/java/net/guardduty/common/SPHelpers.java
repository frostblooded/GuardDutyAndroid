package net.guardduty.common;

import android.content.Context;
import android.content.SharedPreferences;

public class SPHelpers {
    public final static String SP_NAME = "GuardDuty";
    public final static String SP_ACCESS_TOKEN = "access_token";
    public final static String SP_COMPANY_NAME = "company_name";
    public final static String SP_SITE_ID = "site_id";
    public final static String SP_WORKER_ID = "worker_id";
    public final static String SP_WORKER_NAME = "worker_name";
    public final static String SP_SHIFT_START = "shift_start";
    public final static String SP_SHIFT_END = "shift_end";
    public final static String SP_CALL_INTERVAL = "call_interval";

    public static String getString(String string, Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, context.MODE_PRIVATE);
        return sp.getString(string, null);
    }

    public static void saveString(String key, String value, Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
