package com.example.admin.oceanclean;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public
class RemoveActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ProgressDialog progressDialog;
    private String realUsername;
    private ScrollView scrollView;
    private ImageView imageView;
    private GoogleMap mMap;
    private Boolean mLocationPermissionsGranted = false;
    private double muLatitude, muLongitude;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private String amount;
    private TextView tvCategory, tvDescription;
    private ImageView ivPhoto;
    private Button btnConfirmReport;
    private String url;
    private String admin;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();
            allLitter();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public
            boolean onMarkerClick(Marker marker) {
                ivPhoto.setBackgroundResource(android.R.color.transparent);
                tvCategory.setText(getIntent().getStringExtra("category"+marker.getTitle()));
                tvDescription.setText(getIntent().getStringExtra("description"+marker.getTitle()));
                url = getIntent().getStringExtra("photo"+marker.getTitle());
                new DownloadImageTask(ivPhoto)
                        .execute(getIntent().getStringExtra("photo"+marker.getTitle()));
                return false;
            }
        });
    }

    private void allLitter(){
        progressDialog = new ProgressDialog(RemoveActivity.this);
        progressDialog.setMessage("Getting data...");
        progressDialog.show();
        for(int i=1; i<=Integer.parseInt(amount); i++){
            MarkerOptions options = new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(getIntent().getStringExtra("latitude"+i)), Double.parseDouble(getIntent().getStringExtra("longitude"+i))))
                    .title(i+"");
            mMap.addMarker(options);
        }
        progressDialog.dismiss();
    }

    @Override
    protected
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.removelitter);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        scrollView = findViewById(R.id.scrollRemoveLitter);
        imageView =  findViewById(R.id.transparent_image);
        tvCategory = findViewById(R.id.tvCategory);
        tvDescription = findViewById(R.id.tvDescription);
        ivPhoto = findViewById(R.id.ivLitter);
        btnConfirmReport = findViewById(R.id.btnConfirmReport);

        realUsername = getIntent().getStringExtra("realUsername");
        admin = getIntent().getStringExtra("admin");
        amount = getIntent().getStringExtra("amount");

        getLocationPermission();

        btnConfirmReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                BackgroundWorker backgroundWorker = new BackgroundWorker(RemoveActivity.this);
                backgroundWorker.execute("removelitter", realUsername, admin, url);
            }
        });

        imageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
    }

    private void getDeviceLocation(){
        mMap.clear();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Location currentLocation = (Location) task.getResult();

                            muLatitude = currentLocation.getLatitude();
                            muLongitude = currentLocation.getLongitude();

                            moveCamera(new LatLng(muLatitude, muLongitude),
                                    3,
                                    "My Location");

                        }
                    }
                });
            }
        }catch (SecurityException e){
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(RemoveActivity.this);

    }
}
