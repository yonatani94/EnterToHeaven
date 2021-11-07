package com.example.premssion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.btn);
        info  = findViewById(R.id.info);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                start();
            }
            private void updateUI()
            {
                int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS);
                if(result== PackageManager.PERMISSION_GRANTED)
                {
                    info.setText(" CONTACTS GRANTED by johny" );

                }
                else
                {
                    info.setText(" CONTACTS DENIED by johny");

                }
            }
            private void start() {
                updateUI();
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        });




    }
}