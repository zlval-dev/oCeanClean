package com.example.admin.oceanclean;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public
class RegisterActivity extends AppCompatActivity {

    private TextView login, title;
    private Button btnRegister;
    private EditText username, email, password, confirmPassword;
    private ImageView logo;
    private RelativeLayout layout;

    @Override
    public
    void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        login = findViewById(R.id.textView6);
        btnRegister = findViewById(R.id.btnRegister);
        username = findViewById(R.id.rUsername);
        email = findViewById(R.id.rEmail);
        password = findViewById(R.id.rPassword);
        confirmPassword = findViewById(R.id.rConfirmPassword);
        logo = findViewById(R.id.imageView2);
        title = findViewById(R.id.textView2);
        layout = findViewById(R.id.RelativeLayoutRegister);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                if(verificaConexao()){
                    hideSoftKeyboard();
                    if (username.getText().toString().equals("") || email.getText().toString().equals("") || password.getText().toString().equals("") || confirmPassword.getText().toString().equals("")) {
                        Toast.makeText(RegisterActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                    }else{
                        BackgroundWorker backgroundWorker = new BackgroundWorker(RegisterActivity.this);
                        backgroundWorker.execute("register", username.getText().toString(), email.getText().toString(), password.getText().toString(), confirmPassword.getText().toString());
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "You don't have internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                hideSoftKeyboard();
            }
        });
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                hideSoftKeyboard();
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                hideSoftKeyboard();
            }
        });
    }

    private void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(username.getWindowToken(), 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        imm.hideSoftInputFromWindow(email.getWindowToken(), 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        imm.hideSoftInputFromWindow(confirmPassword.getWindowToken(), 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private  boolean verificaConexao() {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }

}
