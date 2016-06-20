package net.mc21.attendancecheck.common;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import net.mc21.attendancecheck.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class MiscHelpers {
    public static String getJsonArrayItem(JSONArray array, String key, String value, String returnKey) {
        for(int i = 0; i < array.length(); i++) {
            try {
                JSONObject obj = array.getJSONObject(i);

                if(obj.getString(key) == value)
                    return obj.getString(returnKey);
            } catch (JSONException e) {
                Log.i(MainActivity.TAG, "JSON error: " + e.toString());
                e.printStackTrace();
            }
        }

        return null;
    }

    public static int getJsonArrayIndex(JSONArray array, String key, int value) {
        for(int i = 0; i < array.length(); i++) {
            try {
                JSONObject obj = array.getJSONObject(i);

                if(obj.getInt(key) == value)
                    return i;
            } catch (JSONException e) {
                Log.i(MainActivity.TAG, "JSON error: " + e.toString());
                e.printStackTrace();
            }
        }

        return -1;
    }

    public static void showToast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void unlockScreen(Activity activity) {
        Window w = activity.getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        w.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        w.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    public static Vibrator startVibration(Context context){
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = { 200, 800 };
        vibrator.vibrate(pattern, 0);
        return vibrator;
    }

    public static Ringtone startSound(Context context) {
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if(alert == null){
            // alert is null, using backup
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            if(alert == null){
                // backup is null, using second backup
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
        }

        Ringtone ringtone = RingtoneManager.getRingtone(context, alert);

        if (ringtone != null) {
            ringtone.play();
        }

        return ringtone;
    }

    public static void setVolume(Context context){
        final AudioManager mAudioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_RING, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
    }
}
