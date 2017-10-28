package com.example.android.vihaanhack.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.vihaanhack.Interfaces.OnGetLocation;
import com.example.android.vihaanhack.R;
import com.example.android.vihaanhack.Utils.GPSTracker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnLost;
    Button btnFound;

    TextView tvTheme;
    FirebaseAuth firebaseAuth;

    public static GPSTracker gps;
    public static Double latitude = null, longitude = null;
    public static OnGetLocation getLocation;
    public static Boolean gpsIsEnabled = false;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLost = (Button) findViewById(R.id.lost);
        btnFound = (Button) findViewById(R.id.found);
        tvTheme = (TextView) findViewById(R.id.moto);


//        Typeface typeFace = Typeface.createFromAsset(getAssets(),"fonts/font2.ttf");
//        tvTheme.setTypeface(typeFace);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Fetching Location...");
//        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        getLocation = new OnGetLocation() {
            @Override
            public void onSuccess() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        };

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    }
                });

        gps = new GPSTracker(MainActivity.this);

        getLoc();

        btnFound.setOnClickListener(this);
        btnLost.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnLost){
            startActivity(new Intent(MainActivity.this,LostActivity.class));
        }
        if (view == btnFound){
            startActivity(new Intent(MainActivity.this,FoundActivity.class));
        }
    }

    private void getLoc() {
        if (gps.getIsGPSTrackingEnabled() && gps.canGetLocation()){
            gpsIsEnabled = true;
            gps.getLocation();
        }
        else{
            gps.showSettingsAlert();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 9876){

            gps = new GPSTracker(MainActivity.this);

            if (gps.getIsGPSTrackingEnabled()){
                gpsIsEnabled = true;
                gps.getLocation();
            }
        }
    }
}
