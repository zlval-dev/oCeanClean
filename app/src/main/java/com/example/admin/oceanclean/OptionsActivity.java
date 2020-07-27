package com.example.admin.oceanclean;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public
class OptionsActivity extends AppCompatActivity {

    private Button changeEmail;
    private Button changePassword;
    private String realUsername;
    private String admin;


    @Override
    public
    void onBackPressed() {
        Intent intent = new Intent(OptionsActivity.this, MenuActivity.class);
        intent.putExtra("realUsername", realUsername);
        intent.putExtra("admin", admin);
        startActivity(intent);
    }

    @Override
    protected
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
        changeEmail = (Button) findViewById(R.id.button);
        changePassword = (Button) findViewById(R.id.button2);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        realUsername = getIntent().getStringExtra("realUsername");
        admin = getIntent().getStringExtra("admin");

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                Intent intent = new Intent(OptionsActivity.this, ChangeEmailActivity.class);
                intent.putExtra("realUsername", realUsername);
                intent.putExtra("admin", admin);
                startActivity(intent);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                Intent intent = new Intent(OptionsActivity.this, ChangePasswordActivity.class);
                intent.putExtra("realUsername", realUsername);
                intent.putExtra("admin", admin);
                startActivity(intent);
            }
        });
    }
}
