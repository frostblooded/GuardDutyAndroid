package net.mc21.attendancecheck;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.mc21.attendancecheck.minutelywork.MinutelyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public static Context context;
    public final static String TAG = "AttendanceCheck";

    public static void showToast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static String getJsonArrayItem(JSONArray array, String key, String value, String returnKey) {
        for(int i = 0; i < array.length(); i++) {
            try {
                JSONObject obj = array.getJSONObject(i);

                if(obj.getString(key) == value)
                    return obj.getString(returnKey);
            } catch (JSONException e) {
                Log.i(MainActivity.TAG, "JSON error: " + e.toString());
                e.printStackTrace();
            }
        }

        return null;
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

    @Override
    protected void onStart() {
        super.onStart();
        startMinutelyHandler();
        setContentView(R.layout.activity_main);

        String company_id = SPManager.getString(SPManager.SP_COMPANY_ID, getApplicationContext());
        String site_id = SPManager.getString(SPManager.SP_SITE_ID, getApplicationContext());

        if (company_id == null || site_id == null) {
            showToast(getString(R.string.please_login_to_configure_application), getApplicationContext());
            openSettingsLogin(null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        context = this;
        initWorkerButton();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void signOutWorker() {
        SPManager.saveString(SPManager.SP_WORKER_ID, null, getApplicationContext());
        toggleButtonStatus();
    }

    // Changes 'log in worker' button to 'sign out worker' button
    // based on if the device is already logged in
    private void initWorkerButton() {
        String workerId = SPManager.getString(SPManager.SP_WORKER_ID, getApplicationContext());
        boolean loggedIn = workerId != null;
        Log.i(MainActivity.TAG, "Logged in: " + loggedIn);

        if(loggedIn) {
            toggleButtonStatus();
        }
    }

    private void startMinutelyHandler() {
        Intent i = new Intent(getApplicationContext(), MinutelyService.class);
        startService(i);
    }

    public void openSettingsLogin(View v) {
        Intent i = new Intent(getApplicationContext(), SettingsLoginActivity.class);
        startActivity(i);
    }

    public void openWorkerLogin(View v) {
        Intent i = new Intent(getApplicationContext(), WorkerLoginActivity.class);
        startActivity(i);
    }
}
