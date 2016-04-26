package net.mc21.attendancecheck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.mc21.attendancecheck.net.mc21.connections.CustomErrorListener;

import org.json.JSONException;
import org.json.JSONObject;

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

        String url = MainActivity.SERVER_IP + "api/v1/mobile/register_device";

        // Request a string response
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(MainActivity.TAG, response.toString());
                    }
                }, new CustomErrorListener());

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(25, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the queue
        Volley.newRequestQueue(this).add(jsonRequest);
    }

    public void logInWorker(View v) {
        EditText worker_name = (EditText)findViewById(R.id.worker_log_in_username_field);
        EditText password = (EditText)findViewById(R.id.worker_log_in_password_field);
        sendGCMToken("rkgerigpuwehFPWRHOIREOPGRAEOPGEARHIGWEARGrkgerigpuwehFPWRHOIREOPGRAEOPGEARHIGWEARGrkgerigpuwehFPWRHOIREOPGRAEOPGEARHIGWEARGrkgerigpuwehFPWRHOIREOPGRAEOP");
    }
}
