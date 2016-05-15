package net.mc21.attendancecheck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Response;

import net.mc21.connections.HTTP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private JSONArray sitesJsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initSpinner();
    }

    private int getElementIndex(JSONArray array, String key, String value) {
        for(int i = 0; i < array.length(); i++) {
            try {
                JSONObject obj = array.getJSONObject(i);

                if(obj.getString(key) == value)
                    return i;
            } catch (JSONException e) {
                Log.i(MainActivity.TAG, "JSON error: " + e.toString());
                e.printStackTrace();
            }
        }

        return -1;
    }

    private void initSpinner() {
        String company_id = SPManager.getString(SPManager.SP_COMPANY_ID, getApplicationContext());
        String token = SPManager.getString(SPManager.SP_ACCESS_TOKEN, getApplicationContext());
        String url = HTTP.SERVER_IP + "api/v1/companies/" + company_id + "/sites?access_token=" + token;

        HTTP.GETArray(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                try {
                    sitesJsonArray = response;
                    List<String> sites = new ArrayList<String>();

                    for(int i = 0; i < sitesJsonArray.length(); i++) {
                        sites.add(sitesJsonArray.getJSONObject(i).getString("name"));
                    }

                    Spinner spinner = (Spinner) findViewById(R.id.settings_site_spinner);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, sites);
                    spinner.setAdapter(adapter);

                    // Set default selection
                    String siteId = SPManager.getString(SPManager.SP_SITE_ID, getApplicationContext());
                    int selectedIndex = getElementIndex(response, "id", siteId);
                    Log.i(MainActivity.TAG, "Default selection: " + selectedIndex);
                    spinner.setSelection(selectedIndex);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedSite = parent.getItemAtPosition(position).toString();
                            String site_id = MainActivity.getJsonArrayItem(sitesJsonArray, "name", selectedSite, "id");
                            SPManager.saveString(SPManager.SP_SITE_ID, site_id, getApplicationContext());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, getApplicationContext());
    }

    public void openRouteCreation(View v) {
        Intent i = new Intent(getApplicationContext(), RouteCreationActivity.class);
        startActivity(i);
    }
}
