package net.mc21.attendancecheck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;

import net.mc21.connections.HTTP;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initSpinner();
    }

    private void initSpinner() {
        String company = SPManager.getString(SPManager.SP_COMPANY_NAME, getApplicationContext());
        String token = SPManager.getString(SPManager.SP_ACCESS_TOKEN, getApplicationContext());
        String url = HTTP.SERVER_IP + "api/v1/companies/" + company + "/sites?access_token=" + token;

        HTTP.GETArray(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    List<String> sites = new ArrayList<String>();

                    for(int i = 0; i < response.length(); i++) {
                        sites.add(response.getJSONObject(i).getString("name"));
                    }

                    Spinner spinner = (Spinner) findViewById(R.id.settings_site_spinner);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, sites);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedSite = parent.getItemAtPosition(position).toString();
                            SPManager.saveString(SPManager.SP_SITE_NAME, selectedSite, getApplicationContext());
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
}
