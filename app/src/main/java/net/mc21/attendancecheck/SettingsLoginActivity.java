package net.mc21.attendancecheck;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Response;

import net.mc21.connections.HTTP;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsLoginActivity extends AppCompatActivity {
    public static Context context;

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

    public void loginCompany(View v) {
        final String company_name = ((EditText) findViewById(R.id.settings_login_company_name_field)).getText().toString();
        String password = ((EditText) findViewById(R.id.settings_login_password_field)).getText().toString();

        JSONObject json = new JSONObject();

        try {
            json.put("company_name", company_name);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = HTTP.SERVER_IP + "api/v1/access_tokens";

        HTTP.POST(url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String token = "";
                String company_id = "";
                String company_name = "";

                try {
                    token = response.getString("access_token");
                    company_id = response.getString("company_id");
                    company_name = response.getString("company_name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i(MainActivity.TAG, "Company login response token: " + token);
                SPManager.saveString(SPManager.SP_ACCESS_TOKEN, token, getApplicationContext());
                SPManager.saveString(SPManager.SP_COMPANY_ID, company_id, getApplicationContext());
                SPManager.saveString(SPManager.SP_COMPANY_NAME, company_name, getApplicationContext());

                boolean noError = response.isNull("error");

                if(noError){
                    Intent i = new Intent(MainActivity.context, SettingsActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, this);
    }
}
