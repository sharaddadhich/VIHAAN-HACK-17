package com.example.android.vihaanhack.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.android.vihaanhack.R;

import java.io.IOException;

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
    boolean mStartRecording = true;
    CoordinatorLayout coordinatorLayout;
    private static String mFileName = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found);

        // Record to the external cache directory for visibility
        mFileName = getExternalCacheDir().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";

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
        if (view == btnSubmit){
            submitData();
        }
    }

    private void submitData() {
        if (etClothes.getText().toString()==""){
            Snackbar snackbar  = Snackbar.make(coordinatorLayout,"Please Enter the Clothes!",Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        if (bitmap == null){
            Snackbar snackbar  = Snackbar.make(coordinatorLayout,"Please Enter the Image!",Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
            Log.d(TAG, "onActivityResult: "+data);
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
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
