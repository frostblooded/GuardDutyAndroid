package net.mc21.attendancecheck;

import android.content.Context;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_login);
    }

    private void saveGCMToken(String token) {
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("access_token", token);
        editor.commit();
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

                try {
                    token = response.getString("access_token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i(MainActivity.TAG, "Company login response token: " + token);
                saveGCMToken(token);
            }
        }, this);
    }
}
