package net.mc21.attendancecheck;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;

import net.mc21.connections.HTTP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class WorkerLoginActivity extends AppCompatActivity {
    private JSONArray workersJsonArray;
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
        Spinner spinner = (Spinner) findViewById(R.id.worker_login_spinner);
        String selectedWorker = (String) spinner.getSelectedItem();
        final String workerId = MainActivity.getJsonArrayItem(workersJsonArray, "name", selectedWorker, "id");
        String password = ((EditText)findViewById(R.id.worker_login_password_field)).getText().toString();
        String access_token = SPManager.getString(SPManager.SP_ACCESS_TOKEN, getApplicationContext());

        JSONObject json = new JSONObject();

        try {
            json.put("password", password);
            json.put("access_token", access_token);
        } catch (JSONException e) {
            Log.i(MainActivity.TAG, "JSON error: " + e.toString());
            e.printStackTrace();
        }

        // Check worker login
        String url = HTTP.SERVER_IP + "api/v1/workers/" + workerId + "/check_login";
        HTTP.POST(url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // If this code gets triggered, login is successful
                Log.i(MainActivity.TAG, "Worker logged in");
                SPManager.saveString(SPManager.SP_WORKER_ID, workerId, getApplicationContext());
                finish();
            }
        }, null, getApplicationContext());
    }

    private void initSpinner() {
        progressDialog = ProgressDialog.show(this, getString(R.string.please_wait), "Getting workers");

        String company_id = SPManager.getString(SPManager.SP_COMPANY_ID, getApplicationContext());
        String token = SPManager.getString(SPManager.SP_ACCESS_TOKEN, getApplicationContext());
        String site_id = SPManager.getString(SPManager.SP_SITE_ID, getApplicationContext());
        String url = HTTP.SERVER_IP + "api/v1/companies/" + company_id + "/sites/" + site_id + "/workers?access_token=" + token;

        HTTP.GETArray(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
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
        }, progressDialog, getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        context = this;
        initSpinner();
    }
}
