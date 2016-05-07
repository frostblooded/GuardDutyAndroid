package net.mc21.attendancecheck;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Response;

import net.mc21.connections.HTTP;

import org.json.JSONException;
import org.json.JSONObject;

public class CompanyProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);
        initCompanyName();
    }

    private void initCompanyName() {
        String url = HTTP.SERVER_IP + "api/v1/companies/" + SharedPreferencesManager.getCompanyID(this);
        HTTP.GET(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                TextView text = (TextView) findViewById(R.id.company_profile_company_name_text);

                try {
                    text.setText(response.getString("company_name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }
}
