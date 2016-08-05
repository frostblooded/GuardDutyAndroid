package net.mc21.attendancecheck.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.VolleyError;

import net.mc21.attendancecheck.R;
import net.mc21.attendancecheck.common.MiscHelpers;
import net.mc21.attendancecheck.common.SPHelpers;
import net.mc21.attendancecheck.internet.interfaces.WorkerLogoutListener;
import net.mc21.attendancecheck.internet.requests.WorkerLogoutRequest;
import net.mc21.attendancecheck.minutelyservice.MinutelyService;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements WorkerLogoutListener {
    public static Context context;
    public final static String TAG = "AttendanceCheck";

    private ProgressDialog progressDialog;

    public void toggleButtonStatus() {
        final String signoutWorkerText = getString(R.string.log_out_worker);
        final String loginWorkerText = getString(R.string.log_in_as_worker);

        Button b = (Button) findViewById(R.id.menu_worker_login_button);
        String buttonText = b.getText().toString();

        if(buttonText == signoutWorkerText) {
            b.setText(loginWorkerText);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openWorkerLogin(v);
                }
            });
        } else {
            b.setText(signoutWorkerText);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog = ProgressDialog.show(MainActivity.this, getString(R.string.please_wait), "Logging out", true);
                    new WorkerLogoutRequest(MainActivity.this, getApplicationContext()).makeRequest();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startMinutelyHandler();
        setContentView(R.layout.activity_main);

        String company_id = SPHelpers.getString(SPHelpers.SP_COMPANY_ID, getApplicationContext());
        String site_id = SPHelpers.getString(SPHelpers.SP_SITE_ID, getApplicationContext());

        if (company_id == null || site_id == null) {
            MiscHelpers.showToast(getString(R.string.please_login_to_configure_application), getApplicationContext());
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

        if(progressDialog != null)
            progressDialog.dismiss();
    }

    private void logoutWorker() {
        SPHelpers.saveString(SPHelpers.SP_WORKER_ID, null, getApplicationContext());
        toggleButtonStatus();
    }

    // Changes 'log in worker' button to 'sign out worker' button
    // based on if the device is already logged in
    private void initWorkerButton() {
        String workerId = SPHelpers.getString(SPHelpers.SP_WORKER_ID, getApplicationContext());
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

    @Override
    public void onWorkerLogout(JSONObject response) {
        logoutWorker();
        progressDialog.hide();
    }

    @Override
    public void onWorkerLogoutError(VolleyError error) {
        progressDialog.hide();
    }
}
