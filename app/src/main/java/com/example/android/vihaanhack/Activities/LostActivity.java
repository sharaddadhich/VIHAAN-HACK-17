package com.example.android.vihaanhack.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.vihaanhack.Models.Lost;
import com.example.android.vihaanhack.R;
import com.firebase.client.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

public class LostActivity extends AppCompatActivity {

    ImageView placeholder, image;
    EditText lostName, lostAge, lostClothes;
    Bitmap picture;
    Button btnSubmit;

    Firebase firebaseRef;
    StorageReference storageReference;

    public static final Integer REQUEST_CAMERA = 10001;
    public static final Integer CAMERA_REQ_CODE = 1001;
    public static final Integer INTENT_REQUEST_GET_IMAGES = 101;
    public static final Integer REQ_CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);

        Firebase.setAndroidContext(this);

        placeholder = (ImageView) findViewById(R.id.placeholder);
        image = (ImageView) findViewById(R.id.imagee);
        lostName = (EditText) findViewById(R.id.lostName);
        lostAge = (EditText) findViewById(R.id.lostAge);
        lostClothes = (EditText) findViewById(R.id.lostClothes);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        storageReference = FirebaseStorage.getInstance().getReference();

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

                    Lost lostt = new Lost(lostName.getText().toString(), Integer.parseInt(lostAge.getText().toString()), lostClothes.getText().toString(), null);

                    firebaseRef = new Firebase("https://vihaanhack.firebaseio.com/lost");
                    firebaseRef.child(getSaltString()).setValue(lostt);

                    lostName.setText("");
                    lostAge.setText("");
                    lostClothes.setText("");
                    image.setVisibility(View.GONE);

                    Toast.makeText(LostActivity.this, "Submitted", Toast.LENGTH_SHORT).show();
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
        return true;
    }

    private void takeFromCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {

        if (resuleCode != RESULT_OK) return;

        if (requestCode == REQUEST_CAMERA && resuleCode == RESULT_OK) {
            picture = (Bitmap) intent.getExtras().get("data");
            image.setImageBitmap(picture);
            image.setVisibility(View.VISIBLE);
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
}