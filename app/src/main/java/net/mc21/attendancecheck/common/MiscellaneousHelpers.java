package net.mc21.attendancecheck.common;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import net.mc21.attendancecheck.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class MiscellaneousHelpers {
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

    public static void showToast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
