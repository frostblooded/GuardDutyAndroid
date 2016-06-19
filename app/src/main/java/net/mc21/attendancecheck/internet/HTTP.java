package net.mc21.attendancecheck.internet;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.mc21.attendancecheck.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class HTTP {
    public final static int REQUEST_TIMEOUT = 5000;
    public static String SERVER_IP = "https://hidden-shelf-43728.herokuapp.com/";
    //public static String SERVER_IP = "http://91.139.243.106:3000/";

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

            MainActivity.showToast(response, context);
        } catch (UnsupportedEncodingException e) {
            Log.i(MainActivity.TAG, "Unsupported encoding: " + e.toString());
            e.printStackTrace();
        } catch (JSONException e) {
            Log.i(MainActivity.TAG, "JSON error: " + e.toString());
            e.printStackTrace();
        }
    }
}
