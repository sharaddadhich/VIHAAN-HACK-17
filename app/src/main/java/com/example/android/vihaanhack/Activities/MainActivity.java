package com.example.android.vihaanhack.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.vihaanhack.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnLost;
    Button btnFound;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLost = (Button) findViewById(R.id.lost);
        btnFound = (Button) findViewById(R.id.found);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(MainActivity.this, "Sucess", Toast.LENGTH_SHORT).show();
                    }
                });



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
}
