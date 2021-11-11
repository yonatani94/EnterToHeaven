package com.example.premssion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class HellActivity extends AppCompatActivity {
    private ImageView hell_IMG_background;
    private TextView  hell_LBL_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hell);

        hell_IMG_background =findViewById(R.id.hell_IMG_background) ;
        hell_LBL_title    =findViewById(R.id.hell_LBL_title) ;
    addPIcWithGlide();
    }

    private void addPIcWithGlide() {
        Glide
                .with(HellActivity.this)
                .load(R.drawable.peakpx)
                .centerCrop()
                .into(hell_IMG_background);

    }
}