package net.mc21.attendancecheck.common;

import android.content.Context;
import android.util.Log;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;

import net.mc21.attendancecheck.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class InternetHelpers {
    public final static int REQUEST_TIMEOUT = 5000;
    //public static String SERVER_IP = "https://hidden-shelf-43728.herokuapp.com/";
    public static String SERVER_IP = "http://91.139.243.106:3000/";

    public static void handleError(VolleyError error, Context context) {
        try {
            String response = "";

            // Check if there was no connection
            if(error instanceof NoConnectionError) {
                response = "Can't reach server!";
            } else if(error.networkResponse != null) {
                response = new String(error.networkResponse.data, "UTF-8");
                JSONObject json = new JSONObject(response);
                response = "Error: " + json.getString("error");
            }

            MiscHelpers.showToast(response, context);
        } catch (UnsupportedEncodingException e) {
            Log.i(MainActivity.TAG, "Unsupported encoding: " + e.toString());
            e.printStackTrace();
        } catch (JSONException e) {
            Log.i(MainActivity.TAG, "JSON error: " + e.toString());
            e.printStackTrace();
        }
    }
}
