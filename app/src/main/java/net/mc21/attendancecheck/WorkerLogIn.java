package net.mc21.attendancecheck;

import android.content.Context;
import android.os.PersistableBundle;
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
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import net.mc21.connections.CustomErrorListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WorkerLogIn extends AppCompatActivity {
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_login);
    }

    private void registerDevice(final JSONObject sentData) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(MainActivity.isGoogleServicesAvailable()) {
                    try {
                        InstanceID instanceID = InstanceID.getInstance(MainActivity.context);
                        String token = instanceID.getToken(MainActivity.GCM_TOKEN_REQUEST_SECRET, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                        sentData.put("gcm_token", token);
                        Log.i(MainActivity.TAG, "Acquired token " + token);

                        String url = MainActivity.SERVER_IP + "api/v1/mobile/register_device";

                        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, sentData,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.i(MainActivity.TAG, "Registration token send response: " + response.toString());
                                    }
                                }, new CustomErrorListener());

                        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(MainActivity.REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                        Volley.newRequestQueue(MainActivity.context).add(jsonRequest);
                    } catch (IOException e) {
                        Log.i(MainActivity.TAG, "Token getting error: " + e.toString());
                        e.printStackTrace();
                    } catch (JSONException e) {
                        Log.i(MainActivity.TAG, "JSON error: " + e.toString());
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void checkWorkerLogin() {
        String first_name = ((TextView)findViewById(R.id.worker_login_first_name_field)).getText().toString();
        String last_name = ((TextView)findViewById(R.id.worker_login_last_name_field)).getText().toString();
        String password = ((TextView)findViewById(R.id.worker_login_password_field)).getText().toString();

        final JSONObject json = new JSONObject();

        try {
            json.put("first_name", first_name);
            json.put("last_name", last_name);
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
                        } catch (JSONException e) {
                            Log.i(MainActivity.TAG, "JSON error: " + e.toString());
                            e.printStackTrace();
                        }

                        Log.i(MainActivity.TAG, "Login check response: " + response.toString());

                        if(success) {
                            registerDevice(json);
                        } else {
                            String error = "";

                            try {
                                error = response.getString("error");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.i(MainActivity.TAG, "Login error: " + error);
                        }
                    }
                }, new CustomErrorListener());

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(MainActivity.REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(jsonRequest);
    }

    public void logInWorker(View v) {
        checkWorkerLogin();
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        context = this;
    }
}
