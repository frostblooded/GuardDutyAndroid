package net.mc21.attendancecheck;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static String SERVER_IP = "https://hidden-shelf-43728.herokuapp.com/";
    public final static String TAG = "AttendanceCheck";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
