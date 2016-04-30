package net.mc21.attendancecheck;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {
    public static Context context;
    public static String SERVER_IP = "https://hidden-shelf-43728.herokuapp.com/";
    public final static String TAG = "AttendanceCheck";

    public static boolean isGoogleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int result = api.isGooglePlayServicesAvailable(context);
        ConnectionResult connectionResult = new ConnectionResult(result);

        if(!connectionResult.isSuccess()) {
            Log.i(MainActivity.TAG, connectionResult.toString());
        }

        return result == ConnectionResult.SUCCESS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openCompanyLogIn(View v) {
        Intent i = new Intent(this, CompanyLogIn.class);
        startActivity(i);
    }

    public void openWorkerLogIn(View v) {
        Intent i = new Intent(this, WorkerLogIn.class);
        startActivity(i);
    }
}
