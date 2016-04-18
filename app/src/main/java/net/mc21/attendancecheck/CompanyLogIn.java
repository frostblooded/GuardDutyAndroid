package net.mc21.attendancecheck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.mc21.connections.HTTPRequest;

import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

public class CompanyLogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_log_in);
    }

    public void onClick(View v) {
        Log.i(MainActivity.TAG, "Making request...");

        JSONObject json = new JSONObject();
        try {
            json.put("first_name", "Nikolay");
            json.put("last_name", "Danailov");
            json.put("password", "foobar");
            json.put("password_confirmation", "foobar");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(MainActivity.TAG, "JSON put error: " + e.toString());
        }

        JSONObject response = HTTPRequest.GET("http://91.139.243.106:3000/api/v1/mobile/workers?access_token=f112e56e4d5a2e450731811e64e2c4c2");
        Log.i(MainActivity.TAG, "Response: " + response);
    }
}
