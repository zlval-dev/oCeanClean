package com.example.admin.oceanclean;

import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public
class ChangePasswordActivity extends AppCompatActivity {

    private String realUsername;
    private String admin;
    private EditText currentPassword, newPassword, confirmPassword;
    private Button btn;
    private RelativeLayout layout;
    private TextView title;

    @Override
    protected
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        realUsername = getIntent().getStringExtra("realUsername");
        admin = getIntent().getStringExtra("admin");
        currentPassword = findViewById(R.id.rUsername);
        newPassword = findViewById(R.id.rEmail);
        confirmPassword = findViewById(R.id.rPassword);
        btn = findViewById(R.id.btnChangePassword);
        title = findViewById(R.id.textView2);
        layout = findViewById(R.id.RelativeLayoutChangePassword);

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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                if(verificaConexao()){
                    hideSoftKeyboard();
                    if(currentPassword.getText().toString().equals("") || newPassword.getText().toString().equals("") || confirmPassword.getText().toString().equals("")){
                        Toast.makeText(ChangePasswordActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                    }else{
                        if(newPassword.getText().toString().equals(confirmPassword.getText().toString())){
                            BackgroundWorker backgroundWorker = new BackgroundWorker(ChangePasswordActivity.this);
                            backgroundWorker.execute("changepassword", realUsername, admin, currentPassword.getText().toString(), newPassword.getText().toString());
                        }else{
                            Toast.makeText(ChangePasswordActivity.this, "Passwords don't match.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(ChangePasswordActivity.this, "You don't have internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(currentPassword.getWindowToken(), 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        imm.hideSoftInputFromWindow(newPassword.getWindowToken(), 0);
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
