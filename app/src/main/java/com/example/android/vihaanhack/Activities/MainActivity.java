package com.example.android.vihaanhack.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android.vihaanhack.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnLost;
    Button btnFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLost = (Button) findViewById(R.id.lost);
        btnFound = (Button) findViewById(R.id.found);
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
