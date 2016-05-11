package net.mc21.attendancecheck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initCompanyName();
    }

    private void initCompanyName() {
        TextView text = (TextView) findViewById(R.id.company_profile_company_name_text);
        text.setText(SharedPreferencesManager.getString(SharedPreferencesManager.SP_COMPANY_NAME, this));
    }
}
