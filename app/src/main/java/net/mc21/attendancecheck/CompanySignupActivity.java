package net.mc21.attendancecheck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;

import net.mc21.connections.HTTP;

import org.json.JSONException;
import org.json.JSONObject;

public class CompanySignupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_signup);
    }

    public void signUpCompany(View v) {
        String companyName = ((TextView)findViewById(R.id.company_signup_name_field)).getText().toString();
        String email = ((TextView)findViewById(R.id.company_signup_email_field)).getText().toString();
        String password = ((TextView)findViewById(R.id.company_signup_password_field)).getText().toString();
        String passwordConfirmation = ((TextView)findViewById(R.id.company_signup_password_confirmation_field)).getText().toString();

        String url = HTTP.SERVER_IP + "api/v1/mobile/signup_company";

        JSONObject json = new JSONObject();

        try {
            json.put("company_name", companyName);
            json.put("email", email);
            json.put("password", password);
            json.put("password_confirmation", passwordConfirmation);
        } catch (JSONException e) {
            Log.i(MainActivity.TAG, "JSON error: " + e.toString());
            e.printStackTrace();
        }

        HTTP.POST(url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(MainActivity.TAG, "Company registration response: " + response.toString());
            }
        }, this);
    }
}
