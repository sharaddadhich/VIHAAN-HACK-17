package com.example.android.vihaanhack.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.vihaanhack.Models.Lost;
import com.example.android.vihaanhack.R;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kairos.Kairos;
import com.kairos.KairosListener;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class LostActivity extends AppCompatActivity {

    ImageView placeholder, image;
    EditText lostName, lostAge, lostClothes, lostMob;
    Bitmap picture;
    Button btnSubmit;

    String app_id = "da94c708";
    String api_key = "c3b72e509462f155a8e5d2381222bb92";
    Kairos myKairos;
    KairosListener listener;

    Firebase firebaseRef;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    ProgressDialog progressDialog;

    Uri photoKaUri = Uri.parse("http://www.google.com");

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    public static final Integer REQUEST_CAMERA = 10001;
    public static final Integer CAMERA_REQ_CODE = 1001;
    public static final Integer INTENT_REQUEST_GET_IMAGES = 101;
    public static final Integer REQ_CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);

        Firebase.setAndroidContext(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        placeholder = (ImageView) findViewById(R.id.placeholder);
        image = (ImageView) findViewById(R.id.imagee);
        lostName = (EditText) findViewById(R.id.lostName);
        lostAge = (EditText) findViewById(R.id.lostAge);
        lostClothes = (EditText) findViewById(R.id.lostClothes);
        lostMob = (EditText) findViewById(R.id.lostMob);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        progressDialog = new ProgressDialog(this);

        myKairos = new Kairos();

// set authentication

        myKairos.setAuthentication(this, app_id, api_key);

        // Create an instance of the KairosListener

        listener = new KairosListener() {

            @Override
            public void onSuccess(String response) {
                // your code here!
                Log.d("KAIROS DEMO", response);


            }

            @Override
            public void onFail(String response) {
                // your code here!
                Log.d("KAIROS DEMO", response);
            }
        };

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("LOST");

        final CharSequence cameraOptions[] = new CharSequence[]{"Camera", "Gallery"};

        placeholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LostActivity.this);
                builder.setTitle("Choose an Option");
                builder.setItems(cameraOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            int cameraPerm = ContextCompat.checkSelfPermission(LostActivity.this, Manifest.permission.CAMERA);
                            if (cameraPerm != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(LostActivity.this, new String[]{
                                        Manifest.permission.CAMERA
                                }, CAMERA_REQ_CODE);
                            } else {
                                takeFromCamera();
                            }
                        } else if (which == 1) {
                            Intent i = new Intent(Intent.ACTION_PICK);
                            i.setType("image/*");
                            startActivityForResult(i, INTENT_REQUEST_GET_IMAGES);
                        }
                    }
                });
                builder.show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {

                    progressDialog.setMessage("Uploading");
                    progressDialog.show();
                    enrollPeople();

                    StorageReference photoref = storageReference.child(String.valueOf(photoKaUri));
                    Log.d("234234", "onClick: " + photoKaUri);
                    photoref.putFile(photoKaUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            Uri downloadUri = taskSnapshot.getDownloadUrl();

                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("mobNo", lostMob.getText().toString());
                            editor.commit();

                            Lost lostt = new Lost(lostName.getText().toString(),lostAge.getText().toString(),lostClothes.getText().toString(),downloadUri.toString(), lostMob.getText().toString());
                            firebaseRef = new Firebase("https://vihaanhack.firebaseio.com/lost");
                            firebaseRef.child(getSaltString()).setValue(lostt);
                            lostName.setText("");
                            lostAge.setText("");
                            lostClothes.setText("");
                            image.setVisibility(View.GONE);


                            Toast.makeText(LostActivity.this, "Submitted", Toast.LENGTH_SHORT).show();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Log.d("123123", "onFailure: ERRORRRRRRRRRR");
                                    e.printStackTrace();
                                }
                            });



                }
            }
        });
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZqwertyuiopasdfghjklzxcvbnm1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    private Boolean isValid() {
        if (TextUtils.isEmpty(lostName.getText().toString())) {
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(lostAge.getText().toString())) {
            Toast.makeText(this, "Enter Age", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(lostClothes.getText().toString())) {
            Toast.makeText(this, "Enter Clothes Description", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(lostMob.getText().toString())) {
            Toast.makeText(this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void takeFromCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//        Long tsLong = System.currentTimeMillis()/1000;
//        String ts = tsLong.toString();
//
//        File f = new File(Environment.getExternalStorageDirectory(), "Lost"+ts+".jpg");
//        photoKaUri = Uri.fromFile(f);
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoKaUri);

        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {

        if (resuleCode != RESULT_OK) return;

        if (requestCode == REQUEST_CAMERA && resuleCode == RESULT_OK) {
            picture = (Bitmap) intent.getExtras().get("data");
            image.setImageBitmap(picture);
//            image.setVisibility(View.VISIBLE);

//            image.setImageURI(photoKaUri);
            image.setVisibility(View.VISIBLE);
//            photoKaUri = intent.getData();
//            Log.d("checkkk", "onActivityResult: " + photoKaUri.toString());





        }

        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == RESULT_OK) {
            picture = (Bitmap) intent.getExtras().get("data");
            image.setImageBitmap(picture);
            image.setVisibility(View.VISIBLE);
        }

        super.onActivityResult(requestCode, resuleCode, intent);
    }

    @Override
    public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
                                             @NonNull int[] grantResults){

        if (requestCode == REQ_CODE) {
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Permission Not Given", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
            }
            takeFromCamera();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    void enrollPeople(){
        String subjectId = lostName.getText().toString();
        String galleryId = "People";
        try {
            myKairos.enroll(picture, subjectId, galleryId, null, null, null, listener);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}