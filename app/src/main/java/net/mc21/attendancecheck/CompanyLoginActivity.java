package net.mc21.attendancecheck;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Response;

import net.mc21.connections.HTTP;

import org.json.JSONException;
import org.json.JSONObject;

public class CompanyLoginActivity extends AppCompatActivity {
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_login);
    }

    @Override
    protected void onResume() {
        super.onResume();
        context = this;
    }

    public void loginCompany(View v) {
        String company_name = ((EditText) findViewById(R.id.company_login_company_name_field)).getText().toString();
        String password = ((EditText) findViewById(R.id.company_login_password_field)).getText().toString();

        JSONObject json = new JSONObject();

        try {
            json.put("company_name", company_name);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = HTTP.SERVER_IP + "api/v1/companies/login";

        HTTP.POST(url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String token = "";
                String company_id = "";

                try {
                    token = response.getString("access_token");
                    company_id = response.getString("company_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i(MainActivity.TAG, "Company login response token: " + token);
                SharedPreferencesManager.saveAccessToken(token, company_id, CompanyLoginActivity.context);

                boolean noError = response.isNull("error");

                if(noError){
                    Intent i = new Intent(MainActivity.context, CompanyProfileActivity.class);
                    startActivity(i);
                }
            }
        }, this);
    }
}
