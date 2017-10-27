package com.example.android.vihaanhack.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.android.vihaanhack.R;

public class FoundActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView pHolder,imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found);

        pHolder = (ImageView) findViewById(R.id.placeholder);
        imageView = (ImageView) findViewById(R.id.image);
        pHolder.setOnClickListener(this);
        imageView.setOnClickListener(this);
        imageView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view == pHolder){
            pHolder.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
        }
    }
}
