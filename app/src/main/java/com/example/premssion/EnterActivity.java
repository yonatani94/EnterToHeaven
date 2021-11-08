package com.example.premssion;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class EnterActivity extends AppCompatActivity implements SensorEventListener,PatternLockViewListener {


    private ImageView enter_IMG_image;
    private TextView enter_TXT_info, enter_TXT_title;
    private AppCompatTextView enter_TXT_begin;
    private EditText enter_LBL_password;
    private Button enter_BTN_login,enter_BTN_click;
    // record the compass picture angle turned
    private SensorManager mSensorManager;
    private String heartRateValue;
    private float currentDegree = 0f;
    private int day;
    private PatternLockView enter_PTR_lock;
    private int battery= 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        findViews();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_WEEK);
        enter_PTR_lock.addPatternLockListener(this);
        battery = getBatteryPercentage(this);

        enter_BTN_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEditText(battery);
            }});



    }

    private void checkEditText(int num) {

        if(enter_LBL_password.getText().toString().equals(String.valueOf(num)))
        {
            Log.d("johny", "onClick: " + enter_LBL_password.getText());

            goToLogin();
        }
        else
        {
            Log.d("johny", "onClick: " + enter_LBL_password.getText());
            Toast.makeText(getApplicationContext(), "Password Wrong", Toast.LENGTH_LONG).show();
        }

    }






    private void findViews() {
        enter_PTR_lock = findViewById(R.id.enter_PTR_lock);
        enter_IMG_image = findViewById(R.id.enter_IMG_image);
        enter_BTN_click = findViewById(R.id.enter_BTN_click);
        enter_TXT_info = findViewById(R.id.enter_TXT_info);
        enter_TXT_title = findViewById(R.id.enter_TXT_title);
        enter_LBL_password = findViewById(R.id.enter_LBL_password);
        enter_BTN_login = findViewById(R.id.enter_BTN_login);
        enter_TXT_begin = findViewById(R.id.enter_TXT_begin);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();
        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE),
                SensorManager.SENSOR_DELAY_GAME);
        battery = getBatteryPercentage(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        battery = getBatteryPercentage(this);

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }


    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType()==Sensor.TYPE_HEART_RATE)
        {
            heartRateValue = Float.toString(event.values.length > 0 ? event.values[0] : 0.0f);
            enter_TXT_begin.setText(heartRateValue);
            Log.d("johny", "onSensorChanged: heart rate is " + event.values[0]);
           String msg = " value sensor heart rate  "+ event.values[0];
           enter_TXT_begin.setText(msg);

        }
        else if(event.sensor.getType()==Sensor.TYPE_ORIENTATION)
        {
            updateAngle(event.values[0]);
        }
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
            enter_BTN_click.setVisibility(View.VISIBLE);
            enter_BTN_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToLogin();
                }
            });
        }
        else
        {
            enter_BTN_click.setVisibility(View.INVISIBLE);
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
        switch (day) {
            case Calendar.SUNDAY:
                matchPatterns(pattern,"036");
                matchPatterns(pattern,"147");
                matchPatterns(pattern,"258");
                break;
            case Calendar.MONDAY:
                matchPatterns(pattern,"012543678");

                break;
            case Calendar.TUESDAY:
                matchPatterns(pattern,"0124678");
                break;
            case Calendar.WEDNESDAY:
                matchPatterns(pattern,"034147");
                matchPatterns(pattern,"145258");
                break;
            case Calendar.THURSDAY:
                matchPatterns(pattern,"210345876");
                break;
            case Calendar.FRIDAY:
                matchPatterns(pattern,"0367854");
                break;
            case Calendar.SATURDAY:
                matchPatterns(pattern,"01258");
                break;
        }
    }

    private void matchPatterns(List<PatternLockView.Dot> pattern, String s) {
        if (PatternLockUtils.patternToString(enter_PTR_lock, pattern).equalsIgnoreCase(s)) {
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