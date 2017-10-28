package com.example.android.vihaanhack.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.vihaanhack.R;

public class SplashScreenActivity extends AppCompatActivity {

    TextView tvTitle;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        tvTitle = (TextView) findViewById(R.id.title);
        imageView = (ImageView) findViewById(R.id.image) ;
        imageView.setAlpha(0.5f);
        Typeface typeFace = Typeface.createFromAsset(getAssets(),"fonts/BLESD___.otf");
        tvTitle.setTypeface(typeFace);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
        }, 3000);
    }
}
