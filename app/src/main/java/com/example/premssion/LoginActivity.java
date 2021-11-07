package com.example.premssion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textview.MaterialTextView;

public class LoginActivity extends AppCompatActivity {
   private MaterialTextView login_LBL_title;
    final int MIN_PASSWORD_LENGTH = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewInitializations();
    }

    void viewInitializations() {
        login_LBL_title = findViewById(R.id.login_LBL_title);

        // To show back button in actionbar
    }



    public void goToSignup(View v) {
        // Open your SignUp Activity if the user wants to signup
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
