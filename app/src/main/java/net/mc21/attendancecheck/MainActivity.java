package net.mc21.attendancecheck;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import net.mc21.connections.HTTP;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static Context context;
    public final static String TAG = "AttendanceCheck";
    public final static int REQUEST_TIMEOUT = 5000;
    public final static String GCM_TOKEN_REQUEST_SECRET = "394378341767";

    public static boolean isGoogleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int result = api.isGooglePlayServicesAvailable(context);
        ConnectionResult connectionResult = new ConnectionResult(result);

        if(!connectionResult.isSuccess()) {
            Log.i(MainActivity.TAG, connectionResult.toString());
        }

        return result == ConnectionResult.SUCCESS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openCompanyLogin(View v) {
        Intent i = new Intent(this, CompanyLoginActivity.class);
        startActivity(i);
    }

    public void openWorkerLogin(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InstanceID instanceID = InstanceID.getInstance(MainActivity.context);
                    String token = instanceID.getToken(MainActivity.GCM_TOKEN_REQUEST_SECRET, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                    JSONObject json = new JSONObject();

                    json.put("gcm_token", token);

                    String url = HTTP.SERVER_IP + "api/v1/mobile/check_device_login_status";
                    HTTP.POST(url, json, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.i(MainActivity.TAG, "Login check: " + response.toString());
                                boolean deviceLoggedIn = response.getBoolean("device_exists");

                                if(!deviceLoggedIn) {
                                    Intent i = new Intent(MainActivity.context, WorkerLoginActivity.class);
                                    startActivity(i);
                                }
                            } catch (JSONException e) {
                                Log.i(MainActivity.TAG, "JSON error: " + e.toString());
                                e.printStackTrace();
                            }
                        }
                    }, MainActivity.context);
                } catch (JSONException e) {
                    Log.i(MainActivity.TAG, "JSON error: " + e.toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i(MainActivity.TAG, "Token acquiring error: " + e.toString());
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
