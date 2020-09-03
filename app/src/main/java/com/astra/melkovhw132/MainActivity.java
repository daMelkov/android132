package com.astra.melkovhw132;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private static final String STORAGE_TYPE = "storage_type";

    private SharedPreferences preferences;
    private CheckBox storageType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init UI widgets
        initViews();

        // init file with login and password
        initData();

        // preferences (storage type)
        initPreferences();
    }

    private void initPreferences() {
        preferences = getPreferences(MODE_PRIVATE);
        storageType.setChecked(preferences.getBoolean(STORAGE_TYPE, false));
    }

    private void initData() {
        Data.addData(MainActivity.this, "", "", true);
    }

    private void initViews() {
        /* storage type */
        storageType = findViewById(R.id.storage_type);
        storageType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(STORAGE_TYPE, checked);
                editor.apply();
            }
        });

        /* Field login */
        final EditText edtLogin = findViewById(R.id.login_name);

        /* Field password */
        final EditText edtPassword = findViewById(R.id.password);

        /* Button login */
        Button btnLogin = findViewById(R.id.login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = edtLogin.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                checkExists(login, password);
            }
        });

        /* Button registration */
        Button btnRegistration = findViewById(R.id.registration);
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = edtLogin.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                // check: login not existsdenis
                if(!Data.checkExists(MainActivity.this, login)) {

                    // registration success
                    Data.addData(MainActivity.this, login, password, false);
                    Toast.makeText(MainActivity.this,
                            R.string.registration_success,
                            Toast.LENGTH_SHORT).show();

                    return;
                }

                // registration fail
                Toast.makeText(MainActivity.this,
                        R.string.registration_fail,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkExists(String login, String password) {
        if(login.length() == 0 || password.length() == 0) {
            Toast.makeText(MainActivity.this,
                    R.string.enter_data,
                    Toast.LENGTH_SHORT).show();

            return;
        }

        if(Data.checkExists(MainActivity.this, login, password)) {
            Toast.makeText(MainActivity.this,
                    R.string.logon_success,
                    Toast.LENGTH_SHORT).show();

            return;
        }

        Toast.makeText(MainActivity.this,
                R.string.logon_fail,
                Toast.LENGTH_SHORT).show();
    }
}