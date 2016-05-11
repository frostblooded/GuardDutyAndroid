package net.mc21.attendancecheck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initCompanyName();
        initSpinner();
    }

    private void initSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.settings_site_spinner);
        String[] items = new String[]{"Site eqrewfewfewgewgewegew", "lkergjreoowerjgoirwegTest site", "ekgwegjewgpwejgpwegpowe3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
    }

    private void initCompanyName() {
        TextView text = (TextView) findViewById(R.id.settings_company_name_text);
        text.setText(SharedPreferencesManager.getString(SharedPreferencesManager.SP_COMPANY_NAME, this));
    }
}
