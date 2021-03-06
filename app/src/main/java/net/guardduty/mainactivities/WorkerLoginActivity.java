package net.guardduty.mainactivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.VolleyError;

import net.guardduty.R;
import net.guardduty.common.MiscHelpers;
import net.guardduty.common.SPHelpers;
import net.guardduty.internet.requests.AcquireWorkersRequest;
import net.guardduty.internet.requests.WorkerLoginRequest;
import net.guardduty.internet.interfaces.AcquireWorkersListener;
import net.guardduty.internet.interfaces.WorkerLoginListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WorkerLoginActivity extends AppCompatActivity implements AcquireWorkersListener, WorkerLoginListener {
    private JSONArray workersJsonArray;
    private String workerId;
    public static Context context;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_login);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(progressDialog != null)
            progressDialog.dismiss();
    }

    public void loginWorker(View v) {
        progressDialog = ProgressDialog.show(this, getString(R.string.please_wait), "Checking worker login");
        Spinner spinner = (Spinner) findViewById(R.id.worker_login_spinner);
        String selectedWorker = (String) spinner.getSelectedItem();
        workerId = MiscHelpers.getJsonArrayItem(workersJsonArray, "name", selectedWorker, "id");
        String password = ((EditText)findViewById(R.id.worker_login_password_field)).getText().toString();

        JSONObject json = new JSONObject();

        try {
            json.put("password", password);
        } catch (JSONException e) {
            Log.i(MainActivity.TAG, "JSON error: " + e.toString());
            e.printStackTrace();
        }

        new WorkerLoginRequest(this, workerId, getApplicationContext()).setData(json).makeRequest();
    }

    private void initSpinner() {
        progressDialog = ProgressDialog.show(this, getString(R.string.please_wait), "Getting workers");
        new AcquireWorkersRequest(this, getApplicationContext()).makeRequest();
    }

    @Override
    protected void onResume() {
        super.onResume();
        context = this;
        initSpinner();
    }

    @Override
    public void onWorkersAcquired(JSONArray response) {
        try {
            workersJsonArray = response;
            List<String> workers = new ArrayList<String>();

            for(int i = 0; i < workersJsonArray.length(); i++) {
                workers.add(workersJsonArray.getJSONObject(i).getString("name"));
            }

            Spinner spinner = (Spinner) findViewById(R.id.worker_login_spinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, workers);
            spinner.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            progressDialog.hide();
        }
    }

    @Override
    public void onWorkersAcquireError(VolleyError error) {
        progressDialog.hide();
    }

    @Override
    public void onWorkerLogin(JSONObject response) {
        Log.i(MainActivity.TAG, "Worker logged in");

        Spinner spinner = (Spinner) findViewById(R.id.worker_login_spinner);
        String workerName = spinner.getSelectedItem().toString();

        SPHelpers.saveString(SPHelpers.SP_WORKER_ID, workerId, getApplicationContext());
        SPHelpers.saveString(SPHelpers.SP_WORKER_NAME, workerName, getApplicationContext());

        progressDialog.hide();
        finish();
    }

    @Override
    public void onWorkerLoginError(VolleyError error) {
        progressDialog.hide();
    }
}
