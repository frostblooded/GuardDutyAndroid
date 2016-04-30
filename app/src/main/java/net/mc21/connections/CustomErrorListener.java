package net.mc21.connections;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.mc21.attendancecheck.MainActivity;

import java.io.UnsupportedEncodingException;

public class CustomErrorListener implements Response.ErrorListener {
    @Override
    public void onErrorResponse(VolleyError error) {
        String response = error.toString();

        if(error.networkResponse != null) {
            try {
                response = new String(error.networkResponse.data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        Log.i(MainActivity.TAG, "Volley error: " + response);
        error.printStackTrace();
    }
}
