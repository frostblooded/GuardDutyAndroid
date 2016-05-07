package net.mc21.attendancecheck;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class SharedPreferencesManager {
    public final static String SP_NAME = "AttendanceCheck";
    public final static String SP_GCM_TOKEN = "gcm_token";
    public final static String SP_ACCESS_TOKEN = "access_token";
    public final static String SP_COMPANY_ID = "company_id";

    public static String getGCMToken(final Context context) throws IOException {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String spToken = sp.getString(SP_GCM_TOKEN, null);

        // If token was in SP, return it
        if(spToken != null) {
            return spToken.toString();
        }

        // Else get token
        final StringBuilder token = new StringBuilder();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(MainActivity.isGoogleServicesAvailable()) {
                        InstanceID instanceID = InstanceID.getInstance(context);
                        token.append(instanceID.getToken(MainActivity.GCM_TOKEN_REQUEST_SECRET, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            Log.i(MainActivity.TAG, "Join interrupted(waiting for GCM token): " + e.toString());
            e.printStackTrace();
        }

        // Save token
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.putString(SP_GCM_TOKEN, token.toString());
        spEditor.commit();

        return token.toString();
    }

    public static String getAccessToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, context.MODE_PRIVATE);
        return sp.getString(SP_ACCESS_TOKEN, null);
    }

    public static String getCompanyID(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, context.MODE_PRIVATE);
        return sp.getString(SP_COMPANY_ID, null);
    }
}