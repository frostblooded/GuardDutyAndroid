package net.mc21.attendancecheck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.mc21.connections.HTTPRequest;

public class CompanyLogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_log_in);
    }

    public void onClick(View v) {
        Log.i(MainActivity.TAG, "Making request...");
        String response = "";

        byte[] data = "".getBytes();
        response = HTTPRequest.sendAsync(data, "https://raw.githubusercontent.com/HristiyanZahariev/AttendanceCheck-RailsApp/master/config/boot.rb", HTTPRequest.RequestType.GET);

        Log.i(MainActivity.TAG, "Response: " + response);
    }
}
