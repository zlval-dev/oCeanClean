package com.example.admin.oceanclean;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public
class MenuActivity extends AppCompatActivity {

    private String admin;
    private String realUsername;
    private Button reportLitter;
    private Button removeLitter;
    private Button options;
    private ImageView download;

    @Override
    public
    void onBackPressed() {
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        reportLitter = (Button) findViewById(R.id.button2);
        removeLitter = (Button) findViewById(R.id.button3);
        options = (Button) findViewById(R.id.button5);
        download = findViewById(R.id.download);

        realUsername = getIntent().getStringExtra("realUsername");
        admin = getIntent().getStringExtra("admin");

        if(Integer.parseInt(admin) == 0){
            removeLitter.setVisibility(View.INVISIBLE);
            download.setVisibility(View.INVISIBLE);
        }

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                if(verificaConexao()){
                    DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse("http://51.38.178.221/csvfile.php");
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Reports.csv");
                    request.setTitle("CSV File");
                    request.setDescription("Downloading...");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setVisibleInDownloadsUi(false);
                    request.allowScanningByMediaScanner();
                    downloadmanager.enqueue(request);
                    Toast.makeText(MenuActivity.this, "Downloading CSV File...", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MenuActivity.this, "You don't have internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reportLitter.setOnClickListener(new View.OnClickListener(){
            @Override
            public
            void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ReportActivity.class);
                intent.putExtra("realUsername", realUsername);
                intent.putExtra("admin", admin);
                startActivity(intent);
            }
        });

        removeLitter.setOnClickListener(new View.OnClickListener(){
            @Override
            public
            void onClick(View v) {
                if(verificaConexao()){
                    BackgroundWorker backgroundWorker = new BackgroundWorker(MenuActivity.this);
                    backgroundWorker.execute("removelittermarkers", realUsername, admin);
                }else{
                    Toast.makeText(MenuActivity.this, "You don't have internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        options.setOnClickListener(new View.OnClickListener(){
            @Override
            public
            void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, OptionsActivity.class);
                intent.putExtra("realUsername", realUsername);
                intent.putExtra("admin", admin);
                startActivity(intent);
            }
        });
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
