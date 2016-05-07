package net.mc21.attendancecheck;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
    public final static String SP_NAME = "AttendanceCheck";
    public final static String SP_GCM_TOKEN = "gcm_token";
    public final static String SP_ACCESS_TOKEN = "access_token";
    public final static String TAG = "AttendanceCheck";
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
    protected void onResume() {
        super.onResume();
        context = this;
        initWorkerButton();
        setContentView(R.layout.activity_main);
    }

    public static String getGCMToken(final Context context) throws IOException {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String spToken = sp.getString(SP_GCM_TOKEN, null);

        // If token was in SP, return it
        if(spToken != null) {
            Log.i(MainActivity.TAG, "Getting token from Shared Preferences");
            return spToken.toString();
        }

        // Else get token
        final StringBuilder token = new StringBuilder();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(isGoogleServicesAvailable()) {
                        InstanceID instanceID = InstanceID.getInstance(context);
                        token.append(instanceID.getToken(MainActivity.GCM_TOKEN_REQUEST_SECRET, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            Log.i(MainActivity.TAG, "Join interrupted(waiting for GCM token): " + e.toString());
            e.printStackTrace();
        }

        // Save token
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.putString(SP_GCM_TOKEN, token.toString());
        spEditor.commit();

        Log.i(MainActivity.TAG, "Getting token from Google servers");
        return token.toString();
    }

    public void toggleButtonStatus() {
        final String signout_worker_text = getString(R.string.sign_out_worker);
        final String login_worker_text = getString(R.string.log_in_as_worker);

        Button b = (Button) findViewById(R.id.menu_worker_login_button);
        String buttonText = b.getText().toString();

        if(buttonText == signout_worker_text) {
            b.setText(login_worker_text);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openWorkerLogin(v);
                }
            });
        } else {
            b.setText(signout_worker_text);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signOutWorker();
                }
            });
        }
    }

    private void signOutWorker() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(MainActivity.isGoogleServicesAvailable()) {
                    try {
                        JSONObject json = new JSONObject();
                        json.put("gcm_token", MainActivity.getGCMToken(MainActivity.context));

                        String url = HTTP.SERVER_IP + "api/v1/devices/" + MainActivity.getGCMToken(MainActivity.context);
                        HTTP.DELETE(url, new Response.Listener<JSONObject>(){
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(MainActivity.TAG, "Signout device response: " + response.toString());
                                toggleButtonStatus();
                            }
                        }, MainActivity.context);
                    } catch (IOException e) {
                        Log.i(MainActivity.TAG, "Signout error: " + e.toString());
                        e.printStackTrace();
                    } catch (JSONException e) {
                        Log.i(MainActivity.TAG, "JSON error: " + e.toString());
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    // Changes 'log in worker' button to 'sign out worker' button
    // based on if the device is already logged in
    private void initWorkerButton() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = HTTP.SERVER_IP + "api/v1/devices/" + MainActivity.getGCMToken(MainActivity.context);
                    HTTP.GET(url, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.i(MainActivity.TAG, "Login check: " + response.toString());
                                boolean deviceLoggedIn = response.getBoolean("device_exists");

                                if(deviceLoggedIn) {
                                    toggleButtonStatus();
                                }
                            } catch (JSONException e) {
                                Log.i(MainActivity.TAG, "JSON error: " + e.toString());
                                e.printStackTrace();
                            }
                        }
                    }, MainActivity.context);
                } catch (IOException e) {
                    Log.i(MainActivity.TAG, "Token acquiring error: " + e.toString());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void openCompanyLogin(View v) {
        Intent i = new Intent(this, CompanyLoginActivity.class);
        startActivity(i);
    }

    public void openWorkerLogin(View v) {
        Intent i = new Intent(MainActivity.context, WorkerLoginActivity.class);
        startActivity(i);
    }

    public void openCompanySignup(View v) {
        Intent i = new Intent(MainActivity.context, CompanySignupActivity.class);
        startActivity(i);
    }
}
