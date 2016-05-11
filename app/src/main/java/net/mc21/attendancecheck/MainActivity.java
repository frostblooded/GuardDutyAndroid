package net.mc21.attendancecheck;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import net.mc21.connections.HTTP;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static Context context;
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

    public static void showToast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        String company_name = SharedPreferencesManager.getString(SharedPreferencesManager.SP_COMPANY_NAME, this);
        String site_name = SharedPreferencesManager.getString(SharedPreferencesManager.SP_SITE_NAME, this);

        if(company_name == null || site_name == null) {
            showToast(getString(R.string.please_login_to_configure_application), getApplicationContext());
            openSettingsLogin(null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        context = this;
        initWorkerButton();
        setContentView(R.layout.activity_main);
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
                        String url = HTTP.SERVER_IP + "api/v1/devices/" + SharedPreferencesManager.getGCMToken(MainActivity.context);
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
                    String url = HTTP.SERVER_IP + "api/v1/devices/" + SharedPreferencesManager.getGCMToken(MainActivity.context);
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

    public void openSettingsLogin(View v) {
        Intent i = new Intent(this, SettingsLoginActivity.class);
        startActivity(i);
    }

    public void openWorkerLogin(View v) {
        Intent i = new Intent(MainActivity.context, WorkerLoginActivity.class);
        startActivity(i);
    }
}
