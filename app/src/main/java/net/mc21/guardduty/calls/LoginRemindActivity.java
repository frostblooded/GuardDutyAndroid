package net.mc21.guardduty.calls;

import android.content.Intent;
import android.util.Log;

import net.mc21.guardduty.main.MainActivity;
import net.mc21.guardduty.R;
import net.mc21.guardduty.main.WorkerLoginActivity;

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
