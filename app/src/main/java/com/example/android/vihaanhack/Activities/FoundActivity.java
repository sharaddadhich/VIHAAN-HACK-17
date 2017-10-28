package com.example.android.vihaanhack.Activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.vihaanhack.R;
import com.kairos.Kairos;
import com.kairos.KairosListener;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class FoundActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "FoundActivity";
    EditText etDescribe,etClothes;
    Button btnSubmit;
    FloatingActionButton btnMic,btnStop;
    ImageView pHolder,imageView;
    private static final int CAMERA_REQUEST = 212;
    Bitmap bitmap;
    MediaRecorder mRecorder;
    MediaPlayer mPlayer;
    ProgressDialog progressDialog;
    boolean mStartRecording = true;
    CoordinatorLayout coordinatorLayout;
    private static String mFileName = null;
    Uri filepath;

    String app_id = "da94c708";
    String api_key = "c3b72e509462f155a8e5d2381222bb92";
    Kairos myKairos;
    KairosListener listener;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found);




        progressDialog = new ProgressDialog(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        myKairos = new Kairos();

// set authentication

        myKairos.setAuthentication(this, app_id, api_key);

        // Create an instance of the KairosListener

        listener = new KairosListener() {

            @Override
            public void onSuccess(String response) {
                // your code here!
                Log.d("KAIROS DEMO", response);
                Toast.makeText(FoundActivity.this, "Recognised!!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

//                Toast.makeText(FoundActivity.this,response, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(String response) {
                // your code here!
                Log.d("KAIROS DEMO", response);

            }
        };

        // Record to the external cache directory for visibility


        btnMic = (FloatingActionButton) findViewById(R.id.record);
        btnStop=(FloatingActionButton) findViewById(R.id.stop);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        etDescribe = (EditText) findViewById(R.id.description);
        etClothes = (EditText) findViewById(R.id.clothes);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        btnStop.setVisibility(View.INVISIBLE);

        pHolder = (ImageView) findViewById(R.id.placeholder);
        imageView = (ImageView) findViewById(R.id.image);
        pHolder.setOnClickListener(this);
        imageView.setOnClickListener(this);
        btnMic.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        imageView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view == pHolder){
            pHolder.setVisibility(View.INVISIBLE);
//            imageView.setVisibility(View.VISIBLE);
            openCamera();
        }
        if (view == btnMic){
            btnMic.setVisibility(View.INVISIBLE);
            btnStop.setVisibility(View.VISIBLE);
            mStartRecording = true;
            onRecord(mStartRecording);
        }
        if (view == btnStop){
            mStartRecording=false;
            btnMic.setVisibility(View.VISIBLE);
            btnStop.setVisibility(View.INVISIBLE);
            onRecord(mStartRecording);
        }
        if (view == btnSubmit) {
            if (submitData()){
                progressDialog.setMessage("Searching ...");
                progressDialog.show();
                recognise();


            }
        }
    }

    private void recognise() {

        String galleryId = "People";
        String selector = "FULL";
        String threshold = "0.75";
        String minHeadScale = "0.25";
        String maxNumResults = "25";
        try {
            myKairos.recognize(bitmap,
                    galleryId,
                    selector,
                    threshold,
                    minHeadScale,
                    maxNumResults,
                    listener);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private boolean submitData() {
        if (etClothes.getText().toString()==""){
            Snackbar snackbar  = Snackbar.make(coordinatorLayout,"Please Enter the Clothes!",Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        if (filepath == null){
            Snackbar snackbar  = Snackbar.make(coordinatorLayout,"Please provide the Image!",Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }

        return true;
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        File f = new File(Environment.getExternalStorageDirectory(),"Found"+ts+".jpg");
        filepath = Uri.fromFile(f);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, filepath);
        startActivityForResult(cameraIntent,CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(TAG, "onActivityResult: ");
        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
//            Log.d(TAG, "onActivityResult: "+data);
//            filepath = data.getData();
//            Log.d(TAG, "onActivityResult: "+data.getExtras().get("data"));
            bitmap = (Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(bitmap);
//            imageView.setImageURI(filepath);
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
//            photoKaUri = intent.getData();
            Log.d(TAG, "onActivityResult: " + filepath.toString());
            imageView.setVisibility(View.VISIBLE);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }




    private void startRecording() {
        mRecorder = new MediaRecorder();
        mFileName = getExternalCacheDir().getAbsolutePath();
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        mFileName += "Found"+ts+".3gp";
        Log.d(TAG, "startRecording: "+mFileName);
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
    }

}
