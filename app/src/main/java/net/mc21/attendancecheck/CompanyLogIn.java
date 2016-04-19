package net.mc21.attendancecheck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.mc21.connections.HTTPRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CompanyLogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_log_in);
    }

    public void onClick(View v) {
        JSONObject json = new JSONObject();
        try {
            json.put("company_name", "frostblooded");
            json.put("password_digest", "$2a$10$q67BfKTG3lHP7/UuJbFoXuWHPbCVLeS5sXo2mnMuqIWdGjuUrqKCS");
        } catch (JSONException e) {
            Log.i(MainActivity.TAG, "JSON put error: " + e.toString());
            e.printStackTrace();
        }

        String response = "";

        response = HTTPRequest.sendAsync(HTTPRequest.SERVER_IP + "api/v1/mobile/login",
                json.toString(),
                HTTPRequest.RequestType.POST);

        Log.i(MainActivity.TAG, "Response: " + response);
        String access_token = null;
        JSONObject response_json = new JSONObject();

        try {
            response_json = new JSONObject(response);
            access_token = response_json.getString("access_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(MainActivity.TAG, "JSON: " + json.toString());

        response = HTTPRequest.sendAsync(HTTPRequest.SERVER_IP + "api/v1/mobile/workers",
                "access_token=" + access_token,
                HTTPRequest.RequestType.GET);

        Log.i(MainActivity.TAG, "Response: " + response);
    }
}
