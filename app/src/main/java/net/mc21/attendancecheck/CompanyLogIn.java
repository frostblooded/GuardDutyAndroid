package net.mc21.attendancecheck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.mc21.connections.HTTPRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

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

        String response = HTTPRequest.sendAsync(json.toString(), "https://raw.githubusercontent.com/ljharb/json-file-plus/master/package.json", HTTPRequest.RequestType.GET);

        Log.i(MainActivity.TAG, "Response: " + response);
    }
}
