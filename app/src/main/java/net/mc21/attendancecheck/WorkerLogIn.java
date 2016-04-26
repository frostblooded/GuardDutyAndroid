package net.mc21.attendancecheck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.mc21.connections.HTTPRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class WorkerLogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_log_in);
    }

    private void sendGCMToken(String GCMToken) {
        JSONObject json = new JSONObject();
        try {
            json.put("gcm_token", GCMToken);
        } catch (JSONException e) {
            Log.i(MainActivity.TAG, "JSON put error: " + e.toString());
            e.printStackTrace();
        }

        String url = HTTPRequest.SERVER_IP + "api/v1/mobile/register_device";

        // Request a string response
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(MainActivity.TAG, response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String response = error.toString();

                        if(error.networkResponse.data != null) {
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
        );

        // Add the request to the queue
        Volley.newRequestQueue(this).add(jsonRequest);
    }

    public void logInWorker(View v) {
        EditText worker_name = (EditText)findViewById(R.id.worker_log_in_username_field);
        EditText password = (EditText)findViewById(R.id.worker_log_in_password_field);
        sendGCMToken("rkgerigpuwehFPWRHOIREOPGRAEOPGEARHIGWEARGrkgerigpuwehFPWRHOIREOPGRAEOPGEARHIGWEARGrkgerigpuwehFPWRHOIREOPGRAEOPGEARHIGWEARGrkgerigpuwehFPWRHOIREOPGRAEOP");
    }
}
