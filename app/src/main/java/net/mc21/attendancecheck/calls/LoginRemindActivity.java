package net.mc21.attendancecheck.calls;

import android.content.Intent;
import android.util.Log;

import net.mc21.attendancecheck.main.MainActivity;
import net.mc21.attendancecheck.R;
import net.mc21.attendancecheck.main.WorkerLoginActivity;

public class LoginRemindActivity extends AbstractCallActivity {
    @Override
    protected void runExitAction() {
        Intent i = new Intent(getApplicationContext(), WorkerLoginActivity.class);
        startActivity(i);
    }

    @Override
    protected void onTimerTick(long millisUntilFinish) {}

    @Override
    protected void onTimerFinish() {
        exit();
    }

    @Override
    protected void initialize() {
        Log.i(MainActivity.TAG, "Starting login remind...");
        setContentView(R.layout.activity_login_remind);
    }
}
