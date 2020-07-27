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
class ChangeEmailActivity extends AppCompatActivity {

    private String realUsername;
    private String admin;
    private Button btn;
    private EditText currentEmail, newEmail, confirmEmail;
    private TextView title;
    private RelativeLayout layout;

    @Override
    protected
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeemail);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        realUsername = getIntent().getStringExtra("realUsername");
        admin = getIntent().getStringExtra("admin");
        btn = findViewById(R.id.btnChangeEmail);
        currentEmail = findViewById(R.id.currentEmail);
        newEmail = findViewById(R.id.newEmail);
        confirmEmail = findViewById(R.id.confirmNewEmail);
        title = findViewById(R.id.textView2);
        layout = findViewById(R.id.RelativeLayoutChangeEmail);

        layout.setOnClickListener(new View.OnClickListener() {
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
        
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                if(verificaConexao()){
                    hideSoftKeyboard();
                    if (currentEmail.getText().toString().equals("") || newEmail.getText().toString().equals("") || confirmEmail.getText().toString().equals("")){
                        Toast.makeText(ChangeEmailActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                    }else{
                        if(newEmail.getText().toString().equals(confirmEmail.getText().toString())) {
                            BackgroundWorker backgroundWorker = new BackgroundWorker(ChangeEmailActivity.this);
                            backgroundWorker.execute("changeemail", realUsername, admin, currentEmail.getText().toString(), newEmail.getText().toString());
                        }else{
                            Toast.makeText(ChangeEmailActivity.this, "Emails don't match.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(ChangeEmailActivity.this, "You don't have internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(currentEmail.getWindowToken(), 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        imm.hideSoftInputFromWindow(newEmail.getWindowToken(), 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        imm.hideSoftInputFromWindow(confirmEmail.getWindowToken(), 0);
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
