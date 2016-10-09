package net.guardduty.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.VolleyError;

import net.guardduty.R;
import net.guardduty.common.MiscHelpers;
import net.guardduty.common.SPHelpers;
import net.guardduty.internet.interfaces.UpdateSettingsListener;
import net.guardduty.internet.requests.AcquireSitesRequest;
import net.guardduty.internet.interfaces.AcquireSitesListener;
import net.guardduty.internet.requests.UpdateSettingsRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements AcquireSitesListener, AdapterView.OnItemSelectedListener, UpdateSettingsListener {
    private JSONArray sitesJsonArray;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initSpinner();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(progressDialog != null)
            progressDialog.dismiss();
    }

    void setSpinnerDefault(Spinner spinner, JSONArray response) {
        String siteId = SPHelpers.getString(SPHelpers.SP_SITE_ID, getApplicationContext());

        if(siteId != null) {
            // Set default selection
            int selectedIndex = MiscHelpers.getJsonArrayIndex(response, "id", Integer.parseInt(siteId));
            Log.i(MainActivity.TAG, "Default selection: " + selectedIndex);
            spinner.setSelection(selectedIndex);
        }
    }

    private void initSpinner() {
        progressDialog = ProgressDialog.show(this, getString(R.string.please_wait), "Getting sites", true);
        new AcquireSitesRequest(this, getApplicationContext()).makeRequest();
    }

    public void openRouteCreation(View v) {
        Intent i = new Intent(getApplicationContext(), RouteCreationActivity.class);
        startActivity(i);
    }

    @Override
    public void onSitesAcquired(JSONArray response) {
        try {
            sitesJsonArray = response;
            List<String> sites = new ArrayList<String>();

            for (int i = 0; i < sitesJsonArray.length(); i++)
                sites.add(sitesJsonArray.getJSONObject(i).getString("name"));

            Spinner spinner = (Spinner) findViewById(R.id.settings_site_spinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, sites);
            spinner.setAdapter(adapter);
            setSpinnerDefault(spinner, sitesJsonArray);
            spinner.setOnItemSelectedListener(this);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            progressDialog.hide();
        }
    }

    @Override
    public void onSitesAcquireError(VolleyError error) {
        progressDialog.hide();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedSite = parent.getItemAtPosition(position).toString();
        String site_id = MiscHelpers.getJsonArrayItem(sitesJsonArray, "name", selectedSite, "id");
        SPHelpers.saveString(SPHelpers.SP_SITE_ID, site_id, getApplicationContext());

        progressDialog = ProgressDialog.show(this, getString(R.string.please_wait), "Getting site call interval");
        new UpdateSettingsRequest(this, getApplicationContext()).makeRequest();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void onSettingsUpdated(JSONObject response) {
        MiscHelpers.saveSettings(response, getApplicationContext());
        progressDialog.hide();
    }

    @Override
    public void onSettingsUpdateError(VolleyError error) {
        progressDialog.hide();
    }
}
