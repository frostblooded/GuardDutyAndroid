package net.mc21.attendancecheck;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_login);
    }

    public void loginWorker(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(MainActivity.isGoogleServicesAvailable()) {
                    try {
                        String password = ((TextView)findViewById(R.id.worker_login_password_field)).getText().toString();

                        final JSONObject json = new JSONObject();
                        json.put("password", password);
                        json.put("gcm_token", SPManager.getGCMToken(WorkerLoginActivity.context));

                        String url = HTTP.SERVER_IP + "api/v1/devices";
                        HTTP.POST(url, json, new Response.Listener<JSONObject>(){
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

    private void initSpinner() {
        String company = SPManager.getString(SPManager.SP_COMPANY_ID, getApplicationContext());
        String token = SPManager.getString(SPManager.SP_ACCESS_TOKEN, getApplicationContext());
        String site = SPManager.getString(SPManager.SP_SITE_ID, getApplicationContext());
        String url = HTTP.SERVER_IP + "api/v1/companies/" + company + "/sites/" + site + "/workers?access_token=" + token;

        Log.i(MainActivity.TAG, url);

        HTTP.GETArray(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    List<String> workers = new ArrayList<String>();

                    for(int i = 0; i < response.length(); i++) {
                        workers.add(response.getJSONObject(i).getString("name"));
                    }

                    Spinner spinner = (Spinner) findViewById(R.id.settings_site_spinner);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, workers);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        context = this;
        initSpinner();
    }
}
