package net.mc21.connections;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.mc21.attendancecheck.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class CustomErrorListener implements Response.ErrorListener {
    private ProgressDialog progressDialog;
    private Context context;

    public CustomErrorListener(ProgressDialog progressDialog, Context context) {
        this.progressDialog = progressDialog;
        this.context = context;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        try {
            String response = "";

            // Check if there was no connection
            if(error instanceof NoConnectionError) {
                response = "No internet connection!";
            } else if(error.networkResponse != null) {
                // Make data into string
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
        } finally {
            // Hide passed progress dialog
            if(progressDialog != null)
                progressDialog.hide();
        }

    }
}
