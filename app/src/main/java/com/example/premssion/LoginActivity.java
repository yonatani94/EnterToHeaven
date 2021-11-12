package com.example.premssion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

public class LoginActivity extends AppCompatActivity {
   private MaterialTextView login_LBL_title;
    private ImageView login_IMG_background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewInitializations();
        addPIcWithGlide();
    }

    void viewInitializations() {
        login_LBL_title = findViewById(R.id.login_LBL_title);
        login_IMG_background = findViewById(R.id.login_IMG_background);
        // To show back button in actionbar
    }

    private void addPIcWithGlide() {
        Glide
                .with(LoginActivity.this)
                .load(R.drawable.backheaven)
                .centerCrop()
                .into(login_IMG_background);



    }


}
