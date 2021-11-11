package com.example.premssion;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Context;
import android.content.DialogInterface;
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
import com.bumptech.glide.Glide;

import java.util.Calendar;
import java.util.List;

public class HeavenActivity extends AppCompatActivity implements SensorEventListener,PatternLockViewListener {


    private ImageView enter_IMG_image,enter_IMG_background;
    private TextView enter_TXT_info, enter_TXT_title,enter_TXT_counter;
    private EditText enter_LBL_password;
    private Button enter_BTN_login,enter_BTN_click,enter_BTN_hint1,enter_BTN_hint2,enter_BTN_hint3;
    // record the compass picture angle turned
    private SensorManager mSensorManager;
    private String heartRateValue;
    private float currentDegree = 0f;
    private int day;
    private PatternLockView enter_PTR_lock;
    private int battery= 0 ,counter = 5;


    private AlertDialog  alertDialog;
    //private acceleration vector
    private float[] floatGravity = new float[3];
    private float[] floatGeoMagnetic = new float[3];

    private float[] floatOrientation = new float[3];
    private float[] floatRotationMatrix = new float[9];


    private float[] gravityValues = null ;
    private float[] magneticValues =null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        findViews();
        addPIcWithGlide();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        initCalender();

        checkCounter();

        enter_PTR_lock.addPatternLockListener(this);
        battery = getBatteryPercentage(this);

        enter_BTN_login.setOnClickListener(clickListener);
        enter_BTN_hint1.setOnClickListener(clickListener);
        enter_BTN_hint2.setOnClickListener(clickListener);
        enter_BTN_hint3.setOnClickListener(clickListener);

    }

    private void initCalender() {
        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_WEEK);
    }

    private void checkCounter() {
        enter_TXT_counter.setText(String.valueOf(counter)+ " attempts left");
        if(counter==0)
        {
            goToHell();
        }
    }

    private void addPIcWithGlide() {
        Glide
                .with(HeavenActivity.this)
                .load(R.drawable.background)
                .centerCrop()
                .into(enter_IMG_background);

    }
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getTag().toString().equals("hint1"))
            {
                showHint(alertDialog,"Hint 1","align the phone to the north than click me");
            }
            else if(v.getTag().toString().equals("hint2"))
            {
                showHint(alertDialog,"Hint 2","Write Your Battery Life");

            }
            else if(v.getTag().toString().equals("hint3"))
            {
                showHint(alertDialog,"Hint 3","draw the day of the week(number)");

            }
            else if(v.getTag().toString().equals("login"))
            {
                checkEditText(battery);
            }
        }
    };

    private void showHint(AlertDialog alert, String title, String msg) {
        alert = new AlertDialog.Builder(HeavenActivity.this).create();

        alert.setMessage(msg);
        alert.setTitle(title);
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }


    private void checkEditText(int num) {

        if(enter_LBL_password.getText().toString().equals(String.valueOf(num)))
        {
            goToLogin();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Password Wrong", Toast.LENGTH_LONG).show();
            counter-=1;
            checkCounter();
        }

    }






    private void findViews() {
        enter_PTR_lock = findViewById(R.id.enter_PTR_lock);
        enter_IMG_background= findViewById(R.id.enter_IMG_background);
        enter_IMG_image = findViewById(R.id.enter_IMG_image);
        enter_BTN_click = findViewById(R.id.enter_BTN_click);
        enter_TXT_info = findViewById(R.id.enter_TXT_info);
        enter_TXT_title = findViewById(R.id.enter_TXT_title);
        enter_LBL_password = findViewById(R.id.enter_LBL_password);
        enter_BTN_login = findViewById(R.id.enter_BTN_login);
        enter_TXT_counter= findViewById(R.id.enter_TXT_counter);
        enter_BTN_hint1=findViewById(R.id.enter_BTN_hint1);
        enter_BTN_hint2=findViewById(R.id.enter_BTN_hint2);
        enter_BTN_hint3=findViewById(R.id.enter_BTN_hint3);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();
        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
        battery = getBatteryPercentage(this);
        checkCounter();

    }

    @Override
    protected void onPause() {
        super.onPause();
        battery = getBatteryPercentage(this);

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }


    public void onSensorChanged(SensorEvent event) {


        if(event.sensor.getType()==Sensor.TYPE_ORIENTATION)
        {
            updateAngle(event.values[0]);
        }
      /*  if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD) {
            floatGeoMagnetic = event.values;

            SensorManager.getRotationMatrix(floatRotationMatrix, null, floatGravity, floatGeoMagnetic);
            SensorManager.getOrientation(floatRotationMatrix, floatOrientation);

            enter_IMG_image.setRotation((float) (-floatOrientation[0]*180/3.14159));

        }*/
       if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
            floatGravity = event.values;

            SensorManager.getRotationMatrix(floatRotationMatrix, null, floatGravity, floatGeoMagnetic);
            SensorManager.getOrientation(floatRotationMatrix, floatOrientation);

            enter_IMG_image.setRotation((float) (-floatOrientation[0] * 180 / 3.14159));

        }

/*        if ((gravityValues != null) && (magneticValues != null)
                && (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)) {

            float[] deviceRelativeAcceleration = new float[4];
            deviceRelativeAcceleration[0] = event.values[0];
            deviceRelativeAcceleration[1] = event.values[1];
            deviceRelativeAcceleration[2] = event.values[2];
            deviceRelativeAcceleration[3] = 0;

            // Change the device relative acceleration values to earth relative values
            // X axis -> East
            // Y axis -> North Pole
            // Z axis -> Sky

            float[] R = new float[16], I = new float[16], earthAcc = new float[16];

            SensorManager.getRotationMatrix(R, I, gravityValues, magneticValues);

            float[] inv = new float[16];

            android.opengl.Matrix.invertM(inv, 0, R, 0);
            android.opengl.Matrix.multiplyMV(earthAcc, 0, inv, 0, deviceRelativeAcceleration, 0);
            Log.d("johny", "Acceleration Values: (" + earthAcc[0] + ", " + earthAcc[1] + ", " + earthAcc[2] + ")");

        } else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            gravityValues = event.values;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticValues = event.values;
            enter_TXT_info.setText(String.valueOf(magneticValues));
        }*/
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
                matchPatterns(pattern,"012543876");
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
            counter-=1;
            checkCounter();

        }
    }

    public void goToHell() {
        // Open your SignUp Activity if the user wants to signup
        Intent intent = new Intent(this, HellActivity.class);
        startActivity(intent);
        finish();
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