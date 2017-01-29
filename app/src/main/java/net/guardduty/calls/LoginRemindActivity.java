package net.guardduty.calls;

import android.content.Intent;
import android.util.Log;

import net.guardduty.mainactivities.MainActivity;
import net.guardduty.R;
import net.guardduty.mainactivities.WorkerLoginActivity;

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
