package net.guardduty.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.VolleyError;

import net.guardduty.R;
import net.guardduty.common.SPHelpers;
import net.guardduty.internet.requests.SettingsLoginRequest;
import net.guardduty.internet.interfaces.SettingsLoginListener;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsLoginActivity extends AppCompatActivity implements SettingsLoginListener {
    public static Context context;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_login);
    }

    @Override
    protected void onResume() {
        super.onResume();
        context = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(progressDialog != null)
            progressDialog.dismiss();
    }

    public void loginCompany(View v) {
        progressDialog = ProgressDialog.show(this, getString(R.string.please_wait), "Logging in", true);
        final String company_name = ((EditText) findViewById(R.id.settings_login_company_name_field)).getText().toString();
        String password = ((EditText) findViewById(R.id.settings_login_password_field)).getText().toString();

        JSONObject json = new JSONObject();

        try {
            json.put("name", company_name);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new SettingsLoginRequest(this, getApplicationContext()).setData(json).makeRequest();
    }

    @Override
    public void onSettingsLogin(JSONObject response) {
        try {
            SPHelpers.saveString(SPHelpers.SP_ACCESS_TOKEN, response.getString("access_token"), getApplicationContext());
            SPHelpers.saveString(SPHelpers.SP_COMPANY_ID, response.getString("company_id"), getApplicationContext());
            SPHelpers.saveString(SPHelpers.SP_COMPANY_NAME, response.getString("name"), getApplicationContext());

            boolean noError = response.isNull("error");

            if (noError) {
                Intent i = new Intent(MainActivity.context, SettingsActivity.class);
                startActivity(i);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            progressDialog.hide();
        }
    }

    @Override
    public void onSettingsLoginError(VolleyError error) {
        progressDialog.hide();
    }
}
