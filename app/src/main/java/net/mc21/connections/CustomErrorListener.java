package net.mc21.connections;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.mc21.attendancecheck.MainActivity;

import java.io.UnsupportedEncodingException;

public class CustomErrorListener implements Response.ErrorListener {
    private ProgressDialog progressDialog;

    public CustomErrorListener(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        try {
            String response = error.toString();

            if(error.networkResponse != null) {
                response = new String(error.networkResponse.data, "UTF-8");
            }

            Log.i(MainActivity.TAG, "Volley error: " + response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            if(progressDialog != null)
                progressDialog.hide();
        }

    }
}
