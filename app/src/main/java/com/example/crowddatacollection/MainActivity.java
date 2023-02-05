package com.example.crowddatacollection;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {



    // creating variable for button
    private Button Lights,bus_stop,sewage;
    private TextView admin_module;
    // creating a strings for storing
    // our values from edittext fields.


    // creating a variable
    // for firebasefirestore.
    private FirebaseFirestore db;
    FusedLocationProviderClient mFusedLocationClient;

    // Initializing other items
    // from layout file
    SimpleDateFormat sdf = new SimpleDateFormat("'\n'dd-MM-yyyy '\n'HH:mm:ss ");

    String currentDateAndTime = sdf.format(new Date());
    float longi = 0;
    float lati = 0;

    int PERMISSION_ID = 44;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        // getting our instance
        // from Firebase Firestore.
        db = FirebaseFirestore.getInstance();

        // initializing our edittext and buttons
        sewage = findViewById(R.id.sewage);
        bus_stop = findViewById(R.id.bus_stop);
        Lights = findViewById(R.id.idBtnSubmitCourse);
        admin_module = findViewById(R.id.admin_module);

        // adding onclick listener to view data in new activity
        admin_module.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new activity on button click
                Intent i = new Intent(MainActivity.this,Admin.class);
                startActivity(i);
            }
        });
        bus_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // calling method to add data to Firebase Firestore.
                addDataToFirestore("bus_stop");

            }

        });
        sewage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // calling method to add data to Firebase Firestore.
                addDataToFirestore("sewage");

            }

        });
        // adding on click listener for button
        Lights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    // calling method to add data to Firebase Firestore.
                    addDataToFirestore("Lights");

            }

        });
    }
//////////////////////////////////////////////////////////
private void getLastLocation() {
    // check if permissions are given
    if (checkPermissions()) {

        // check if location is enabled
        if (isLocationEnabled()) {

            // getting last
            // location from
            // FusedLocationClient
            // object
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location == null) {
                        requestNewLocationData();
                    } else {
                        //  latitudeTextView.setText(location.getLatitude() + "");

                        //  longitTextView.setText(location.getLongitude() + "");
                        String x = location.getLatitude() + " " + location.getLongitude() + "      " + currentDateAndTime;

                        Toast.makeText(MainActivity.this, x, Toast.LENGTH_SHORT).show();
                        longi = (float) location.getLongitude();
                        lati = (float) location.getLatitude();


                    }
                }
            });
        } else {
            Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();

        }
    } else {
        // if permissions aren't available,
        // request for permissions
        requestPermissions();
    }
}

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            //latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
            String x = mLastLocation.getLatitude()+" "+mLastLocation.getLongitude()+"                "+currentDateAndTime;
            Toast.makeText(MainActivity.this, x, Toast.LENGTH_SHORT).show();

//            longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");

            longi= (float) mLastLocation.getLongitude();
            lati = (float) mLastLocation.getLatitude();


        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }


    /////////////////////////////////////////////////////////
    private void addDataToFirestore(String collection) {

        // creating a collection reference
        // for our Firebase Firestore database.
        CollectionReference dbCourses = db.collection(collection);

        // adding our data to our courses object class.
        Details courses = new Details(longi, lati, currentDateAndTime);

        // below method is use to add data to Firebase Firestore.
        dbCourses.add(courses).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // after the data addition is successful
                // we are displaying a success toast message.
                String x="Your data for "+collection+" has been added";
                Toast.makeText(MainActivity.this, x, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(MainActivity.this, "Fail to add course \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
























