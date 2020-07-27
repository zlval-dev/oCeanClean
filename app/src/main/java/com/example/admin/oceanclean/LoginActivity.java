package com.example.admin.oceanclean;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public
class LoginActivity extends AppCompatActivity{

    private Button confirm;
    private TextView register, title;
    private EditText etusername;
    private EditText etpassword;
    private ImageView logo;
    private RelativeLayout layout;

    @Override
    public
    void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        confirm = (Button) findViewById(R.id.loginButton);
        register = (TextView) findViewById(R.id.textView6);
        etusername = (EditText) findViewById(R.id.eUserLogin);
        etpassword = (EditText) findViewById(R.id.ePasswordLogin);
        logo = findViewById(R.id.imageView2);
        title = findViewById(R.id.textView2);
        layout = findViewById(R.id.RelativeLayoutLogin);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                if(verificaConexao()){
                    hideSoftKeyboard();
                    BackgroundWorker backgroundWorker = new BackgroundWorker(LoginActivity.this);
                    backgroundWorker.execute("login", etusername.getText().toString(), etpassword.getText().toString());
                }else{
                    Toast.makeText(LoginActivity.this, "You don't have internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public
            void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        logo.setOnClickListener(new View.OnClickListener(){
            @Override
            public
            void onClick(View v) {
                hideSoftKeyboard();
            }
        });

        title.setOnClickListener(new View.OnClickListener(){
            @Override
            public
            void onClick(View v) {
                hideSoftKeyboard();
            }
        });

        layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public
            void onClick(View v) {
                hideSoftKeyboard();
            }
        });
    }

    private void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etusername.getWindowToken(), 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        imm.hideSoftInputFromWindow(etpassword.getWindowToken(), 0);
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
