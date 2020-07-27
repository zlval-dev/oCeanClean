package com.example.admin.oceanclean;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.ScriptC;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by User on 10/2/2017.
 */

public class ReportActivity extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }
    }


    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final int CAMERA_PERMISSION_CODE = 2342;
    private static final int PICK_IMAGE_REQUEST = 22;
    private static final String UPLOAD_URL = "http://zlval.pt/UploadExample/upload.php";

    //widgets
    private EditText uNotes;
    private TextView textView2;
    private RelativeLayout ulayout;
    private Button photo;
    private TextView uComments;
    private Button btnConfirm;
    private EditText commentNotesSelected;
    private ScrollView mainScrollView;
    private ImageView transparentImageView;
    private Bitmap uBitMap;
    private ProgressDialog progressDialog;
    private File finalFile;

    private double muLatitude;
    private double muLongitude;
    private String categorySelected;
    private boolean photoSelected;
    private String commentNotes;
    private String realUsername;
    private String admin;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reportlitter);
        photo = (Button) findViewById(R.id.btnPhoto);
        uNotes = (EditText) findViewById(R.id.etDescription);
        uComments = (TextView) findViewById(R.id.textViewComment);
        btnConfirm = (Button) findViewById(R.id.btnConfirmReport);
        commentNotesSelected = (EditText) findViewById(R.id.etDescription);
        mainScrollView = (ScrollView) findViewById(R.id.scrollReport);
        transparentImageView = (ImageView) findViewById(R.id.transparent_image);

        realUsername = getIntent().getStringExtra("realUsername");
        admin = getIntent().getStringExtra("admin");

        transparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });

        categorySelected = "";
        photoSelected = false;

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                if(verificaConexao()){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ReportActivity.this);
                    commentNotes = commentNotesSelected.getText().toString();
                    if (categorySelected.equals(""))
                    {
                        alertDialog.setTitle("Report Failed");
                        alertDialog.setMessage("Please insert the category of the litter.");
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public
                            void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                    else if (!photoSelected){
                        alertDialog.setTitle("Report Failed");
                        alertDialog.setMessage("Please insert a photo of the litter.");
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public
                            void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                    else if (commentNotes.equals("")){
                        alertDialog.setTitle("Report Failed");
                        alertDialog.setMessage("Please insert a comment/note of the liiter.");
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public
                            void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                    else
                    {
                        String photoPath = finalFile.toString();
                        try{
                            progressDialog = new ProgressDialog(ReportActivity.this);
                            progressDialog.setMessage("Uploading image...");
                            progressDialog.show();
                            String uploadid = UUID.randomUUID().toString();
                            new MultipartUploadRequest(ReportActivity.this, uploadid, UPLOAD_URL)
                                    .addFileToUpload(photoPath, "image")
                                    .addParameter("name", "Imagem")
                                    .setNotificationConfig(new UploadNotificationConfig())
                                    .setMaxRetries(2)
                                    .setDelegate(new UploadStatusDelegate() {
                                        @Override
                                        public
                                        void onProgress(Context context, UploadInfo uploadInfo) {
                                        }

                                        @Override
                                        public
                                        void onError(Context context, UploadInfo uploadInfo, Exception exception) {
                                            progressDialog.dismiss();
                                            Toast.makeText(context, "Occurred error while upload image.", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public
                                        void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                            progressDialog.dismiss();
                                            progressDialog.setMessage("Sending Report...");
                                            progressDialog.show();
                                            BackgroundWorker backgroundWorker = new BackgroundWorker(ReportActivity.this);
                                            backgroundWorker.execute("reportlitter", realUsername, admin, muLatitude+"", muLongitude+"", categorySelected, commentNotes);
                                            progressDialog.dismiss();
                                        }

                                        @Override
                                        public
                                        void onCancelled(Context context, UploadInfo uploadInfo) {
                                            progressDialog.dismiss();
                                            Toast.makeText(context, "The image upload was cancelled.", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .startUpload();
                        }catch(Exception e){

                        }
                    }
                }else{
                    Toast.makeText(ReportActivity.this, "You don't have internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        uComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                hideSoftKeyboard();
            }
        });

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        createDropDownCategories();

        getLocationPermission();
        requestCameraPermission();

        textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                hideSoftKeyboard();
            }
        });

        ulayout = (RelativeLayout) findViewById(R.id.LayoutReport);
        ulayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                hideSoftKeyboard();
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected
    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK){
            uBitMap = (Bitmap) data.getExtras().get("data");
            Uri tempUri = getImageUri(getApplicationContext(), uBitMap);

            finalFile = new File(getRealPathFromURI(tempUri));

            photoSelected = true;
            Toast.makeText(this, "Photo successfully insert!", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void init(){
        Log.d(TAG, "init: initializing");


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public
            void onMapClick(LatLng latLng) {
                hideSoftKeyboard();
            }
        });


        hideSoftKeyboard();
    }

    private void requestCameraPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE);
    }

    private void getDeviceLocation(){
        mMap.clear();
        hideSoftKeyboard();
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            muLatitude = currentLocation.getLatitude();
                            muLongitude = currentLocation.getLongitude();

                            moveCamera(new LatLng(muLatitude, muLongitude),
                                    3,
                                    "My Location");

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        hideSoftKeyboard();
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(ReportActivity.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
            case CAMERA_PERMISSION_CODE:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                }else{
                    requestCameraPermission();
                }
            }
        }
    }

    private void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(uNotes.getWindowToken(), 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void createDropDownCategories()
    {
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.categories)){
            @Override
            public
            View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if(position==0)
                {
                    TextView textView = new TextView(ReportActivity.this);
                    textView.setHeight(0);
                    textView.setVisibility(View.GONE);
                    convertView=textView;
                    return convertView;
                }
                else
                {
                    return super.getDropDownView(position, null, parent);
                }
            }
        };
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position != 0){
            categorySelected = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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