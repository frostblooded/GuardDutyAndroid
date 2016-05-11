package net.mc21.attendancecheck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.mc21.connections.CustomErrorListener;
import net.mc21.connections.HTTP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initCompanyName();
        initSpinner();
    }

    private List<String> getSites() {
        final List<String> sites = new ArrayList<String>();
        String company = SharedPreferencesManager.getString(SharedPreferencesManager.SP_COMPANY_NAME, getApplicationContext());
        String token = SharedPreferencesManager.getString(SharedPreferencesManager.SP_ACCESS_TOKEN, getApplicationContext());
        String url = HTTP.SERVER_IP + "api/v1/companies/" + company + "/sites?access_token=" + token;

        HTTP.GETArray(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i(MainActivity.TAG, "Response: " + response.toString());
            }
        }, getApplicationContext());

        return null;
    }

    private void initSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.settings_site_spinner);
        String[] items = new String[]{"Site eqrewfewfewgewgewegew", "lkergjreoowerjgoirwegTest site", "ekgwegjewgpwejgpwegpowe3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        getSites();
    }

    private void initCompanyName() {
        TextView text = (TextView) findViewById(R.id.settings_company_name_text);
        text.setText(SharedPreferencesManager.getString(SharedPreferencesManager.SP_COMPANY_NAME, this));
    }
}
