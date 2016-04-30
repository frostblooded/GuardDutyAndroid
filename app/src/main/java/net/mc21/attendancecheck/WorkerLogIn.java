package net.mc21.attendancecheck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.mc21.attendancecheck.net.mc21.connections.CustomErrorListener;

import org.json.JSONException;
import org.json.JSONObject;

public class WorkerLogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_login);
    }

    private void registerDevice() {

    }

    private void checkWorkerLogin() {

    }

    private void validateLogIn(String firstName, String lastName, String password) {
        JSONObject json = new JSONObject();
        try {
            json.put("first_name", firstName);
            json.put("last_name", lastName);
            json.put("password", password);
        } catch (JSONException e) {
            Log.i(MainActivity.TAG, "JSON put error: " + e.toString());
            e.printStackTrace();
        }

        String url = MainActivity.SERVER_IP + "api/v1/mobile/check_worker_login";

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        boolean success = false;
                        try {
                            success = response.getBoolean("success");
                            Log.i(MainActivity.TAG, String.valueOf(success));

                            if(success) {
                                registerDevice();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new CustomErrorListener());

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(jsonRequest);
    }

    public void logInWorker(View v) {
        String first_name = ((TextView)findViewById(R.id.worker_login_first_name_field)).getText().toString();
        String last_name = ((TextView)findViewById(R.id.worker_login_last_name_field)).getText().toString();
        String password = ((TextView)findViewById(R.id.worker_login_password_field)).getText().toString();
        validateLogIn(first_name, last_name, password);
    }
}
