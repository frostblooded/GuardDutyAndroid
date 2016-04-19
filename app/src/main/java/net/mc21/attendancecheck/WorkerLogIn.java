package net.mc21.attendancecheck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import net.mc21.connections.HTTPRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class WorkerLogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_log_in);
    }

    public void logInWorker(View v) {
        EditText worker_name = (EditText)findViewById(R.id.worker_log_in_username_field);
        EditText password = (EditText)findViewById(R.id.worker_log_in_password_field);

        JSONObject json = new JSONObject();
        try {
            json.put("worker_name", worker_name);
            json.put("password", password);
        } catch (JSONException e) {
            Log.i(MainActivity.TAG, "JSON put error: " + e.toString());
            e.printStackTrace();
        }

        String response = HTTPRequest.sendAsync(HTTPRequest.SERVER_IP + "api/v1/mobile/login",
                json.toString(),
                HTTPRequest.RequestType.POST);

        Log.i(MainActivity.TAG, "Response: " + response);
    }
}
