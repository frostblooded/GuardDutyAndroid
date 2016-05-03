package net.mc21.attendancecheck;

import android.content.Context;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import net.mc21.connections.HTTP;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WorkerLoginActivity extends AppCompatActivity {
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
                        sentData.put("gcm_token", MainActivity.obtainToken(WorkerLoginActivity.context));

                        String url = HTTP.SERVER_IP + "api/v1/mobile/register_device";
                        HTTP.POST(url, sentData, new Response.Listener<JSONObject>(){
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(MainActivity.TAG, "Registration token send response: " + response.toString());
                                finish();
                            }
                        }, WorkerLoginActivity.context);
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

    public void loginWorker(View v) {
        String first_name = ((TextView)findViewById(R.id.worker_login_first_name_field)).getText().toString();
        String last_name = ((TextView)findViewById(R.id.worker_login_last_name_field)).getText().toString();
        String password = ((TextView)findViewById(R.id.worker_login_password_field)).getText().toString();
        String company_name = ((TextView)findViewById(R.id.worker_login_company_name_field)).getText().toString();

        final JSONObject json = new JSONObject();

        try {
            json.put("first_name", first_name);
            json.put("last_name", last_name);
            json.put("password", password);
            json.put("company_name", company_name);
        } catch (JSONException e) {
            Log.i(MainActivity.TAG, "JSON put error: " + e.toString());
            e.printStackTrace();
        }

        String url = HTTP.SERVER_IP + "api/v1/mobile/check_worker_login";
        HTTP.POST(url, json, new Response.Listener<JSONObject>() {
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
                }
            }
        }, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        context = this;
    }
}