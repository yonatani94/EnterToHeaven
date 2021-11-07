package com.example.premssion;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

public class EnterActivity extends AppCompatActivity implements SensorEventListener,PatternLockViewListener {


    private ImageView enter_IMG_image;
    private TextView enter_TXT_info, enter_TXT_title;
    private EditText enter_LBL_password;
    private Button enter_BTN_login;
    // record the compass picture angle turned
    private SensorManager mSensorManager;
    private float currentDegree = 0f;

    private PatternLockView enter_PTR_lock;
    private int battery= 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        findViews();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        enter_PTR_lock.addPatternLockListener(this);
        battery = getBatteryPercentage(this);
        Log.d("johny", "onComplete: battery is "  + battery);
        String tmp = 0+String.valueOf(battery);

        if(enter_LBL_password.getText().equals("Johny land"))
        {
            enter_BTN_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToLogin();
                }
            });

        }
        else
        {
            Toast.makeText(this, "Password Wrong", Toast.LENGTH_LONG).show();
        }
    }

    private void findViews() {
        enter_PTR_lock = findViewById(R.id.enter_PTR_lock);
        enter_IMG_image = findViewById(R.id.enter_IMG_image);
        enter_TXT_info = findViewById(R.id.enter_TXT_info);
        enter_TXT_title = findViewById(R.id.enter_TXT_title);
        enter_LBL_password = findViewById(R.id.enter_LBL_password);
        enter_BTN_login = findViewById(R.id.enter_BTN_login);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }


    public void onSensorChanged(SensorEvent event) {
        updateAngle(event.values[0]);
    }

    private void updateAngle(float value) {
        float degree = Math.round(value);

        enter_TXT_info.setText("Heading: " + Float.toString(degree) + " degrees");

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(240);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        enter_IMG_image.startAnimation(ra);
        currentDegree = -degree;
        if (currentDegree == 0 ) {
            enter_BTN_login.setVisibility(View.VISIBLE);
            enter_BTN_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToLogin();
                }
            });
        }
        else
        {
            enter_BTN_login.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }




    @Override
    public void onStarted() {
        Log.d(getClass().getName(), "Pattern drawing started");
    }

    @Override
    public void onProgress(List<PatternLockView.Dot> progressPattern) {

    }

    @Override
    public void onComplete(List<PatternLockView.Dot> pattern) {
        String tmp = 0 + String.valueOf(battery);
        if (PatternLockUtils.patternToString(enter_PTR_lock, pattern).equalsIgnoreCase(tmp)) {
            enter_PTR_lock.setViewMode(PatternLockView.PatternViewMode.CORRECT);
            Toast.makeText(this, "Pattern Correct", Toast.LENGTH_LONG).show();
            goToLogin();

        } else {
            enter_PTR_lock.setViewMode(PatternLockView.PatternViewMode.WRONG);
            Toast.makeText(this, "Pattern Wrong", Toast.LENGTH_LONG).show();

        }
    }

    public void goToLogin() {
        // Open your SignUp Activity if the user wants to signup
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    @Override
    public void onCleared() {

    }
    public  int getBatteryPercentage(Context context) {

        if (Build.VERSION.SDK_INT >= 21) {

            BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
            return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        } else {

            IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, iFilter);

            int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
            int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

            double batteryPct = level / (double) scale;

            return (int) (batteryPct * 100);
        }
    }
}